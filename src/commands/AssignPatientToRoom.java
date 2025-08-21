package commands;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.RoomInterface;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import view.ClinicPanel;
import view.ClinicView;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which assigns a patient to the specified room.
 */
public class AssignPatientToRoom implements CommandInterface {
  
  private SelectionState currentState = SelectionState.SELECT_PATIENT;
  private int patientId = -1;
  private int roomId = -1;
  
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
              patientId = id;
              v.setStatus("Select a room by clicking on its space.", 1);
              JOptionPane.showMessageDialog(null, 
                  String.format("You selected patient [%d] %s.\n" 
                  + "Select a room by clicking on its space.", id, panel.getPatientName(id)),
                  "Assign a patient to a room", 
                  JOptionPane.INFORMATION_MESSAGE);
              //v.resetSelectedRoomId();
              v.setSelectType(0);
              currentState = SelectionState.SELECT_STAFF; // select room
              break;
              
            case SELECT_STAFF: // select room
              roomId = id;
              boolean success;
              // Perform assignment
              try {
                List<PatientInterface> patients = m.getPatients();
                PatientInterface patient = patients.stream()
                    .filter(p -> p.getId() == patientId).findFirst().orElse(null);
                List<RoomInterface> rooms = m.getRooms();
                RoomInterface room = rooms.stream()
                    .filter(r -> r.getId() == roomId).findFirst().orElse(null);
                m.assignPatientToRoom(patient, room);
                success = true;
              } catch (IllegalStateException e1) {
                success = false;
              } 
              v.refresh();
              if (success) {
                v.setStatus(
                    String.format("Assigned patient %d to room %d.", patientId, roomId), 0);
                JOptionPane.showMessageDialog(null, 
                    String.format("Assigned patient [%d] %s\nto room [%d] %s.", 
                        patientId, panel.getPatientName(patientId), 
                        roomId, panel.getRoomName(roomId)),
                    "Assign a patient to a room", 
                    JOptionPane.INFORMATION_MESSAGE);
              } else {
                v.setStatus("Room is already occupied.", 2);
                JOptionPane.showMessageDialog(null, 
                    String.format("Cannot assign patient [%d] %s\nto room [%d] %s." 
                        + "\n\nRoom is already occupied.", 
                        patientId, panel.getPatientName(patientId), 
                        roomId, panel.getRoomName(roomId)),
                    "Assign a patient to a room", 
                    JOptionPane.ERROR_MESSAGE);
              }
              // Reset the state
              currentState = SelectionState.DONE;
              v.clearState(2);
              v.enableMenu();
              v.refresh();
              patientId = -1;
              roomId = -1;
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
        "Assign a patient to a room", 
        JOptionPane.INFORMATION_MESSAGE);
    v.resetSelectedPatientId();
    v.setSelectType(1);
    
    return "";
  }
}
