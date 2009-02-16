package test;

import org.junit.*;
import org.junit.Before;
import src.lib.Locale;

import static org.junit.Assert.*;

/**
 * Test class for Locale.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TestLocale
{
  /**
   * Locale object that is visible for all test methods in this class.
   */
  private Locale locale;

  /**
   * Sets up a Locale object and reads in a locale file used for
   * testing.
   */
  @Before public void setUpLocaleObject()
  {
    locale = Locale.getInstance();
    locale.setLocalePath( "test/locales" );
    locale.setLocale( "en" );
    locale.load();
  }

  /**
   * Tests so that the locale path is set.
   */
  @Test public void testShouldSetLocalePath()
  {
    String localePath = "path/to/locale/files";
    locale.setLocalePath( localePath );

    assertEquals( "Should return same locale path as the one that was set.", localePath, locale.getLocalePath() );
  }

  /**
   * Tests so that the default locale is set.
   */
  @Test public void testShouldSetLocale()
  {
    String defaultLocale = "en";
    locale.setLocale( defaultLocale );

    assertEquals( "Should return same locale as the one that was set.", defaultLocale, locale.getLocale() );
  }

  /**
   * get method should return value for tag.
   */
  @Test public void testGetShouldReturnCorrectValueForTag()
  {
    String tag = "person.name";

    assertNotNull( "Should get value for tag", locale.get( tag ) );
  }

  /**
   * get method should return null if there's no value for tag in
   * current locale.
   */
  @Test public void testGetShouldReturnNullIfTheresNoValueForTag()
  {
    String tag = "person.sex";

    assertNull( "Should return null since value does not exist.", locale.get( tag ) );
  }
}