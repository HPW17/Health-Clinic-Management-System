package clinic;

import java.io.FileReader;
import java.io.IOException;

/**
 * Driver class to demonstrate the usage of the Clinic model.
 */
public class ClinicDriver {

  /**
   * Demonstration of how the Clinic model should be used. 
   * 
   * @param args the clinic specification text file
   * @throws IOException if reading specification file error
   */
  public static void main(String[] args) throws IOException {
    // Must assign specification file in the argument
    if (args.length < 1) {
      System.out.println("Missing argument for the clinic specification file.");
      return;
    }

    Clinic clinic = Clinic.getInstance();

    // Read specification file into model
    demonstrateReadIntoModel(clinic, args[0]);

    // Initial seating chart and room info before all operations
    demonstrateSeatingChart(clinic, "Initial seating chart:");
    demonstrateRoomInfo(clinic);

    // Demonstrate different scenarios
    demonstrateRegisterPatient(clinic);
    demonstrateRegisterClinicalStaff(clinic);
    demonstrateSendPatientHome(clinic);
    demonstrateDeactivateStaff(clinic);
    demonstrateAssignPatientToRoom(clinic);
    demonstrateAssignStaffToPatient(clinic);

    // Final seating chart and room info after all operations
    demonstrateSeatingChart(clinic, "Seating chart after all operations:");
    demonstrateRoomInfo(clinic);
  }
  
  /**
   * Read the clinic text file into the model.
   * 
   * @param clinic the instance of Clinic
   * @param specFile the clinic specification text file from argument
   * @throws IOException if reading specification file error
   */
  private static void demonstrateReadIntoModel(Clinic clinic, String specFile) 
      throws IOException {
    try {
      System.out.println("[ Read file into model ]");
      FileReader fileReader = new FileReader(specFile);
      clinic.readIntoModel(fileReader);
      System.out.println("File '" + specFile + "'read into model successfully.");
      System.out.println("Clinc '" + clinic.getName() + "' instantiated.");
    } catch (IOException e) {
      System.err.println("Error reading the specification file: " + e.getMessage());
    }
  }

  /**
   * Register a new patient. Patients start in the primary waiting room.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateRegisterPatient(Clinic clinic) {
    System.out.println("\n[ Patient registration ]");
    Patient newPatient = new Patient("John", "Doe", "1/1/1990");
    clinic.registerPatient(newPatient);
    System.out.println("Registered new patient: " + newPatient.getFirstName());
    demonstrateSeatingChart(clinic, "Updated seating chart:");
  }

  /**
   * Register a new clinical staff member.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateRegisterClinicalStaff(Clinic clinic) {
    System.out.println("\n[ Clinical staff registration ]");
    ClinicalStaff newStaff = new ClinicalStaff("NURSE", "Sam", "Smith", "MASTERS", "9999999999");
    clinic.registerClinicalStaff(newStaff);
    System.out.println("Registered new clinical staff: " + newStaff.getFirstName());
  }
  
  /**
   * Send a patient home, which is approved by a clinical staff member.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateSendPatientHome(Clinic clinic) {
    System.out.println("\n[ Sending a patient home ]");
    PatientInterface patient = clinic.getPatients().get(1);
    ClinicalStaff staff = (ClinicalStaff) clinic.getStaff().get(0);
    clinic.sendPatientHome(patient, staff);
    System.out.println("Sent patient " + patient.getFirstName() 
        + " home, approved by staff " + staff.getFirstName());
    demonstrateSeatingChart(clinic, "Updated seating chart:");
  }

  /**
   * Deactivate a clinical staff member.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateDeactivateStaff(Clinic clinic) {
    System.out.println("\n[ Deactivating clinical staff ]");
    ClinicalStaff staff = (ClinicalStaff) clinic.getStaff().get(1);
    clinic.deactivateClinicalStaff(staff);
    System.out.println("Deactivated clinical staff: " + staff.getFirstName());
  }

  /**
   * Assign one patient to a room.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateAssignPatientToRoom(Clinic clinic) {
    System.out.println("\n[ Assigning patient to a room ]");
    PatientInterface patient = clinic.getPatients().get(0);
    RoomInterface room = clinic.getRooms().get(1);
    clinic.assignPatientToRoom(patient, room);
    System.out.println("Assigned patient " + patient.getFirstName() 
        + " to room: " + room.getRoomName());
    demonstrateSeatingChart(clinic, "Updated seating chart:");
  }

  /**
   * Assign a clinical staff member to a patient.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateAssignStaffToPatient(Clinic clinic) {
    System.out.println("\n[ Assigning clinical staff to a patient ]");
    ClinicalStaff staff = (ClinicalStaff) clinic.getStaff().get(clinic.getStaff().size() - 1);
    PatientInterface patient = clinic.getPatients().get(0);
    clinic.assignStaffToPatient(staff, patient);
    System.out.println("Assigned clinical staff " + staff.getFirstName() 
        + " to patient " + patient.getFirstName());
  }

  /**
   * Return information about a specific room as a string, including what patient is 
   * assigned to that room and any clinicians who are assigned to that patient.
   * 
   * Here we loop and show this information for all rooms.
   * 
   * @param clinic the instance of Clinic
   */
  private static void demonstrateRoomInfo(Clinic clinic) {
    System.out.println("\n[ Showing room information ]");
    for (int i = 0; i < clinic.getRooms().size(); i++) {
      System.out.println(clinic.roomInfo(clinic.getRooms().get(i)));
    }
  }
  
  /**
   * Display a seating chart that is a text list of every room and who is in each room.
   * 
   * @param clinic the instance of Clinic
   * @param prompt the prompt information of current situation 
   */
  private static void demonstrateSeatingChart(Clinic clinic, String prompt) {
    System.out.println("\n[ Showing seat chart ]");
    System.out.println(">> " + prompt);
    System.out.println(clinic.seatingChart());
  }
}
