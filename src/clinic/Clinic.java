package clinic;

import clinic.NonClinicalStaff.CprLevel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements {@link ClinicInterface} and represents a clinic including its information.
 * It is designed as Singleton since it is expected to have only one instance. 
 * - {@code instance}: the sole instance of Clinic
 * - {@code clinicName}: the name of this Clinic
 * - {@code rooms}: the list of instances of the {@link Room} in clinic
 * - {@code staffs}: the list of instances of {@link ClinicalStaff} and {@link NonClinicalStaff}
 * - {@code patients}: the list of instances of {@link Patient} being treated in clinic
 */
public class Clinic implements ClinicInterface {
  
  private static Clinic instance;
  
  private String clinicName;
  private final List<RoomInterface> rooms;
  private final List<StaffInterface> staffs;
  private final List<PatientInterface> patients;

  /**
   * Default constructor of Clinic. 
   * It is declared as private to hide it from public.
   */
  private Clinic() {
    rooms = new ArrayList<>();
    staffs = new ArrayList<>();
    patients = new ArrayList<>();
  }

  /**
   * This public method is not included in the contract defined by ClinicInterface. 
   * Since Clinic is designed as a singleton with a sole instance, this method creates a new 
   * instance by calling the hidden constructor and return it when firstly called, and 
   * returns the existing instance when called afterwards.
   * 
   * @return the sole instance of Clinic
   */
  public static Clinic getInstance() {
    if (instance == null) {
      instance = new Clinic();
    }
    return instance;
  }
  
  @Override
  public String getName() {
    return clinicName;
  }

  @Override
  public List<RoomInterface> getRooms() {
    return new ArrayList<>(rooms); // Returning a copy
  }

  @Override
  public List<StaffInterface> getStaff() {
    return new ArrayList<>(staffs); // Returning a copy
  }

  @Override
  public List<PatientInterface> getPatients() {
    return new ArrayList<>(patients); // Returning a copy
  }
  
