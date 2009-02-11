package test;

import java.io.File;

import org.junit.*;
import org.junit.Before;
import src.lib.Config;

import static org.junit.Assert.*;

/**
 * Test class for Config.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TestConfig
{
  /**
   * Config object that is visible for all test methods in this class.
   */
  private Config config;

  /**
   * Sets up a Config object and reads in a configuration file used
   * for testing.
   */
  @Before public void setUpConfigObject()
  {
    config = Config.getInstance();
    config.setFilePath("test/test_config");
    config.parse();
  }

  /**
   * Tests so that the parser reads in the options correctly.
   */
  @Test public void testConfigParserShouldReadInTheOptionsCorrectly()
  {
    assertNotNull( "First option should be valid.", config.getOption( "first" ) );
    assertNotNull( "Second option should be valid.", config.getOption( "second" ) );
    assertNull( "Third option should *not* be valid.", config.getOption( "third.option" ) );
    assertNotNull( "Fourth option should be valid.", config.getOption( "fourth_option" ) );
  }

  /**
   * Tests so that the file path is set correctly and so that the getter is
   * what you actually set.
   */
  @Test public void testShouldGetAndSetNameCorrectly()
  {
    String fileName = "thisismyconfigurationfile.txt";
    config.setFilePath( fileName );
    assertEquals( "Should return a string that is the absolute path to the configuration file", new File( fileName ).getAbsolutePath(), config.getFilePath() );
  }
  
  /**
   * Tests that the getOption method returns the correct value to a
   * key.
   */
  @Test public void testThatGetOptionReturnsCorrectValue()
  {
    assertEquals( "Should give correct value for given key.", "Great day", config.getOption("fourth_option") );
  }
}