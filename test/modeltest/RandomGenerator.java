package modeltest;

import java.util.Random;

/**
 * The second constructor takes a variable number of integers as input, 
 * storing them in the predefinedSet array. This array will be used to provide 
 * predictable random numbers.
 * 
 * - {@code random}: instance of Random
 * - {@code predefinedSet}: a predefined set of values from which to pick up randomly
 */
public class RandomGenerator {

  private final Random random;
  private final int[] predefinedSet;

  /**
   * This class represents an encapsulated Random class that provides a truly random number 
   * or a mocked random number from a predefined set of values.
   */
  public RandomGenerator() {
    this.random = new Random();
    this.predefinedSet = null;
  }

  /**
   * The second constructor takes a variable number of integers as input, 
   * storing them in the predefinedSet array. This array will be used to provide 
   * predictable random numbers.
   * 
   * @param predefinedSet the predefined set of values from which to pick up randomly
   */
  public RandomGenerator(int... predefinedSet) {
    this.random = new Random();
    this.predefinedSet = predefinedSet;
  }

  /**
   * If predefinedSet is provided, this method uses the random object to generate 
   * a random index within the bounds of the sequence and returns the value at that index. 
   * Otherwise, it uses the random object to generate a random integer.
   * 
   * @return the next random number
   */
  public int nextInt() {
    if (predefinedSet != null) {
      int index = Math.abs(random.nextInt()) % predefinedSet.length;
      return predefinedSet[index];
    } else {
      return random.nextInt();
    }
  }
  
  /**
   * Overloaded method to get a random number within the range of 0..bound-1. 
   * If predefinedSet is provided, the result must clamp to the range. 
   * 
   * @param bound the upper bound for the range of generated random number (0..bound-1)
   * @return the next random number clamped to the range
   */
  public int nextInt(int bound) {
    if (predefinedSet != null) {
      int index = Math.abs(random.nextInt()) % predefinedSet.length;
      return Math.max(0, Math.min(bound, predefinedSet[index])); // Clamp to the range
    } else {
      return random.nextInt(bound);
    }
  }
}

