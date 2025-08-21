package clinic;

import java.time.LocalDate;
import java.util.SortedSet;

/**
 * This interface represents the model of a patient in the {@link Clinic}.
 */
public interface PatientInterface extends Comparable<PatientInterface> {
  
  /**
   * Retrieves the ID of this patient.
   * 
   * @return the ID of the patient
   */
  public int getId();
  
  /**
   * Retrieves the first name of this patient.
   * 
   * @return the first name of the patient
   */
  public String getFirstName();

  /**
   * Retrieves the last name of this patient.
   * 
   * @return the last name of the patient
   */
  public String getLastName();

  /**
   * Retrieves the date of birth of this patient.
   * 
   * @return the date of birth of the patient
   */
  public LocalDate getDateOfBirth();
  
  /**
   * Retrieves the assigned room of this patient.
   * 
   * @return the assigned room
   */
  public RoomInterface getAssignedRoom();
  
  /**
   * Retrieves the visit records of this patient. 
   * It returns a sorted copy (ascending order) of the list to avoid external manipulation.
   * 
   * @return the visit records
   */
  public SortedSet<VisitRecordInterface> getVisitRecords();
  
  /**
   * Assign the specified room to this patient.
   * 
   * @param assignedRoom the specified room that this patient is assigned to
   */
  public void assignToRoom(RoomInterface assignedRoom);
  
  /**
   * Add a visit record to this patient. 
   * 
   * @param registrationDateTime the date and time of registration (yyyy/MM/dd HH:mm)
   * @param chiefComplaint the patient's chief complaint
   * @param bodyTemperature the patient's body temperature in degrees Celsius
   */
  public void addVisitRecord(String registrationDateTime, 
      String chiefComplaint, double bodyTemperature);
  
  /** 
   * Returns a formatted string representation of the last visit record of the patient.
   * 
   * Examples:
   * Last visit on: 2024/10/07 10:30, Body Temperature: 37.1°C, Chief Complaint: Headaches
   * or
   * No visit records
   *
   * @return a string containing the information of the last visit record. 
   */
  public String showLastVisitRecord();
}
