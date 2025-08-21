package view;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.RoomInterface;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.swing.JPanel;

/**
 * This class extends {@link JPanel} and implements {@link ClinicPanelInterface} 
 * to draw the clinic image with rooms and patients. 
 */
public class ClinicPanel extends JPanel implements ClinicPanelInterface {

  private static final long serialVersionUID = 1L;
  private static final int PROPER_IMAGE_WIDTH = 800;
  private static final int IMAGE_GAP = 50;
  private final ClinicInterface model;
  private final Map<Rectangle, Integer> roomMap = new HashMap<>();
  private final Map<Rectangle, Integer> patientMap = new HashMap<>();
  private final Map<Integer, String> roomName = new HashMap<>();
  private final Map<Integer, String> patientName = new HashMap<>();
  private int highlightedRoomId = -1;
  private int highlightedPatientId = -1;
  private int selectType = -1;
  private int imageWidth;
  private int imageHeight;
  private int panelWidth;
  private int panelHeight;
  private int xgap;
  private int ygap;
  
  /**
   * Constructor of ClinicPanel. 
   * 
   * @param m the clinic model
   */
  public ClinicPanel(ClinicInterface m) {
    model = m;
    
    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        handleMouseHover(e.getPoint());
      }
    });
  }
  
  @Override
  public int handleMouseClick(Point point) {
    if (selectType == 0) { // room
      for (Entry<Rectangle, Integer> room : roomMap.entrySet()) {
        if (room.getKey().contains(point)) {
          return room.getValue();
        }
      }
      return -1;
    } else if (selectType == 1) { // patient
      for (Entry<Rectangle, Integer> patient : patientMap.entrySet()) {
        if (patient.getKey().contains(point)) {
          return patient.getValue();
        }
      }
      return -1;
    } else {
      return -1;
    }
  }
  
  @Override
  public String getPatientName(int id) {
    return patientName.getOrDefault(id, null);
  }
  
  @Override
  public String getRoomName(int id) {
    return roomName.getOrDefault(id, null);
  }
  
  /**
   * Helper method to handle mouse hover rooms and patients.
   * 
   * @param point the mouse location
   */
  private void handleMouseHover(Point point) {
    boolean patientHovered = false;
    boolean roomHovered = false;
    
    if (selectType == 1) { // patient
      for (Entry<Rectangle, Integer> patient : patientMap.entrySet()) {
        if (patient.getKey().contains(point)) {
          if (highlightedPatientId != patient.getValue()) {
            highlightedPatientId = patient.getValue();
            repaint();
          }
          setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          patientHovered = true;
          break;
        }
      }
      if (!patientHovered && highlightedPatientId != -1) {
        highlightedPatientId = -1;
        repaint();
      }
    
    } else if (selectType == 0) { // room
      for (Entry<Rectangle, Integer> room : roomMap.entrySet()) {
        if (room.getKey().contains(point)) {
          if (highlightedRoomId != room.getValue()) {
            highlightedRoomId = room.getValue();
            repaint();
          }
          setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          roomHovered = true;
          break;
        }
      }
      if (!roomHovered && highlightedRoomId != -1) {
        highlightedRoomId = -1;
        repaint();
      }
    }
    
    if (!patientHovered && !roomHovered) {
      setCursor(Cursor.getDefaultCursor());
    }
  }
  
  @Override
  public void setSelectType(int type) {
    selectType = type;
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0,  0, getWidth(), getHeight());
    
    if (model.getRooms().isEmpty()) {
      int fontSize;
      int fontGap;
      g2d.setColor(Color.BLACK);
      fontSize = (int) (14 + (Math.min(getWidth(), 520) - 300.0) / (520 - 300) * (24 - 14));
      fontGap = (int) (30 + (Math.min(getHeight(), 380) - 300.0) / (380 - 300) * (40 - 30));
      g2d.setFont(new Font("Tahoma", Font.BOLD, fontSize));
      FontMetrics metrics = g2d.getFontMetrics();
      String welcome = "Welcome to the Clinic Management System";
      g2d.drawString(welcome, 
          (getWidth() - metrics.stringWidth(welcome)) / 2, 
          (getHeight() + fontSize) / 2 - fontGap);
      fontSize = (int) (10 + (Math.min(getWidth(), 520) - 300.0) / (520 - 300) * (16 - 10));
      g2d.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
      metrics = g2d.getFontMetrics();
      String credit = "(c) 2024 Developed by Hao-Peng Wang";
      g2d.drawString(credit, 
          (getWidth() - metrics.stringWidth(credit)) / 2, 
          (getHeight() + fontSize) / 2 + fontGap);
    } else {
      panelWidth = getWidth();
      panelHeight = getHeight();
      roomMap.clear();
      patientMap.clear();
      drawMap(g2d);
    }
  }
  
  /**
   * Helper method to draw the content of the clinic image.
   * 
   * @param g the Graphics of the JPanel
   */
  private void drawMap(Graphics2D g) {
    // Find the minimum and maximum x and y coordinates
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    List<RoomInterface> rooms = model.getRooms();
    for (RoomInterface room : rooms) {
      minX = Math.min(minX, room.getPosition()[0]); // left
      minY = Math.min(minY, room.getPosition()[1]); // bottom
      maxX = Math.max(maxX, room.getPosition()[2]); // right
      maxY = Math.max(maxY, room.getPosition()[3]); // top
    }
    
    // Find proper scale and height then create graphics
    int scale = PROPER_IMAGE_WIDTH / (maxX - minX);
    imageWidth = (maxX - minX) * scale + IMAGE_GAP * 2; // Surrounds by a gap
    imageHeight = (maxY - minY) * scale + IMAGE_GAP * 2;
    
    // Establish the background of clinic layout
    drawClinicBackground(g, imageWidth, imageHeight, maxX, maxY, scale, model.getName());
    
    // Draw rooms
    List<PatientInterface> patients = model.getPatients();
    for (RoomInterface room : rooms) {
      drawRoom(g, scale, room, patients);
    }
    g.dispose();
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
  private void drawClinicBackground(
      Graphics2D g, int imageWidth, int imageHeight, 
      int maxX, int maxY, int scale, String name) {
    g.setColor(new Color(240, 240, 255, 255));
    g.fillRect(0, 0, panelWidth, panelHeight);
    xgap = panelWidth > imageWidth ? (panelWidth - imageWidth) / 2 : 0;
    ygap = panelHeight > imageHeight ? (panelHeight - imageHeight) / 2 : 0;
    g.setColor(Color.WHITE);
    g.fillRect(xgap, ygap, imageWidth, imageHeight);
    g.setColor(Color.BLACK);
    g.setStroke(new BasicStroke(1));
    g.drawRect(xgap + 40, ygap + 40, imageWidth - 80, imageHeight - 80);
    g.setFont(new Font("Tahoma", Font.BOLD, 18));
    FontMetrics metrics = g.getFontMetrics();
    g.drawString(name, xgap + (imageWidth - metrics.stringWidth(name)) / 2, ygap + 38);
  }
  
  /**
   * Helper method to draw a room, including its border, number, name, type, and patients.
   * 
   * @param g the graphics object from BufferedImage
   * @param scale the proper scale based on user-defined coordinates and PROPER_IMAGE_WIDTH
   * @param room the target room
   * @param patients the list of patients in clinic
   */
  private void drawRoom(
      Graphics2D g, int scale, RoomInterface room, List<PatientInterface> patients) {
    
    int left = xgap + room.getPosition()[0] * scale;
    int bottom = ygap + room.getPosition()[1] * scale;
    int right = xgap + room.getPosition()[2] * scale;
    int top = ygap + room.getPosition()[3] * scale;
    roomMap.put(new Rectangle(left + IMAGE_GAP, bottom + IMAGE_GAP, right - left, top - bottom), 
        room.getId());
    roomName.put(room.getId(), room.getRoomName());
    
    int textX = left + IMAGE_GAP;
    int textY = bottom + IMAGE_GAP;
    drawRoomBorder(g, room, left, bottom, right, top);
    drawRoomNumber(g, room, left, bottom, right, top, textX, textY);
    textY += (18 + 10);
    drawRoomName(g, room, left, bottom, right, top, textX, textY);
    textY += (14 + 5);
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
  private void drawRoomBorder(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top) {
    if (room.getId() == highlightedRoomId) {
      g.setColor(new Color(255, 255, 200, 255));
      g.fillRect(left + IMAGE_GAP, bottom + IMAGE_GAP, right - left, top - bottom);
    }
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(left + IMAGE_GAP, bottom + IMAGE_GAP, right - left, 18 + 10);
    g.setColor(Color.BLACK);
    g.setStroke(new BasicStroke(3));
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
  private void drawRoomNumber(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    g.setColor(Color.RED); 
    g.setFont(new Font("Tahoma", Font.BOLD, 18));
    FontMetrics metrics = g.getFontMetrics();
    String roomNumber = String.valueOf(room.getId());
    g.drawString(roomNumber, 
        textX + (right - left - metrics.stringWidth(roomNumber)) / 2, 
        textY + 18);
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
  private void drawRoomName(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    g.setColor(Color.BLUE); 
    g.setFont(new Font("Tahoma", Font.BOLD, 14));
    FontMetrics metrics = g.getFontMetrics();
    String roomName = room.getRoomName();
    g.drawString(
        roomName, 
        textX + (right - left - metrics.stringWidth(roomName)) / 2, 
        textY + 14);
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
  private void drawRoomType(Graphics2D g, RoomInterface room, 
      int left, int bottom, int right, int top, int textX, int textY) {
    String roomType = String.format("(%s)", room.getRoomType());
    g.setFont(new Font("Tahoma", Font.ITALIC, 12));
    FontMetrics metrics = g.getFontMetrics();
    g.drawString(
        roomType, 
        textX + (right - left - metrics.stringWidth(roomType)) / 2, 
        textY + 12);
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
  private void drawPatientsInRoom(Graphics2D g, 
      RoomInterface room, List<PatientInterface> patients, 
      int left, int bottom, int right, int top, int textX, int textY) {
    List<PatientInterface> patientsInRoom = patients.stream()
        .filter(p -> room.equals(p.getAssignedRoom())).collect(Collectors.toList());
    int patientsCount = patientsInRoom.size();
    g.setColor(Color.BLACK); 
    if (patientsCount == 0) {
      String empty = "(Empty)";
      g.setFont(new Font("Tahoma", Font.BOLD, 14));
      FontMetrics metrics = g.getFontMetrics();
      g.drawString(
          empty, 
          textX + (right - left - metrics.stringWidth(empty)) / 2, 
          textY + (top - textY + 14) / 2);
    } else {
      int nameWidth = 100;
      int nameHeight = 40;
      int boxX = IMAGE_GAP + left + (right - left - nameWidth) / 2;
      int boxY = 10 + textY + (top - textY - 45 * patientsInRoom.size()) / 2;
      textY = boxY + 26;
      
      for (PatientInterface p : patientsInRoom) {
        g.setFont(new Font("Tahoma", Font.PLAIN, 14));
        FontMetrics metrics = g.getFontMetrics();
        String name = String.format("%s %s", p.getFirstName(), p.getLastName());
        textX = boxX + (nameWidth - metrics.stringWidth(name)) / 2;
        patientMap.put(new Rectangle(boxX, boxY, nameWidth, nameHeight), p.getId());
        patientName.put(p.getId(), p.getFirstName() + " " + p.getLastName());
        if (p.getId() == highlightedPatientId) {
          g.setColor(new Color(255, 255, 200, 255));
          g.fillRect(boxX, boxY, nameWidth, nameHeight);
          g.setColor(Color.RED);
        } else {
          g.setColor(Color.BLACK);
        }
        g.setStroke(new BasicStroke(1));
        g.drawRect(boxX, boxY, nameWidth, nameHeight);
        g.drawString(name, textX, textY);
        boxY += 45;
        textY += 45;
      }
    }
  }
  
  /**
   * Override getPreferredSize to help scrollPane understand the actual image size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(imageWidth, imageHeight);
  }
  
  @Override
  public void clearState() {
    highlightedRoomId = -1;
    highlightedPatientId = -1;
    selectType = -1;
    for (MouseListener listener : getMouseListeners()) {
      removeMouseListener(listener);
    }
  }
}
