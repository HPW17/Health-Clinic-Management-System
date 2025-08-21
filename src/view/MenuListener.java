package view;

import clinic.ClinicInterface;
import controller.Controller;
import controller.ControllerInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class implements {@link ActionListener} to pass the selected menu item to the controller. 
 */
public class MenuListener implements ActionListener {
  private final ClinicInterface model;
  private final ClinicViewInterface view;
  
  /**
   * Constructor of MenuListener. 
   * 
   * @param m the clinic model
   * @param v the clinic view
   */
  public MenuListener(ClinicInterface m, ClinicViewInterface v) {
    if (m == null || v == null) {
      throw new IllegalArgumentException("Model and View cannot be null.");
    }
    model = m;
    view = v;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String getSource = e.getSource().toString();
    String item = getSource.substring(getSource.indexOf("text=") + 5, getSource.length() - 1);
    ControllerInterface c = Controller.getInstance(model, view);
    c.handleMenuClick(item);
  }
}
