package xyz.vopen.mixmicro.components.enhance.mongo.client.test.model;

import lombok.*;
import org.bson.types.ObjectId;
import xyz.vopen.mixmicro.components.mongo.client.annotations.*;

import java.util.List;

/**
 * {@link Employee}
 *
 * <p>Class Employee Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/3
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity("employees")
@Indexes(
    @Index(fields = @Field("salary"))
)
public class Employee {

  @Id private ObjectId id;

  private String name;

  @Reference private Employee manager;

  @Reference private List<Employee> directReports;

  @Property("wage") private Double salary;

  @Builder
  public Employee(ObjectId id, String name, Employee manager, List<Employee> directReports, Double salary) {
    this.id = id;
    this.name = name;
    this.manager = manager;
    this.directReports = directReports;
    this.salary = salary;
  }
}
