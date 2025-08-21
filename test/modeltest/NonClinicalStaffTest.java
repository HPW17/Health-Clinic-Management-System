package modeltest;

import static org.junit.Assert.assertEquals;

import clinic.NonClinicalStaff;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link NonClinicalStaff} class.
 */
public class NonClinicalStaffTest {

  /**
   * Test constructor with valid arguments, expect to create instance successfully.
   * Getter methods getFirstName(), getLastName(), getJobTitle(), getEducationLevel(), 
   * and getCprLevel() also tested.
   */
  @Test
  public void testValidNonClinicalStaff() {
    NonClinicalStaff staff1 = new NonClinicalStaff("RECEPTION", "Frank", "Febrile", "ALLIED", "B");
    assertEquals("Frank", staff1.getFirstName());
    assertEquals("Febrile", staff1.getLastName());
    assertEquals("RECEPTION", staff1.getJobTitle());
    assertEquals("ALLIED", staff1.getEducationLevel());
    assertEquals("B", staff1.getNpiCpr());
  }
  
  
  /**
   * Expected IllegalArgumentException due to invalid CPR level
   * (should be in predefined enum values).
   * 
   * @throws IllegalArgumentException if entered invalid CPR level
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCprLevel() throws IllegalArgumentException {
    new NonClinicalStaff("RECEPTION", "Frank", "Febrile", "ALLIED", "E");
  }
  
  
  /**
   * Test setCprLevel() to re-assign CPR level of this staff.
   */
  @Test
  public void testSetCprLevel() {
    NonClinicalStaff staff = new NonClinicalStaff("RECEPTION", "Frank", "Febrile", "ALLIED", "B");
    assertEquals("Initial CPR level", "B", staff.getNpiCpr());
    
    // re-assign CPR level
    staff.setNpiCpr("BLS");
    assertEquals("Re-assigned CPR level", "BLS", staff.getNpiCpr());
  }
  
  /**
   * Test toString() for a formatted string representing 
   * the name of the non-clinical staff member.
   */
  @Test
  public void testToString() {
    NonClinicalStaff staff = new NonClinicalStaff("RECEPTION", "Frank", "Febrile", "ALLIED", "B");
    assertEquals("Frank Febrile", staff.toString());
  }
}
