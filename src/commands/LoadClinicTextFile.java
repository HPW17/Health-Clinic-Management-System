package commands;

import clinic.ClinicInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.ClinicViewInterface;

/**
 * This class implements {@link CommandInterface} and represents the command 
 * which loads clinic specification text into the model.
 */
public class LoadClinicTextFile implements CommandInterface {

  @Override
  public String execute(ClinicInterface m, ClinicViewInterface v) {
    String status;
    
    if (v.getSpecFile().isEmpty()) { // No pre-specified clinic text file
      
      // Set up file chooser dialog
      JFileChooser fileChooser = new JFileChooser();
      File targetDir = new File("./res");
      if (targetDir.exists() && targetDir.isDirectory()) {
        fileChooser.setCurrentDirectory(targetDir);
      } else {
        fileChooser.setCurrentDirectory(new File("."));
      }
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
      fileChooser.setFileFilter(filter);
      
      // Confirm selection
      int result = fileChooser.showOpenDialog(fileChooser);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile(); 
        status = loadSpec(m, v, selectedFile.getPath());
      } else {
        status = "Operation canceled.";
      }
      
    } else { // Already specified clinic text file through command-line arguments
      status = loadSpec(m, v, v.getSpecFile());
      v.setSpecFile("");
    }
    
    return status;
  }
  
  /**
   * Helper method to read clinic specification text file into the Clinic model.
   * 
   * @param m the Clinic model
   * @param v the Clinic view
   * @param specFile the selected or default text file
   * @return the message indicating success or failure
   */
  private String loadSpec(ClinicInterface m, ClinicViewInterface v, String specFile) {
    try {
      m.readIntoModel(new FileReader(specFile));
      v.setMenu(2);
      return "File read into model successfully.";
    } catch (FileNotFoundException e) {
      return "File not found: " + e.getMessage();
    } catch (IOException e) {
      return "Error reading file: " + e.getMessage();
    } catch (IllegalArgumentException e) {
      return "Spec error: " + e.getMessage();
    } catch (NullPointerException e) {
      return "Spec error: " + e.getMessage();
    }
  }
}
