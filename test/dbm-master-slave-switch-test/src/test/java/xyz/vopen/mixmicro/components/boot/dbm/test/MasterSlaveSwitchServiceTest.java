package xyz.vopen.mixmicro.components.boot.dbm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.boot.dbm.test.service.MasterSlaveSwitchService;

/**
 * {@link MasterSlaveSwitchServiceTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DBMSwitchBootstrap.class)
public class MasterSlaveSwitchServiceTest {

  @Autowired
  MasterSlaveSwitchService switchService;

  @Test
  public void testSwitch() {

    switchService.testWriteAndReadContext("test-name");

    switchService.count();
  }

}
