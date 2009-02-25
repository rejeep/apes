package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import apes.models.Samples;

/**
 * Test file for Samples
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class TestSamples
{
  
  /**
   * Tests that a new samples object have the right size.
   */
  @Test
  public void testSizeIsSetCorrectly()
  {
    int expected = 10;
    Samples s = new Samples( 10 );
    assertEquals( "Samples(int,int) should give specified size.", expected, s.getSize() );
    
    byte[] b = new byte[ expected * Samples.BYTES_PER_SAMPLE ];
    try
    {
      s = new Samples( Samples.BITS_PER_SAMPLE, b );
    }catch(Exception e)
    {
      e.printStackTrace();
    }
    assertEquals( "Samples(int,byte[]) should give correct size.", expected, s.getSize() );
  }
  
  /**
   * Tests that initialization seems to work. 
   * Warning: This assumes getter works. This is bad, but how can we check one without assuming the other?
   */
  @Test
  public void testInitialization()
  {
    assertTrue( "Not implemented yet", false );
  }
  
  /**
   * Tests that getSample returns the correct value.
   */
  @Test
  public void testGetSample()
  {
    assertTrue( "Not implemented yet", false );
  }
  
  /**
   * Tests that setSample updates the samples correctly.
   */
  @Test
  public void testSet()
  {
    assertTrue( "Not implemented yet", false );
  }
  
  /**
   * Tests that maximum amplitude is set correctly.
   */
  @Test
  public void testMaxAmplitudeIsCorrect()
  {
    int lowest = 0, mid = 6, max = 8, newMax = 10;
    int size = 3;
    byte[] b = new byte[ size ];
    
    // First Sample
    b[0] = (byte)lowest;
      
    // Second Sample
    b[1] = (byte)mid;
    
    // Third Sample
    b[2] = (byte)max;
    
    try
    {
      Samples s = new Samples( size );
      assertEquals( "Default max amplitude is 0", 0, s.getMaxAmplitude() );
      
      s = new Samples( 8, b );
      assertEquals( "Maximum amplitude correctly initialized", max, s.getMaxAmplitude() );
      
      s.setSample( 1, newMax );
      assertEquals( "Max amplitude updates when new max is set", newMax, s.getMaxAmplitude() );
      
      s.setSample( 1, lowest );
      assertEquals( "Max amplitude updates when current max is overwritten", max, s.getMaxAmplitude() );
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
}
