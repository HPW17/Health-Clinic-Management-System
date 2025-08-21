package commands;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.StaffInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import view.ClinicPanel;
import view.ClinicView;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which assigns selected staff members to a patient.
 */
public class AssignStaffToPatient extends JFrame implements CommandInterface {
  
  private static final long serialVersionUID = 1L;
  private final JPanel mainPanel;
  private JTable staffTable;
  private DefaultTableModel staffTableModel;
  private PatientInterface selectedPatient;
  private SelectionState currentState = SelectionState.SELECT_PATIENT;
  
  /**
   * Default constructor of AssignStaffToPatient.
   */
  public AssignStaffToPatient() {
    setTitle("Assign Staff to Patient");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(700, 400);
    setMinimumSize(new Dimension(500, 300));
    setLocationRelativeTo(null);

    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);
    JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.setBackground(Color.WHITE);
    outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    outerPanel.add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(outerPanel, BorderLayout.CENTER);
  }
  
  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    
    ClinicPanel panel = ((ClinicView) v).getPanel();
    v.clearState(2);
    v.disableMenu();
    
    panel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int id = panel.handleMouseClick(e.getPoint());
        if (id != -1) {
          switch (currentState) {
            case SELECT_PATIENT:
              v.setStatus("Select the staff member(s) to be assigned to this patient.", 1);
              JOptionPane.showMessageDialog(null, 
                  String.format("You selected patient [%d] %s.\n" 
                  + "Select the staff member(s) to be assigned to this patient.", 
                  id, panel.getPatientName(id)),
                  "Assign a staff member to a patient", 
                  JOptionPane.INFORMATION_MESSAGE);
              selectedPatient = m.getPatients().stream()
                  .filter(p -> p.getId() == id).findFirst().orElse(null);
              loadStaffSelector(m, v);
              setVisible(true); 
              currentState = SelectionState.SELECT_STAFF;
              break;
            
            case SELECT_STAFF:
              currentState = SelectionState.DONE;
              v.clearState(2);
              v.enableMenu();
              v.refresh();
              break;
            
            default:
              break;
          }
        }
      }
    });
    
    // Initial prompt to the user
    v.setStatus("Select a patient by clicking on its name.", 1);
    JOptionPane.showMessageDialog(null, 
        "Select a patient by clicking on its name.",
        "Select a patient", 
        JOptionPane.INFORMATION_MESSAGE);
    v.resetSelectedPatientId();
    v.setSelectType(1);
    
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
   * Helper method to generate a list of active staff members as selector.
   * Allows multiple selection by clicking on more than one check boxes. 
   * 
   * @param m the Clinic model
   * @param v the Clinic view
   */
  private void loadStaffSelector(ClinicInterface m, ClinicViewInterface v) {
    List<StaffInterface> activeStaffMembers = m.getStaff().stream()
        .filter(s -> s.isActive() && s.getNpiCpr().length() == 10)
        .collect(Collectors.toList());
    String[] columns = { "", "ID", "Name", "Job Title", "Assigned Patients" };
    staffTableModel = new DefaultTableModel(columns, 0) {
      private static final long serialVersionUID = 1L;
      
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
      }
      
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 0; // Only checkbox column is editable
      }
    };
    for (StaffInterface s : activeStaffMembers) {
      String assignedPatients = s.getAssignedPatients().stream()
          .map(p -> String.format("%d/%s %s", p.getId(), p.getFirstName(), p.getLastName()))
          .collect(Collectors.joining(", "));
      staffTableModel.addRow(new Object[] {
          false, s.getId(), s.toString(), toTitleCase(s.getJobTitle()), assignedPatients});
    }
    staffTable = new JTable(staffTableModel);
    staffTable.setRowHeight(25);
    JScrollPane scrollPane = new JScrollPane(staffTable, 
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(Color.WHITE);
    scrollPane.setMinimumSize(new Dimension(700, 400));
    //staffTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    //staffTable.setFillsViewportHeight(true);
    
    staffTable.getColumnModel().getColumn(0).setPreferredWidth(30); // Checkbox
    staffTable.getColumnModel().getColumn(1).setPreferredWidth(30); // ID
    staffTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Name
    staffTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Job title
    staffTable.getColumnModel().getColumn(4).setPreferredWidth(300); // Assigned patients
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 1; i < staffTable.getColumnCount(); i++) {
      staffTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    staffTable.getTableHeader()
        .setFont(staffTable.getTableHeader().getFont().deriveFont(Font.BOLD));
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(Color.WHITE);
    
    JButton assignButton = new JButton("Assign");
    assignButton.addActionListener(e -> {
      assignStaff(activeStaffMembers, m, v);
      v.clearState(2);
      v.enableMenu();
      v.refresh();
    });
    
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener(e -> resetSelection(v));
    
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> {
      v.clearState(2);
      v.enableMenu(); 
      v.refresh();
      dispose();
    });
    
    buttonPanel.add(assignButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(cancelButton);

    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    validate();
    repaint();
  }
  
  /**
   * Helper method to assign selected staff members to the specified patient.
   * 
   * @param activeStaffMembers the list of staff members with selection
   * @param m the Clinic model
   * @param v the Clinic view
   */
  private void assignStaff(List<StaffInterface> activeStaffMembers, 
      ClinicInterface m, ClinicViewInterface v) {
    List<StaffInterface> selectedStaff = new ArrayList<>();
    for (int i = 0; i < staffTableModel.getRowCount(); i++) {
      if ((Boolean) staffTableModel.getValueAt(i, 0)) {
        selectedStaff.add(activeStaffMembers.get(i));
      }
    }
    if (selectedStaff.isEmpty()) {
      v.setStatus("Please select at least one staff member.", 2);
      JOptionPane.showMessageDialog(this, 
          "No staff selected. Please select at least one staff member.", 
          "Error", 
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    String message = "Staff member(s):\n";
    for (StaffInterface s : selectedStaff) {
      s.assignToPatient(selectedPatient);
      message += String.format("[%d] %s\n", s.getId(), s.toString());
    }
    v.setStatus("Assigned staff member(s) to patient successfully.", 0);
    JOptionPane.showMessageDialog(this, 
        message + String.format("successfully assigned to patient [%d] %s %s.", 
            selectedPatient.getId(), selectedPatient.getFirstName(), selectedPatient.getLastName()),
        "Success", 
        JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }
  
  /**
   * Helper method to reset selection.
   * 
   * @param v the Clinic view
   */
  private void resetSelection(ClinicViewInterface v) {
    for (int i = 0; i < staffTableModel.getRowCount(); i++) {
      staffTableModel.setValueAt(false, i, 0);
    }
    v.setStatus(" ", 0);
  }
  
  /**
   * Helper method to capitalize the first character of a string and lowercase the rest.
   * 
   * @param str the string to be formatted
   * @return the formatted string
   */
  private String toTitleCase(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
}
