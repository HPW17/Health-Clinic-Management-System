package clinic;

/**
 * This interface represents the model of a generic person in the {@link Clinic}.
 */
public interface PersonInterface {
  
  /**
   * Retrieves the first name of this person.
   * 
   * @return the first name of the person
   */
  public String getFirstName();
  
  /**
   * Retrieves the last name of this person.
   * 
   * @return the last name of the person
   */
  public String getLastName();
  
  /**
   * Retrieves the ID of this person.
   * 
   * @return the ID of the person
   */
  public int getId();
}
