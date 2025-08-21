package controller;

import clinic.Clinic;
import clinic.ClinicInterface;
import view.ClinicView;
import view.ClinicViewInterface;

/**
 * Driver class to demonstrate the usage of the Clinic MVC application.
 */
public class ClinicDriver {

  /**
   * Demonstration of the usage of the Clinic MVC application.
   * It is optional to specify a clinic text file in the command-line arguments.
   * 
   * @param args the optional specified clinic specification text file
   */
  public static void main(String[] args) {
    ClinicInterface m = Clinic.getInstance();
    ClinicViewInterface v = ClinicView.getInstance();
    ControllerInterface c = Controller.getInstance(m, v);
    
    if (args.length > 0) {
      c.start(args[0]);
    } else {
      c.start();
    }
  }
}
