package apes.models;

import java.lang.InterruptedException;
import javax.sound.sampled.SourceDataLine;

/**
 * This class plays an internal format. It implements Runnable rather
 * than extending Thread because methods such as stop and pause is in
 * the Thread class aswell as in this (with a different meaning).
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Player implements Runnable
{
  /**
   * The data line.
   */
  private SourceDataLine line;

  /**
   * Different status a Player can be in.
   */
  public enum Status { PLAY, PAUSE, STOP, WAIT };

  /**
   * This variable holds the current status for the player.
   */
  private Status status;

  /**
   * A Player is always connected to an internal format.
   */
  private InternalFormat internalFormat;

  /**
   * Keeps state of which samples that is currently playing.
   */
  private int currentSamples;

  /**
   * What sample (not samples are we on).
   */
  private int currentSample;

  /**
   * The number of samples in a channel.
   */
  private int numSamples;

  /**
   * Current channel.
   *
   * TODO: Must be able to play from many channels.
   */
  private Channel channel;

  /**
   * This class must be run as a thread. Otherwise nothing can be done
   * while playing.
   */
  private Thread thread;

  /**
   * Creates a new <code>Player</code>.
   *
   * @param internalFormat The internal format that is to be played.
   */
  public Player( InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;

    channel = internalFormat.getChannel( 0 );
    numSamples = channel.getSamplesSize();

    reset();
    setStatus( Status.WAIT );

    thread = new Thread( this );
    thread.start();
  }

  /**
   * Plays the internal format.
   */
  public void play()
  {
    setStatus( Status.PLAY );
  }

  /**
   * Pauses playing if any.
   *
   * TODO: Should stop at once when pause is pressed. But calling
   * line.stop() does not help.
   */
  public void pause()
  {
    setStatus( Status.PAUSE );
  }

  /**
   * Stops playing if any.
   */
  public void stop()
  {
    setStatus( Status.STOP );
  }

  /**
   * Goes forward.
   */
  public void forward()
  {
    int start = currentSamples;
    int stop = start + ( numSamples / 20 );

    if( stop > numSamples )
    {
      stop = numSamples;
    }

    for( int i = start; i < stop; i++ )
    {
      Samples samples = channel.getSamples( currentSamples );

      currentSamples++;
      currentSample += samples.getSize();
    }
  }

  /**
   * Goes backward.
   */
  public void backward()
  {
    int start = currentSamples;
    int stop = start - ( numSamples / 20 );

    if( stop < 0 )
    {
      stop = 0;
    }

    for( int i = start; i > stop; i-- )
    {
      Samples samples = channel.getSamples( currentSamples );

      currentSamples--;
      currentSample -= samples.getSize();
    }
  }

  /**
   * Returns the <code>InternalFormat</code> connected to this player.
   *
   * @return the <code>InternalFormat</code>.
   */
  public InternalFormat getInternalFormat()
  {
    return internalFormat;
  }

  /**
   * Set the <code>InternalFormat</code> for this player.
   */
  public void setInternalFormat( InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;
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
      if( status != Status.WAIT )
      {
        if( status == Status.PLAY )
        {
          // Do the playing.
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
            status = Status.WAIT;
          }
        }
        else if( status == Status.STOP )
        {
          reset();

          setStatus( Status.WAIT );
        }
        else
        {
          // Status is PAUSE
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
   * Sets the line for this player.
   *
   * @param line The line.
   */
  public void setLine( SourceDataLine line )
  {
    this.line = line;
  }

  /**
   * Resets this player by setting starting points to 0.
   */
  private void reset()
  {
    currentSamples = 0;
    currentSample = 0;
  }
}