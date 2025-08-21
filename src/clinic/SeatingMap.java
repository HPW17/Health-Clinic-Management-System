package clinic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class with static drawMap method to generate the image of clinic with patients.
 */
public class SeatingMap {
  public static JFrame frame;
  
  private static final int PROPER_IMAGE_WIDTH = 800;
  private static final int IMAGE_GAP = 50;
  private static final Color CLINIC_BORDER_COLOR = Color.BLACK;
  private static final Color CLINIC_BACKGROUND_COLOR = Color.WHITE;
  private static final Color ROOM_BORDER_COLOR = Color.DARK_GRAY;
  private static final Color ROOM_NUMBER_COLOR = Color.RED;
  private static final Color ROOM_TEXT_COLOR = Color.BLUE;
  private static final Color PATIENT_TEXT_COLOR = Color.BLACK;
  private static final int FONT_SIZE_EMPHASIZE = 18;
  private static final int FONT_SIZE_TITLE = 14;
  private static final int FONT_SIZE_NORMAL = 12;
  private static final int FONT_SIZE_LITTLE = 12;
  private static final String FONT_TYPE = "Tahoma";
  private static final BasicStroke STROKE_THIN = new BasicStroke(1); 
  private static final BasicStroke STROKE_THICK = new BasicStroke(3); 
  
