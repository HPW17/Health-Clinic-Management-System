package commands;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.RoomInterface;
import clinic.StaffInterface;
import clinic.VisitRecordInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which displays the details about a specified patient.
 */
public class DisplayPatient extends JFrame implements CommandInterface {

  private static final long serialVersionUID = 1L;
  private final JPanel mainPanel;
  private final GridBagConstraints gbc;
  private final JTextField name = new JTextField("");
  private final JTextField dob = new JTextField("");
  private final JTextField inClinic = new JTextField("");
  private final JTextField assignedStaff = new JTextField("");
  private final JTextField everAssigned = new JTextField("");
  private JTable visitTable;
  private DefaultTableModel visitTableModel;
  
  /**
   * Default constructor of DisplayPatient.
   */
  public DisplayPatient() {
    mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 20, 5, 20); // top, left, bottom, right
    gbc.weightx = 1.0;
    
    setTitle("Display Patient");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(600, 750);
    setMinimumSize(new Dimension(500, 400));
    setLocationRelativeTo(null);
    
    JScrollPane outerPanel = new JScrollPane(mainPanel,  
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    outerPanel.setBackground(Color.WHITE);
    getContentPane().add(outerPanel, BorderLayout.CENTER);
  }
  
  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    v.clearState(2);
    v.disableMenu();
    mainPanel.removeAll();
    
    addTitle(0, "List of Existing Patients");
    addPatientTable(m, v);
    addTitle(2, "Patient Information");
    addVisitRecords();
    addButtonPanel(v);
    mainPanel.add(Box.createVerticalGlue());

