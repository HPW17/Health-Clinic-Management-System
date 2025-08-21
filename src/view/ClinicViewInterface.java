package view;

/**
 * Represents a View for Health Clinic and provides the visual interface for users. 
 */
public interface ClinicViewInterface {
  
  /**
   * Set up menu groups in JFrame.
   * 
   * @param mode 1 for initial (Clinic), 2 for after load clinic (Clinic + Features)
   */
  public void setMenu(int mode);
  
  /**
   * Refresh the view to reflect any changes in the clinic graphical representation.
   */
  public void refresh();

  /**
   * Make the view visible to start the application.
   */
  public void makeVisible();
  
  /**
   * Update status message.
   * 
   * @param status message shown in the status bar
   * @param type message font type, 0 for normal, 1 for emphasize, 2 for warning
   */
  public void setStatus(String status, int type);
  
  /**
   * Set current selection type.
   * 
   * @param type 0 for room, 1 for patient
   */
  public void setSelectType(int type);
  
  /**
   * Get the ID of the patient selected by clicking on GUI.
   *  
   * @return the selected patient ID
   */
  public int getSelectedPatientId();
  
  /**
   * Get the ID of the room selected by clicking on GUI.
   *  
   * @return the selected room ID
   */
  public int getSelectedRoomId();
  
  /**
   * Reset the selected patient ID.
   */
  public void resetSelectedPatientId();
  
  /**
   * Reset the selected room ID.
   */
  public void resetSelectedRoomId();
  
  /**
   * Specify the clinic text file.
   * 
   * @param specFile the specified clinic text file
   */
  public void setSpecFile(String specFile);
  
  /**
   * Get the clinic text file.
   * 
   * @return the clinic text file
   */
  public String getSpecFile();
  
  /**
   * Reset viewer state with specified menu type.
   * 
   * @param menu 1 for 'clinic' menu only, 2 for both 'clinic' and 'features' menu
   */
  public void clearState(int menu);
  
  /**
   * Get the ClinicPanel of the viewer.
   * 
   * @return the ClinicPanel of the viewer
   */
  public ClinicPanel getPanel();
  
  /**
   * Enable the menu bar when a feature function is finished.
   */
  public void enableMenu();
  
  /**
   * Disable the menu bar when a feature function is executing.
   */
  public void disableMenu();
}
