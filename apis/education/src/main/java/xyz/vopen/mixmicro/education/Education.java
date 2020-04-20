package xyz.vopen.mixmicro.education;

import xyz.vopen.mixmicro.kits.jre.http.HttpRequest;
import xyz.vopen.mixmicro.kits.thread.ThreadKit;

import java.util.HashMap;
import java.util.Map;

import static xyz.vopen.mixmicro.education.HtmlPaser.map;

/**
 * {@link Education}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-18.
 */
public class Education {

  //  static String courseId = "FFA6A251-0022-412A-9135-8344DA5548FF";

  //      static String cookie = "JSESSIONID=DFC460C28A14AEA5ADB6D9493F0E3446.tomcat_139;
  // UC00OOIIll11=\"WUIyMDI3MTUwMDA0MjotMTo2MjU3NGVkNTk0OGRhMGE3MWQwY2I1YTVkYThjYjU0ZDpudWxsOmp4anljai5zdWRhLmVkdS5jbg==\"; UNTYXLCOOKIE=anhqeWNqLnN1ZGEuZWR1LmNuOjgwfHw2NDViNTQwNTNkZDlkYWVlOTQ3MTc1ZmRjODkyZmVlOHx8WUIyMDI3MTUwMDA0Mnx8Y2Vw; JSESSIONID=AAC54F120AB0AC31B409CE344531FF0E.tomcat_139;";
  // YB20271500042:-1:62574ed5948da0a71d0cb5a5da8cb54d:null:jxjycj.suda.edu.cn
  // jxjycj.suda.edu.cn:80||645b54053dd9daee947175fdc892fee8||YB20271500042||cep

  // static String cookie = "JSESSIONID=7884805A0E1DE83D5E3C712AFB153EFF.tomcat_139;
  // UNTYXLCOOKIE=anhqeWNqLnN1ZGEuZWR1LmNuOjgwfHw2NDZlODM4Mzk4NmM3OWQ3NmEzNjJkMjhkOTYzMzg2Nnx8WUIyMDI3MTUwMDEyMXx8Y2Vw; UC00OOIIll11=\"WUIyMDI3MTUwMDEyMTotMTo1YjcwNmI0NjMxNWE2OTNhZDJjYjVkODUxMmNhZDBiMTpudWxsOmp4anljai5zdWRhLmVkdS5jbg==";

  // jxjycj.suda.edu.cn:80||646e8383986c79d76a362d28d9633866||YB20271500121||cep
  // YB20271500121:-1:5b706b46315a693ad2cb5d8512cad0b1:null:jxjycj.suda.edu.cn

  static String cookie =
      "JSESSIONID=4C9FC543D89CF0269361FE831F109DC1.tomcat_139; Hm_lvt_b2425ee6854b3fdca040d311f7799f40=1587219980; JSESSIONID=0F70998C2F0177475BCC4E0600FD6D27.tomcat_139; UC00OOIIll11=\"WUIyMDI3MTUwMDEyNzotMTowYjg2NjY4NDQ3NGJiMzU1OWQyNDlkYTIwM2I2NGFkMDpudWxsOmp4anljai5zdWRhLmVkdS5jbg==\"; UNTYXLCOOKIE=anhqeWNqLnN1ZGEuZWR1LmNuOjgwfHw2MWMzMzIzMTlhZjIwOWQ5MTIxMjQ4NDZkOWIxNDIzNnx8WUIyMDI3MTUwMDEyN3x8Y2Vw";
  //  static String itemId = "ff808081594e62030159638b15c41a60";

  // YB20271500127:-1:0b866684474bb3559d249da203b64ad0:null:jxjycj.suda.edu.cn
  //

  public static void main(String[] args) throws Exception {

    //    learningTime_saveVideoLearnDetailRecord();
    //
    //    System.out.println( " ------ ");
    //
    //    learningTime_saveLearningTime();
    //
    //    System.out.println(" ------ ");
    //
    //    learningTime_saveVideoLearnTimeLongRecord();

    map()
        .forEach(
            (courseId, courses) -> {
              System.out.println(courseId);

              for (Course cours : courses) {

                learningTime_saveVideoLearnDetailRecord(
                    courseId, cours.getItemId(), cours.getTimeString(), cours.getTime());

                learningTime_saveLearningTime(courseId, cours.getTime());

                learningTime_saveVideoLearnTimeLongRecord(
                    courseId, cours.getItemId(), cours.getTime(), cours.getTimeString());

                System.out.println(String.format("课程：%s, 时长：%s， 处理完成!", cours.getName(), cours.getTimeString()));

                System.out.println(
                    " ============================================================================================================================ ");
                System.out.println();
                ThreadKit.sleep(1000);
              }
            });
  }

  /**
   * 保存在线时长
   */
  static void learningTime_saveLearningTime(String courseId, long time) {
    String url = "http://jxjycj.suda.edu.cn/learnspace/course/study/learningTime_saveLearningTime.action";

    Map<String, Object> param = new HashMap<>();
    param.put("courseId", courseId);
    param.put("studyTime", time);


    HttpRequest request = HttpRequest.post(url,param,true)
        .header("cookie",cookie);

    int code = request.code();

    System.out.println("code = " + code);

    String body = request.body("UTF-8");

    System.out.println("body = " + body);

  }

  /**
   * 保存视频学习记录
   */
  static void learningTime_saveVideoLearnDetailRecord(String courseId, String itemId, String timeString, long time) {

    String url = "http://jxjycj.suda.edu.cn/learnspace/course/study/learningTime_saveVideoLearnDetailRecord.action";

    Map<String, Object> param = new HashMap<>();

    param.put("videoStudyRecord.courseId",courseId);
    param.put("videoStudyRecord.itemId",itemId);
    param.put("videoStudyRecord.startTime","00:00:00");
    param.put("videoStudyRecord.endTime",timeString);
    param.put("videoStudyRecord.videoIndex","0");
    param.put("videoStudyRecord.studyTimeLong",time);

    HttpRequest request = HttpRequest.post(url,param,true)
        .header("cookie",cookie);

    int code = request.code();

    System.out.println("code = " + code);

    String body = request.body("UTF-8");

    System.out.println("body = " + body);
  }

  /**
   * 保存视频已学习时长
   */
  static void learningTime_saveVideoLearnTimeLongRecord(String courseId , String itemId, long time, String timeString) {
    String url = "http://jxjycj.suda.edu.cn/learnspace/course/study/learningTime_saveVideoLearnTimeLongRecord.action";
    Map<String, Object> param = new HashMap<>();

    param.put("courseId",courseId);
    param.put("itemId",itemId);
    param.put("studyTime",time);
    param.put("resourceTotalTime",timeString);

    HttpRequest request = HttpRequest.post(url,param,true)
        .header("cookie",cookie);

    int code = request.code();

    System.out.println("code = " + code);

    String body = request.body("UTF-8");

    System.out.println("body = " + body);

  }


}
