package modeltest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import clinic.Room;
import org.junit.Test;

/**
 * A JUnit test class for testing the functionality of the {@link Room} class.
 */
public class RoomTest {
  
  /**
   * Test constructor with valid arguments, expect to create instance successfully.
   * Getter methods getPosition(), getRoomName(), getRoomType() are also tested.
   */
  @Test
  public void testValidRoomandAndGetters() {
    Room newRoom = new Room(5, 0, 10, 5, "WAITING", "Primary Waiting Room");
    int[] expectedResult = { 5, 0, 10, 5 };
    assertArrayEquals(expectedResult, newRoom.getPosition());
    assertEquals("Primary Waiting Room", newRoom.getRoomName());
    assertEquals("WAITING", newRoom.getRoomType());
  }
  
  /**
   * Expected IllegalArgumentException due to negative coordinates.
   * 
   * @throws IllegalArgumentException if any of the coordinates is negative
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCoordinatesNegative() throws IllegalArgumentException {
    new Room(-5, -5, -10, -10, "WAITING", "Primary Waiting Room");
  }
  
  /**
   * Expected IllegalArgumentException due to lower-left coordinates > upper-right coordinates.
   * 
   * @throws IllegalArgumentException if invalid coordinates
   */ 
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCoordinatesWrongPosition() throws IllegalArgumentException {
    new Room(20, 15, 10, 5, "WAITING", "Primary Waiting Room");
  }
  
  /**
   * Expected IllegalArgumentException
   * due to invalid room type (should be in predefined enum values).
   * 
   * @throws IllegalArgumentException if entered invalid room type
   */ 
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRoomType() throws IllegalArgumentException {
    new Room(5, 0, 10, 5, "LIVING", "Living Room");
  } 
  
  /**
   * Test toString() for a formatted string representation of the room.
   */
  @Test
  public void testToString() {
    Room newRoom = new Room(5, 0, 10, 5, "WAITING", "Primary Waiting Room");
    assertEquals("Room Name: Primary Waiting Room | Room Type: WAITING", newRoom.toString());
  }

}
