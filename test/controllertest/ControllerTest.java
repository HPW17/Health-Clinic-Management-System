package controllertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import clinic.Clinic;
import clinic.ClinicInterface;
import controller.Controller;
import controller.ControllerInterface;
import org.junit.Test;
import view.ClinicView;
import view.ClinicViewInterface;

/**
 * Test cases for the Clinic controller.
 */
public class ControllerTest {
  
  /**
   * Test the constructor (getInstance) of the controller. 
   */
  @Test
  public void testControllerConstructor() {
    ClinicInterface m;
    ClinicViewInterface v;
    ControllerInterface c;
    try {
      m = null;
      v = null;
      c = Controller.getInstance(m, v);
      fail("Expected IllegalArgumentException but not happening.");
    } catch (IllegalArgumentException e) {
      assertEquals("Model and ClinicView cannot be null.", e.getMessage());
    }
    
    m = Clinic.getInstance();
    v = ClinicView.getInstance();
    c = Controller.getInstance(m, v);
    assertEquals("class controller.Controller", c.getClass().toString());
  }
  
  /**
   * Tests getInstance returns a new instance if not created 
   * or the same sole instance if it already exists.
   */
  @Test
  public void testGetInstance() {
    ClinicInterface m = Clinic.getInstance();
    ClinicViewInterface v = ClinicView.getInstance();
    ControllerInterface c1 = Controller.getInstance(m, v);
    assertEquals("class controller.Controller", c1.getClass().toString());
    
    ControllerInterface c2 = Controller.getInstance(m, v);
    assertEquals("class controller.Controller", c2.getClass().toString());
    assertTrue(c1.equals(c2));
  }
}
