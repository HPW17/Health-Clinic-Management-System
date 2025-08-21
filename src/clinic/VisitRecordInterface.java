package clinic;

import java.time.LocalDateTime;

/**
 * Represents a record for a patient's visit to the clinic. This record includes
 * the date and time of registration, the chief complaint, and the patient's body
 * temperature in degrees Celsius.
 */
public interface VisitRecordInterface extends Comparable<VisitRecordInterface> {

  /**
   * Gets the date and time of registration.
   *
   * @return the date and time of registration
   */
  public LocalDateTime getRegistrationDateTime();

  /**
   * Gets the patient's chief complaint.
   *
   * @return the patient's chief complaint
   */
  public String getChiefComplaint();

  /**
   * Gets the patient's body temperature in degrees Celsius.
   *
   * @return the body temperature in degrees Celsius
   */
  public double getBodyTemperature();
  
  /**
   * Gets the patient's body temperature formatted to one decimal place as a string.
   *
   * @return the body temperature formatted as a string with one decimal place
   */
  public String getFormattedBodyTemperature();
  
}
