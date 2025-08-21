package commands;

import clinic.Clinic;
import clinic.ClinicInterface;
import javax.swing.JOptionPane;
import view.ClinicView;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which clears previous records in the clinic model.
 */
public class ClearOutRecords implements CommandInterface {
  
  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    int choice = JOptionPane.showConfirmDialog(null, 
        "Are you sure?", "Clear Out Records", JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      ((Clinic) m).clearState();
      ((ClinicView) v).clearState(1);
      return "Clinic records cleared successfully.";
    } else {
      return "Operation canceled.";
    }
  }
}
