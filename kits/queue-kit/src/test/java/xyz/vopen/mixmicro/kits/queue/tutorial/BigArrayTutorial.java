package xyz.vopen.mixmicro.kits.queue.tutorial;

import static org.junit.Assert.*;

import java.io.IOException;

import xyz.vopen.mixmicro.kits.queue.DefaultBigArray;
import xyz.vopen.mixmicro.kits.queue.BigArray;
import org.junit.Test;


/**
 * A tutorial to show the basic API usage of the big array.
 * 
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 *
 */
public class BigArrayTutorial {

	@Test
	public void demo() throws IOException {
		BigArray bigArray = null;
		try {
			// create a new big array
			bigArray = new DefaultBigArray("d:/bigarray/tutorial", "demo");
			// ensure the new big array is empty
			assertNotNull(bigArray);
			assertTrue(bigArray.isEmpty());
			assertTrue(bigArray.size() == 0);
			assertTrue(bigArray.getHeadIndex() == 0);
			assertTrue(bigArray.getTailIndex() == 0);
			
			// append some items into the array
			for(int i = 0; i < 10; i++) {
				String item = String.valueOf(i);
				long index = bigArray.append(item.getBytes());
				assertTrue(i == index);
			}
			assertTrue(bigArray.size() == 10);
			assertTrue(bigArray.getHeadIndex() == 10);
			assertTrue(bigArray.getTailIndex() == 0);
			
			// randomly read items in the array
			String item0 = new String(bigArray.get(0));
			assertEquals(String.valueOf(0), item0);
			
			String item3 = new String(bigArray.get(3));
			assertEquals(String.valueOf(3), item3);
			
			String item9 = new String(bigArray.get(9));
			assertEquals(String.valueOf(9), item9);
			
			// empty the big array
			bigArray.removeAll();
			assertTrue(bigArray.isEmpty());
		} finally {
			bigArray.close();
		}
	}

}
