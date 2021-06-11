package xyz.vopen.mixmicro.components.enhance.apidoc.plugin;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocContext;
import xyz.vopen.mixmicro.components.enhance.apidoc.DocsConfig;
import xyz.vopen.mixmicro.components.enhance.apidoc.IPluginSupport;
import xyz.vopen.mixmicro.components.enhance.apidoc.exception.PluginException;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.DHttpRequest;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.DHttpResponse;
import xyz.vopen.mixmicro.components.enhance.apidoc.utils.DHttpUtils;
import xyz.vopen.mixmicro.components.enhance.apidoc.model.ControllerNode;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
public class RapSupportPlugin implements IPluginSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(RapSupportPlugin.class);
  private String rapHost;
  private Integer projectId; // project id in rap
  private String cookie;

  private List<ControllerNode> controllerNodeList;

  @Override
  public void execute(List<ControllerNode> controllerNodeList) {
    this.controllerNodeList = controllerNodeList;
    postToRap();
  }

  /** do post */
  private void postToRap() {

    DocsConfig docsConfig = DocContext.getDocsConfig();
    if (controllerNodeList == null
        || controllerNodeList.isEmpty()
        || docsConfig == null
        || docsConfig.getRapHost() == null
        || docsConfig.getRapProjectId() == null) {
      LOGGER.warn("docs config properties miss, we don't think you want to post to rap!");
      return;
    }

    this.rapHost = docsConfig.getRapHost();
    this.projectId = Integer.valueOf(docsConfig.getRapProjectId());
    this.cookie = docsConfig.getRapLoginCookie();

    if (StringUtils.isEmpty(cookie)) {
      String account = docsConfig.getRapAccount();
      String password = docsConfig.getRapPassword();
      DHttpResponse response = doLogin(loginUrl(rapHost), account, password);
      this.cookie = response.getHeader("Set-Cookie");
    }

    Set<Module> moduleSet = getModuleList();

    ProjectForm projectForm = new ProjectForm();
    projectForm.setId(projectId);

    Set<DeleteActionFrom> deleteModuleForms = new HashSet<>(moduleSet.size());
    if (!moduleSet.isEmpty()) {
      for (Module module : moduleSet) {
        if (Module.MODULE_NAME.equalsIgnoreCase(module.getName())) {
          DeleteActionFrom delForm = new DeleteActionFrom();
          delForm.setClassName("Module");
          delForm.setId(module.getId());
          deleteModuleForms.add(delForm);
        }
      }
    }
    projectForm.setDeletedObjectListData(JSON.toJSONString(deleteModuleForms));

    Project project = Project.valueOf(projectId, controllerNodeList);
    projectForm.setProjectData(JSON.toJSONString(project));

    postProject(projectForm);
  }

  public DHttpResponse doLogin(String loginUrl, String userName, String password) {
    DHttpRequest request = new DHttpRequest();
    request.setAutoRedirect(false);
    request.setUrl(loginUrl);
    request.addParam("account", userName);
    request.addParam("password", password);
    try {
      return DHttpUtils.httpPost(request);
    } catch (IOException ex) {
      LOGGER.error("login rap fail , userName : {}, pass : {}", userName, password);
      throw new PluginException(ex);
    }
  }

  private Set<Module> getModuleList() {
    try {
      DHttpResponse modelResp = DHttpUtils.httpGet(queryModelUrl(rapHost, projectId));
      if (modelResp.getCode() == 200) {
        ModelResponse model = JSON.parseObject(modelResp.streamAsString(), ModelResponse.class);
        return model.getModel().getModuleList();
      } else {
        LOGGER.error("request module data fail, rapHost : {}, projectId : {}", rapHost, projectId);
        throw new PluginException("request module data fail , code : " + modelResp.getCode());
      }
    } catch (IOException e) {
      LOGGER.error("get rap models fail", e);
    }
    return new HashSet<>();
  }

  private void postProject(ProjectForm projectForm) {
    DHttpRequest request = new DHttpRequest();
    request.setUrl(checkInUrl(rapHost));
    Map<String, String> params = new HashMap<>();
    params.put("id", String.valueOf(projectForm.getId()));
    params.put("projectData", projectForm.getProjectData());
    if (projectForm.getDeletedObjectListData() != null) {
      params.put("deletedObjectListData", projectForm.getDeletedObjectListData());
    }
    if (projectForm.getDescription() != null) {
      params.put("description", projectForm.getDescription());
    }
    if (projectForm.getVersionPosition() != null) {
      params.put("versionPosition", projectForm.getVersionPosition());
    }
    request.setParams(params);

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    headers.put("Cookie", cookie);
    request.setHeaders(headers);

    try {
      DHttpResponse response = DHttpUtils.httpPost(request);
      String responseString = response.streamAsString();
      if (response.getCode() == 200) {
        LOGGER.info("post project to rap success, response : {}", responseString);
      } else {
        LOGGER.error("post project to rap fail !!! code : {}", responseString);
      }
    } catch (IOException e) {
      LOGGER.error("post project to rap fail", e);
    }
  }

  private String queryModelUrl(String host, Integer projectId) {
    return String.format("%s/api/queryModel.do?projectId=%s", host, projectId);
  }

  private String checkInUrl(String host) {
    return String.format("%s/workspace/checkIn.do", host);
  }

  private String loginUrl(String host) {
    return String.format("%s/account/doLogin.do", host);
  }
}
