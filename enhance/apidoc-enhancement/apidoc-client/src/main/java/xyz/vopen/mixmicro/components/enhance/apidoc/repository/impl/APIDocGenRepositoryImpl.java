package xyz.vopen.mixmicro.components.enhance.apidoc.repository.impl;

import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.MongoHelper;
import xyz.vopen.mixmicro.components.enhance.apidoc.bean.IndexBean;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.RepositoryException;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.DocIndexModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.EndpointNodeModel;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.APIDocGenRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
public class APIDocGenRepositoryImpl implements APIDocGenRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(APIDocGenRepositoryImpl.class);

  private final MongoTemplate mongoTemplate;

  public static final Integer ID_LENGTH = 18;

  public APIDocGenRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String crateIndexIfAbsent(IndexBean indexBean) {
    if (null == indexBean
        || StringUtils.isAnyBlank(indexBean.getCurrentApiVersion(), indexBean.getProjectName())) {
      LOGGER.info("indexBean:{}", indexBean);
      throw new RepositoryException("crateIndexIfAbsent param error");
    }
    String id = genIndexId(indexBean);
    if (StringUtils.isBlank(id)) {
      throw new RepositoryException("get index id error");
    }
    Query query = Query.query(Criteria.where(MongoHelper.ID).is(id));
    DocIndexModel docIndexModel =
        mongoTemplate.findOne(
            query, DocIndexModel.class, MongoHelper.getCollectionName(DocIndexModel.class));
    if (null != docIndexModel) {
      docIndexModel.setProjectName(indexBean.getProjectName());
      docIndexModel.setCurrentApiVersion(indexBean.getCurrentApiVersion());
      docIndexModel.setUpdateTime(System.currentTimeMillis());
    } else {
      long currentTimeMillis = System.currentTimeMillis();
      docIndexModel = new DocIndexModel();
      docIndexModel.setProjectName(indexBean.getProjectName());
      docIndexModel.setCurrentApiVersion(indexBean.getCurrentApiVersion());
      docIndexModel.setCreateTime(currentTimeMillis);
      docIndexModel.setUpdateTime(currentTimeMillis);
      docIndexModel.setId(genIndexId(indexBean));
    }
    mongoTemplate.save(docIndexModel);
    return id;
  }

  @Override
  public void createControllerNode(String indexId, List<ControllerNode> controllerNodeList) {
    if (StringUtils.isBlank(indexId) || CollectionUtils.isEmpty(controllerNodeList)) {
      LOGGER.info("indexId:{},controllerNodeList:{}", indexId, controllerNodeList);
      throw new RepositoryException("createControllerNode param error");
    }
    DeleteResult deleteResult =
        mongoTemplate.remove(
            Query.query(Criteria.where("indexId").is(indexId)),
            MongoHelper.getCollectionName(EndpointNodeModel.class));
    LOGGER.info("endpoint node deleted:{} line", deleteResult.getDeletedCount());
    for (ControllerNode controllerNode : controllerNodeList) {
      EndpointNodeModel endpointNodeModel = new EndpointNodeModel();
      endpointNodeModel.setAuthor(controllerNode.getAuthor());
      endpointNodeModel.setBaseUrl(controllerNode.getBaseUrl());
      endpointNodeModel.setClassName(controllerNode.getClassName());
      endpointNodeModel.setDescription(controllerNode.getDescription());
      endpointNodeModel.setRequestNodes(controllerNode.getRequestNodes());
      endpointNodeModel.setSrcFileName(controllerNode.getSrcFileName());
      endpointNodeModel.setPackageName(controllerNode.getPackageName());
      endpointNodeModel.setId(getID());
      endpointNodeModel.setIndexId(indexId);
      mongoTemplate.save(endpointNodeModel);
    }
  }

  /**
   * generate index id
   *
   * @param indexBean index param
   * @return index id
   */
  private String genIndexId(IndexBean indexBean) {
    String currentApiVersion = indexBean.getCurrentApiVersion();
    String projectName = indexBean.getProjectName();
    return Base64.encode((projectName + "_" + currentApiVersion).getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 普通id 生成工具
   *
   * @return 18位Id，前13位是时间戳，后5位随机数
   */
  public static String getID() {
    StringBuilder randomStr = new StringBuilder(String.valueOf(System.currentTimeMillis()));
    randomStr.append(new Random().nextInt(99999));
    while (randomStr.length() < ID_LENGTH) {
      randomStr.append("0");
    }
    return randomStr.toString();
  }
}
