package clinic;

import java.util.List;
import java.util.SortedSet;

/**
 * This interface represents the model of a staff member in the {@link Clinic}.
 */
public interface StaffInterface {

  /**
   * Retrieves the staff ID of this staff member.
   * 
   * @return the staff ID of the staff member
   */
  public int getId();
  
  /**
   * Retrieves the first name of this staff member.
   * 
   * @return the first name of the staff member
   */
  public String getFirstName();

  /**
   * Retrieves the last name of this staff member.
   * 
   * @return the last name of the staff member
   */
  public String getLastName();
  
  /**
   * Retrieves the job title of this staff member.
   * 
   * @return the job title of the staff member
   */
  public String getJobTitle();
  
  /**
   * Retrieves the education level of this staff member, 
   * as defined in the enumeration EducationLevel.
   * 
   * @return the education level of the staff member
   */
  public String getEducationLevel();
  
  /**
   * A clinical staff member can be assigned to a patient.
   * This method gets the patients that the staff has been assigned to.
   * 
   * @return the list of the patients that the staff has been assigned to
   */
  public SortedSet<PatientInterface> getAssignedPatients();
  
  /**
   * This method gets all the patients that the staff has ever been assigned to.
   * 
   * @return the list of the patients that the staff has ever been assigned to
   */
  public SortedSet<PatientInterface> getEverAssigned();
  
  /**
   * This method gets the patients that the staff has been assigned to, and the patients 
   * are in the clinic.
   * 
   * @return the list of the patients in clinic that the staff has been assigned to
   */
  public List<PatientInterface> getPatientsInClinic();
  
  /**
   * Retrieves the NPI for a clinical staff or the CPR level for a non-clinical staff.
   * 
   * @return NPI for clinical staff, or CPR level for non-clinical staff
   */
  public String getNpiCpr();
  
  /**
   * Retrieves the active status of this staff member. 
   * 
   * @return true if the staff is active, false if inactive (deactivated)
   */
  public Boolean isActive();
  
  /**
   * Set the job title of this staff member. 
   * 
   * @param jobTitle the updated job title of the staff member
   */
  public void setJobTitle(String jobTitle);
  
  /**
   * Set the education level of this staff member. 
   * 
   * @param educationLevel the updated education level of the staff member
   */
  public void setEducationLevel(String educationLevel);
  
  /**
   * Set the NPI for a clinical staff or the CPR level for a non-clinical staff.
   * 
   * @param npiCpr the NPI for clinical staff, or the CPR level for non-clinical staff
   */
  public void setNpiCpr(String npiCpr);
  
  /**
   * Assign this staff member to the specified patient.
   * 
   * @param assignedPatient the patient to be assigned to
   */
  public void assignToPatient(PatientInterface assignedPatient);
  
  /**
   * Un-assign this staff member from the specified patient.
   * 
   * @param assignedPatient the patient to be un-assigned from
   */
  public void unassignFromPatient(PatientInterface assignedPatient);
  
  /**
   * Set the active status of this staff member.
   * 
   * @param active true to activate this staff member, false to deactivate
   */
  public void setActive(Boolean active);
  
}