  /**
   * The static method to generate the image of a clinic with patients in the rooms.
   * It is self-adjustable to user-defined room coordinates.
   * 
   * @param clinic the clinic instance
   */
  public static void drawMap(ClinicInterface clinic) {
    List<RoomInterface> rooms = clinic.getRooms();
    List<PatientInterface> patients = clinic.getPatients();
    
    // Find the minimum and maximum x and y coordinates
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (RoomInterface room : rooms) {
      minX = Math.min(minX, room.getPosition()[0]); // left
      minY = Math.min(minY, room.getPosition()[1]); // bottom
      maxX = Math.max(maxX, room.getPosition()[2]); // right
      maxY = Math.max(maxY, room.getPosition()[3]); // top
    }
    
    // Find proper scale and height then create graphics
    int scale = PROPER_IMAGE_WIDTH / (maxX - minX);
    int imageWidth = (maxX - minX) * scale + IMAGE_GAP * 2; // Surrounds by a gap
    int imageHeight = (maxY - minY) * scale + IMAGE_GAP * 2;
    
    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = image.createGraphics();
    
    // Establish the background of clinic layout
    drawClinicBackground(g, imageWidth, imageHeight, maxX, maxY, scale, clinic.getName());
    
    // Draw rooms
    for (RoomInterface room : rooms) {
      drawRoom(g, scale, room, patients);
    }
    g.dispose();
    
    // Display image
    frame = new JFrame("Clinic Map");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.add(new JLabel(new ImageIcon(image)));
    frame.setSize(image.getWidth(), image.getHeight()); // Adjust size based on image dimensions
    frame.setVisible(true);

    // Save image as a file
    File outputFile;
    if (new File("./res").exists()) {
      outputFile = new File("./res/clinic_map.jpg");
    } else {
      outputFile = new File("./clinic_map.jpg");
    }

    try {
      ImageIO.write(image, "jpg", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Helper method to draw the clinic background with a border.
   * 
   * @param g the graphics object from BufferedImage
   * @param imageWidth the calculated image width
   * @param imageHeight the calculated image height
   * @param maxX the maximum x coordinate of all rooms
   * @param maxY the maximum y coordinate of all rooms
   * @param scale the proper scale based on user-defined coordinates and PROPER_IMAGE_WIDTH
   * @param name the clinic name to put on top of the image
   */
  private static void drawClinicBackground(
      Graphics2D g, int imageWidth, int imageHeight, int maxX, int maxY, int scale, String name) {
    FontMetrics metrics = g.getFontMetrics();
    g.setColor(CLINIC_BACKGROUND_COLOR);
    g.fillRect(0, 0, imageWidth, imageHeight);
    g.setColor(CLINIC_BORDER_COLOR);
    g.setStroke(STROKE_THIN);
    g.drawRect(40, 40, imageWidth - 80, imageHeight - 80);
    
    g.setFont(new Font(FONT_TYPE, Font.BOLD, FONT_SIZE_EMPHASIZE));
    g.drawString(name, (imageWidth - metrics.stringWidth(name)) / 2, 38);
  }
  
  /**
   * Helper method to draw a room, including its border, number, name, type, and patients.
   * 
   * @param g the graphics object from BufferedImage
   * @param scale the proper scale based on user-defined coordinates and PROPER_IMAGE_WIDTH
   * @param room the target room
   * @param patients the list of patients in clinic
   */
  private static void drawRoom(
      Graphics2D g, int scale, RoomInterface room, List<PatientInterface> patients) {
    
    int left = room.getPosition()[0] * scale;
    int bottom = room.getPosition()[1] * scale;
    int right = room.getPosition()[2] * scale;
    int top = room.getPosition()[3] * scale;
    int textX = left + IMAGE_GAP;
    int textY = bottom + IMAGE_GAP;
    
    drawRoomBorder(g, left, bottom, right, top);
    drawRoomNumber(g, room, left, bottom, right, top, textX, textY);
    textY += (FONT_SIZE_EMPHASIZE + 10);
    drawRoomName(g, room, left, bottom, right, top, textX, textY);
    textY += (FONT_SIZE_TITLE + 5);
    drawRoomType(g, room, left, bottom, right, top, textX, textY);
    drawPatientsInRoom(g, room, patients, left, bottom, right, top, textX, textY);
  }
  
  /**
   * Helper method to draw the border of a room.
   * 
   * @param g the graphics object from BufferedImage
   * @param left the left coordinate of the room
   * @param bottom the bottom coordinate of the room
   * @param right the right coordinate of the room
   * @param top the top coordinate of the room
   */
  private static void drawRoomBorder(Graphics2D g, int left, int bottom, int right, int top) {
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(left + IMAGE_GAP, bottom + IMAGE_GAP, right - left, FONT_SIZE_EMPHASIZE + 10);
    g.setColor(ROOM_BORDER_COLOR);
    g.setStroke(STROKE_THICK);
    g.drawRect(left + IMAGE_GAP, bottom + IMAGE_GAP, right - left, top - bottom);
  }
  
  /**
   * Helper method to draw the number of a room.
   * 
   * @param g the graphics object from BufferedImage
   * @param room the target room
   * @param left the left coordinate of the room
   * @param bottom the bottom coordinate of the room
   * @param right the right coordinate of the room
   * @param top the top coordinate of the room
   * @param textX the x coordinate of the text
   * @param textY the y coordinate of the text
   */
  private static void drawRoomNumber(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    g.setColor(ROOM_NUMBER_COLOR); 
    g.setFont(new Font(FONT_TYPE, Font.BOLD, FONT_SIZE_EMPHASIZE));
    FontMetrics metrics = g.getFontMetrics();
    String roomNumber = String.valueOf(room.getId());
    g.drawString(roomNumber, 
        textX + (right - left - metrics.stringWidth(roomNumber)) / 2, 
        textY + FONT_SIZE_EMPHASIZE);
  }
  
  /**
   * Helper method to draw the name of a room.
   * 
   * @param g the graphics object from BufferedImage
   * @param room the target room
   * @param left the left coordinate of the room
   * @param bottom the bottom coordinate of the room
   * @param right the right coordinate of the room
   * @param top the top coordinate of the room
   * @param textX the x coordinate of the text
   * @param textY the y coordinate of the text
   */
  private static void drawRoomName(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    g.setColor(ROOM_TEXT_COLOR); 
    g.setFont(new Font(FONT_TYPE, Font.BOLD, FONT_SIZE_TITLE));
    FontMetrics metrics = g.getFontMetrics();
    String roomName = room.getRoomName();
    g.drawString(
        roomName, 
        textX + (right - left - metrics.stringWidth(roomName)) / 2, 
        textY + FONT_SIZE_TITLE);
  }
  
  /**
   * Helper method to draw the type of a room.
   * 
   * @param g the graphics object from BufferedImage
   * @param room the target room
   * @param left the left coordinate of the room
   * @param bottom the bottom coordinate of the room
   * @param right the right coordinate of the room
   * @param top the top coordinate of the room
   * @param textX the x coordinate of the text
   * @param textY the y coordinate of the text
   */
  private static void drawRoomType(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    String roomType = String.format("(%s)", room.getRoomType());
    g.setFont(new Font(FONT_TYPE, Font.ITALIC, FONT_SIZE_LITTLE));
    FontMetrics metrics = g.getFontMetrics();
    g.drawString(
        roomType, 
        textX + (right - left - metrics.stringWidth(roomType)) / 2, 
        textY + FONT_SIZE_LITTLE);
  }
  
  /**
   * Helper method to draw the names of the patients in a room, each in a single line.
   * Shows '(Empty)' if there's no patients in this room.
   * 
   * @param g the graphics object from BufferedImage
   * @param room the target room
   * @param patients the list of patients in clinic
   * @param left the left coordinate of the room
   * @param bottom the bottom coordinate of the room
   * @param right the right coordinate of the room
   * @param top the top coordinate of the room
   * @param textX the x coordinate of the text
   * @param textY the y coordinate of the text
   */
  private static void drawPatientsInRoom(Graphics2D g, 
      RoomInterface room, List<PatientInterface> patients, 
      int left, int bottom, int right, int top, int textX, int textY) {
    List<PatientInterface> patientsInRoom = patients.stream()
        .filter(p -> room.equals(p.getAssignedRoom())).collect(Collectors.toList());
    int patientsCount = patientsInRoom.size();
    g.setColor(PATIENT_TEXT_COLOR); 
    if (patientsCount == 0) {
      String empty = "(Empty)";
      g.setFont(new Font(FONT_TYPE, Font.BOLD, FONT_SIZE_TITLE));
      FontMetrics metrics = g.getFontMetrics();
      g.drawString(
          empty, 
          textX + (right - left - metrics.stringWidth(empty)) / 2, 
          textY + (top - textY + FONT_SIZE_TITLE) / 2);
    } else {
      g.setFont(new Font(FONT_TYPE, Font.PLAIN, FONT_SIZE_NORMAL));
      FontMetrics metrics = g.getFontMetrics();
      Boolean leftIsSet = false;
      textY = textY + (top - textY + FONT_SIZE_NORMAL * patientsInRoom.size()) / 2;
      for (PatientInterface p : patientsInRoom) {
        String name = String.format("• %s %s", p.getFirstName(), p.getLastName());
        if (!leftIsSet) {
          textX = textX + (right - left - metrics.stringWidth(name)) / 2;
          leftIsSet = true;
        }
        g.drawString(name, textX, textY);
        textY += FONT_SIZE_NORMAL + 5;
      }
    }
  }
}
