package clinic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * This class implements {@link VisitRecordInterface} and represents a record for 
 * a patient's visit to the clinic. 
 
 * This class is immutable, meaning the values cannot be changed after initialization.
 */
public class VisitRecord implements VisitRecordInterface {
  private final LocalDateTime registrationDateTime;
  private final String chiefComplaint;
  private final double bodyTemperature;

  /**
   * Constructs a new patient visit record.
   *
   * @param registrationDateTime the date and time of registration
   * @param chiefComplaint the patient's chief complaint
   * @param bodyTemperature the patient's body temperature in degrees Celsius
   */
  public VisitRecord(String registrationDateTime, 
      String chiefComplaint, double bodyTemperature) {
    LocalDateTime regDt;
    
    // Validate registrationDateTime
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
      regDt = LocalDateTime.parse(registrationDateTime, formatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date/time format (yyyy/MM/dd HH:mm).");
    }
    // Assuming a reasonable range for visit date is year 2000 till now
    if (regDt.isAfter(LocalDateTime.now()) 
        || regDt.isBefore(LocalDateTime.of(2000, 1, 1, 0, 0))) {
      throw new IllegalArgumentException("Unreasonable visit date/time.");
    }
    
    // Assuming a reasonable range (including illness) for body temperature is 30-45C
    if (bodyTemperature < 30 || bodyTemperature > 45) {
      throw new IllegalArgumentException("Unreasonable body temperature.");
    }
    
    this.registrationDateTime = regDt;
    this.chiefComplaint = chiefComplaint;
    this.bodyTemperature = bodyTemperature;
  }

  @Override
  public LocalDateTime getRegistrationDateTime() {
    return registrationDateTime;
  }

  @Override
  public String getChiefComplaint() {
    return chiefComplaint;
  }

  @Override
  public double getBodyTemperature() {
    return bodyTemperature;
  }
  
  @Override
  public String getFormattedBodyTemperature() {
    return String.format("%.1f", bodyTemperature);
  }

  /** 
   * Returns a formatted string representation of the patient's visit record.
   * Example:
   * Visit on: 2024/10/10 09:30, Body Temperature: 37.1°C, Chief Complaint: Headaches
   *
   * @return a string containing the visit details 
   */
  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    return String.format("Visit on: %s, Body Temperature: %.1f°C, Chief Complaint: %s",
        registrationDateTime.format(formatter), bodyTemperature, chiefComplaint);
  }
  
  /**
   * Compares to another object for equality.
   * They are considered equal when all key fields are equal.
   * 
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof VisitRecord)) {
      return false;
    }
    VisitRecord that = (VisitRecord) o;
    return Objects.equals(this.registrationDateTime, that.registrationDateTime) 
        && Objects.equals(this.chiefComplaint, that.chiefComplaint) 
        && Math.abs(this.bodyTemperature - that.bodyTemperature) < 0.01;
  }

  /**
   * Calculates a hash code for this object based on its key fields.
   * 
   * @return the hash code for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(registrationDateTime, chiefComplaint, bodyTemperature);
  }
  
  @Override
  public int compareTo(VisitRecordInterface other) {
    return this.registrationDateTime.compareTo(((VisitRecord) other).registrationDateTime);
  }
  
}
