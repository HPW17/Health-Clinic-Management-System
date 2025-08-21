package clinic;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * It represents a health clinic including its designated operations:
 *  1. Read the clinic text file into the model.
 *  2. Register a new patient. Patients start in the primary waiting room.
 *  3. Register a new clinical staff member.
 *  4. Send a patient home, which is approved by a clinical staff member.
 *  5. Deactivate a clinical staff member.
 *  6. Assign one patient to a room.
 *  7. Assign a clinical staff member to a patient.
 *  8. Return information about a specific room as a string.
 *  9. Display a seating chart that is a text list of every room and who is in each room.
 */
public interface ClinicInterface {
  
  /**
   * Retrieves the name of the clinic.
   * 
   * @return the name of the clinic
   */
  public String getName();

  /**
   * Retrieves the list of the rooms of the clinic.
   * 
   * @return the list of the rooms of the clinic
   */
  public List<RoomInterface> getRooms();

  /**
   * Retrieves the list of the staff members of the clinic.
   * 
   * @return the list of the staff members of the clinic
   */
  public List<StaffInterface> getStaff();

  /**
   * Retrieves the list of the patients of the clinic.
   * 
   * @return the list of the patients of the clinic
   */
  public List<PatientInterface> getPatients();
  
  /**
   * Read the clinic specifications from a source to populate the data into model.
   * This source can be a text file using a FileReader, or a string using a StringReader.
   * It throws IOException if any IO error is encountered in the process.
   * 
   * @param source the Reader object used to read the clinic specifications
   * @throws IllegalArgumentException if the provided source is null or invalid.
   * @throws IOException if any IO error is encountered
   */
  public void readIntoModel(Reader source) throws IllegalArgumentException, IOException;
  
  /**
   * Register a new patient. 
   * Patients start in the primary waiting room (the first room in the rooms list).
   * 
   * @param patient the patient to be registered
   */
  public void registerPatient(PatientInterface patient);
  
  /**
   * Register a new clinical staff member.
   * 
   * @param staff the staff to be registered
   */
  public void registerClinicalStaff(StaffInterface staff);
  
  /**
   * Send a patient home, which is approved by a clinical staff.
   * Since a clinical staff can be deactivated, assume this approving staff must be active.
   * 
   * @param patient the patient to be sent home
   * @param staff the approving staff
   * @throws IllegalStateException if the approving staff is deactivated
   */
  public void sendPatientHome(PatientInterface patient, StaffInterface staff) 
      throws IllegalStateException;
  
  /**
   * Deactivate a clinical staff member.
   * 
   * @param staff the staff to be deactivated
   */
  public void deactivateClinicalStaff(StaffInterface staff);
  
  /**
   * Assign a patient to a specified room, which will supersede a room the patient was 
   * previously assigned to. Will check room availability before assignment. 
   * 
   * @param patient the patient to be assigned
   * @param room the room to be assigned to
   * @throws IllegalStateException if the room is already occupied
   */
  public void assignPatientToRoom(PatientInterface patient, RoomInterface room) 
      throws IllegalStateException;
  
  /**
   * Assign a clinical staff member to a patient. 
   * Multiple staff can be assigned to the same patient. 
   * Since a clinical staff can be deactivated, assume this staff must be active.
   * 
   * @param staff the staff member to be assigned
   * @param patient the patient to be assigned to
   * @throws IllegalStateException if the staff is deactivated
   */
  public void assignStaffToPatient(StaffInterface staff, PatientInterface patient) 
      throws IllegalStateException;
  
  /**
   * Un-assign a clinical staff member from a patient. 
   * 
   * @param staff the staff member to be un-assigned
   * @param patient the patient to be un-assigned from
   * @throws IllegalStateException if the staff is deactivated
   */
  public void unassignStaffFromPatient(StaffInterface staff, PatientInterface patient) 
      throws IllegalStateException;
  
  /**
   * Delivers information about a specific room, including the patient that is assigned 
   * to that room, and the clinical staff who are assigned to that patient.
   * 
   * @param room the specified room to show information about
   * @return a pre-formatted string containing the room information
   */
  public String roomInfo(RoomInterface room);
  
  /**
   * Delivers a seating chart of every room and the patients in each room.
   * 
   * @return a pre-formatted string containing the seating chart of all rooms
   */
  public String seatingChart();
}
