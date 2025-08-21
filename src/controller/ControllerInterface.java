package controller;

/**
 * Represents a Controller for Health Clinic and handles user commands by executing them 
 * using the model and convey the outcomes to the view.
 */
public interface ControllerInterface {

  /**
   * Gives control to the controller.
   */
  public void start();
  
  /**
   * Gives control to the controller with specified clinic text file.
   * 
   * @param specFile the specified clinic text file
   */
  public void start(String specFile);
  
  /**
   * Handle a menu selection.
   * 
   * @param command the selected command
   */
  public void handleMenuClick(String command);
}
