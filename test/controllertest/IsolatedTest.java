package controllertest;

import static org.junit.Assert.assertEquals;

import clinic.ClinicalStaff;
import clinic.Patient;
import clinic.PatientInterface;
import clinic.Room;
import clinic.RoomInterface;
import clinic.StaffInterface;
import controller.Controller;
import controller.ControllerInterface;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import view.ClinicPanel;

/**
 * Test cases for the isolated MVC application.
 */
public class IsolatedTest {
  private final int mockInt;
  private final String mockString;
  private final MockModel mockModel;
  private final MockView mockView;
  private final RoomInterface mockRoom;
  private final PatientInterface mockPatient;
  private final StaffInterface mockStaff;
  private final ClinicPanel mockPanel;
  private final StringBuilder log;
  
  /**
   * Default constructor of IsolatedTest to initialize the test data. 
   */
  public IsolatedTest() {
    log = new StringBuilder();
    mockInt = 999;
    mockString = "mmmmmmmmmm";
    mockRoom = new Room(0, 0, 1, 1, "Waiting", "Mock Room");
    mockPatient = new Patient("John", "Doe", "1990/01/01");
    mockStaff = new ClinicalStaff("Physician", "Mary", "Doe", "Doctoral", "9999999999");
    mockModel = new MockModel(log, mockString, mockRoom, mockPatient, mockStaff);
    mockPanel = new ClinicPanel(mockModel);
    mockView = new MockView(log, mockInt, mockString, mockPanel);
  }
  
  /**
   * Initial setup to clear the log before each test.
   */
  @Before
  public void setUp() {
    log.setLength(0);
  }

  /**
   * Test cases for the isolated Clinic controller.
   */
  @Test
  public void testIsolatedController() {
    ClinicPanel mockPanel = new ClinicPanel(mockModel);
    MockView mockView = new MockView(log, mockInt, mockString, mockPanel);
    ControllerInterface isoController = new Controller(mockModel, mockView) {
      @Override 
      public void handleMenuClick(String command) {
        log.append("handleMenuClick: command = " + command + "\n");
      }
    };
    
    isoController.start();
    assertEquals("makeVisible\n", log.toString());
    log.setLength(0);
    
    isoController.start("mockfile.txt");
    assertEquals("setSpecFile: specFile = mockfile.txt\n"
        + "makeVisible\n"
        + "handleMenuClick: command = Load clinic text file\n", log.toString());
    log.setLength(0);
    
    isoController.handleMenuClick("Mock command");
    assertEquals("handleMenuClick: command = Mock command\n", log.toString());
  }
  
  /**
   * Test cases for the mock Clinic model {@link MockModel}.
   * 
   * @throws IllegalArgumentException not used here
   * @throws IOException not used here
   */
  @Test
  public void testMockModel() 
      throws IllegalArgumentException, IOException {
    // Demonstrate the mock values
    mockModel.showMockValues();
    assertEquals("Mock values:\n"
        + "mockString = " + mockString + "\n"
        + "mockRoom = Mock Room\n"
        + "mockPatient = John Doe\n"
        + "mockStaff = Mary Doe\n", log.toString());
    log.setLength(0);
    
    // Test overrode public methods
    String resultString = mockModel.getName();
    assertEquals("getName\n", log.toString());
    assertEquals(mockString, resultString);
    log.setLength(0);
    
    List<RoomInterface> resultRooms = mockModel.getRooms();
    assertEquals("getRooms\n", log.toString());
    assertEquals(1, resultRooms.size());
    assertEquals("Mock Room", resultRooms.get(0).getRoomName());
    log.setLength(0);
    
    List<StaffInterface> resultStaffs = mockModel.getStaff();
    assertEquals("getStaff\n", log.toString());
    assertEquals(1, resultStaffs.size());
    assertEquals("Mary", resultStaffs.get(0).getFirstName());
    assertEquals("Doe", resultStaffs.get(0).getLastName());
    log.setLength(0);
    
    List<PatientInterface> resultPatients = mockModel.getPatients();
    assertEquals("getPatients\n", log.toString());
    assertEquals(1, resultPatients.size());
    assertEquals("John", resultPatients.get(0).getFirstName());
    assertEquals("Doe", resultPatients.get(0).getLastName());
    log.setLength(0);
    
    mockModel.readIntoModel(new StringReader(mockString));
    assertEquals("readIntoModel:\n"
        + "source = class java.io.StringReader\n"
        + "content = mmmmmmmmmm\n", log.toString());
    log.setLength(0);
    
    mockModel.registerPatient(mockPatient);
    assertEquals("registerPatient: patient = John Doe\n", log.toString());
    log.setLength(0);
    
    mockModel.registerClinicalStaff(mockStaff);
    assertEquals("registerClinicalStaff: staff = Mary Doe\n", log.toString());
    log.setLength(0);
    
    mockModel.sendPatientHome(mockPatient, mockStaff);
    assertEquals("sendPatientHome: patient = John Doe, staff = Mary Doe\n", log.toString());
    log.setLength(0);
    
    mockModel.deactivateClinicalStaff(mockStaff);
    assertEquals("deactivateClinicalStaff: staff = Mary Doe\n", log.toString());
    log.setLength(0);
    
    mockModel.assignPatientToRoom(mockPatient, mockRoom);
    assertEquals("assignPatientToRoom: patient = John Doe, room = Mock Room\n", log.toString());
    log.setLength(0);
    
    mockModel.assignStaffToPatient(mockStaff, mockPatient);
    assertEquals("assignStaffToPatient: staff = Mary Doe, patient = John Doe\n", log.toString());
    log.setLength(0);
    
    mockModel.unassignStaffFromPatient(mockStaff, mockPatient);
    assertEquals("unassignStaffFromPatient: staff = Mary Doe, patient = John Doe\n", 
        log.toString());
    log.setLength(0);
    
    resultString = mockModel.roomInfo(mockRoom);
    assertEquals("roomInfo, room = Mock Room\n", log.toString());
    assertEquals(mockString, resultString);
    log.setLength(0);
    
    resultString = mockModel.seatingChart();
    assertEquals("seatingChart\n", log.toString());
    assertEquals(mockString, resultString);
    log.setLength(0);
    
  }

