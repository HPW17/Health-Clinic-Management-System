package controller;

import clinic.ClinicInterface;
import commands.CommandInterface;
import controllertest.MockModel;
import controllertest.MockView;
import java.util.function.Supplier;
import view.ClinicViewInterface;

/**
 * This class implements {@link ControllerInterface} and represents the controller of the clinic.
 * It is designed as a singleton.
 */
public class Controller implements ControllerInterface {

  private static Controller instance;
  private final ClinicInterface model;
  private final ClinicViewInterface view;
  
  /**
   * Hidden constructor of Controller.
   * 
   * @param m the clinic model
   * @param v the clinic view
   */
  private Controller(ClinicInterface m, ClinicViewInterface v) {
    if (m == null || v == null) {
      throw new IllegalArgumentException("Model and ClinicView cannot be null.");
    }
    model = m;
    view = v;
  }
  
  /**
   * This public constructor is just for JUnit testing of isolated controller, 
   * therefore is not included in the UML design document. 
   * 
   * @param m the mock model
   * @param v the mock view
   */
  public Controller(MockModel m, MockView v) {
    model = m;
    view = v;
  }
  
  /**
   * This public method is not included in the contract defined by ControllerInterface. 
   * Since Controller is designed as a singleton with a sole instance, this method creates a 
   * new instance by calling the hidden constructor and return it when firstly called, or 
   * returns the existing instance when called afterwards.
   * 
   * @param m the clinic model
   * @param v the clinic view
   * @return the sole instance of Controller
   */
  public static Controller getInstance(ClinicInterface m, ClinicViewInterface v) {
    if (instance == null) {
      instance = new Controller(m, v);
    }
    return instance;
  }
  
  @Override
  public void start() {
    view.makeVisible();
  }
  
  @Override
  public void start(String specFile) {
    view.setSpecFile(specFile);
    view.makeVisible();
    handleMenuClick("Load clinic text file");
  }
  
  @Override
  public void handleMenuClick(String command) {
    Supplier<CommandInterface> func = KnownCommands.getMenu().getOrDefault(command, null);
    if (func == null) {
      // Should not happen in GUI environment
      // System.out.println("Unknown command: " + command);
      view.setStatus("Unknown command: " + command, 2);
    } else {
      view.setStatus(func.get().execute(model, view), 0);
    }
    view.refresh();
  }
}
