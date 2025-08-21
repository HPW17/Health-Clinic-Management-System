package commands;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.StaffInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
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
 * which unassigns a staff member from a patient.
 */
public class UnassignStaffFromPatient extends JFrame implements CommandInterface {
  
  private static final long serialVersionUID = 1L;
  private final JPanel mainPanel;
  private PatientInterface selectedPatient;
  private StaffInterface selectedStaff;
  private SelectionState currentState = SelectionState.SELECT_PATIENT;
  
  /**
   * Default constructor of UnassignStaffFromPatient.
   */
  public UnassignStaffFromPatient() {
    setTitle("Unassign Staff from Patient");
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
              selectedPatient = m.getPatients().stream()
              .filter(p -> p.getId() == id).findFirst().orElse(null);
              
              List<StaffInterface> staffs = m.getStaff();
              List<StaffInterface> assigned = new ArrayList<>();
              for (StaffInterface s : staffs) {
                if (s.getAssignedPatients().contains(selectedPatient)) {
                  assigned.add(s);
                }
              }
              if (assigned.isEmpty()) { // No staff is assigned to this patient
                v.setStatus("No staff member is assigned to this patient.", 2);
                JOptionPane.showMessageDialog(null, 
                    "No staff member is assigned to this patient.", 
                    "Cannot unassign a staff member from this patient", 
                    JOptionPane.WARNING_MESSAGE);
                v.clearState(2);
                v.enableMenu();
                v.refresh();
                dispose();
              } else {
                v.setStatus("Select the staff member to be unassigned from this patient.", 1);
                JOptionPane.showMessageDialog(null, 
                    String.format("You selected patient [%d] %s.\n" 
                    + "Select the staff member to be unassigned from this patient.", 
                    id, panel.getPatientName(id)),
                    "Unassign a staff member from a patient", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadStaffSelector(m, v, assigned);
                addButton(v);
                setVisible(true); 
                currentState = SelectionState.SELECT_STAFF;
              }
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
   * Helper method to generate a list of staff members assigned to the selected patient.
   * 
   * @param m the Clinic model
   * @param v the Clinic view
   */
  private void loadStaffSelector(ClinicInterface m, ClinicViewInterface v, 
      List<StaffInterface> assigned) {
    Object[][] data = new Object[assigned.size()][4];
    int rowNum = 0;
    for (StaffInterface s : assigned) {
      data[rowNum][0] = s.getId();
      data[rowNum][1] = s.toString();
      data[rowNum][2] = toTitleCase(s.getJobTitle());
      data[rowNum][3] = s.getNpiCpr();
      rowNum++;
    }
    String[] columnNames = { "ID", "Name", "Job Title", "NPI" };
    DefaultTableModel model = new DefaultTableModel(data, columnNames);
    JTable table = new JTable(model);
    table.setRowHeight(25);
    
    table.getColumnModel().getColumn(0).setPreferredWidth(10); // ID
    table.getColumnModel().getColumn(1).setPreferredWidth(120); // Name
    table.getColumnModel().getColumn(2).setPreferredWidth(50); // Job Title
    table.getColumnModel().getColumn(3).setPreferredWidth(50); // NPI
    
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    table.getTableHeader()
        .setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
    table.setEnabled(false);
    
    JScrollPane scrollPane = new JScrollPane(table,  
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(400, 250));
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        int staffId = (int) table.getValueAt(row, 0);
        selectedStaff = m.getStaff().stream()
            .filter(p -> p.getId() == staffId).findFirst().orElse(null); 
        v.setStatus(String.format("Selected %s to be unassigned.", 
            table.getValueAt(row, 1)), 0);
        int choice = JOptionPane.showConfirmDialog(null, 
            String.format("Unassign %s\nfrom patient %s?", 
                selectedStaff.toString(),
                selectedPatient.getFirstName() + " " + selectedPatient.getLastName()),
            "Unassign staff from patient", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
          m.unassignStaffFromPatient(selectedStaff, selectedPatient);
          v.clearState(2);
          v.enableMenu(); 
          v.refresh();
          v.setStatus("Unassign staff from patient successfully.", 0);
          dispose();
        } else {
          v.setStatus("Operation canceled.", 0);
        }
      }
      
      @Override
      public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
      
      @Override
      public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
        table.clearSelection();
      }
    });
    
    table.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        table.setRowSelectionInterval(row, row);
      }
    });
  }

  /**
   * Helper method to add the button panel for the Cancel button.
   * 
   * @param v the Clinic view
   */
  private void addButton(ClinicViewInterface v) {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(Color.WHITE);
    
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> {
      v.clearState(2);
      v.enableMenu(); 
      v.refresh();
      dispose();
    });
    buttonPanel.add(cancelButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
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
