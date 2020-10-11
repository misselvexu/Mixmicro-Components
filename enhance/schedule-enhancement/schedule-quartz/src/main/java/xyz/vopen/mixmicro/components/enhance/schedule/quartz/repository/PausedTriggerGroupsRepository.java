package xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static xyz.vopen.mixmicro.components.enhance.schedule.quartz.util.Keys.KEY_GROUP;

public class PausedTriggerGroupsRepository {

  private final MongoCollection<Document> triggerGroupsCollection;

  public PausedTriggerGroupsRepository(MongoCollection<Document> triggerGroupsCollection) {
    this.triggerGroupsCollection = triggerGroupsCollection;
  }

  public HashSet<String> getPausedGroups() {
    return triggerGroupsCollection.distinct(KEY_GROUP, String.class).into(new HashSet<String>());
  }

  public void pauseGroups(Collection<String> groups) {
    List<Document> list = new ArrayList<Document>();
    for (String s : groups) {
      list.add(new Document(KEY_GROUP, s));
    }
    triggerGroupsCollection.insertMany(list);
  }

  public void remove() {
    triggerGroupsCollection.deleteMany(new Document());
  }

  public void unpauseGroups(Collection<String> groups) {
    triggerGroupsCollection.deleteMany(Filters.in(KEY_GROUP, groups));
  }
}
