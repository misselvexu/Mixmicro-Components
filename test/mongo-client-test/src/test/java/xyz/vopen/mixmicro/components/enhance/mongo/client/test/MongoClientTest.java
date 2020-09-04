package xyz.vopen.mixmicro.components.enhance.mongo.client.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.enhance.mongo.client.test.model.Employee;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;

import java.util.List;

/**
 * {@link MongoClientTest}
 *
 * <p>Class MongoClientTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoAutoConfigureTest.class)
public class MongoClientTest {

  @Autowired
  private MongoRepository mongoRepository;

  @Test
  public void test01() throws Exception {

    List<Employee> employees = mongoRepository.find(Employee.class).asList();

    for (Employee employee : employees) {
      System.out.println(employee);
    }
  }

}
