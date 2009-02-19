package test;

import apes.models.Player;
import apes.models.InternalFormat;

import org.junit.Test;
import org.junit.Before;

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
    player = Player.getInstance();
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
    player.pause();

    assertEquals( "Pause should set status to pause", Player.Status.PAUSE, player.getStatus() );
  }

  /**
   * Tests so that stop sets status to stop.
   */
  @Test public void testStopShouldSetStatusToStop()
  {
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
    assertFalse( "Should not set new internal format", player.setInternalFormat( null ) );
    assertTrue( "Should set new internal format", player.setInternalFormat( new InternalFormat( null, 0, null ) ) );
  }

  /**
   * Tests so that setting a new internal format will set status to
   * stop.
   */
  @Test public void testSettingInteranlFormatShouldStopPlayer()
  {
    player.setInternalFormat( new InternalFormat( null, 0, null ) );
    
    assertEquals( "Setting internal format should set status to stop", Player.Status.STOP, player.getStatus() );
  }
}