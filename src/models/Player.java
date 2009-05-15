package apes.models;

import java.awt.Point;
import java.lang.InterruptedException;
import javax.sound.sampled.SourceDataLine;
import apes.lib.Config;
import java.util.Observable;

/**
 * This class plays an internal format.
 * TODO: May be a bug when reaching end of file, making strange noises without stopping or something
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Player extends Observable implements Runnable
{
  /**
   * How large is a chunk.
   */
  private final static int CHUNK_SIZE = 1024; 
  
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
   * The fixed position.
   */
  private int fixed;

  /**
   * The moving position.
   */
  private int moving;

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
    int start = getStart();
    int stop = getStop();
    int max = stop;
      
    if( stop == 0 || start == stop )
    {
      max = getSampleAmount();
    }
    
    setCurrentSample( temp >= max ? max : temp );
  }

  /**
   * Goes backward.
   */
  public void backward()
  {
    int temp = currentSample - getWindLength();
    int start = getStart();
    int stop = getStop();
    int min = start;

    if( start == stop )
    {
      min = 0;
    }
    
    setCurrentSample( temp < min ? min : temp );
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
    return new Point( getStart(), getStop() );
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
   * Sets the line for this player.
   *
   * @param line The line.
   */
  public void setLine( SourceDataLine line )
  {
    this.line = line;
  }

  /**
   * Returns true if the player is playing. False otherwise.
   *
   * @return true if playing. False otherwise.
   */
  public boolean isPlaying()
  {
    return status == Status.PLAY;
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
    return Math.min( fixed, moving );
  }

  /**
   * Sets the start position.
   *
   * @param start the start position.
   */
  public void setStart( int start )
  {
    if( moving < fixed )
    {
      moving = start;
    }
    else
    {
      fixed = start;
    }

    setChangedAndNotifyAll();
  }

  /**
   * Returns stop position.
   *
   * @return The stop position.
   */
  public int getStop()
  {
    return Math.max( fixed, moving );
  }

  /**
   * Sets the stop position.
   *
   * @param stop the stop position.
   */
  public void setStop( int stop )
  {
    if( moving > fixed )
    {
      moving = stop;
    }
    else
    {
      fixed = stop;
    }

    setChangedAndNotifyAll();
  }

  /**
   * Sets the moving mark to <code>mark</code>.
   *
   * @param mark The <code>mark</code>.
   */
  public void setMark( int mark )
  {
    moving = mark;

    setChangedAndNotifyAll();
  }

  /**
   * Change mark closest to <code>mark</code> to <code>mark.</code>.
   *
   * @param mark The mark.
   */
  public void setClosestMark( int mark )
  {
    int fromMoving = Math.abs( mark - moving );
    int fromFixed = Math.abs( mark - fixed );

    if( fromMoving > fromFixed )
    {
      int temp = fixed;
      fixed = moving;
      moving = temp;
    }

    setMark( mark );
  }

  /**
   * Set all marks to <code>mark</code>.
   *
   * @param mark The mark.
   */
  public void setAllMarks( int mark )
  {
    moving = mark;
    fixed = mark;
    currentSample = mark;

    setChangedAndNotifyAll();
  }

  /**
   * Increases <code>currentSample</code> by one step.
   */
  private void increaseCurrentSample()
  {
    setCurrentSample( currentSample + CHUNK_SIZE );
  }

  /**
   * Set changed and notify all observers.
   */
  private void setChangedAndNotifyAll()
  {
    setChanged();

    notifyObservers();
  }

  /**
   * Returns true if playing is allowed. False otherwise.
   *
   * @return True if playing is allowed. False otherwise.
   */
  private boolean playingAllowed()
  {
    int start = getStart();
    int stop = getStop();

    if( (stop != 0 && start != stop && currentSample > stop) || (currentSample > getSampleAmount()))
      return false;
    return true;
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
      switch(status)
      {
      case WAIT:
        break;
      case PLAY:
        if( playingAllowed() )
        {
          int chunk = CHUNK_SIZE;
          if(getSampleAmount() < currentSample + CHUNK_SIZE)
            chunk = getSampleAmount() - currentSample;
          byte[] data = internalFormat.getChunk( currentSample, chunk );

          line.write( data, 0, data.length );

          increaseCurrentSample();
        }
        else
        {
          if( currentSample > getSampleAmount() )
          {
            stop();
          }
          else
          {
            pause();         
            setCurrentSample(getStart());
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
        break;
      case STOP:
        currentSample = 0;
        setStatus( Status.WAIT );
        break;
      case PAUSE:
        setStatus( Status.WAIT );
        break;
      }  
    }
  }
}