package xyz.vopen.mixmicro.components.boot.dbm.test.service;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.vopen.mixmicro.components.boot.dbm.MixmicroDBMRouter;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link MasterSlaveSwitchService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@Component
public class MasterSlaveSwitchService {

  final private JdbcTemplate template;

  public MasterSlaveSwitchService(JdbcTemplate template) {
    this.template = template;
  }



  // 原生事务注解
  @Transactional(rollbackFor = Exception.class)
  // 主库路由标记注解
  @MixmicroDBMRouter(isMasterRouteOnly = true)
  public void testWriteAndReadContext(String appname) {

    // 新增用户操作
    String sql = "INSERT INTO `log_login`(`user_id`, `app_name`) VALUES (1, ?)";

    int row = template.update(sql, appname);

    System.out.println("insert rows: " + row);

    // 针对用户的第一次查询操作
    String ssql = "SELECT * FROM `log_login` WHERE `app_name` = ? ";

    LoginLog log = template.queryForObject(ssql, new Object[]{appname}, new RowMapper<LoginLog>() {
      @Override
      public LoginLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return LoginLog.builder()
            .appname(rs.getString("app_name"))
            .userId(rs.getLong("user_id"))
            .id(rs.getLong("id"))
            .build();
      }
    });

    System.out.println("query result: " + log);

    // 针对用户的删除操作
    String dsql = "DELETE FROM `log_login` WHERE `app_name` = ? ";

    row = template.update(dsql, appname);

    System.out.println("clear & delete rows : " + row);
  }


  public void count() {
    String ssql = "SELECT COUNT(*) FROM `log_login`";
    System.out.println(template.queryForObject(ssql,Integer.class));
  }


  @Data
  @Builder
  @ToString
  public static class LoginLog implements Serializable {

    private long id;

    private long userId;

    private String appname;

  }

}
