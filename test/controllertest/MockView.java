package controllertest;

import view.ClinicPanel;
import view.ClinicViewInterface;

/**
 * This class implements {@link ClinicViewInterface} and represents a mock Clinic view that 
 * does not perform actual functionalities but only logs the input values and returns 
 * unique output values. 
 * This class facilitates the testing of isolated {@link Controller}.
 */
public class MockView implements ClinicViewInterface {
  private StringBuilder log;
  private final int mockInt;
  private final String mockString;
  private final ClinicPanel mockPanel;
  
  /**
   * Constructor of mock view.
   * 
   * @param log records the input values
   * @param mockInt unique value of mock int
   * @param mockString unique value of mock String
   * @param mockPanel unique value of mock ClinicPanel
   */
  public MockView(StringBuilder log, int mockInt, String mockString, ClinicPanel mockPanel) {
    this.log = log;
    this.mockInt = mockInt;
    this.mockString = mockString;
    this.mockPanel = mockPanel;
  }
  
  @Override
  public void setMenu(int mode) {
    log.append("setMenu: mode = " + mode + "\n");
  }
  
  @Override
  public void refresh() {
    log.append("refresh\n");
  }
  
  @Override
  public void makeVisible() {
    log.append("makeVisible\n");
  }
  
  @Override
  public void setStatus(String status, int type) {
    log.append("setStatus: status = " + status + ", type = " + type + "\n");
  }
  
  @Override
  public void setSelectType(int type) {
    log.append("setSelectType: type = " + type + "\n");
  }
  
  @Override
  public int getSelectedPatientId() {
    log.append("getSelectedPatientId\n");
    return mockInt;
  }
  
  @Override
  public int getSelectedRoomId() {
    log.append("getSelectedRoomId\n");
    return mockInt;
  }
  
  @Override
  public void resetSelectedPatientId() {
    log.append("resetSelectedPatientId\n");
  }
  
  @Override
  public void resetSelectedRoomId() {
    log.append("resetSelectedRoomId\n");
  }
  
  @Override
  public void setSpecFile(String specFile) {
    log.append("setSpecFile: specFile = " + specFile + "\n");
  }
  
  @Override
  public String getSpecFile() {
    log.append("getSpecFile\n");
    return mockString;
  }
  
  @Override
  public void clearState(int menu) {
    log.append("clearState: menu = " + menu + "\n");
  }
  
  @Override
  public ClinicPanel getPanel() {
    log.append("getPanel\n");
    return mockPanel;
  }
  
  @Override
  public void enableMenu() {
    log.append("enableMenu\n");
  }
  
  @Override
  public void disableMenu() {
    log.append("disableMenu\n");
  }
  
  /**
   * Helper method to show all mock values. 
   */
  public void showMockValues() {
    log.append("Mock values:\nmockInt = " + mockInt 
        + "\nmockString = " + mockString
        + "\nmockPanel = " + mockPanel.getName() + "\n");
  }
}
