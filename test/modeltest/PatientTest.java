package modeltest;

import static org.junit.Assert.assertEquals;

import clinic.Patient;
import clinic.Room;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link Patient} class.
 */
public class PatientTest {
  
  /**
   * Test constructor with valid arguments, expect to create instance successfully.
   * Getter methods getFirstName(), getLastName(), and getDateOfBirth() also tested.
   */
  @Test
  public void testValidPatientAndGetters() {
    Patient.clearLastNumberAssigned();
    Patient newPatient = new Patient("Aandi", "Acute", "1981/01/01");
    assertEquals("Aandi", newPatient.getFirstName());
    assertEquals("Acute", newPatient.getLastName());
    assertEquals(LocalDate.parse("1981/01/01", DateTimeFormatter.ofPattern("yyyy/MM/dd")), 
        newPatient.getDateOfBirth());
    assertEquals(1, newPatient.getId());
    assertEquals(null, newPatient.getAssignedRoom());
    assertEquals(new TreeSet<>(), newPatient.getVisitRecords());
  }
  
  
  /**
   * Expected IllegalArgumentException due to invalid date format.
   * 
   * @throws IllegalArgumentException if entered invalid date format
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateFormat() throws IllegalArgumentException {
    new Patient("Aandi", "Acute", "1981/13/01");
  }
  
  
  /**
   * Test assignToRoom() to assign patient to specified room and getAssignedRoom() to get the 
   * assigned room. If the patient was already assigned to a room and later re-assign to another 
   * room, this will supersede the room the patient was previously assigned to. 
   */
  @Test
  public void testAssignRoomSetterAndGetter() {
    Patient patient = new Patient("Aandi", "Acute", "1981/01/01");
    assertEquals("Initially no assignment", null, patient.getAssignedRoom());
    
    // assign patient to room1
    Room room1 = new Room(5, 0, 10, 5, "WAITING", "Waiting Room");
    patient.assignToRoom(room1);
    assertEquals("Patient assigned to 1st room", room1, patient.getAssignedRoom());
    
    // assign patient to room2
    Room room2 = new Room(15, 10, 20, 15, "EXAM", "Exam Room");
    patient.assignToRoom(room2);
    assertEquals("Patient assigned to 2nd room", room2, patient.getAssignedRoom());
  }

  /**
   * Test patient visit records. The following methods are tested:
   *   1. getter: getVisitRecords()
   *   2. setter: addVisitRecord()
   *   3. display: toString() to show patient's info with all visit records (sorted), and 
   *               showLastVisitRecord() to display the last record (sorted by date)
   *   4. Attempting to add records with invalid arguments: 
   *      invalid date/time format and unreasonable body temperature
   */
  @Test
  public void testPatientVisitRecord() {
    // Initial state, no visit records
    Patient patient = new Patient("Aandi", "Acute", "1981/01/01");
    assertEquals("Initially no records list", 0, patient.getVisitRecords().size());
    assertEquals("Initially no last record", "No visit records", patient.showLastVisitRecord());
    assertEquals("Initially no records in patient info", 
        "Patient Name: Aandi Acute, Date Of Birth: 1981/01/01\n"
        + "- No visit records", patient.toString());
    
    // Add two visit records to this patient
    patient.addVisitRecord("2024/10/07 10:30", "Headaches", 38.2);
    patient.addVisitRecord("2024/09/20 10:15", "Chest pain", 36.8);
    assertEquals("Added 2 records to list", 2, patient.getVisitRecords().size());
    assertEquals("Show last visit (sorted by date)", 
        "Last visit on: 2024/10/07 10:30, Body Temperature: 38.2°C, Chief Complaint: Headaches", 
        patient.showLastVisitRecord());
    assertEquals("Show all (two) visit records", 
        "Patient Name: Aandi Acute, Date Of Birth: 1981/01/01\n"
        + "- Visit on: 2024/09/20 10:15, Body Temperature: 36.8°C, Chief Complaint: Chest pain\n"
        + "- Visit on: 2024/10/07 10:30, Body Temperature: 38.2°C, Chief Complaint: Headaches", 
        patient.toString());
    
    // Try adding a visit record with invalid date/time format
    try {
      patient.addVisitRecord("October 7, 2024", "Headaches", 38.2);
    } catch (IllegalArgumentException e) {
      // expected exception
      assertEquals("Invalid date/time format (yyyy/MM/dd HH:mm).", e.getMessage());
    }
    
    // Try adding a visit record with invalid date/time format
    try {
      patient.addVisitRecord("2024/10/07 10:30", "Headaches", 50);
    } catch (IllegalArgumentException e) {
      // expected exception
      assertEquals("Unreasonable body temperature.", e.getMessage());
    }
    
    // Add the third visit record to this patient after two invalid attempts
    patient.addVisitRecord("2024/10/09 14:15", "Sore throat", 37.9);
    assertEquals("Added 3 records to list", 3, patient.getVisitRecords().size());
    assertEquals("Show last visit (sorted by date)", 
        "Last visit on: 2024/10/09 14:15, Body Temperature: 37.9°C, Chief Complaint: Sore throat", 
        patient.showLastVisitRecord());
    assertEquals("Show all (three) visit records", 
        "Patient Name: Aandi Acute, Date Of Birth: 1981/01/01\n"
        + "- Visit on: 2024/09/20 10:15, Body Temperature: 36.8°C, Chief Complaint: Chest pain\n"
        + "- Visit on: 2024/10/07 10:30, Body Temperature: 38.2°C, Chief Complaint: Headaches\n"
        + "- Visit on: 2024/10/09 14:15, Body Temperature: 37.9°C, Chief Complaint: Sore throat", 
        patient.toString());
  }
}