  /**
   * Test cases for the mock Clinic view {@link MockView}.
   */
  @Test
  public void testMockView() {
    // Demonstrate the mock values
    mockView.showMockValues();
    assertEquals("Mock values:\n"
        + "mockInt = " + mockInt + "\n"
        + "mockString = " + mockString + "\n"
        + "mockPanel = null\n", log.toString());
    log.setLength(0);
    
    // Test overrode public methods
    mockView.setMenu(mockInt);
    assertEquals("setMenu: mode = " + mockInt + "\n", log.toString());
    log.setLength(0);
    
    mockView.refresh();
    assertEquals("refresh\n", log.toString());
    log.setLength(0);
    
    mockView.makeVisible();
    assertEquals("makeVisible\n", log.toString());
    log.setLength(0);
    
    mockView.setStatus(mockString, mockInt);
    assertEquals("setStatus: status = " + mockString + ", type = " + mockInt + "\n", 
        log.toString());
    log.setLength(0);
    
    mockView.setSelectType(mockInt);
    assertEquals("setSelectType: type = " + mockInt + "\n", log.toString());
    log.setLength(0);
    
    int resultInt = mockView.getSelectedPatientId();
    assertEquals("getSelectedPatientId\n", log.toString());
    assertEquals(mockInt, resultInt);
    log.setLength(0);
    
    resultInt = mockView.getSelectedRoomId();
    assertEquals("getSelectedRoomId\n", log.toString());
    assertEquals(mockInt, resultInt);
    log.setLength(0);
    
    mockView.resetSelectedPatientId();
    assertEquals("resetSelectedPatientId\n", log.toString());
    log.setLength(0);
    
    mockView.resetSelectedRoomId();
    assertEquals("resetSelectedRoomId\n", log.toString());
    log.setLength(0);
    
    mockView.setSpecFile(mockString);
    assertEquals("setSpecFile: specFile = " + mockString + "\n", log.toString());
    log.setLength(0);
    
    String resultString = mockView.getSpecFile();
    assertEquals("getSpecFile\n", log.toString());
    assertEquals(mockString, resultString);
    log.setLength(0);
    
    mockView.clearState(mockInt);
    assertEquals("clearState: menu = " + mockInt + "\n", log.toString());
    log.setLength(0);
    
    ClinicPanel resultPanel = mockView.getPanel();
    assertEquals("getPanel\n", log.toString());
    assertEquals("class view.ClinicPanel", resultPanel.getClass().toString());
    log.setLength(0);
    
    mockView.enableMenu();
    assertEquals("enableMenu\n", log.toString());
    log.setLength(0);
    
    mockView.disableMenu();
    assertEquals("disableMenu\n", log.toString());
    log.setLength(0);
  }
}
