package apes.views;

import javax.swing.JProgressBar;

/**
 * A progress bar that gives an indication of how far a task is.
 *
 * TODO: How to use.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ProgressView extends JProgressBar
{
  /**
   * The minimum value for the bar.
   */
  public final static int MIN_VALUE = 0;
  
  /**
   * The maximum value for the bar.
   */
  public final static int MAX_VALUE = 100;
  
  /**
   * An instance of this class.
   */
  private static ProgressView instance = null;

  /**
   * Private so that it only can be created through getInstance().
   */
  private ProgressView()
  {
    setStringPainted(true);
    reset();
  }
  
  /**
   * Resets the values.
   */
  public void reset()
  {
    setMaximum(MIN_VALUE);
    setMinimum(MAX_VALUE);
    setValue(MIN_VALUE);
  }
  
  /**
   * Will return an instance of this class.
   * 
   * @return An instance of this class.
   */
  public static ProgressView getInstance()
  {
    if( instance == null )
    {
      instance = new ProgressView();
    }

    return instance;
  }
}