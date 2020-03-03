package xyz.vopen.mixmicro.kits.queue.page;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import xyz.vopen.mixmicro.kits.queue.TestUtil;
import xyz.vopen.mixmicro.kits.queue.utils.FileUtil;
import org.junit.After;
import org.junit.Test;

public class MappedPageFactoryTest {
	
	private MappedPageFactory mappedPageFactory;
	private String testDir = TestUtil.TEST_BASE_DIR + "bigqueue/unit/mapped_page_factory_test";
	
	
	@Test 
	public void testGetBackPageFileSet() throws IOException {
		mappedPageFactory = new DefaultMappedPageFactory(1024, testDir + "/test_get_backpage_fileset", 2 * 1000);
		
		for(int i = 0; i < 10; i++ ) {
			mappedPageFactory.acquirePage(i);
		}
		
		Set<String> fileSet = mappedPageFactory.getBackPageFileSet();
		assertTrue(fileSet.size() == 10);
		for(int i = 0; i < 10; i++ ) {
			assertTrue(fileSet.contains(DefaultMappedPageFactory.PAGE_FILE_NAME + "-" + i + DefaultMappedPageFactory.PAGE_FILE_SUFFIX));
		}
	}
	
	@Test
	public void testGetBackPageFileSize() throws IOException {
		mappedPageFactory = new DefaultMappedPageFactory(1024 * 1024, testDir + "/test_get_backpage_filesize", 2 * 1000);
		
		for(int i = 0; i < 100; i++ ) {
			mappedPageFactory.acquirePage(i);
		}
		
		assertTrue(1024 * 1024 * 100 == mappedPageFactory.getBackPageFileSize());
	}
	
	
	@Test
	public void testSingleThread() throws IOException {
	
		mappedPageFactory = new DefaultMappedPageFactory(1024 * 1024 * 128, testDir + "/test_single_thread", 2 * 1000);
		
		MappedPage mappedPage = mappedPageFactory.acquirePage(0); // first acquire
		assertNotNull(mappedPage);
		MappedPage mappedPage0 = mappedPageFactory.acquirePage(0); // second acquire
		assertSame(mappedPage, mappedPage0);
		
		MappedPage mappedPage1 = mappedPageFactory.acquirePage(1);
		assertNotSame(mappedPage0, mappedPage1);
		
		mappedPageFactory.releasePage(0); // release first acquire
		mappedPageFactory.releasePage(0); // release second acquire
		TestUtil.sleepQuietly(2200);// let page0 expire
		mappedPageFactory.acquirePage(2);// trigger mark&sweep and purge old page0
		mappedPage = mappedPageFactory.acquirePage(0);// create a new page0
		assertNotSame(mappedPage, mappedPage0);
		TestUtil.sleepQuietly(1000);// let the async cleaner do the job
		assertTrue(!mappedPage.isClosed());
		assertTrue(mappedPage0.isClosed());
		
		
		for(long i = 0; i < 100; i++) {
			assertNotNull(mappedPageFactory.acquirePage(i));
		}
		assertTrue(mappedPageFactory.getCacheSize() == 100);
		Set<Long> indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 100);
		for(long i = 0; i < 100; i++) {
			assertTrue(indexSet.contains(i));
		}
		
