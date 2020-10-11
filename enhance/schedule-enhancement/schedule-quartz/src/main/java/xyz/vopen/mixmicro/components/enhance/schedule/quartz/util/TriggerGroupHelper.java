package xyz.vopen.mixmicro.components.enhance.schedule.quartz.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TriggerGroupHelper extends GroupHelper {
  public static final String JOB_ID = "jobId";

  public TriggerGroupHelper(MongoCollection<Document> collection, QueryHelper queryHelper) {
    super(collection, queryHelper);
  }

  public List<String> groupsForJobId(ObjectId jobId) {
    return collection
        .distinct(Keys.KEY_GROUP, String.class)
        .filter(Filters.eq(JOB_ID, jobId))
        .into(new LinkedList<String>());
  }

  public List<String> groupsForJobIds(Collection<ObjectId> ids) {
    return collection
        .distinct(Keys.KEY_GROUP, String.class)
        .filter(Filters.in(JOB_ID, ids))
        .into(new LinkedList<String>());
  }
}