  @Override
  public void readIntoModel(Reader source) throws IllegalArgumentException, IOException {
    try (BufferedReader reader = new BufferedReader(source)) {
  
      // Read the clinic name
      this.clinicName = reader.readLine().trim();
  
      // Read rooms
      int numRooms = Integer.parseInt(reader.readLine());
      readRooms(reader, numRooms);
  
      // Read staff
      int numStaff = Integer.parseInt(reader.readLine());
      readStaff(reader, numStaff);

      // Read patients
      int numPatients = Integer.parseInt(reader.readLine());
      readPatients(reader, numPatients);
      
      // Read visit records (optional)
      String tryGetVisits = reader.readLine();
      if (!(tryGetVisits == null || tryGetVisits.isBlank())) {
        int numVisits = Integer.parseInt(tryGetVisits);
        readVisits(reader, numVisits);
      }
      
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }
  
  /**
   * Helper method to read the rooms into model.
   * 
   * @param reader the reader for input specification
   * @param numRooms the number of rooms
   * @throws IOException if any IO issues with reader
   */
  private void readRooms(BufferedReader reader, int numRooms) throws IOException {
    for (int i = 0; i < numRooms; i++) {
      String line = reader.readLine();
      String[] roomData = line.split("[\\s,]+");
      
      int left = Integer.parseInt(roomData[0]);
      int bottom = Integer.parseInt(roomData[1]);
      int right = Integer.parseInt(roomData[2]);
      int top = Integer.parseInt(roomData[3]);
      String roomType = roomData[4].toUpperCase();

      // Need combine the rest in case the input name consists of multiple words
      StringBuilder roomNameBuilder = new StringBuilder();
      for (int j = 5; j < roomData.length; j++) {
        roomNameBuilder.append(roomData[j]).append(" ");
      }
      String roomName = roomNameBuilder.toString().trim();

      // The first room must be WAITING type (primary waiting room)
      if (i == 0 && !"WAITING".equals(roomType)) {
        throw new IllegalArgumentException("The first room must be waiting room.");
      }
      
      //The rooms cannot overlap each other
      for (RoomInterface existingRoom : rooms) {
        int[] existingRoomPos = existingRoom.getPosition();
        if (isIntersect(left, bottom, right, top, 
            existingRoomPos[0], existingRoomPos[1], existingRoomPos[2], existingRoomPos[3])) {
          throw new IllegalArgumentException("Overlap with existing rooms.");
        }
      }
      
      RoomInterface room = new Room(left, bottom, right, top, roomType, roomName);
      rooms.add(room);
    }
  }
  
  /**
   * Helper method to read the staff members into model.
   * 
   * @param reader the reader for input specification
   * @param numStaff the number of staff members
   * @throws IOException if any IO issues with reader
   */
  private void readStaff(BufferedReader reader, int numStaff) throws IOException {
    for (int i = 0; i < numStaff; i++) {
      String line = reader.readLine();
      String[] staffData = line.split("[\\s,]+");
      
      String jobTitle = staffData[0].toUpperCase();
      String firstName = staffData[1];
      String lastName = staffData[2];
      String educationLevel = staffData[3].toUpperCase();
      String npiCpr = staffData[4].toUpperCase();
      
      // Judge if this is a clinical or non-clinical staff member based on NPI/CPR
      ArrayList<String> cprLevelNames = new ArrayList<>();
      for (CprLevel c : CprLevel.values()) {
        cprLevelNames.add(c.name());
      }
      if (cprLevelNames.contains(npiCpr)) {
        staffs.add(new NonClinicalStaff(jobTitle, firstName, lastName, educationLevel, npiCpr));
      } else {
        staffs.add(new ClinicalStaff(jobTitle, firstName, lastName, educationLevel, npiCpr));
      }
    }
  }
  
  /**
   * Helper method to read the patients into model.
   * 
   * @param reader the reader for input specification
   * @param numPatients the number of patients
   * @throws IOException if any IO issues with reader
   */
  private void readPatients(BufferedReader reader, int numPatients) throws IOException {
    for (int i = 0; i < numPatients; i++) {
      String line = reader.readLine();
      String[] patientData = line.split("[\\s,]+");
      
      int roomNumber = Integer.parseInt(patientData[0]);
      String firstName = patientData[1];
      String lastName = patientData[2];
      String dateOfBirth = patientData[3];
      patients.add(new Patient(firstName, lastName, dateOfBirth));

      // Assign patient to room
      if (roomNumber > 0) {
        RoomInterface assignedRoom = rooms.get(roomNumber - 1);
        assignPatientToRoom(patients.get(patients.size() - 1), assignedRoom);
      }
    }
  }
  
  /**
   * Helper method to read the visit records into model.
   * 
   * @param reader the reader for input specification
   * @param numVisits the number of visit records
   * @throws IOException if any IO issues with reader
   */
  private void readVisits(BufferedReader reader, int numVisits) throws IOException {
    for (int i = 0; i < numVisits; i++) {
      String line = reader.readLine();
      String[] visitData = line.split("[\\s,]+");
      
      int patientId = Integer.parseInt(visitData[0]);
      String visitDateTime = visitData[1] + " " + visitData[2];
      Double temperature = Double.parseDouble(visitData[3]);
      String complaint = Arrays.stream(visitData, 4, visitData.length)
          .collect(Collectors.joining(" "));

      PatientInterface patient = patients.get(patientId - 1);
      patient.addVisitRecord(visitDateTime, complaint, temperature);
    }
  }
  
  @Override
  public void registerPatient(PatientInterface patient) {
    for (PatientInterface p : patients) {
      if (Objects.equals(p.getFirstName(), patient.getFirstName())
          && Objects.equals(p.getLastName(), patient.getLastName())
          && Objects.equals(p.getDateOfBirth(), patient.getDateOfBirth())) {
        throw new IllegalStateException("This patient is already registered.");
      }
    }
    
    patients.add(patient);
    // patient.assignToRoom(getRooms().get(0)); // start in the primary waiting room
  }

  @Override
  public void registerClinicalStaff(StaffInterface staff) {
    staffs.add(staff);
  }

  @Override
  public void sendPatientHome(PatientInterface patient, StaffInterface staff) 
      throws IllegalStateException {
    if (!staff.isActive()) {
      throw new IllegalStateException("This staff has been deactivated.");
    }
    if (patient.getAssignedRoom() == null) {
      throw new IllegalStateException("The patient was already sent home.");
    }
    patient.assignToRoom(null);
    for (StaffInterface s : getStaff()) {
      if (s.getAssignedPatients().contains(patient)) {
        s.unassignFromPatient(patient);
      }
    }
  }

  @Override
  public void deactivateClinicalStaff(StaffInterface staff) {
    // staff.assignToPatient(null); // (x)clear previous assignment -> keep records
    staff.setActive(false);
  }

  @Override
  public void assignPatientToRoom(PatientInterface patient, RoomInterface room) 
      throws IllegalStateException {
    if (!isRoomAvailable(room)) {
      throw new IllegalStateException("Room is already occupied.");
    }
    patient.assignToRoom(room);
  }

  @Override
  public void assignStaffToPatient(StaffInterface staff, PatientInterface patient) {
    if (!(staff instanceof ClinicalStaff)) {
      throw new IllegalStateException("Can't assign non-clinical staff to patient.");
    }
    staff.assignToPatient(patient);
  }
  
  @Override
  public void unassignStaffFromPatient(StaffInterface staff, PatientInterface patient) {
    staff.unassignFromPatient(patient);
  }

  /**
   * Display information about a specific room. This includes what patient is 
   * assigned to that room and any clinicians who are assigned to that patient. 
   * Example: 
   * 
   * Room Number: 1 | Room Name: Front Waiting Room | Room Type: WAITING
   *   * Patient: Aandi Acute, assigned clinical staff: none
   *     Last visit on: 2024/10/07 10:30, Body Temperature: 37.1°C, Chief Complaint: Headaches
   *   * Patient: Doug Derm, assigned clinical staff: none
   *     No visit records
   *   * Patient: Greg Gastric, assigned clinical staff: none
   *     No visit records
   */
  @Override
  public String roomInfo(RoomInterface room) {
    StringBuilder info = new StringBuilder();
    // Basic room info
    info.append(String.format("Room Number: %d | %s\n", room.getId(), room.toString()));
    // Loop for patients and assigned clinical staff
    List<PatientInterface> patientsInRoom = patients.stream()
        .filter(p -> room.equals(p.getAssignedRoom())).collect(Collectors.toList());
    for (PatientInterface p : patientsInRoom) {
      // Patient's name
      info.append(String.format("  * Patient: %s %s, assigned clinical staff: ", 
          p.getFirstName(), p.getLastName()));
      // Assigned clinical staff
      List<StaffInterface> staffList = getAssignedStaff(p);
      if (staffList.isEmpty()) {
        info.append("none\n");
      }
      for (int i = 0; i < staffList.size(); i++) {
        info.append(String.format("%s", staffList.get(i).toString())); // name with title
        if (i < staffList.size() - 1) {
          info.append(", ");  // more staff to go
        } else {
          info.append("\n");  // last one, end of line
        }
      }
      info.append("    - " + p.showLastVisitRecord() + "\n");
    }
    return info.toString();
  }

  /**
   * Display a seating chart that is a text list of every room and who is in each room.
   * Example: 
   * 
   * Room Number: 1 | Room Name: Front Waiting Room | Room Type: WAITING
   *   * Patients: Aandi Acute, Doug Derm, Greg Gastric
   * Room Number: 2 | Room Name: Triage | Room Type: EXAM
   *   * Patients: Beth Bunion
   * Room Number: 3 | Room Name: Inside Waiting Room | Room Type: WAITING
   *   * Patients: Clive Cardiac
   * Room Number: 4 | Room Name: Exam_1 | Room Type: EXAM
   *   * Patients: Elise Enzyme
   * Room Number: 5 | Room Name: Surgical | Room Type: PROCEDURE
   *   * Patients: Fatima Follicle
   */
  @Override
  public String seatingChart() {
    StringBuilder info = new StringBuilder();
    for (RoomInterface r : rooms) {
      // Basic room info
      info.append(String.format("Room: %d | %s\n  * Patients: ", r.getId(), r.toString()));
      // List patients in room
      List<PatientInterface> patientsInRoom = getPatientsInRoom(r);
      if (patientsInRoom.isEmpty()) {
        info.append("none\n");
      }
      for (int i = 0; i < patientsInRoom.size(); i++) {
        info.append(String.format("%s %s", 
            patientsInRoom.get(i).getFirstName(), patientsInRoom.get(i).getLastName()));
        if (i < patientsInRoom.size() - 1) {
          info.append(", ");  // more patients to go
        } else {
          info.append("\n");  // last one, end of line
        }
      }
    }
    return info.toString();
  }
  
  /** 
   * Returns a formatted string representation of the clinic.
   * 
   * Example:
   * Clinic name: Happy Clinic, Rooms number: 3, Staff number: 5, Patients number: 10 
   *
   * @return a string containing the information of the room. 
   */
  @Override
  public String toString() {
    return String.format(
        "Clinic name: %s, Rooms number: %d, Staff number: %d, Patients number: %d", 
        clinicName, rooms.size(), staffs.size(), patients.size());
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { 
      return true;
    }
    if (!(o instanceof Clinic)) {
      return false;
    }
    Clinic that = (Clinic) o;
    return Objects.equals(this.clinicName, that.clinicName) 
        && Objects.equals(this.rooms, that.rooms) 
        && Objects.equals(this.staffs, that.staffs) 
        && Objects.equals(this.patients, that.patients);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clinicName, rooms, staffs, patients);
  }

