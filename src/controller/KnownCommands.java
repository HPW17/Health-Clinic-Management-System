package controller;

import commands.AssignPatientToRoom;
import commands.AssignStaffToPatient;
import commands.ClearOutRecords;
import commands.CommandInterface;
import commands.DisplayPatient;
import commands.LoadClinicTextFile;
import commands.QuitApplication;
import commands.RegisterNewPatient;
import commands.SendPatientHome;
import commands.UnassignStaffFromPatient;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class initializes the command map and provides static access to its content.
 * No need to modify the controller when adding/removing commands.
 */
public class KnownCommands {
  private static KnownCommands instance;
  private static final Map<String, Supplier<CommandInterface>> menu = new LinkedHashMap<>();
  
  /**
   * Default constructor of KnownCommands which initializes the command map.
   * Items start with "-" represent the end of group menus.
   * Declared as private to hide from public access. 
   */
  private KnownCommands() {
    // JMenu 'Client'
    menu.put("Load clinic text file", LoadClinicTextFile::new);
    menu.put("Clear out records", ClearOutRecords::new);
    menu.put("Quit application", QuitApplication::new);
    menu.put("-1", null);
    
    // JMenu 'Features
    menu.put("Register new patient", RegisterNewPatient::new);
    menu.put("Display patient", DisplayPatient::new);
    menu.put("Assign patient to room", AssignPatientToRoom::new);
    menu.put("Assign staff to patient", AssignStaffToPatient::new);
    menu.put("Unassign staff from patient", UnassignStaffFromPatient::new);
    menu.put("Send patient home", SendPatientHome::new);
    menu.put("-2", null);
  }
  
  /**
   * Static method to get the content of the command map.
   * 
   * @return a copy of the command map
   */
  public static Map<String, Supplier<CommandInterface>> getMenu() {
    if (instance == null) {
      instance = new KnownCommands();
    }
    Map<String, Supplier<CommandInterface>> copy = new LinkedHashMap<>();
    for (Map.Entry<String, Supplier<CommandInterface>> e : menu.entrySet()) {
      copy.put(e.getKey(), e.getValue());
    }
    return copy;
  }
}
