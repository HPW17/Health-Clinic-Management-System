package clinic;

import clinic.AbstractStaff.EducationLevel;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class extends the {@link AbstractStaff} interface.
 * This class represents a clinical staff member and has a National Provider Identifier (NPI). 
 */
public class ClinicalStaff extends AbstractStaff {
  
  /**
   * Constructor which initialize all the fields and set this staff as active.
   * 
   * @param jobTitle the job title
   * @param firstName the first name of the staff
   * @param lastName the last name of the staff
   * @param educationLevel the education level defined in {@link EducationLevel}
   * @param npi the National Provider Identifier 10-digit number as a string
   */
  public ClinicalStaff(String jobTitle, String firstName, String lastName, 
      String educationLevel, String npi) {
    super(firstName, lastName, jobTitle.toUpperCase(), educationLevel.toUpperCase());
    // The education level should be valid as defined in enumeration EducationLevel
    ArrayList<String> educationLevelNames = new ArrayList<>();
    for (EducationLevel e : EducationLevel.values()) {
      educationLevelNames.add(e.name());
    }
    if (!educationLevelNames.contains(educationLevel.toUpperCase())) {
      throw new IllegalArgumentException("Invalid education level.");
    }
    // The NPI should be a string consisting of 10-digit numbers
    if (npi == null || !npi.matches("\\d{10}")) { 
      throw new IllegalArgumentException("NPI should be a 10-digits number.");
    }
    // All arguments are valid, go ahead to create an instance
    this.npiCpr = npi;
  }
  
  @Override
  public void assignToPatient(PatientInterface assignedPatient) {
    if (!isActive()) {
      throw new IllegalStateException("This staff has been deactivated.");
    }
    this.assignedPatients.add(assignedPatient);
    this.everAssigned.add(assignedPatient);
  }
  
  @Override
  public void unassignFromPatient(PatientInterface assignedPatient) {
    if (!isActive()) {
      throw new IllegalStateException("This staff has been deactivated.");
    }
    if (!assignedPatients.contains(assignedPatient)) {
      throw new IllegalStateException("This staff wasn't assigned to this patient.");
    }
    this.assignedPatients.remove(assignedPatient);
  }
  
  @Override
  public void setNpiCpr(String npi) {
    if (npi == null || !npi.matches("\\d{10}")) { 
      throw new IllegalArgumentException("NPI should be a 10-digits number.");
    }
    this.npiCpr = npi;
  }
  
  /** 
   * Returns a formatted string representing the title and name of the staff member.
   * 
   * Examples:
   * Dr. Amy Anguish
   * Nurse Camila Crisis
   * Frank Febrile
   *
   * @return the title and the name of the staff member. 
   */
  @Override
  public String toString() {
    String title;
    switch (this.getJobTitle()) {
      case "PHYSICIAN":
        title = "Dr. ";
        break;
      case "NURSE":
        title = "Nurse ";
        break;
      default:
        title = "";
        break;
    }
    return title + this.getFirstName() + " " + this.getLastName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof ClinicalStaff)) {
      return false;
    }
    ClinicalStaff that = (ClinicalStaff) o;
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
  
}