  /**
   * This is a helper method to get the patients in the specified room.
   * 
   * @param room The specified room.
   * @return A list of the patients in that room.
   */
  private List<PatientInterface> getPatientsInRoom(RoomInterface room) {
    return patients.stream()
        .filter(p -> room.equals(p.getAssignedRoom())).collect(Collectors.toList());
  }

  /**
   * This is a helper method to get the staff assigned to a specified patient.
   * @param patient The specified patient.
   * @return A list of the staff that is assigned to this patient.
   */
  private List<StaffInterface> getAssignedStaff(PatientInterface patient) {
    return staffs.stream()
        .filter(s -> s.getAssignedPatients().contains(patient)) 
        .collect(Collectors.toList()); 
  }

  /**
   * Helper method to check if the specified room is available for a patient to be assigned to.
   * A waiting room is always available since it can accommodate multiple patients.
   * A non-waiting room is available only when it is not currently assigned to another patient.
   * 
   * @param room the specified room
   * @return true if the room is available, false if not
   */
  private boolean isRoomAvailable(RoomInterface room) {
    if (!room.getRoomType().equals("WAITING")) { // A waiting room can host multiple patients
      for (PatientInterface patient : patients) {
        if (room.equals(patient.getAssignedRoom())) {
          return false;  // A non-waiting room can only accommodate a single patient
        }
      }
    }
    return true;
  }
  
