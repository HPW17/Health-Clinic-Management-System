package view;

import clinic.Clinic;
import clinic.ClinicInterface;
import controller.KnownCommands;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * This class implements {@link ClinicViewInterface} and represents the frame GUI.
 * It is designed as a singleton.
 */
public class ClinicView extends JFrame implements ClinicViewInterface {

  private static final long serialVersionUID = 1L;
  private static final int INITIAL_WIDTH = 950;
  private static final int INITIAL_HEIGHT = 700;
  private static final int MIN_WIDTH = 300;
  private static final int MIN_HEIGHT = 300;
  private static ClinicView instance;
  private final ActionListener menuListener;
  private final ClinicPanel panel;
  private final JScrollPane scrollPane;
  private final JMenuBar mb = new JMenuBar();
  private final JMenu clinic = new JMenu("Clinic");
  private final JMenu features = new JMenu("Features");
  private final JLabel status = new JLabel();
  private int selectedPatientId = -1;
  private int selectedRoomId = -1;
  private String specFile = "";
  
  /**
   * Hidden constructor of ClinicView. 
   * It initializes the frame with a JMenuBar, a ScrollPane, and a JLabel.
   */
  private ClinicView() {
    ClinicInterface m = Clinic.getInstance();
    menuListener = new MenuListener(m, this);
    panel = new ClinicPanel(m);
    initializeMenu();
    setTitle("Health Clinic Management System");
    setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setJMenuBar(mb);
    scrollPane = new JScrollPane(panel, 
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane);
    status.setFont(new Font("Tahoma", Font.PLAIN, 12));
    status.setPreferredSize(new Dimension(panel.getWidth(), 30));
    status.setVerticalAlignment(SwingConstants.CENTER);
    add(status, BorderLayout.SOUTH);
    setStatus("Start by loading clinic text file.", 0);
  }
  
  /**
   * This public method is not included in the contract defined by ClinicViewInterface. 
   * Since ClinicView is designed as a singleton with a sole instance, this method creates a 
   * new instance by calling the hidden constructor and return it when firstly called, or 
   * returns the existing instance when called afterwards.
   * 
   * @return the sole instance of ClinicView
   */
  public static ClinicView getInstance() {
    if (instance == null) {
      instance = new ClinicView();
    }
    return instance;
  }
  
  /**
   * Helper method to initialize the menu of the viewer frame.
   * It dynamically constructs the menus based on the content in {@link KnownCommands}.
   */
  private void initializeMenu() {
    String[] menu = KnownCommands.getMenu().keySet().toArray(new String[0]);
    int i = 0;
    while (!menu[i].substring(0, 1).equals("-") && i < menu.length) {
      JMenuItem item = new JMenuItem(menu[i]);
      item.addActionListener(menuListener);
      clinic.add(item);
      if (i == 1) {
        item.setEnabled(false);
      }
      i++;
    }
    i++;
    while (!menu[i].substring(0, 1).equals("-") && i < menu.length) {
      JMenuItem item = new JMenuItem(menu[i]);
      item.addActionListener(menuListener);
      features.add(item);
      i++;
    }
    mb.add(clinic);
  }
  
  @Override
  public void setMenu(int mode) {
    mb.removeAll();
    switch (mode) {
      case 1:
        this.clinic.getItem(0).setEnabled(true);
        this.clinic.getItem(1).setEnabled(false);
        mb.add(this.clinic);
        break;
      case 2:
        this.clinic.getItem(0).setEnabled(false);
        this.clinic.getItem(1).setEnabled(true);
        mb.add(this.clinic);
        mb.add(this.features);
        break;
      default:
        break;
    }
  }
  
  @Override
  public void refresh() {
    mb.repaint();
    panel.repaint();
    status.repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }
  
  @Override
  public void setStatus(String status, int type) {
    if (!status.isEmpty()) {
      switch (type) {
        case 1: 
          this.status.setForeground(Color.BLUE);
          this.status.setFont(new Font("Tahoma", Font.BOLD, 12));
          break;
        case 2:
          this.status.setForeground(Color.RED);
          this.status.setFont(new Font("Tahoma", Font.BOLD, 12));
          break;
        default:
          this.status.setForeground(Color.BLACK);
          this.status.setFont(new Font("Tahoma", Font.PLAIN, 12));
          break;
      }
      this.status.setText("    " + status);
    }
  }
  
  @Override
  public void setSelectType(int type) {
    panel.setSelectType(type);
  }
  
  @Override
  public int getSelectedPatientId() {
    return selectedPatientId;
  }
  
  @Override
  public int getSelectedRoomId() {
    return selectedRoomId;
  }
  
  @Override
  public void resetSelectedPatientId() {
    selectedPatientId = -1;
  }
  
  @Override
  public void resetSelectedRoomId() {
    selectedRoomId = -1;
  }
  
  @Override
  public void setSpecFile(String specFile) {
    this.specFile = specFile;
  }
  
  @Override
  public String getSpecFile() {
    return specFile;
  }
  
  @Override
  public void clearState(int menu) {
    if (instance != null) {
      setMenu(menu);
      resetSelectedPatientId();
      resetSelectedRoomId();
      setSelectType(-1);
      setSpecFile("");
      setStatus(" ", 0);
      panel.clearState();
    }
  }
  
  @Override
  public ClinicPanel getPanel() {
    return panel;
  }

  @Override
  public void disableMenu() {
    JMenuBar mb = getJMenuBar();
    if (mb != null) {
      for (int i = 0; i < mb.getMenuCount(); i++) {
        JMenu menu = mb.getMenu(i);
        if (menu != null) {
          menu.setEnabled(false);
        }
      }
    }
  }

  @Override
  public void enableMenu() {
    JMenuBar mb = getJMenuBar();
    if (mb != null) {
      for (int i = 0; i < mb.getMenuCount(); i++) {
        JMenu menu = mb.getMenu(i);
        if (menu != null) {
          menu.setEnabled(true);
        }
      }
    }
  }
}
