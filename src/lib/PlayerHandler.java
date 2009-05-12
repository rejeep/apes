package apes.lib;

import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.Samples;

/**
 * Wrapper for all players. So instead of calling play on a Player,
 * play in this class is called.
 *
 * It also handles the volume.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PlayerHandler
{
  /**
   * Current volume.
   */
  private int volume;

  /**
   * Minimum value for the player.
   */
  public static final int MIN_VALUE = 0;

  /**
   * Maximum value for the player.
   */
  public static final int MAX_VALUE = 100;

  /**
   * Represents a special kind of data line whose audio data can be
   * loaded prior to playback.
   */
  private SourceDataLine line;

  /**
   * Keeps track over floating-point values.
   */
  private FloatControl gainControl;

  /**
   * All players that currently exists.
   */
  private Set<Player> players;

  /**
   * The player that is playing.
   */
  private Player currentPlayer;

  /**
   * Temporary internal format. See usage in code.
   */
  private InternalFormat internalFormat;


  /**
   * Creates a new <code>PlayerHandler</code>.
   */
  public PlayerHandler()
  {
    players = new HashSet<Player>();
  }

  /**
   * Does some initialization such as fetching the line and getting
   * volume control.
   *
   * NOTE: {@link PlayerHandler#setInternalFormat setInternalFormat} must be called
   * before this.
   */
  private void init()
  {
    try
    {
      AudioFormat format = new AudioFormat( internalFormat.getSampleRate(), Samples.BITS_PER_SAMPLE, internalFormat.getNumChannels(), true, false );
      DataLine.Info info = new DataLine.Info( SourceDataLine.class, format );

      if( line != null )
      {
        line.close();
      }

      line = (SourceDataLine) AudioSystem.getLine( info );
      line.open( format );
      line.start();

      // Get volume control.
      gainControl = (FloatControl) line.getControl( FloatControl.Type.MASTER_GAIN );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Plays the current player.
   */
  public void play()
  {
    if( currentPlayer != null )
    {
      currentPlayer.play();
    }
  }

  /**
   * Pauses the current player.
   */
  public void pause()
  {
    if( currentPlayer != null )
    {
      currentPlayer.pause();
    }
  }

  /**
   * Stops the current player.
   */
  public void stop()
  {
    if( currentPlayer != null )
    {
      currentPlayer.stop();
    }
  }

  /**
   * Go forward.
   */
  public void forward()
  {
    if( currentPlayer != null )
    {
      currentPlayer.forward();
    }
  }

  /**
   * Go backward.
   */
  public void backward()
  {
    if( currentPlayer != null )
    {
      currentPlayer.backward();
    }
  }

  /**
   * Get the current volume.
   *
   * @return the volume.
   */
  public int getVolume()
  {
    return volume;
  }

  /**
   * Set the volume.
   *
   * @param volume The new volume.
   */
  public void setVolume( int volume )
  {
    if( volume >= MIN_VALUE && volume <= MAX_VALUE )
    {
      this.volume = volume;

      float value = (float)( Math.log( this.volume / 100.0 ) / Math.log( 10.0 ) * 20.0 );

      if( gainControl != null )
      {
        gainControl.setValue( value );
      }
    }
  }

  /**
   * Return the internal format connected to the current player.
   *
   * @return The internal format.
   */
  public InternalFormat getInternalFormat()
  {
    if( currentPlayer != null )
    {
      return currentPlayer.getInternalFormat();
    }

    return null;
  }

  /**
   * Sets a new internal format. If a player exists with that
   * internalFormat, it is used. Otherwise a new Player is created.
   *
   * @param internalFormat an <code>InternalFormat</code> value
   * @return The player that is associated with <code>internalFormat</code>.
   */
  public Player setInternalFormat( InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;

    // Pause if there's a player.
    if( currentPlayer != null )
    {
      currentPlayer.pause();
    }

    // Get or create a new player.
    Player player = getPlayer( internalFormat );

    if( player == null )
    {
      player = new Player( internalFormat );
    }

    init();

    players.add( player );
    currentPlayer = player;
    currentPlayer.setLine( line );
    
    return player;
  }

  /**
   * Returns the Player that is connected to an internal format.
   *
   * @param internalFormat The internal format the Player should have.
   * @return The Player.
   */
  public Player getPlayer( InternalFormat internalFormat )
  {
    for( Player player : players )
    {
      if( player.getInternalFormat().equals(internalFormat) )
      {
        return player;
      }
    }

    return null;
  }

  /**
   * Remove the Player that has <code>internalFormat</code> as
   * internal format.
   *
   * @param internalFormat an <code>InternalFormat</code> value
   */
  public void remove( InternalFormat internalFormat )
  {
    Player player = getPlayer( internalFormat );
    player.stop();
    
    if( player.equals( currentPlayer ) )
    {
      currentPlayer = null;
    }

    players.remove( player );
  }
  
  /**
   * Returns the current player.
   *
   * @return The current player.
   */
  public Player getCurrentPlayer()
  {
    return currentPlayer;
  }
}
