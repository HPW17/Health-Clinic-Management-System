package commands;

import clinic.ClinicInterface;
import javax.swing.JOptionPane;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which quits the application.
 */
public class QuitApplication implements CommandInterface {

  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    int choice = JOptionPane.showConfirmDialog(null, 
        "Are you sure?", "Quit Application", JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      System.exit(0);
    }
    return "Quit operation canceled.";
  }
}