  /**
   * Helper method to check if two rectangles intersect.
   * 
   * @param left1 the lower-left corner x coordinate of the first rectangle
   * @param bottom1 the lower-left corner y coordinate of the first rectangle
   * @param right1 the upper-right corner x coordinate of the first rectangle
   * @param top1 the upper-right corner y coordinate of the first rectangle
   * @param left2 the lower-left corner x coordinate of the second rectangle
   * @param bottom2 the lower-left corner y coordinate of the second rectangle
   * @param right2 the upper-right corner x coordinate of the second rectangle
   * @param top2 the upper-right corner y coordinate of the second rectangle
   * @return true if two rectangles intersect, false if not
   */
  private static Boolean isIntersect(
      int left1, int bottom1, int right1, int top1, 
      int left2, int bottom2, int right2, int top2) {
    // Check for horizontal overlap
    if (right1 < left2 || right2 < left1) {
      return false;
    }
    // Check for vertical overlap
    if (top1 < bottom2 || top2 < bottom1) {
      return false;
    }
    return true;
  }
  
  /**
   * This is just for JUnit testing purpose to refresh state each run, 
   * therefore it is not included in the contract defined by ClinicInterface.
   * Without calling this the method after each run, the singleton instance persists 
   * in memory with state and will cause confusions across test runs. 
   */
  public void clearState() {
    if (instance != null) {
      rooms.clear();
      staffs.clear();
      patients.clear();
      Room.clearLastNumberAssigned();
      AbstractStaff.clearLastNumberAssigned();
      Patient.clearLastNumberAssigned();
    }
  }
}
