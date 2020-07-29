package xyz.vopen.framework.logging.client.admin.discovery.lb;

/**
 * Load Balance Node
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoadBalanceNode {
  /** node init weight */
  private int initWeight = 1;
  /** logging admin address */
  private String address;
  /** current weight */
  private int currentWeight;

  public LoadBalanceNode(String address) {
    this.address = address;
  }

  public int getInitWeight() {
    return initWeight;
  }

  public void setInitWeight(int initWeight) {
    this.initWeight = initWeight;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getCurrentWeight() {
    return currentWeight;
  }

  public void setCurrentWeight(int currentWeight) {
    this.currentWeight = currentWeight;
  }
}
