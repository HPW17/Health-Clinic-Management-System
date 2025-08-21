package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import clinic.VisitRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link VisitRecord} class.
 */
public class PatientVisitRecordTest {

  /**
   * Test constructor with valid arguments, expect to create instance successfully.
   * Getter methods getRegistrationDateTime(), getChiefComplaint(), getBodyTemperature(), 
   * getFormattedBodyTemperature(), and toString() are also tested.
   */
  @Test
  public void testValidPatientVisitRecord() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    String now = LocalDateTime.now().format(formatter);
    VisitRecord rec = new VisitRecord(now, "Sore throat", 37.1);
    assertEquals(now, rec.getRegistrationDateTime().format(formatter));
    assertEquals("Sore throat", rec.getChiefComplaint());
    assertEquals(37.1, rec.getBodyTemperature(), 0.0001);
    assertEquals("37.1", rec.getFormattedBodyTemperature());
    assertEquals("Visit on: " + now + ", Body Temperature: 37.1°C, "
        + "Chief Complaint: Sore throat", rec.toString());
    
    RandomGenerator randYear = new RandomGenerator(1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 
        2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 
        2022, 2023, 2024, 2025);
    RandomGenerator randTemp = new RandomGenerator(299, 300, 315, 330, 345, 360, 375, 390, 405, 
        420, 435, 450, 451);
    
    for (int i = 0; i < 10000; i++) {
      String randDt = String.format("%d/01/01 08:00", randYear.nextInt());
      LocalDateTime dt = LocalDateTime.parse(randDt, formatter);
      try {
        new VisitRecord(randDt, "Sore throat", 37.1);
      } catch (IllegalArgumentException e) {
        // Visit date/time is unreasonable (before year 2000, or after now)
        assertEquals("Unreasonable visit date/time.", e.getMessage());
        assertTrue(dt.isAfter(LocalDateTime.now()) 
            || dt.isBefore(LocalDateTime.of(2000, 1, 1, 0, 0)));
      }
    }
    
    for (int i = 0; i < 10000; i++) {
      Double bodyTemp = randTemp.nextInt() / 10.0;
      try {
        new VisitRecord("2024/10/10 08:00", "Sore throat", bodyTemp);
      } catch (IllegalArgumentException e) {
        // Body temperature is unreasonable, assuming reasonable range 30-45C
        assertEquals("Unreasonable body temperature.", e.getMessage());
        assertTrue(bodyTemp < 30 || bodyTemp > 45);
      }
    }
  }
  
  /**
   * Test VisitRecord with invalid arguments.
   */
  @Test
  public void testInvalidPatientVisitRecord() {
    
    // Test invalid date/time format
    try {
      new VisitRecord("2024/10/10", "Sore throat", 37.1);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date/time format (yyyy/MM/dd HH:mm).", e.getMessage());
    }
    
    try {
      new VisitRecord("YYYY/MM/DD", "Sore throat", 37.1);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date/time format (yyyy/MM/dd HH:mm).", e.getMessage());
    }
    
    // Test unreasonable date/time
    try {
      new VisitRecord("1999/12/31 09:00", "Sore throat", 37.1);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unreasonable visit date/time.", e.getMessage());
    }
    
    try {
      new VisitRecord("2026/01/01 09:00", "Sore throat", 37.1);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unreasonable visit date/time.", e.getMessage());
    }
    
    // Test unreasonable body temperature (assume possible range 30 - 45)
    try {
      new VisitRecord("2024/10/10 09:00", "Sore throat", 29.9);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unreasonable body temperature.", e.getMessage());
    }
    
    try {
      new VisitRecord("2024/10/10 09:00", "Sore throat", 45.1);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unreasonable body temperature.", e.getMessage());
    }
  }
}