		this.mappedPageFactory.deletePage(0);
		assertTrue(mappedPageFactory.getCacheSize() == 99);
		indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 99);
		
		this.mappedPageFactory.deletePage(1);
		assertTrue(mappedPageFactory.getCacheSize() == 98);
		indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 98);
		
		for(long i = 2; i < 50; i++) {
			this.mappedPageFactory.deletePage(i);
		}
		assertTrue(mappedPageFactory.getCacheSize() == 50);
		indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 50);
		
		this.mappedPageFactory.deleteAllPages();
		assertTrue(mappedPageFactory.getCacheSize() == 0);
		indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 0);
		
		long start = System.currentTimeMillis();
		for(long i = 0; i < 5; i++) {
			assertNotNull(this.mappedPageFactory.acquirePage(i));
			TestUtil.sleepQuietly(1000);
		}
		indexSet = mappedPageFactory.getPageIndexSetBefore(start - 1000);
		assertTrue(indexSet.size() == 0);
		indexSet = mappedPageFactory.getPageIndexSetBefore(start + 2500);
		assertTrue(indexSet.size() == 3);
		indexSet = mappedPageFactory.getPageIndexSetBefore(start + 5000);
		assertTrue(indexSet.size() == 5);
		
		mappedPageFactory.deletePagesBefore(start + 2500);
		indexSet = mappedPageFactory.getExistingBackFileIndexSet();
		assertTrue(indexSet.size() == 2);
		assertTrue(mappedPageFactory.getCacheSize() == 2);
		
		mappedPageFactory.releaseCachedPages();
		assertTrue(mappedPageFactory.getCacheSize() == 0);
		
		assertTrue(((DefaultMappedPageFactory)mappedPageFactory).getLockMapSize() == 0);
		mappedPageFactory.deleteAllPages();
		
		start = System.currentTimeMillis();
		for(int i = 0; i <= 100; i++) {
			MappedPage mappedPageI = mappedPageFactory.acquirePage(i);
			mappedPageI.getLocal(0).put(("hello " + i).getBytes());
			mappedPageI.setDirty(true);
			((DefaultMappedPage)mappedPageI).flush();
			long currentTime = System.currentTimeMillis();
			long iPageFileLastModifiedTime = mappedPageFactory.getPageFileLastModifiedTime(i);
			assertTrue(iPageFileLastModifiedTime >= start);
			assertTrue(iPageFileLastModifiedTime <= currentTime);
			
			long index = mappedPageFactory.getFirstPageIndexBefore(currentTime + 1);
			assertTrue(index == i);
			
			start = currentTime;
		}
		
		mappedPageFactory.deleteAllPages();
	}
	
	@After
	public void clear() throws IOException {
		if (this.mappedPageFactory != null) {
			this.mappedPageFactory.deleteAllPages();
		}
		FileUtil.deleteDirectory(new File(testDir));
	}
	
	@Test
	public void testMultiThreads() throws IOException {
		mappedPageFactory = new DefaultMappedPageFactory(1024 * 1024 * 128, testDir + "/test_multi_threads", 2 * 1000);
		
		int pageNumLimit = 200;
		int threadNum = 1000;
		
		Map<Integer, MappedPage[]> sharedMap1 = this.testAndGetSharedMap(mappedPageFactory, threadNum, pageNumLimit);
		assertTrue(this.mappedPageFactory.getCacheSize() == pageNumLimit);
		Map<Integer, MappedPage[]> sharedMap2 = this.testAndGetSharedMap(mappedPageFactory, threadNum, pageNumLimit);
		assertTrue(this.mappedPageFactory.getCacheSize() == pageNumLimit);
		// pages in two maps should be same since they are all cached
		verifyMap(sharedMap1, sharedMap2, threadNum, pageNumLimit, true);
		verifyClosed(sharedMap1, threadNum, pageNumLimit, false);
		
		TestUtil.sleepQuietly(2500);
		this.mappedPageFactory.acquirePage(pageNumLimit + 1); // trigger mark&sweep
		assertTrue(this.mappedPageFactory.getCacheSize() == 1);
		Map<Integer, MappedPage[]> sharedMap3 = this.testAndGetSharedMap(mappedPageFactory, threadNum, pageNumLimit);
		assertTrue(this.mappedPageFactory.getCacheSize() == pageNumLimit + 1);
		// pages in two maps should be different since all pages in sharedMap1 has expired and purged out
		verifyMap(sharedMap1, sharedMap3, threadNum, pageNumLimit, false);
		verifyClosed(sharedMap3, threadNum, pageNumLimit, false);
		
		verifyClosed(sharedMap1, threadNum, pageNumLimit, true);
		
		// ensure no memory leak
		assertTrue(((DefaultMappedPageFactory)mappedPageFactory).getLockMapSize() == 0);
	}
	
	private void verifyClosed(Map<Integer, MappedPage[]> map, int threadNum, int pageNumLimit, boolean closed) {
		for(int i = 0; i < threadNum; i++) {
			MappedPage[] pageArray = map.get(i);
			for(int j = 0; j < pageNumLimit; j++) {
				if (closed) {
					assertTrue(pageArray[j].isClosed());
				} else {
					assertTrue(!pageArray[j].isClosed());
				}
			}
		}
	}
	
	private void verifyMap(Map<Integer, MappedPage[]> map1, Map<Integer, MappedPage[]> map2, int threadNum, int pageNumLimit, boolean same) {
		for(int i = 0; i < threadNum; i++) {
			MappedPage[] pageArray1 = map1.get(i);
			MappedPage[] pageArray2 = map2.get(i);
			for(int j = 0; j < pageNumLimit; j++) {
				if (same) {
					assertSame(pageArray1[j], pageArray2[j]);
				} else {
					assertNotSame(pageArray1[j], pageArray2[j]);
				}
			}
		}
	}
	
	private Map<Integer, MappedPage[]> testAndGetSharedMap(MappedPageFactory pageFactory, int threadNum, int pageNumLimit) {
		
		// init shared map
		Map<Integer, MappedPage[]> sharedMap = new ConcurrentHashMap<Integer, MappedPage[]>();
		for(int i = 0; i < threadNum; i++) {
			MappedPage[] pageArray = new MappedPage[pageNumLimit];
			sharedMap.put(i, pageArray);
		}
		
		// init threads and start
		CountDownLatch latch = new CountDownLatch(threadNum);
		Worker[] workers = new Worker[threadNum];
		for(int i = 0; i < threadNum; i++) {
			workers[i] = new Worker(i, sharedMap, mappedPageFactory, pageNumLimit, latch);
			workers[i].start();
		}
		
		// wait to finish
		for(int i = 0; i < threadNum; i++) {
			try {
				workers[i].join();
			} catch (InterruptedException e) {
				// ignore silently
			}
		}
		
		// validate
		MappedPage[] firstPageArray = sharedMap.get(0);
		for(int j = 0; j < pageNumLimit; j++) {
			MappedPage page = firstPageArray[j];
			assertTrue(!page.isClosed());
		}
		for(int i = 1; i < threadNum; i++) {
			MappedPage[] pageArray = sharedMap.get(i);
			for(int j = 0; j < pageNumLimit; j++) {
				assertSame(firstPageArray[j], pageArray[j]);
			}
		}
		
		return sharedMap;
	}
	
	private static class Worker extends Thread {
		private Map<Integer, MappedPage[]> map;
		private MappedPageFactory pageFactory;
		private int id;
		private int pageNumLimit;
		private CountDownLatch latch;
		public Worker(int id, Map<Integer, MappedPage[]> sharedMap, MappedPageFactory mappedPageFactory, int pageNumLimit, CountDownLatch latch) {
			this.map = sharedMap;
			this.pageFactory = mappedPageFactory;
			this.id = id;
			this.pageNumLimit = pageNumLimit;
			this.latch = latch;
		}
		
		public void run() {
			List<Integer> pageNumList = new ArrayList<Integer>();
			for(int i = 0; i < pageNumLimit; i++) {
				pageNumList.add(i);
			}
			Collections.shuffle(pageNumList);
			MappedPage[] pages = map.get(id);
			latch.countDown();
			try {
				latch.await();
			} catch (InterruptedException e1) {
				// ignore silently
			}
			for(int i : pageNumList) {
				try {
					pages[i] = this.pageFactory.acquirePage(i);
					this.pageFactory.releasePage(i);
				} catch (IOException e) {
					fail("Got IOException when acquiring page " + i);
				}
			}
		}
	}
}
