package modeltest;

import clinic.Clinic;
import clinic.ClinicInterface;
import clinic.SeatingMap;
import java.io.FileReader;
import java.io.IOException;

/**
 * A JUnit test class for testing the functionality of the {@link SeatingMap} class.
 */
public class SeatingMapTestDriver {

  /**
   * Demonstration of how the SeatingMap should be used. 
   * @param args not used here
   * @throws IllegalArgumentException if the specification file is in invalid format
   * @throws IOException if file I/O error
   */
  public static void main(String[] args) throws IllegalArgumentException, IOException {
    ClinicInterface clinic = Clinic.getInstance();
    FileReader fileReader = new FileReader("./res/clinicfile.txt");
    clinic.readIntoModel(fileReader);
    SeatingMap.drawMap(clinic);
  }
}
