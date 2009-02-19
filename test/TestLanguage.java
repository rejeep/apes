package test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import apes.lib.Language;
import static junit.framework.Assert.assertTrue;


public class TestLanguage
{

  /**
   * Sets up a Config object and reads in a configuration file used
   * for testing.
   */
  @Before
  public void setUpLanguage()
  {
    Language.initLanguage("en");
    try
    {
      Language.load();
    } catch ( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Tests so that the parser reads in the words correctly from different files.
   */
  @Test
  public void findWordInDifferentFiles()
  {
    assertEquals( "Words should be equal", "File", Language.get( "menu.head.file" ) );
    Language.setLanguage( "sv" );
    try
    {
      Language.load();
    } catch ( Exception e )
    {
      e.printStackTrace();
    }
    assertEquals( "Words should be equal", "Arkiv", Language.get( "menu.head.file" ) );
  }

  /**
   * Tests so that the parser can handle differnt groups.
   */
  @Test
  public void testDifferentWordsInDifferentGroups()
  {
    assertEquals( "Words should be equal", "Open", Language.get( "menu.file.open" ) );
    assertEquals( "Words should be equal", "In", Language.get( "menu.view.zoom.in" ) );
  }

  /**
   * Tests so that the parser can handle sentences.
   */
  @Test
  public void testSentence()
  {                               
    assertEquals( "Sentences should be equal", "Open music file", Language.get( "button.open.description" ) );
  }
}

