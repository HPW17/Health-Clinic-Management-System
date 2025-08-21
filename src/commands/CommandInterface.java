package commands;

import clinic.ClinicInterface;
import view.ClinicViewInterface;

/**
 * Represents a high-level abstract Controller Command for Health Clinic. It uses the  
 * command design pattern to unify different sets of operations under one umbrella, so 
 * that they can be treated uniformly.
 */
public interface CommandInterface {

  /**
   * Method to execute a set of operations on the given clinic model.
   * 
   * @param m the clinic model
   * @param v the clinic view
   * @return status message
   */
  public String execute(ClinicInterface m, ClinicViewInterface v);
}
