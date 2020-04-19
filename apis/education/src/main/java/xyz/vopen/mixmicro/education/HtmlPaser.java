package xyz.vopen.mixmicro.education;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.vopen.mixmicro.kits.json.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link HtmlPaser}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-18.
 */
public class HtmlPaser {

  private static final String basePath =
      "/Users/misselvexu/Documents/yunlsp.gitlab.com/Mixmicro-Components/apis/education/src/main/resources/";

  private static final String[] courseIds = {"FFA6A251-0022-412A-9135-8344DA5548FF" ,"9AD679E6-F350-4EFD-B617-B81C50D0396E", "24D5A2A5-59CC-40E5-B4B7-5D93FB0A4C8E","38E8ECD9-8DF7-48E4-8F83-E4962024E93C"};
  private static final String[] items = {"gltj.html" , "c.html", "szlj.html", "en.html"};

  public static void main (String[] args) throws Exception {
    map()
        .forEach(
            (s, courses) -> {
              System.out.println(s);

              for (Course cours : courses) {
                System.out.println("\t\t" + JSON.toJSONString(cours));
              }
            });
  }

  public static Map<String, List<Course>> map() throws Exception{

    Map<String, List<Course>> result = new HashMap<>();
    for (int i = 0; i < items.length; i++) {
      String courseId = courseIds[i];

      Document document =
          Jsoup.parse(
              new File(
                  "/Users/misselvexu/Documents/yunlsp.gitlab.com/Mixmicro-Components/apis/education/src/main/resources/" + items[i]),
              "UTF-8");
      result.put(courseId,parseCourse(document));
    }
    return result;
  }

  public static List<Course> parseCourse(Document document) throws Exception {

    List<Course> courses = new ArrayList<>();

    Elements elements = document.select("tr > td:first-child").select("td[class='mn3 rdm rdr']");
    for (Element element : elements) {
      String value = element.attr("id");
      String time = element.select("span").text();
      String text = element.text();
      if (text.indexOf("（Video）") > 0) {
        courses.add(
            Course.builder()
                .name(element.text())
                .itemId(value.split("_")[1])
                .time(parseSecond(time))
                .timeString(time)
                .build());
      }
    }

    return courses;
  }

  private static long parseSecond(String time) throws Exception {
    String[] temp = time.split(":");
    return Integer.parseInt(temp[0]) * 60 * 60
        + Integer.parseInt(temp[1]) * 60
        + Integer.parseInt(temp[2]);
  }
}
