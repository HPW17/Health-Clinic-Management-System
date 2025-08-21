package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import clinic.ClinicalStaff;
import clinic.Patient;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link ClinicalStaff} class.
 */
public class ClinicalStaffTest {
  
  /**
   * Test constructor with valid arguments, expect to create instance successfully.
   * Getter methods getFirstName(), getLastName(), getJobTitle(), getEducationLevel(), 
   * getNpi(), isActive(), getAssignedPatient() also tested.
   */
  @Test
  public void testValidClinicalStaff() {
    ClinicalStaff staff1 = new ClinicalStaff("PHYSICIAN", "Amy", "Anguish", 
        "DOCTORAL", "1234567890");
    assertEquals("Amy", staff1.getFirstName());
    assertEquals("Anguish", staff1.getLastName());
    assertEquals("PHYSICIAN", staff1.getJobTitle());
    assertEquals("DOCTORAL", staff1.getEducationLevel());
    assertEquals("1234567890", staff1.getNpiCpr());
    assertEquals(true, staff1.isActive());
    assertEquals("[]", staff1.getAssignedPatients().toString());
  }

    
  /**
   * Expected IllegalArgumentException due to invalid education level
   * (should be in predefined enum values).
   * 
   * @throws IllegalArgumentException if entered invalid education level
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEducationLevel() throws IllegalArgumentException {
    new ClinicalStaff("PHYSICIAN", "Amy", "Anguish", "COLLEGE", "1234567890");
  }
  
  
  /**
   * Expected IllegalArgumentException due to invalid NPI format (must be 10-digit numbers).
   * 
   * @throws IllegalArgumentException if entered invalid NPI format.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNpiFormat() throws IllegalArgumentException {
    new ClinicalStaff("PHYSICIAN", "Amy", "Anguish", "DOCTORAL", "123456");
  }
  
  
  /**
   * Test setJobTitle() to re-assign job title of this staff.
   */
  @Test
  public void testSetJobTitle() {
    ClinicalStaff staff = new ClinicalStaff("NURSE", "Camila", "Crisis", "DOCTORAL", "2224443338");
    assertEquals("Initial job title", "NURSE", staff.getJobTitle());
    
    // re-assign job title
    staff.setJobTitle("PHYSICIAN");
    assertEquals("Re-assigned job title", "PHYSICIAN", staff.getJobTitle());
  }
  
  
  /**
   * Test setEducationLevel() to re-assign education level of this staff.
   */
  @Test
  public void testSetEducationLevel() {
    ClinicalStaff staff = new ClinicalStaff("NURSE", "Denise", "Danger", "MASTERS", "8877665544");
    assertEquals("Initial education level", "MASTERS", staff.getEducationLevel());
    
    // re-assign education level
    staff.setEducationLevel("DOCTORAL");
    assertEquals("Re-assigned education level", "DOCTORAL", staff.getEducationLevel());
  }
  
  
  /**
   * Test assignToPatient() to assign staff to the specified patient, and 
   * getAssignedPatient() to get the assigned patient.
   */
  @Test
  public void testAssignToPatientAndGetAssignedPatient() {
    // initially no assigned patient
    ClinicalStaff staff = new ClinicalStaff("NURSE", "Denise", "Danger", "MASTERS", "8877665544");
    assertEquals("Initially no assigned patient", "[]", staff.getAssignedPatients().toString());
    
    // assign to the specified patient
    Patient patient = new Patient("Aandi", "Acute", "1981/01/01");
    staff.assignToPatient(patient);
    assertTrue("After assigning to patient", staff.getAssignedPatients().contains(patient));   
  }
  
  
  /**
   * Test setActive() to set activation status of staff and isActive() to get status info.
   * If staff is not active (deactivated), cannot be assigned to a patient.
   */
  @Test
  public void testSetActiveAndIsActive() {
    // initially staff is active
    ClinicalStaff staff = new ClinicalStaff("NURSE", "Denise", "Danger", "MASTERS", "8877665544");
    assertEquals("Initially active", true, staff.isActive());
    
    // deactivate
    staff.setActive(false);
    assertEquals("Initially active", false, staff.isActive());
    
    // a deactivated staff cannot be assigned to a patient
    Patient patient = new Patient("Aandi", "Acute", "1981/01/01");
    try {
      staff.assignToPatient(patient);
      fail("Expected IllegalStateException but not happening.");
    } catch (IllegalStateException e) {
      // expected exception
      assertEquals("This staff has been deactivated.", e.getMessage());
    }
  }
  
  /**
   * Test toString() for a formatted string representing 
   * the title and name of the clinical staff member.
   */
  @Test
  public void testToString() {
    // A physician with a title "Dr."
    ClinicalStaff staff1 = 
        new ClinicalStaff("PHYSICIAN", "Amy", "Anguish", "DOCTORAL", "1234567890");
    assertEquals("Dr. Amy Anguish", staff1.toString());
    
    // A nurse with a title "Nurse"
    ClinicalStaff staff2 = 
        new ClinicalStaff("NURSE", "Camila", "Crisis", "MASTERS", "2224443338");
    assertEquals("Nurse Camila Crisis", staff2.toString());
  }
}
