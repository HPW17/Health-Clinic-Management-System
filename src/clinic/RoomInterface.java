package clinic;

/**
 * This interface represents the model of a {@link Room} in the {@link Clinic}.
 */
public interface RoomInterface {

  /**
   * Retrieves the ID of this room.
   * 
   * @return the ID of the room
   */
  public int getId();
  
  /**
   * Retrieves the current position represented by two coordinate points, the 
   * lower left corner (left, bottom) and upper right corner (right, top).
   * 
   * @return an array of integers (4 elements) representing the coordinates of two points.
   */
  public int[] getPosition();
  
  /**
   * Retrieves the name of the room.
   * 
   * @return the name of the room
   */
  public String getRoomName();
  
  /**
   * Retrieves the room type of the room, as defined in the enumeration RoomType.
   * 
   * @return a string representing the room type
   */
  public String getRoomType();

}
