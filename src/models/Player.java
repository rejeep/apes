package apes.models;

import java.awt.Point;
import java.lang.InterruptedException;
import javax.sound.sampled.SourceDataLine;
import apes.lib.Config;
import java.util.Observable;

/**
 * This class plays an internal format. It implements Runnable rather
 * than extending Thread because methods such as stop and pause is in
 * the Thread class aswell as in this (with a different meaning).
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Player extends Observable implements Runnable
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
   * What sample (not samples are we on).
   */
  private int currentSample;

  /**
   * Where to start playing, in samples.
   */
  private int start;

  /**
   * This is how far we are allowed to play, in samples.
   */
  private int stop;

  /**
   * Tells how long a wind should be. The larger value, the less wind
   * amount.
   */
  private int wind;

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
    this.wind = Config.getInstance().getIntOption( "wind" );

    resetStop();
    resetStart();

    currentSample = 0;
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
    int temp = currentSample + getWindLength();
    int max = Math.min( getSampleAmount(), stop );

    currentSample = temp >= max ? max : temp;
  }

  /**
   * Goes backward.
   */
  public void backward()
  {
    int temp = currentSample - getWindLength();
    int min = Math.max( 0, start );

    currentSample = temp <= min ? min : temp;
  }

  /**
   * Returns the wind length.
   *
   * @return The wins length.
   */
  private int getWindLength()
  {
    return getSampleAmount() / wind;
  }

  /**
   * Returns the selection as a point. x is start and y i stop.
   *
   * @return The selection.
   */
  public Point getSelection()
  {
    return new Point( start, stop );
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
          if( stop == 0 || currentSample <= stop )
          {
            byte[] data = internalFormat.getChunk( currentSample, Channel.SAMPLES_SIZE );

            line.write( data, 0, data.length );

            increaseCurrentSample();
          }
          else
          {
            if( stop == getSampleAmount() )
            {
              stop();
            }
            else
            {
              pause();

              currentSample = start;
            }
          }
        }
        else
        {
          if( status == Status.STOP )
          {
            currentSample = 0;
          }

          setStatus( Status.WAIT );
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
   * Sets the line for this player.
   *
   * @param line The line.
   */
  public void setLine( SourceDataLine line )
  {
    this.line = line;
  }

  /**
   * Sets playing region.
   *
   * @param selection The selection.
   */
  public void setRegion( Point selection )
  {
    if( isPlaying() )
    {
      resetStop();
    }
    else
    {
      int min = selection.x;
      int max = selection.y;

      if( min == max )
      {
        resetStop();
        resetStart();
      }
      else
      {
        start = min;
        stop = max;
      }

      currentSample = min;
    }
  }

  /**
   * Returns true if the player is playing. False otherwise.
   *
   * @return true if playing. False otherwise.
   */
  public boolean isPlaying()
  {
    if( status == Status.PLAY )
    {
      return true;
    }

    return false;
  }

  /**
   * Sets the stop position to the end position.
   */
  private void resetStop()
  {
    stop = getSampleAmount();
  }

  /**
   * Sets the start position to the beginning of the file.
   */
  private void resetStart()
  {
    start = 0;
  }

  /**
   * Returns the number of samples.
   *
   * @return The number of samples.
   */
  public int getSampleAmount()
  {
    return internalFormat.getSampleAmount() - 1;
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
   * Sets the current sample position.
   *
   * @param currentSample The position.
   */
  public void setCurrentSample( int currentSample )
  {
    this.currentSample = currentSample;
    
    setChangedAndNotifyAll();
  }

  /**
   * Returns start position.
   *
   * @return The start position.
   */
  public int getStart()
  {
    return start;
  }

  /**
   * Sets the start position.
   *
   * @param start the start position.
   */
  public void setStart( int start )
  {
    this.start = start;

    setChangedAndNotifyAll();
  }

  /**
   * Returns stop position.
   *
   * @return The stop position.
   */
  public int getStop()
  {
    return stop;
  }

  /**
   * Sets the stop position.
   *
   * @param stop the stop position.
   */
  public void setStop( int stop )
  {
    this.stop = stop;

    setChangedAndNotifyAll();
  }
  
  /**
   * Increases <code>currentSample</code> by one step.
   */
  private void increaseCurrentSample()
  {
    setCurrentSample( currentSample + Channel.SAMPLES_SIZE );
  }
 
  /**
   * Set changed and notify all observers.
   */
  private void setChangedAndNotifyAll()
  {
    setChanged();
    
    notifyObservers();
  }
}