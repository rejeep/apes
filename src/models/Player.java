package apes.models;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import apes.exceptions.NoInternalFormatException;

/**
 * This class plays a music file. Or more precise, an {@link
 * InternalFormat InternalFormat}.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Player implements Runnable
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
  private InternalFormat internalFormat;

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
   * Used to keep track of where in audio file the pause occurred.
   */
  private long pausePosition;

  /**
   * This class must be run as a thread. Otherwise nothing can be done
   * while playing.
   */
  Thread thread;

  /**
   * Private so that an object of this class can not be created
   * without using the Singleton pattern.
   */
  private Player()
  {
    thread = new Thread( this );
    thread.start();
  }

  /**
   * Plays audio file from beginning.
   */
  public void play()
  {
    play( 0 );
  }

  private int currentSamples = 0;

  /**
   * Plays audio file starting from the given <code>position</code>.
   *
   * @param position the position to start playing from in
   * milliseconds.
   */
  public void play( long position )
  {
    if( line != null )
    {
      if( status != Status.PLAY )
      {
        setStatus( Status.PLAY );

        Channel channel = internalFormat.getChannel( 0 );

        for( int i = currentSamples; i < channel.getSamplesSize(); i++ )
        {
          Samples samples = channel.getSamples( i );
          byte[] data = samples.getData();

          line.write( data, 0, data.length );
        }

        line.drain();
        line.close();
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

      pausePosition = line.getMicrosecondPosition();

      line.stop();
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

      line.stop();
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
  private void init() throws NoInternalFormatException
  {
    if( internalFormat == null )
    {
      throw new NoInternalFormatException();
    }
    else
    {
      try
      {
        AudioFormat format = new AudioFormat( internalFormat.getSampleRate(), Samples.BITS_PER_SAMPLE, internalFormat.getNumChannels(), true, false );
        DataLine.Info info = new DataLine.Info( SourceDataLine.class, format );

        line = (SourceDataLine) AudioSystem.getLine( info );
        line.open( format );
        line.start();

        // For volume control
        // gainControl = (FloatControl) line.getControl( FloatControl.Type.MASTER_GAIN );

        this.line = line;
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns the <code>InternalFormat</code> connected to this player.
   *
   * @return the <code>InternalFormat</code>.
   */
  public InternalFormat getInternalFormat()
  {
    return this.internalFormat;
  }

  /**
   * Set the <code>InternalFormat</code> for this player and
   * initialize it.
   *
   * @param internalFormat the <code>InternalFormat</code> to use.
   * @return true if <code>internalFormat</code> is not null. False
   * otherwise.
   * @exception Exception if {@link Player#init init} fails.
   */
  public boolean setInternalFormat( InternalFormat internalFormat ) throws NoInternalFormatException, UnsupportedAudioFileException, IOException
  {
    if( internalFormat != null )
    {
      stop();

      this.internalFormat = internalFormat;

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

  public void run()
  {
    
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