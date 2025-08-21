package clinic;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents the rooms in the clinic. It is defined with the coordinates of the 
 * lower-left and upper-right corners, its name, and its room type.
 */
public class Room implements RoomInterface {
  private static int lastNumberAssigned = 0;
  
  private final int roomId;
  private final int left; // lower-left x coordinates
  private final int bottom; // lower-left y coordinates
  private final int right; // upper-right x coordinates
  private final int top; // upper-right y coordinates
  private final String roomName;
  private final RoomType roomType;

  /**
   * Constructor which initialize all the fields. 
   * 
   * @param left the x coordinate of the lower-left corner
   * @param bottom the y coordinate of the lower-left corner
   * @param right the x coordinate of the upper-right corner
   * @param top the y coordinate of the upper-right corner
   * @param roomType the room type defined in enumeration RoomType
   * @param roomName the name of the room
   */
  public Room(int left, int bottom, int right, int top, String roomType, String roomName) 
      throws IllegalArgumentException {
    // The coordinates should be positive and valid
    if (left < 0 || bottom < 0 || right < 0 || top < 0 || left >= right || bottom >= top) {
      throw new IllegalArgumentException("Invalid room coordinates.");
    }
    // The room type should be valid as defined in enumeration RoomType
    ArrayList<String> roomTypeNames = new ArrayList<>();
    for (RoomType r : RoomType.values()) {
      roomTypeNames.add(r.name());
    }
    if (!roomTypeNames.contains(roomType.toUpperCase())) {
      throw new IllegalArgumentException("Invalid room type.");
    }
    // Arguments are valid, create an instance
    this.roomId = ++lastNumberAssigned;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    this.top = top;
    this.roomType = RoomType.valueOf(roomType.toUpperCase());
    this.roomName = roomName;
  }
  
  /**
   * Copy constructor, creates a new Room object that is a copy of the given one.
   * 
   * @param other the room to be copied
   */
  public Room(Room other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot create a copy of a null Room object.");
    }
    this.left = other.left;
    this.bottom = other.bottom;
    this.right = other.right;
    this.top = other.top;
    this.roomType = other.roomType;
    this.roomName = other.roomName;
    this.roomId = other.roomId;
  }

  @Override
  public int getId() {
    return roomId;
  }
  
  @Override
  public int[] getPosition() {
    int[] coordinates = new int[4];
    coordinates[0] = left;
    coordinates[1] = bottom;
    coordinates[2] = right;
    coordinates[3] = top;
    return coordinates;
  }
  
  @Override
  public String getRoomName() {
    return roomName;
  }
  
  @Override
  public String getRoomType() {
    return roomType.name();
  }
  
  /** 
   * Returns a formatted string representation of the room.
   * 
   * Examples:
   * Room Name: Front Waiting Room | Room Type: WAITING
   * or
   * Room Name: Triage | Room Type: EXAM
   *
   * @return a string containing the information of the room. 
   */
  @Override
  public String toString() {
    return String.format("Room Name: %s | Room Type: %s", roomName, roomType.name());
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof Room)) {
      return false;
    }
    Room that = (Room) o;
    return this.roomId == that.roomId
        && this.left == that.left
        && this.bottom == that.bottom
        && this.right == that.right
        && this.top == that.top
        && Objects.equals(this.roomType, that.roomType)
        && Objects.equals(this.roomName, that.roomName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, bottom, right, top, roomType, roomName, roomId);
  }
  
  /**
   * This is for JUnit testing purpose to refresh state each run, 
   * therefore it is not included in the contract defined by RoomInterface.
   */
  public static void clearLastNumberAssigned() {
    lastNumberAssigned = 0;
  }

  /**
   * An enumeration representing three different types of rooms in the clinic.
   */
  public enum RoomType {
    EXAM, 
    PROCEDURE, 
    WAITING
  }
}
