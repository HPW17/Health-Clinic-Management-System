package commands;

import clinic.ClinicInterface;
import clinic.Patient;
import clinic.PatientInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which registers a new patient and enters a visit record.
 */
public class RegisterNewPatient extends JFrame implements CommandInterface {

  private static final long serialVersionUID = 1L;
  private final JPanel mainPanel;
  private final GridBagConstraints gbc;
  private final JTextField firstNameField;
  private final JTextField lastNameField;
  private final JSpinner dobSpinner;
  private final JSpinner visitDateSpinner;
  private final JSpinner visitTimeSpinner;
  private final JTextField bodyTempField;
  private final JTextField chiefComplaintField;
  private JTable patientTable;
  private DefaultTableModel patientTableModel;
  
  /**
   * Default constructor of RegisterNewPatient.
   */
  public RegisterNewPatient() {
    mainPanel = new JPanel();
    gbc = new GridBagConstraints();
    firstNameField = new JTextField();
    lastNameField = new JTextField();
    dobSpinner = createDateSpinner(false);
    visitDateSpinner = createDateSpinner(true);
    visitTimeSpinner = createTimeSpinner();
    bodyTempField = new JTextField();
    chiefComplaintField = new JTextField();
    bodyTempField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        String text = bodyTempField.getText();
        // Allow digits and a single decimal point
        if (!(Character.isDigit(c) || (c == '.' && !text.contains(".")))) {
          e.consume(); // Ignore
        }
      }
    });
    
    setTitle("Register New Patient");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(550, 800);
    setMinimumSize(new Dimension(500, 400));
    setLocationRelativeTo(null);
    
    mainPanel.setLayout(new GridBagLayout());
    mainPanel.setBackground(Color.WHITE);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 20, 10, 20); // top, left, bottom, right
    gbc.weightx = 1.0;
    
    JScrollPane scrollPane = new JScrollPane(mainPanel, 
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setMinimumSize(new Dimension(400, 400));
    scrollPane.setMaximumSize(new Dimension(800, 900));
    setLayout(new BorderLayout(20, 20));
    add(scrollPane, BorderLayout.CENTER);
  }

  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    v.disableMenu();
    addTitle(0, "List of Existing Patients");
    addPatientTable(1, m);
    addTitle(2, "Enter New Patient Data");
    addFormItem(3, "First Name:", firstNameField);
    addFormItem(4, "Last Name:", lastNameField);
    addFormItem(5, "Date of Birth:", dobSpinner);
    addSeparator(6);
    addFormItem(7, "Visit Date:", visitDateSpinner);
    addFormItem(8, "Visit Time:", visitTimeSpinner);
    addFormItem(9, "Body Temp (°C):", bodyTempField);
    addFormItem(10, "Chief Complaint:", chiefComplaintField);
    addSeparator(11);
    addButtonPanel(12, m, v);
    setVisible(true);
    
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
   * @param gridy the gridy of the GridBagLayout
   * @param title the text of the title
   */
  private void addTitle(int gridy, String title) {
    JLabel label = new JLabel(title, SwingConstants.CENTER);
    label.setFont(new Font("Tahoma", Font.BOLD, 16));
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 2;
    mainPanel.add(label, gbc);
  }
  
  /**
   * Helper method to construct the existing patients table.
   * 
   * @param gridy the gridy of the GridBagLayout
   * @param m the Clinic model
   */
  private void addPatientTable(int gridy, ClinicInterface m) {
    List<PatientInterface> patients = m.getPatients();
    Object[][] data = new Object[patients.size()][4];
    int row = 0;
    for (PatientInterface p : patients) {
      data[row][0] = p.getId();
      data[row][1] = p.getFirstName() + " " + p.getLastName();
      data[row][2] = p.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
      data[row][3] = p.getAssignedRoom() == null ? "" : p.getAssignedRoom().getId();
      row++;
    }
    String[] columnNames = { "ID", "Name", "Date Of Birth", "Room" };
    patientTableModel = new DefaultTableModel(columnNames, 0);
    patientTable = new JTable(patientTableModel);
    patientTable.setPreferredScrollableViewportSize(new Dimension(400, 150));
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
    //table.setDefaultEditor(Object.class, null);
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 2;
    JScrollPane scrollPane = new JScrollPane(patientTable);
    mainPanel.add(scrollPane, gbc);
    updatePatientTable(m);
  }
  
  /**
   * Helper method to refresh the patients table with updated data.
   * 
   * @param m the Clinic model
   */
  private void updatePatientTable(ClinicInterface m) {
    patientTableModel.setRowCount(0); // Clear existing rows
    List<PatientInterface> patients = m.getPatients();
    for (PatientInterface p : patients) {
      Object[] row = {
        p.getId(),
        p.getFirstName() + " " + p.getLastName(),
        p.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
        (p.getAssignedRoom() == null ? "" : p.getAssignedRoom().getId())
      };
      patientTableModel.addRow(row);
    }
  }
  
  /**
   * Helper method to add a pair of a label and an input field.
   * 
   * @param gridy the gridy of the GridBagLayout
   * @param label the text of the label
   * @param formItem the input field, can be a JTextField or a JSpinner
   */
  private void addFormItem(int gridy, String label, JComponent formItem) {
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 1;
    gbc.weightx = 0;
    mainPanel.add(new JLabel(label), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    formItem.setPreferredSize(new Dimension(
        formItem.getPreferredSize().width, (int) (formItem.getPreferredSize().height * 1.5)));
    mainPanel.add(formItem, gbc);
  }
  
  /**
   * Helper method to add a separator line.
   * 
   * @param gridy the gridy of the GridBagLayout
   */
  private void addSeparator(int gridy) {
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 2;
    mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
  }
  
  /**
   * Helper method to add the button panel, including Register, Reset, and Cancel buttons.
   * 
   * @param gridy the gridy of the GridBagLayout
   * @param m the Clinic model
   * @param v the Clinic view
   */
  private void addButtonPanel(int gridy, ClinicInterface m, ClinicViewInterface v) {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(Color.WHITE);
    
    JButton registerButton = new JButton("Register");
    registerButton.addActionListener(e -> {
      try {
        String firstName = toTitleCase(firstNameField.getText().trim());
        String lastName = toTitleCase(lastNameField.getText().trim());
        String dob = new java.text.SimpleDateFormat("yyyy/MM/dd").format(dobSpinner.getValue());
        String visitTime = new java.text.SimpleDateFormat("yyyy/MM/dd")
            .format(visitDateSpinner.getValue()) + " "
            + new java.text.SimpleDateFormat("HH:mm").format(visitTimeSpinner.getValue());
        String complaint = toTitleCase(chiefComplaintField.getText().trim());
        double temperature = Double.parseDouble(bodyTempField.getText());

        PatientInterface patient = new Patient(firstName, lastName, dob);
        patient.addVisitRecord(visitTime, complaint, temperature);
        patient.assignToRoom(m.getRooms().get(0));
        m.registerPatient(patient);

        v.setStatus("Patient registered successfully.", 0);
        v.refresh();
        updatePatientTable(m);
        mainPanel.repaint();
        JOptionPane.showMessageDialog(this, 
            "Patient registered successfully.\n" 
            + "Patient: " + firstName + " " + lastName + ", " + dob 
            + "\nVisit: " + visitTime + ", " + temperature + "°C, " + complaint, 
            "Registration Complete", 
            JOptionPane.INFORMATION_MESSAGE);
        resetFields();
        //dispose();
      } catch (IllegalArgumentException ex) {
        v.setStatus("Error registering patient: " + ex.getMessage(), 2);
        JOptionPane.showMessageDialog(this, 
            "Error registering patient: " + ex.getMessage(), 
            "Registration Error", 
            JOptionPane.ERROR_MESSAGE);
      }
    });
    
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener(e -> {
      v.setStatus(" ", 0);
      firstNameField.setText("");
      lastNameField.setText("");
      Calendar calendar = Calendar.getInstance();
      calendar.set(1990, 0, 1);
      dobSpinner.setValue(calendar.getTime());
      visitDateSpinner.setValue(new java.util.Date());
      visitTimeSpinner.setValue(new java.util.Date());
      bodyTempField.setText("");
      chiefComplaintField.setText("");
    });
    
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> {
      v.clearState(2);
      v.enableMenu(); 
      v.refresh();
      dispose();
    });
    
    buttonPanel.add(registerButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(cancelButton);
    gbc.gridx = 0;
    gbc.gridy = gridy;
    gbc.gridwidth = 2;
    mainPanel.add(buttonPanel, gbc);
  }
  
  /**
   * Helper method to capitalize the first character of a string and lowercase the rest.
   * 
   * @param name the string to be formatted
   * @return formatted string
   */
  private String toTitleCase(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
  
  /**
   * Helper method to reset all input fields.
   */
  private void resetFields() {
    firstNameField.setText("");
    lastNameField.setText("");
    Calendar calendar = Calendar.getInstance();
    calendar.set(1990, 0, 1);
    dobSpinner.setValue(calendar.getTime());
    visitDateSpinner.setValue(new java.util.Date());
    visitTimeSpinner.setValue(new java.util.Date());
    bodyTempField.setText("");
    chiefComplaintField.setText("");
  }
  
  /**
   * Helper method to create a date selector input field.
   * 
   * @param defaultToToday true if default to today, false if not (to 1990/01/01)
   * @return a date spinner
   */
  private JSpinner createDateSpinner(boolean defaultToToday) {
    Calendar calendar = Calendar.getInstance();
    if (defaultToToday) {
      calendar.setTime(new java.util.Date());
    } else {
      calendar.set(1990, 0, 1);
    }
    SpinnerDateModel dateModel = new SpinnerDateModel(
        calendar.getTime(), null, null, Calendar.DAY_OF_MONTH);
    JSpinner dateSpinner = new JSpinner(dateModel);
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd");
    dateSpinner.setEditor(dateEditor);
    return dateSpinner;
  }
  
  /**
   * Helper method to create a time selector input field.
   * 
   * @return a time spinner
   */
  private JSpinner createTimeSpinner() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.util.Date());
    SpinnerDateModel timeModel = new SpinnerDateModel(
        calendar.getTime(), null, null, Calendar.HOUR_OF_DAY);
    JSpinner timeSpinner = new JSpinner(timeModel);
    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
    timeSpinner.setEditor(timeEditor);
    return timeSpinner;
  }
}