    mainPanel.revalidate();
    mainPanel.repaint();
    pack();
    setVisible(true);
    v.setStatus("Click the row of the desired patient.", 1);
    
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        v.clearState(2);
        v.enableMenu(); 
        v.refresh();
      }
    });
    return "";
  }
  
  /**
   * Helper method to add a title in bold font.
   * 
   * @param title the text of the title
   */
  private void addTitle(int gridy, String title) {
    JLabel label = new JLabel(title, SwingConstants.CENTER);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setFont(new Font("Tahoma", Font.BOLD, 16));
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 2;
    //System.out.println(gbc.insets);
    gbc.insets = new Insets(20, 20, 5, 20);
    mainPanel.add(label, gbc);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    gbc.insets = new Insets(5, 20, 5, 20);
  }
  
  /**
   * Helper method to construct the existing patients table.
   * 
   * @param m the Clinic model
   */
  private void addPatientTable(ClinicInterface m, ClinicViewInterface v) {
    List<PatientInterface> patients = m.getPatients();
    Object[][] data = new Object[patients.size()][4];
    int rowNum = 0;
    for (PatientInterface p : patients) {
      data[rowNum][0] = p.getId();
      data[rowNum][1] = p.getFirstName() + " " + p.getLastName();
      data[rowNum][2] = p.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
      data[rowNum][3] = p.getAssignedRoom() == null ? "" : p.getAssignedRoom().getId();
      rowNum++;
    }
    String[] columnNames = { "ID", "Name", "Date Of Birth", "Room" };
    DefaultTableModel model = new DefaultTableModel(data, columnNames);
    JTable patientTable = new JTable(model);
    patientTable.setRowHeight(25);

    patientTable.getColumnModel().getColumn(0).setPreferredWidth(10); // ID
    patientTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Name
    patientTable.getColumnModel().getColumn(2).setPreferredWidth(80); // DOB
    patientTable.getColumnModel().getColumn(3).setPreferredWidth(30); // Room
    
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < patientTable.getColumnCount(); i++) {
      patientTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    patientTable.getTableHeader()
        .setFont(patientTable.getTableHeader().getFont().deriveFont(Font.BOLD));
    patientTable.setEnabled(false);

    JScrollPane scrollPane = new JScrollPane(patientTable,  
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(400, 250));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    mainPanel.add(scrollPane, gbc);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
    
    patientTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = patientTable.rowAtPoint(e.getPoint());
        updateVisitPanel(m, (int) patientTable.getValueAt(row, 0));
        v.setStatus(
            String.format("Displaying %s's information.", patientTable.getValueAt(row, 1)), 0);
      }
      
      @Override
      public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
      
      @Override
      public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
        patientTable.clearSelection();
      }
    });
    
    patientTable.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int row = patientTable.rowAtPoint(e.getPoint());
        patientTable.setRowSelectionInterval(row, row);
      }
    });
  }
  
  /**
   * Helper method to display the visit records of the specified patient. 
   * 
   * @param m the Clinic model
   */
  private void addVisitRecords() {
    addItem(3, "Name: ", name);
    addItem(4, "Date of Birth: ", dob);
    addItem(5, "In Clinic? ", inClinic);
    addItem(6, "Currently assigned staff: ", assignedStaff);
    addItem(7, "Ever assigned staff: ", everAssigned);
    
    String[] columnNames = { "Visit Date/Time", "Body Temp(°C)", "Chief Complaints" };
    visitTableModel = new DefaultTableModel(columnNames, 0);
    visitTable = new JTable(visitTableModel);
    visitTable.setPreferredScrollableViewportSize(new Dimension(400, 100));
    visitTable.setRowHeight(25);
    visitTable.getColumnModel().getColumn(0).setPreferredWidth(40); // Visit date/time
    visitTable.getColumnModel().getColumn(1).setPreferredWidth(10); // Body temperature
    visitTable.getColumnModel().getColumn(2).setPreferredWidth(50); // Chief complaints
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < visitTable.getColumnCount(); i++) {
      visitTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    visitTable.getTableHeader()
        .setFont(visitTable.getTableHeader().getFont().deriveFont(Font.BOLD));
    visitTable.setEnabled(false);

    JScrollPane scrollPane = new JScrollPane(visitTable,  
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(400, 100));
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(20, 20, 5, 20);
    mainPanel.add(scrollPane, gbc);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    gbc.insets = new Insets(5, 20, 5, 20);
  }
  
  /**
   * Helper method to update the information of visit records.
   * 
   * @param p the selected patient
   */
  private void updateVisitPanel(ClinicInterface m, int patientId) {
    PatientInterface selected = m.getPatients().stream()
        .filter(p -> p.getId() == Integer.valueOf(patientId)).findFirst().orElse(null);
    
    if (selected != null) {
      name.setText(selected.getFirstName() + " " + selected.getLastName());
      dob.setText(selected.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
      if (selected.getAssignedRoom() == null) {
        inClinic.setText("No");
      } else {
        RoomInterface room = selected.getAssignedRoom();
        inClinic.setText("Yes, in room " + room.getId() + "/" + room.getRoomName());
      }
      getAssignedStaff(m, selected);
      
      // Re-populate contents   
      visitTableModel.setRowCount(0); // Clear existing rows
      SortedSet<VisitRecordInterface> records = selected.getVisitRecords();
      for (VisitRecordInterface r : records) {
        Object[] row = {
            r.getRegistrationDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")), 
            r.getFormattedBodyTemperature(), 
            r.getChiefComplaint()
            };
        visitTableModel.addRow(row);
      }
      mainPanel.revalidate();
      mainPanel.repaint();
    }
  }
  
  /**
   * Helper method to get the assigned staff members to the selected patient. 
   * 
   * @param m the Clinic model
   * @param p the selected patient
   */
  private void getAssignedStaff(ClinicInterface m, PatientInterface p) {
    List<StaffInterface> staffs = m.getStaff();
    List<StaffInterface> assigned = new ArrayList<>();
    List<StaffInterface> ever = new ArrayList<>();
    
    for (StaffInterface s : staffs) {
      if (s.getAssignedPatients().contains(p)) {
        assigned.add(s);
      }
      if (s.getEverAssigned().contains(p)) {
        ever.add(s);
      }
    }
    assignedStaff.setText(assigned.isEmpty() ? "N/A" : assigned.stream()
        .map(s -> s.getId() + "/" + s.toString()).collect(Collectors.joining(", ")));
    everAssigned.setText(ever.isEmpty() ? "N/A" : ever.stream()
        .map(s -> s.getId() + "/" + s.toString()).collect(Collectors.joining(", ")));
  }
  
  /**
   * Helper method to add the button panel for the Cancel button.
   * 
   * @param v the Clinic view
   */
  private void addButtonPanel(ClinicViewInterface v) {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(Color.WHITE);
    
    JButton doneButton = new JButton("Done");
    doneButton.addActionListener(e -> {
      v.clearState(2);
      v.enableMenu(); 
      v.refresh();
      dispose();
    });
    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.gridwidth = 2;
    buttonPanel.add(doneButton);
    mainPanel.add(buttonPanel, gbc);
  }
  
  /**
   * Helper method to add a pair of two labels for an information item.
   * 
   * @param gridy the gridy of the GridBagLayout
   * @param label the text of the label
   * @param info the text of the information
   */
  private void addItem(int gridy, String label, JTextField info) {
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 1;
    gbc.weightx = 0;
    mainPanel.add(new JLabel(label), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    info.setPreferredSize(new Dimension(info.getPreferredSize().width, 30));
    info.setEditable(false);
    info.setFocusable(false);
    if (gridy == 3) { // Name
      info.setFont(new Font("Tahoma", Font.BOLD, info.getFont().getSize()));
      info.setForeground(Color.BLUE);
    }
    mainPanel.add(info, gbc);
  }
}
