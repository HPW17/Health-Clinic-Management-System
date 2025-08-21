package clinic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class that implements the {@link PersonInterface} interface.
 * This class represents a patient and has the date-of-birth information.
 */
public class Patient implements PatientInterface {
  private static int lastNumberAssigned = 0;
  
  private final int patientId;
  private final String firstName;
  private final String lastName;
  private final LocalDate dateOfBirth;
  private RoomInterface assignedRoom;
  private final SortedSet<VisitRecordInterface> visitRecords;

  /**
   * Constructor which initialize all the fields.
   * 
   * @param firstName the first name of the patient
   * @param lastName the last name of the patient
   * @param dateOfBirth the date of birth of the patient
   */
  public Patient(String firstName, String lastName, String dateOfBirth) 
      throws IllegalArgumentException {
    // Check if the dateOfBirth format is correct
    LocalDate dob;
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
      dob = LocalDate.parse(dateOfBirth, formatter);
    } catch (DateTimeParseException e1) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        dob = LocalDate.parse(dateOfBirth, formatter);
      } catch (DateTimeParseException e2) {
        throw new IllegalArgumentException("Invalid date format.");
      }
    }
    // Arguments are valid, create an instance
    this.patientId = ++lastNumberAssigned;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dob;
    this.assignedRoom = null;
    this.visitRecords = new TreeSet<>();
  }
  
  /**
   * Copy constructor, creates a new Room object that is a copy of the given one. 
   * 
   *  @param other the patient to be copied
   */
  public Patient(Patient other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot create a copy of a null Patient object.");
    }
    this.patientId = other.patientId;
    this.firstName = other.firstName;
    this.lastName = other.lastName;
    this.dateOfBirth = other.dateOfBirth;
    this.assignedRoom = other.assignedRoom != null ? new Room((Room) other.assignedRoom) : null;
    this.visitRecords = new TreeSet<>(other.visitRecords);
  }
  
  @Override
  public int getId() {
    return patientId;
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
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }
  
  @Override
  public RoomInterface getAssignedRoom() {
    if (assignedRoom != null) {
      return new Room((Room) assignedRoom);
    } else {
      return null;
    }
  }
  
  @Override
  public SortedSet<VisitRecordInterface> getVisitRecords() {
    return new TreeSet<>(visitRecords);
  }
  
  @Override
  public void assignToRoom(RoomInterface assignedRoom) {
    this.assignedRoom = assignedRoom;
  }
  
  @Override
  public void addVisitRecord(String registrationDateTime, 
      String chiefComplaint, double bodyTemperature) {
    visitRecords.add(
        new VisitRecord(registrationDateTime, chiefComplaint, bodyTemperature));
  }
  
  @Override
  public String showLastVisitRecord() {
    if (visitRecords.isEmpty()) {
      return "No visit records";
    } else {
      VisitRecordInterface v = visitRecords.last(); 
      return String.format(
          "Last visit on: %s, Body Temperature: %.1f°C, Chief Complaint: %s",
          v.getRegistrationDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")), 
          v.getBodyTemperature(), v.getChiefComplaint());
    }
  }
  
  /** 
   * Returns a formatted string representation of the patient, with all visit records.
   * Example:
   * 
   * Patient Name: Aandi Acute, Date Of Birth: 1981/01/01
   * - Visit on: 2024/09/20 10:15, Body Temperature: 36.8°C, Chief Complaint: Chest pain
   * - Visit on: 2024/10/07 10:30, Body Temperature: 38.2°C, Chief Complaint: Headaches
   *
   * @return a string containing the information of the patient with all visit records.
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(String.format("Patient Name: %s %s, Date Of Birth: %s", firstName, lastName, 
        dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
    if (visitRecords.isEmpty()) {
      result.append("\n- No visit records");
    } else {
      for (VisitRecordInterface v : visitRecords) {
        result.append(String.format(
            "\n- Visit on: %s, Body Temperature: %.1f°C, Chief Complaint: %s",
            v.getRegistrationDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")), 
            v.getBodyTemperature(), v.getChiefComplaint())); 
      }
    }
    return result.toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof Patient)) {
      return false;
    }
    Patient that = (Patient) o;
    return this.patientId == that.patientId 
        && Objects.equals(this.firstName, that.firstName)
        && Objects.equals(this.lastName, that.lastName)
        && Objects.equals(this.dateOfBirth, that.dateOfBirth)
        && Objects.equals(this.assignedRoom, that.assignedRoom)
        && Objects.equals(this.visitRecords, that.visitRecords);
  }

  @Override
  public int hashCode() {
    return Objects.hash(patientId, firstName, lastName, dateOfBirth, assignedRoom, visitRecords);
  }
  
  @Override
  public int compareTo(PatientInterface other) {
    return Integer.compare(this.patientId, ((Patient) other).patientId);
  }
  
  /**
   * This is for JUnit testing purpose to refresh state each run, 
   * therefore it is not included in the contract defined by PatientInterface.
   */
  public static void clearLastNumberAssigned() {
    lastNumberAssigned = 0;
  }
}
