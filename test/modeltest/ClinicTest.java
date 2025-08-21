package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import clinic.Clinic;
import clinic.ClinicalStaff;
import clinic.Patient;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link Clinic} class.
 */
public class ClinicTest {
  Clinic clinic;
  
  @Before
  public void setUp() {
    clinic = Clinic.getInstance();
    clinic.clearState();
  }
  
  
  /**
   * Helper method to create a sample instance of Clinic with 2 rooms, 2 staff, 2 patients.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  private void createSampleInstance() throws IllegalArgumentException, IOException {
    String clinicData = "Cybernetic Implant Clinic\n"
        + "2\n"
        + "28  0 35  5 waiting Front Waiting Room\n"
        + "30  6 35 11 exam Triage\n"
        + "2\n"
        + "physician Amy Anguish doctoral 1234567890\n"
        + "reception Frank Febrile allied B\n"
        + "2\n"
        + "1 Aandi Acute 1981/01/01\n"
        + "2 Beth Bunion 1982/02/02\n";
    clinic.readIntoModel(new StringReader(clinicData));
  }
  
  /**
   * This test is expected to pass successfully, meaning that the following items work:
   * - Clinic: readIntoModel() reads the specifications from String and populates data into model.
   * - Clinic: getters (getRooms(), getStaff(), getPatients()) gets data successfully.
   * - Room: getRoomName(), getRoomType() works successfully (also tested in RoomTest.java) .
   * - AbstractStaff: getJobType() works successfully (also tested in ClinicalStaff.java).
   * - Patient: getDateOfBirth() works (also tested in PatientTest.java).
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testReadFromFile() throws IllegalArgumentException, IOException {
    createSampleInstance();
    assertEquals("Check clinic name", "Cybernetic Implant Clinic", clinic.getName());
    assertEquals("Check room1 name", "Front Waiting Room", clinic.getRooms().get(0).getRoomName());
    assertEquals("Check room2 type", "EXAM", clinic.getRooms().get(1).getRoomType());
    assertEquals("Check staff2 title", "RECEPTION", clinic.getStaff().get(1).getJobTitle());
    assertEquals("Check staff2 CPR", "B", clinic.getStaff().get(1).getNpiCpr());
    assertEquals("Check patient2 BOD", 
        LocalDate.parse("1982/02/02", DateTimeFormatter.ofPattern("yyyy/MM/dd")), 
        clinic.getPatients().get(1).getDateOfBirth());
    assertEquals(2, clinic.getRooms().get(1).getId());
    assertEquals("EXAM", clinic.getRooms().stream().filter(r -> r.getId() == 2)
            .findFirst().orElse(null).getRoomType());
  }
  
  
  /**
   * This test is expected to throw exception:
   * IllegalArgumentException with message "The first room must be waiting room."
   * Since the first room in specification data must be a waiting room.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */ 
  @Test
  public void testInvalidFirstRoomType() throws IllegalArgumentException, IOException {
    String clinicData = "Cybernetic Implant Clinic\n"
        + "2\n"
        + "28  0 35  5 exam Front Waiting Room\n"
        + "30  6 35 11 exam Triage\n"
        + "0\n"
        + "0\n";
    try {
      clinic.readIntoModel(new StringReader(clinicData));
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      // expected exception
      assertEquals("The first room must be waiting room.", e.getMessage());
    }
  }
 
  
  /**
   * This test is expected to throw exception:
   * IllegalArgumentException with message "Overlap with existing rooms."
   * Since the specification data has improper room coordinates, will cause room overlap.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testOverlappingRoomCoordinates() throws IllegalArgumentException, IOException {
    String clinicData = "Cybernetic Implant Clinic\n"
        + "2\n"
        + "28  0 35  5 waiting Front Waiting Room\n"
        + "30  3 35 11 exam Triage\n"
        + "0\n"
        + "0\n";
    try {
      clinic.readIntoModel(new StringReader(clinicData));
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      // expected exception
      assertEquals("Overlap with existing rooms.", e.getMessage());
    }
  }

  
  /**
   * This test is expected to throw exception:
   * IllegalArgumentException with message "NPI should be a 10-digits number."
   * Since the NPI in the specification data is invalid (only 9 digits, should be 10).
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testInvalidNpi() throws IllegalArgumentException, IOException {
    String clinicData = "Cybernetic Implant Clinic\n"
        + "0\n"
        + "1\n"
        + "physician Amy Anguish doctoral 123456789\n";
    try {
      clinic.readIntoModel(new StringReader(clinicData));
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      // expected exception
      assertEquals("NPI should be a 10-digits number.", e.getMessage());
    }
  }
  
  
  /**
   * Test Clinic.registerPatient() with a newly created patient.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testRegisterPatient() throws IllegalArgumentException, IOException {
    createSampleInstance();
    Patient newPatient = new Patient("John", "Doe", "1983/03/03");
    clinic.registerPatient(newPatient);
    assertEquals("John", clinic.getPatients().get(2).getFirstName());
    assertEquals(3, newPatient.getId());
  }
  
  
  /**
   * Test Clinic.registerClinicalStaff() with a newly created clinical staff member.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testRegisterClinicalStaff() throws IllegalArgumentException, IOException {
    createSampleInstance();
    ClinicalStaff newStaff1 = new ClinicalStaff("NURSE", "Mary", "Doe", "MASTERS", "5678901234");
    clinic.registerClinicalStaff(newStaff1);
    assertEquals("5678901234", clinic.getStaff().get(2).getNpiCpr());
    assertEquals(3, (clinic.getStaff().get(2)).getId());
  }
  
  
  /**
   * Test Clinic.sendPatientHome() and Clinic.deactivateClinicalStaff().
   * 1. Send the first patient, approved by a clinical staff.
   * 2. Deactivate this staff.
   * 3. Then send the second patient by the same staff, expect to have IllegalStateException.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IllegalStatetException if a deactivated staff tries to send a patient home
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testSendPatientHomeAndDeactivateClinicalStaff() 
      throws IllegalArgumentException, IllegalStateException, IOException {
    createSampleInstance();
    // initially 2 patients
    assertEquals("Current number of patients is 2.", 2, clinic.getPatients().size());
    assertEquals("Patient1 name is Aandi.", "Aandi", clinic.getPatients().get(0).getFirstName());
    assertEquals("Patient1 room is 1.", 1, clinic.getPatients().get(0).getAssignedRoom().getId());
    assertEquals("Staff1 is active.", true, clinic.getStaff().get(0).isActive());
    // send patient home
    clinic.sendPatientHome(clinic.getPatients().get(0), clinic.getStaff().get(0));
    assertEquals("Patient1 name is Aandi.", "Aandi", clinic.getPatients().get(0).getFirstName());
    assertEquals("Patient1 room is null.", null, clinic.getPatients().get(0).getAssignedRoom());
    // deactivate staff1
    clinic.deactivateClinicalStaff(clinic.getStaff().get(0));
    // send patient home
    assertEquals("Patient2 name is Beth.", "Beth", clinic.getPatients().get(1).getFirstName());
    assertEquals("Patient2 room is 2.", 2, clinic.getPatients().get(1).getAssignedRoom().getId());
    assertEquals("Staff1 deactivated.", false, 
        clinic.getStaff().get(0).isActive());
    try {
      clinic.sendPatientHome(clinic.getPatients().get(1), clinic.getStaff().get(0));
      fail("Expected IllegalStateException but not happening.");
    } catch (IllegalStateException e) {
      // expected exception
      assertEquals("This staff has been deactivated.", e.getMessage());
    }
    // since send patient home by a deactivated staff will fail, status not changed
    assertEquals("Patient2 name is Beth.", "Beth", clinic.getPatients().get(1).getFirstName());
    assertEquals("Patient2 room is 2.", 2, clinic.getPatients().get(1).getAssignedRoom().getId());
  }
  
  /**
   * Test Clinic.assignPatientToRoom().
   * 1. Initially patient1 in room1, patient2 in room2. 
   * 2. Assign patient2 to room1, expect to succeed since WAITING room can host multiple patients.
   * 3. Then try to assign both patients to the same EXAM room, expect IllegalStateException.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IllegalStateException if try to assign a deactivated staff to a patient
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testAssignPatientToRoom() 
      throws IllegalArgumentException, IllegalStateException, IOException {
    createSampleInstance();
    // initially patient1 in room1 (WAITING), patient2 in room2 (EXAM)
    assertEquals("Patient1's room is WAITING room.", "WAITING", 
        clinic.getPatients().get(0).getAssignedRoom().getRoomType());
    assertEquals("Patient2's room is EXAM room.", "EXAM", 
        clinic.getPatients().get(1).getAssignedRoom().getRoomType());
    // assign patient2 to room1, expect to succeed
    clinic.assignPatientToRoom(clinic.getPatients().get(1), clinic.getRooms().get(0));
    assertEquals("Patient1's room is WAITING room.", "WAITING", 
        clinic.getPatients().get(0).getAssignedRoom().getRoomType());
    assertEquals("Patient2's room is WAITING room.", "WAITING", 
        clinic.getPatients().get(1).getAssignedRoom().getRoomType());
    // assign patient1 to room2, expect to succeed
    clinic.assignPatientToRoom(clinic.getPatients().get(0), clinic.getRooms().get(1));
    // try to assign patient2 to room2 as well, expect IllegalStateException
    try {
      clinic.assignPatientToRoom(clinic.getPatients().get(1), clinic.getRooms().get(1));
      fail("Expected IllegalStateException but not happening.");
    } catch (IllegalStateException e) {
      // expected exception
      assertEquals("Room is already occupied.", e.getMessage());
    }
  }
  
  /**
   * Test Clinic.assignStaffToPatient().
   * 1. Initially staff1 is not assigned to any patient. 
   * 2. Assign staff1 to patient1, expect to succeed.
   * 3. Deactivate staff1.
   * 4. Then try to assign deactivated staff1 to patient2, expect IllegalStateException.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IllegalStateException if try to assign a deactivated staff to a patient
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testAssignStaffToPatient() 
      throws IllegalArgumentException, IllegalStateException, IOException {
    createSampleInstance();
    // initially staff1 is not assigned to any patient
    assertEquals("Staff1 initially assigned to no patient.", "[]", 
        clinic.getStaff().get(0).getAssignedPatients().toString());
    // assign staff1 to patient1, expect to succeed
    clinic.assignStaffToPatient(clinic.getStaff().get(0), clinic.getPatients().get(0));
    assertEquals("Staff1 now assigned to patient1.", 
        "[Patient Name: Aandi Acute, Date Of Birth: 1981/01/01\n- No visit records]", 
        clinic.getStaff().get(0).getAssignedPatients().toString());
    // Deactivate staff1
    clinic.deactivateClinicalStaff(clinic.getStaff().get(0));
    assertEquals("Staff1 deactivated.", false, clinic.getStaff().get(0).isActive());
    // assertEquals("Staff1 assignment to patient is cleared.", null, 
    //     clinic.getStaff().get(0).getAssignedPatients().toString());
    // try to assign deactivated staff1 to patient2, expect IllegalStateException
    try {
      clinic.assignStaffToPatient(clinic.getStaff().get(0), clinic.getPatients().get(1));
      fail("Expected IllegalStateException but not happening.");
    } catch (IllegalStateException e) {
      // expected exception
      assertEquals("This staff has been deactivated.", e.getMessage());
    }
  }
  
  /**
   * Test Clinic.roomInfo() to get information about a specific room.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testRoomInfo() throws IllegalArgumentException, IOException {
    createSampleInstance();
    String result = clinic.roomInfo(clinic.getRooms().get(0));
    assertEquals(
        "Room Number: 1 | Room Name: Front Waiting Room | Room Type: WAITING\n"
        + "  * Patient: Aandi Acute, assigned clinical staff: none\n"
        + "    - No visit records\n", 
        result);
  }
  
  /**
   * Test Clinic.seatingChart() to get information about a seating chart of 
   * every room and the patients in each room.
   * 
   * @throws IllegalArgumentException if specification data is invalid
   * @throws IOException if file/string access has problems
   */
  @Test
  public void testSeatingChart() throws IllegalArgumentException, IOException {
    createSampleInstance();
    String result = clinic.seatingChart();
    assertEquals(
        "Room: 1 | Room Name: Front Waiting Room | Room Type: WAITING\n"
        + "  * Patients: Aandi Acute\n"
        + "Room: 2 | Room Name: Triage | Room Type: EXAM\n"
        + "  * Patients: Beth Bunion\n", 
        result);
  }
}
