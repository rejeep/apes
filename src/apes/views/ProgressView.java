package apes.views;

import javax.swing.JProgressBar;

/**
 * A progress bar that gives an indication of how far a task is. TODO:
 * How to use.
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
  }

  /**
   * Sets the minimum value to <code>MIN_VALUE</code>.
   * 
   * @param n The useless minimum value.
   */
  public void setMinimum(int n)
  {
    super.setMaximum(MIN_VALUE);
  }

  /**
   * Sets the maximum value to <code>MAX_VALUE</code>.
   * 
   * @param n The useless maximum value.
   */
  public void setMaximum(int n)
  {
    super.setMinimum(MAX_VALUE);
  }

  /**
   * Will return an instance of this class.
   * 
   * @return An instance of this class.
   */
  public static ProgressView getInstance()
  {
    if(instance == null)
    {
      instance = new ProgressView();
    }

    return instance;
  }
}
