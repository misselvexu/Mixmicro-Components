package xyz.vopen.mixmicro.components.mongo.client.aggregation;

import com.mongodb.DBObject;

interface AggregationElement {
  /**
   * This is an internal method and subject to change or removal. Do not use.
   *
   * @return the DBObject form of the the element
   */
  DBObject toDBObject();
}
