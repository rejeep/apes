package apes.models;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import apes.exceptions.NoInternalFormatException;

/**
 * This class plays a music file. Or more precise, an {@link
 * InternalFormat InternalFormat}.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Player
{
  /**
   * An instance of this class.
   */
  private static Player instance = null;

  /**
   * Different status a Player can be in.
   */
  public enum Status { PLAY, PAUSE, STOP };

  /**
   * This variable holds the current status for the player.
   */
  private Status status = Status.STOP;

  /**
   * A Player is always connected to an internal format.
   */
  private InternalFormat interalFormat;

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
  private Clip clip;

  /**
   * Keeps track over floating-point values.
   */
  private FloatControl gainControl;

  /**
   * Used to keep track of where in audio file the pause occurred.
   */
  private long pausePosition;

  /**
   * Empty and private so that you can not create an object of this
   * class without using the Singleton pattern.
   */
  private Player() {}

  /**
   * Plays audio file from beginning.
   */
  public void play()
  {
    play( 0 );
  }

  /**
   * Plays audio file starting from the given <code>position</code>.
   *
   * @param position the position to start playing from in
   * milliseconds.
   */
  public void play( long position )
  {
    if( clip != null )
    {
      if( status != Status.PLAY )
      {
        setStatus( Status.PLAY );

        clip.setMicrosecondPosition( position );
        clip.loop( 0 );
      }
    }
  }

  /**
   * Pause playing if any.
   */
  public void pause()
  {
    // If there's playing.
    if( status == Status.PLAY )
    {
      setStatus( Status.PAUSE );

      pausePosition = clip.getMicrosecondPosition();

      clip.stop();
    }
  }

  /**
   * Stop playing of any.
   */
  public void stop()
  {
    // If there's no playing.
    if( status == Status.PLAY )
    {
      setStatus( Status.STOP );

      pausePosition = 0L;

      clip.stop();
    }
  }

  /**
   *
   */
  public void forward()
  {

  }

  /**
   *
   */
  public void backward()
  {

  }

  /**
   * Get the volume for this player.
   *
   * @return the volume.
   */
  public int getVolume()
  {
    return volume;
  }

  /**
   * Set the volume for this player.
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
   * Return pause position for this player. 0L means that there's no
   * pausing.
   *
   * @return The pause position.
   */
  public long getPausePosition()
  {
    return this.pausePosition;
  }

  /**
   * Initializes this player. This must be done when setting a new
   * internal format.
   *
   * @exception NoInternalFormatException If there's no internal
   * format.
   * @exception UnsupportedAudioFileException if the File does not
   * point to valid audio file.
   * @exception IOException if any I/O exception occurs.
   */
  private void init() throws NoInternalFormatException, UnsupportedAudioFileException, IOException
  {
    if( interalFormat == null )
    {
      throw new NoInternalFormatException();
    }
    else
    {
      File file = new File( interalFormat.getFileStatus().getFullPath() );
      AudioInputStream audioIn = AudioSystem.getAudioInputStream( file );
      clip = makeClip( audioIn );
    }
  }

  /**
   * Returns the <code>InternalFormat</code> connected to this player.
   *
   * @return the <code>InternalFormat</code>.
   */
  public InternalFormat getInternalFormat()
  {
    return this.interalFormat;
  }

  /**
   * Set the <code>InternalFormat</code> for this player and
   * initialize it.
   *
   * @param interalFormat the <code>InternalFormat</code> to use.
   * @return true if <code>internalFormat</code> is not null. False
   * otherwise.
   * @exception Exception if {@link Player#init init} fails.
   */
  public boolean setInternalFormat( InternalFormat interalFormat ) throws NoInternalFormatException, UnsupportedAudioFileException, IOException
  {
    if( interalFormat != null )
    {
      stop();
      
      this.interalFormat = interalFormat;      
      
      init();
      
      return true;
    }

    return false;
  }

  /**
   * Returns current status.
   *
   * @return Current status.
   */
  public Status getStatus()
  {
    return this.status;
  }

  /**
   * Sets the status to <code>status</code>
   *
   * @param status The new status.
   */
  private void setStatus( Status status )
  {
    this.status = status;
  }

  /**
   * Returns a <code>Clip</code> for an audio input stream.
   *
   * @param The <code>AudioInputStream</code> object to connect the
   * <code>Clip</code> to.
   * @return the <code>Clip</code>.
   */
  private Clip makeClip( AudioInputStream audioIn )
  {
    Clip clip = null;

    try
    {
      DataLine.Info dataLineInfo = new DataLine.Info( Clip.class, audioIn.getFormat() );

      clip = (Clip) AudioSystem.getLine( dataLineInfo );
      clip.open( audioIn );

      // For volume control
      gainControl = (FloatControl) clip.getControl( FloatControl.Type.MASTER_GAIN );
    }
    catch( LineUnavailableException e )
    {
      e.printStackTrace();
    }
    catch( IOException e2 )
    {
      e2.printStackTrace();
    }

    return clip;
  }

  /**
   * Will return an instance of this class.
   *
   * @return An instance of this class.
   */
  public static Player getInstance()
  {
    if( instance == null )
    {
      instance = new Player();
    }

    return instance;
  }
}