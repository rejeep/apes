package test;

import apes.models.InternalFormat;
import apes.models.Player;
import org.junit.Before;
import org.junit.Test;
import apes.interfaces.AudioFormatPlugin;
import apes.plugins.WaveFileFormat;

import static org.junit.Assert.*;

/**
 * Test class for Player.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TestPlayer
{
  /**
   * Player to test.
   */
  private Player player;

  /**
   * Initiate player.
   */
  @Before public void createNewPlayer()
  {
    // Tests
    AudioFormatPlugin wave = new WaveFileFormat();
    InternalFormat internalFormat = null;
    player = Player.getInstance();
    
    try
    {
      internalFormat = wave.importFile( "test", "test.wav" );

      player.setInternalFormat( internalFormat );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

    player.setVolume( 0 );
  }

  /**
   * Tests so that status is false by default.
   */
  @Test public void testStatusShuoldBeFalseByDefault()
  {
    assertEquals( "Status should be false by default", Player.Status.STOP, player.getStatus() );
  }

  /**
   * Tests so that play sets status to true.
   */
  @Test public void testPlayShouldSetStatusToPlay()
  {
    player.play();

    assertEquals( "Play should set status to play", Player.Status.PLAY, player.getStatus() );
  }

  /**
   * Tests so that pause sets status to pause.
   */
  @Test public void testPauseShouldSetStatusToPause()
  {
    player.play();
    player.pause();

    assertEquals( "Pause should set status to pause", Player.Status.PAUSE, player.getStatus() );
  }

  /**
   * Tests so that stop sets status to stop.
   */
  @Test public void testStopShouldSetStatusToStop()
  {
    player.play();
    player.stop();

    assertEquals( "Stop should set status to stop", Player.Status.STOP, player.getStatus() );
  }

  /**
   * TODO
   */
  @Test public void testShouldMoveFoward()
  {

  }

  /**
   * TODO
   */
  @Test public void testShouldMoveBackward()
  {

  }

  /**
   * Tests that volume is set if it's a correct volume.
   */
  @Test public void testValidVolumeShuoldBeSet()
  {
    int volume = 50;

    player.setVolume( volume );

    assertEquals( "Should set volume correctly", volume, player.getVolume() );
  }

  /**
   * Tests that volume is not set if it's an incorrect volume.
   */
  @Test public void testInvalidVolumeShuoldNotBeSet()
  {
    int validVolume = 50;
    int invalidVolume = 200;

    player.setVolume( validVolume );
    player.setVolume( invalidVolume );

    assertEquals( "Should not set volume", validVolume, player.getVolume() );
  }

  /**
   * Tests that setting an internal format which is null will return
   * false. And a correct one will return true.
   */
  @Test public void testSettingInteranlFormatShouldOnlyBeValidIfNotNull()
  {
    try
    {
      assertFalse( "Should not set new internal format", player.setInternalFormat( null ) );
      assertTrue( "Should set new internal format", player.setInternalFormat( new InternalFormat( null, 0, null ) ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Tests so that setting a new internal format will set status to
   * stop.
   */
  @Test public void testSettingInteranlFormatShouldStopPlayer()
  {
    try
    {
      player.setInternalFormat( new InternalFormat( null, 0, null ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    
    assertEquals( "Setting internal format should set status to stop", Player.Status.STOP, player.getStatus() );
  }
  
  /**
   * Tests that stopping playing will reset pause position.
   */
  @Test public void testStopShuoldResetPausePosition()
  {
    long expected = 0L;

    player.play();
    player.pause();
    player.stop();
    
    assertEquals( "Stop should reset pause position", expected, player.getPausePosition() );
  }
}