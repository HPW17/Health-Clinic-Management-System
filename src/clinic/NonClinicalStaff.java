package clinic;

import clinic.AbstractStaff.EducationLevel;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class extends the {@link AbstractStaff} interface.
 * This class represents a non-clinical staff member and has the CPR level information. 
 */
public class NonClinicalStaff extends AbstractStaff {
  
  /**
   * Constructor which initialize all the fields.
   * 
   * @param jobTitle the job title defined in {@link JobTitle}
   * @param firstName the first name of the staff
   * @param lastName the last name of the staff
   * @param educationLevel the education level defined in {@link EducationLevel}
   * @param cprLevel the CPR level defined in {@link CprLevel}
   */
  public NonClinicalStaff(String jobTitle, String firstName, String lastName, 
      String educationLevel, String cprLevel) {
    super(firstName, lastName, jobTitle, educationLevel);
    // The education level should be valid as defined in enumeration EducationLevel
    ArrayList<String> educationLevelNames = new ArrayList<>();
    for (EducationLevel e : EducationLevel.values()) {
      educationLevelNames.add(e.name());
    }
    if (!educationLevelNames.contains(educationLevel.toUpperCase())) {
      throw new IllegalArgumentException("Invalid education level.");
    }
    // The CPR level should be valid as defined in enumeration CprLevel
    ArrayList<String> cprLevelNames = new ArrayList<>();
    for (CprLevel c : CprLevel.values()) {
      cprLevelNames.add(c.name());
    }
    if (!cprLevelNames.contains(cprLevel.toUpperCase())) {
      throw new IllegalArgumentException("Invalid NPI/CPR input.");
    }
    // All arguments are valid, go ahead to create an instance
    this.npiCpr = cprLevel.toUpperCase();
  }
  
  @Override
  public void assignToPatient(PatientInterface assignedPatient) {
    throw new IllegalStateException("Cannot assign non-clinical staff to a patient.");
  }
  
  @Override
  public void unassignFromPatient(PatientInterface assignedPatient) {
    throw new IllegalStateException("Cannot unassign non-clinical staff from a patient.");
  }
  
  @Override
  public void setNpiCpr(String cpr) {
    ArrayList<String> cprLevelNames = new ArrayList<>();
    for (CprLevel c : CprLevel.values()) {
      cprLevelNames.add(c.name());
    }
    if (!cprLevelNames.contains(cpr.toUpperCase())) {
      throw new IllegalArgumentException("Invalid CPR level.");
    }
    this.npiCpr = cpr.toUpperCase();
  }
  
  @Override
  public String toString() {
    return this.getFirstName() + " " + this.getLastName();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof NonClinicalStaff)) {
      return false;
    }
    NonClinicalStaff that = (NonClinicalStaff) o;
    return this.getId() == that.getId() 
        && Objects.equals(this.getFirstName(), that.getFirstName())
        && Objects.equals(this.getLastName(), that.getLastName()) 
        && Objects.equals(this.getJobTitle(), that.getJobTitle()) 
        && Objects.equals(this.getEducationLevel(), that.getEducationLevel())
        && Objects.equals(this.getAssignedPatients(), that.getAssignedPatients()) 
        && Objects.equals(this.getNpiCpr(), that.getNpiCpr()) 
        && this.isActive() == that.isActive();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getFirstName(), getLastName(), getJobTitle(), 
        getEducationLevel(), getAssignedPatients(), getNpiCpr(), isActive());
  }
  
  /**
   * CPR levels that the staff members have acquired.
   */
  public enum CprLevel {
    A,   // CPR for adults
    B,   // CPR for children and infants
    C,   // CPR for adults, children, and infants
    BLS  // Basic Life Support
  }
}
