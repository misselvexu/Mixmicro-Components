package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class ResultData {
  private CacheResultCode resultCode;
  private String message;
  private Object data;

  public ResultData(Throwable e) {
    this.resultCode = CacheResultCode.FAIL;
    this.message = "Ex : " + e.getClass() + ", " + e.getMessage();
  }

  public ResultData(CacheResultCode resultCode, String message, Object data) {
    this.resultCode = resultCode;
    this.message = message;
    this.data = data;
  }

  public Object getData() {
    return CacheGetResult.unwrapValue(data);
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Object getOriginData() {
    return data;
  }

  public CacheResultCode getResultCode() {
    return resultCode;
  }

  public void setResultCode(CacheResultCode resultCode) {
    this.resultCode = resultCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
