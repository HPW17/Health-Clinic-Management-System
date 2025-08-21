package controllertest;

import clinic.ClinicInterface;
import clinic.PatientInterface;
import clinic.RoomInterface;
import clinic.StaffInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements {@link ClinicInterface} and represents a mock Clinic model that 
 * does not perform actual functionalities but only logs the input values and returns 
 * unique output values. 
 * This class facilitates the testing of isolated {@link Controller}.
 */
public class MockModel implements ClinicInterface {
  private StringBuilder log;
  private final String mockString;
  private final RoomInterface mockRoom;
  private final PatientInterface mockPatient;
  private final StaffInterface mockStaff;
  private final List<RoomInterface> mockRooms;
  private final List<PatientInterface> mockPatients;
  private final List<StaffInterface> mockStaffs;

  /**
   * Constructor of mock model.
   * 
   * @param log records the input values
   * @param mockString unique value of mock String
   * @param mockRoom unique value of mock Room
   * @param mockPatient unique value of mock Patient
   * @param mockStaff unique value of mock Staff
   */
  public MockModel(StringBuilder log, String mockString, 
      RoomInterface mockRoom, PatientInterface mockPatient, StaffInterface mockStaff) {
    this.log = log;
    this.mockString = mockString;
    this.mockRoom = mockRoom;
    this.mockPatient = mockPatient;
    this.mockStaff = mockStaff;
    mockRooms = new ArrayList<>();
    mockRooms.add(mockRoom);
    mockPatients = new ArrayList<>();
    mockPatients.add(mockPatient);
    mockStaffs = new ArrayList<>();
    mockStaffs.add(mockStaff);
  }
  
  @Override
  public String getName() {
    log.append("getName\n");
    return mockString;
  }
  
  @Override
  public List<RoomInterface> getRooms() {
    log.append("getRooms\n");
    return mockRooms;
  }
  
  @Override
  public List<StaffInterface> getStaff() {
    log.append("getStaff\n");
    return mockStaffs;
  }
  
  @Override
  public List<PatientInterface> getPatients() {
    log.append("getPatients\n");
    return mockPatients;
  }
  
  @Override
  public void readIntoModel(Reader source) 
      throws IllegalArgumentException, IOException { 
    BufferedReader reader = new BufferedReader(source);
    String line = reader.readLine().trim();
    log.append("readIntoModel:\nsource = " + source.getClass().toString() 
        + "\ncontent = " + line + "\n");
  }
  
  @Override
  public void registerPatient(PatientInterface patient) {
    log.append("registerPatient: patient = " 
        + patient.getFirstName() + " " + patient.getLastName() + "\n");
  }
  
  @Override
  public void registerClinicalStaff(StaffInterface staff) {
    log.append("registerClinicalStaff: staff = " 
        + staff.getFirstName() + " " + staff.getLastName() + "\n");
  }
  
  @Override
  public void sendPatientHome(PatientInterface patient, StaffInterface staff) 
      throws IllegalStateException {
    log.append("sendPatientHome: patient = " 
        + patient.getFirstName() + " " + patient.getLastName()
        + ", staff = " + staff.getFirstName() + " " + staff.getLastName() + "\n");
  }
  
  @Override
  public void deactivateClinicalStaff(StaffInterface staff) {
    log.append("deactivateClinicalStaff: staff = " 
        + staff.getFirstName() + " " + staff.getLastName() + "\n");
  }
  
  @Override
  public void assignPatientToRoom(PatientInterface patient, RoomInterface room) 
      throws IllegalStateException {
    log.append("assignPatientToRoom: patient = " 
        + patient.getFirstName() + " " + patient.getLastName()
        + ", room = " + room.getRoomName() + "\n");
  }
  
  @Override
  public void assignStaffToPatient(StaffInterface staff, PatientInterface patient) 
      throws IllegalStateException {
    log.append("assignStaffToPatient: staff = " 
        + staff.getFirstName() + " " + staff.getLastName()
        + ", patient = " + patient.getFirstName() + " " + patient.getLastName() + "\n");
  }
  
  @Override
  public void unassignStaffFromPatient(StaffInterface staff, PatientInterface patient) 
      throws IllegalStateException {
    log.append("unassignStaffFromPatient: staff = " 
        + staff.getFirstName() + " " + staff.getLastName()
        + ", patient = " + patient.getFirstName() + " " + patient.getLastName() + "\n");
  }
  
  @Override
  public String roomInfo(RoomInterface room) {
    log.append("roomInfo, room = " + room.getRoomName() + "\n");
    return mockString;
  }
  
  @Override
  public String seatingChart() {
    log.append("seatingChart\n");
    return mockString;
  }
  
  /**
   * Helper method to show all mock values. 
   */
  public void showMockValues() {
    log.append("Mock values:\nmockString = " + mockString 
        + "\nmockRoom = " + mockRoom.getRoomName()
        + "\nmockPatient = " + mockPatient.getFirstName() + " " + mockPatient.getLastName()
        + "\nmockStaff = " + mockStaff.getFirstName() + " " + mockStaff.getLastName() + "\n");
  }
}
