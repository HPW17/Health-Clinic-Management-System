package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import clinic.Clinic;
import clinic.SeatingMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import org.junit.Test;

/**
 * Tests for {@link SeatingMap} class.
 */
public class SeatingMapTest {

  @Test
  public void testSeatingMap() throws IOException {
    Clinic clinic = Clinic.getInstance();
    clinic.clearState();
    FileReader fileReader = new FileReader("./res/clinicfile.txt");
    clinic.readIntoModel(fileReader);
    SeatingMap.drawMap(clinic);
    
    // Test file existence and format
    File outputFile;
    if (new File("./res").exists()) {
      outputFile = new File("./res/clinic_map.jpg");
    } else {
      outputFile = new File("./clinic_map.jpg");
    }
    assertTrue(outputFile.exists());
    assertTrue(outputFile.length() > 0);
    
    String mimeType = Files.probeContentType(outputFile.toPath());
    assertEquals("image/jpeg", mimeType);
    
    // Test generated image type and dimension
    try {
      BufferedImage image = ImageIO.read(outputFile);
      assertNotNull(image);
      assertEquals(BufferedImage.TYPE_3BYTE_BGR, image.getType());

      int expectedWidth = 900; // from SeatingMap.PROPER_IMAGE_WIDTH + IMAGE_GAP * 2
      assertEquals(expectedWidth, image.getWidth());

    } catch (IOException e) {
      fail("Failed to read the generated image file: " + e.getMessage());
        
    } 
  }
}
