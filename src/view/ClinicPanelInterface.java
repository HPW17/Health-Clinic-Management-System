package view;

import java.awt.Point;

/**
 * Represents the panel to draw the clinic image with rooms and patients.
 */
public interface ClinicPanelInterface {

  /**
   * Handle mouse click and retrieves the ID of clicked room or patient.
   * 
   * @param point the location of mouse click
   * @return the ID of clicked room or patient
   */
  public int handleMouseClick(Point point);
  
  /**
   * Retrieves the patient name by the ID. 
   * 
   * @param id the patient ID
   * @return the name of the patient
   */
  public String getPatientName(int id);
  
  /**
   * Retrieves the room name by the ID.
   * 
   * @param id the room ID
   * @return the name of the room
   */
  public String getRoomName(int id);
  
  /**
   * Set the select type for mouse click on the clinic image.
   * 
   * @param type 0 for room, 1 for patient
   */
  public void setSelectType(int type);
  
  /**
   * Reset the panel state.
   */
  public void clearState();
}
