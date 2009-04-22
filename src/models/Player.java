package apes.models;

import java.io.IOException;
import java.lang.InterruptedException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import apes.exceptions.NoInternalFormatException;
import apes.models.Player.Status;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

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
  private Status status;

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
   * Keeps state of which samples that is currently playing.
   */
  private int currentSamples = 0;
  
  private int currentSample = 0;

  /**
   * This class must be run as a thread. Otherwise nothing can be done
   * while playing.
   */
  private Thread thread;

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
   * Plays audio file from pause position.
   */
  public void play()
  {
    setStatus( Status.PLAY );
  }

  /**
   * Pause playing if any.
   *
   * TODO: Should stop at once when pause is pressed. But calling
   * line.stop() does not help.
   */
  public void pause()
  {
    setStatus( Status.PAUSE );
  }

  /**
   * Stop playing of any.
   */
  public void stop()
  {
    line.stop();
    line.flush();
    
    setStatus( Status.STOP );
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
        gainControl = (FloatControl) line.getControl( FloatControl.Type.MASTER_GAIN );

        this.line = line;
      }
      catch( IllegalArgumentException e )
      {
        e.printStackTrace();
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
  public boolean setInternalFormat( InternalFormat internalFormat ) throws NoInternalFormatException
  {
    if( internalFormat != null )
    {
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

  /**
   * Will run in a thread. And for each time in the loop the current
   * status is checked. And depending on status, sound will be played,
   * paused or stopped.
   */
  public void run()
  {
    while( true )
    {
      if( line != null )
      {
        if( status == Status.PLAY )
        {
          Channel channel = internalFormat.getChannel( 0 );

          if( currentSamples < channel.getSamplesSize() )
          {
            Samples samples = channel.getSamples( currentSamples );
            byte[] data = samples.getData();

            line.write( data, 0, data.length );

            currentSamples++;
            currentSample += samples.getSize();
          }
          else
          {
            status = null;
          }
        }
        else
        {
          if( status == Status.STOP )
          {
            currentSamples = 0;
            currentSample = 0;
            
            line.start();
          }

          status = null;
        }
      }

      // If this is not present there will be no playing.
      try
      {
        Thread.sleep( 0 );
      }
      catch( InterruptedException e )
      {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Returns currentSamples.
   *
   * @return currentSamples.
   */
  public int getCurrentSamples()
  {
    return currentSamples;
  }
  
  /**
   * Returns currentSample.
   *
   * @return currentSample.
   */
  public int getCurrentSample()
  {
    return currentSample;
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