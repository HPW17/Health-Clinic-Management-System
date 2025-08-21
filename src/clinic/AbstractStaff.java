package clinic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class implements {@link StaffInterface} and represents a staff member in {@link Clinic}.
 * This class provides shared functionality and fields to be used by staff for different 
 * types of jobs, such as {@link ClinicalStaff} and {@link NonClinicalStaff}.
 * 
 */
public abstract class AbstractStaff implements StaffInterface {
  private static int lastNumberAssigned = 0;
  
  protected String npiCpr;
  protected final SortedSet<PatientInterface> assignedPatients;
  protected final SortedSet<PatientInterface> everAssigned;
  private final int staffId;
  private final String firstName;
  private final String lastName;
  private String jobTitle;
  private EducationLevel educationLevel;
  private Boolean active;
  
  /**
   * Constructor which initializes all the fields.
   * 
   * @param firstName the first name of the staff
   * @param lastName the last name of the staff
   * @param jobTitle the job title of the staff
   * @param educationLevel the education level defined in enumeration EducationLevel
   */
  public AbstractStaff(String firstName, String lastName, 
      String jobTitle, String educationLevel) throws IllegalArgumentException {
    // The education level should be valid as defined in enumeration EducationLevel
    ArrayList<String> educationLevelNames = new ArrayList<>();
    for (EducationLevel j : EducationLevel.values()) {
      educationLevelNames.add(j.name());
    }
    if (!educationLevelNames.contains(educationLevel)) {
      throw new IllegalArgumentException("Invalid education level: " + educationLevel);
    }
    // Arguments are valid, create an instance
    this.staffId = ++lastNumberAssigned;
    this.firstName = firstName;
    this.lastName = lastName;
    this.jobTitle = jobTitle;
    this.educationLevel = EducationLevel.valueOf(educationLevel);
    this.npiCpr = null;
    this.assignedPatients = new TreeSet<>();
    this.everAssigned = new TreeSet<>();
    this.active = true; 
  }
  
  /**
   * Copy constructor, creates a new AbstractStaff object that is a copy of the given one.
   * 
   * @param other the staff to be copied
   */
  public AbstractStaff(AbstractStaff other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot create a copy of a null Staff object.");
    }
    this.staffId = other.staffId;
    this.firstName = other.firstName;
    this.lastName = other.lastName;
    this.jobTitle = other.jobTitle;
    this.educationLevel = other.educationLevel;
    this.npiCpr = other.npiCpr;
    this.assignedPatients = new TreeSet<>(other.assignedPatients);
    this.everAssigned = new TreeSet<>(other.everAssigned);
    this.active = other.active;
  }
  
  @Override
  public int getId() {
    return staffId;
  }
  
  @Override
  public String getFirstName() {
    return firstName;
  }
  
  @Override
  public String getLastName() {
    return lastName;
  }
  
  @Override
  public String getJobTitle() {
    return jobTitle;
  }
  
  @Override
  public String getEducationLevel() {
    return educationLevel.name();
  }
  
  @Override
  public SortedSet<PatientInterface> getAssignedPatients() {
    return new TreeSet<>(assignedPatients);
  }
  
  @Override
  public SortedSet<PatientInterface> getEverAssigned() {
    return new TreeSet<>(everAssigned);
  }
  
  @Override
  public List<PatientInterface> getPatientsInClinic() {
    List<PatientInterface> inClinic = new ArrayList<>();
    for (PatientInterface p : getAssignedPatients()) {
      if (p.getAssignedRoom() != null) {
        inClinic.add(p);
      }
    }
    return inClinic;
  }
  
  @Override
  public String getNpiCpr() {
    return npiCpr;
  }
  
  @Override
  public Boolean isActive() {
    return active;
  }
  
  @Override
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle.toUpperCase();
  }
  
  @Override
  public void setEducationLevel(String educationLevel) {
    ArrayList<String> educationLevelNames = new ArrayList<>();
    for (EducationLevel e : EducationLevel.values()) {
      educationLevelNames.add(e.name());
    }
    if (!educationLevelNames.contains(educationLevel.toUpperCase())) {
      throw new IllegalArgumentException("Invalid education level.");
    }
    this.educationLevel = EducationLevel.valueOf(educationLevel.toUpperCase());
  }
  
  @Override
  public void setActive(Boolean active) {
    this.active = active;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof AbstractStaff)) {
      return false;
    }
    AbstractStaff that = (AbstractStaff) o;
    return this.staffId == that.staffId 
        && this.firstName.equals(that.firstName)
        && this.lastName.equals(that.lastName) 
        && this.jobTitle.equals(that.jobTitle) 
        && this.educationLevel.equals(that.educationLevel)
        && this.assignedPatients.equals(that.assignedPatients) 
        && this.npiCpr.equals(that.npiCpr) 
        && this.active == that.active;
  }

  @Override
  public int hashCode() {
    return Objects.hash(staffId, firstName, lastName, jobTitle, educationLevel, 
        assignedPatients, npiCpr, active);
  }
  
  /**
   * This is for JUnit testing purpose to refresh state each run, 
   * therefore it is not included in the contract defined by StaffInterface.
   */
  public static void clearLastNumberAssigned() {
    lastNumberAssigned = 0;
  }
    
  /**
   * An enumeration representing three possible levels of education of staff members.
   */
  public enum EducationLevel {
    DOCTORAL, 
    MASTERS, 
    ALLIED
  }
}
