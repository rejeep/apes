package apes.controllers;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import apes.models.InternalFormat;
import apes.models.Player;
import apes.views.ChannelView;
import apes.views.InternalFormatStatusPanel;


/**
 * Channel controller.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ChannelController extends ApplicationController implements MouseListener,
    MouseMotionListener, MouseWheelListener
{
  /**
   * True if mouse if pressed. False otherwise.
   */
  private boolean mouseDown;

  /**
   * The player.
   */
  private Player player;

  /**
   * The channel view.
   */
  private ChannelView channelView;

  /**
   * The status panel.
   */
  private InternalFormatStatusPanel statusPanel;

  /**
   * Creates a new <code>ChannelController</code> instance.
   * 
   * @param player The player.
   */
  public ChannelController(Player player)
  {
    this.player = player;
  }

  public void mousePressed(MouseEvent e)
  {
    int x = e.getX();

    // Left mouse button.
    if(e.getButton() == MouseEvent.BUTTON1)
    {
      // Single click.
      if(e.getClickCount() == 1)
      {
        int mark = channelView.pixelsToSamples(x);

        player.setAllMarks(mark);
      }
      // Select all if more than one click.
      else
      {
        int firstVisibleSample = channelView.getCenter() - channelView.getZoom() / 2;
        int lastVisibelSample = channelView.getCenter() + channelView.getZoom() / 2;

        player.setStart(firstVisibleSample);
        player.setStop(lastVisibelSample);
      }
    }
    // Middle mouse button (often wheel).
    else if(e.getButton() == MouseEvent.BUTTON2)
    {
      player.setStart(0);
      player.setStop(0);
    }

    mouseDown = true;
  }

  public void mouseReleased(MouseEvent e)
  {
    mouseDown = false;

    if(isSelection())
    {
      player.setCurrentSample(player.getStart());
    }
  }

  public void mouseExited(MouseEvent e)
  {
    int y = e.getY();
    int x = e.getX();

    // If the mouse don't exist at the top or bottom.
    if(y > 0 && y < channelView.getGraphHeight())
    {
      if(mouseDown)
      {
        // If the mouse existed to the left.
        if(x <= 0)
        {
          player.setStart(0);
        }
        else
        {
          player.setStop(player.getSampleAmount());
        }
      }
    }
  }

  public void mouseDragged(MouseEvent e)
  {
    int y = e.getY();
    int x = e.getX();
    int mark = channelView.pixelsToSamples(x);

    // Is the mouse inside the panel.
    if(inView(x, y))
    {
      if(e.getModifiers() == InputEvent.BUTTON1_MASK)
      {
        player.setMark(mark);
      }
      else if(e.getModifiers() == InputEvent.BUTTON3_MASK)
      {
        // Is there a selection.
        if(isSelection())
        {
          player.setClosestMark(mark);
        }
      }
    }
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    if(isSelection())
    {
      // -1 scroll wheel up.
      // 1 scroll wheel down.
      int rotation = e.getWheelRotation();

      int start = player.getStart();
      int stop = player.getStop();

      InternalFormat internalFormat = player.getInternalFormat();
      internalFormat.scaleSamples(start, stop, 1.0f - rotation * 0.1f);
      internalFormat.updated();
    }
  }

  public void mouseMoved(MouseEvent e)
  {
    int y = e.getY();
    int x = e.getX();

    // Is the mouse inside the panel.
    if(inView(x, y))
    {
      channelView.setMousePosX(x);
      channelView.repaint();
    }
  }

  public void mouseEntered(MouseEvent e)
  {}

  public void mouseClicked(MouseEvent e)
  {}

  /**
   * Returns this player.
   * 
   * @return The player;
   */
  public Player getPlayer()
  {
    return player;
  }

  /**
   * Is called when the refresh button in the panel is pressed.
   */
  public void refresh()
  {
    int startValue = statusPanel.getStartValue();
    int stopValue = statusPanel.getStopValue();
    int playerValue = statusPanel.getPlayerValue();

    // Make sure values are ok.
    if(startValue <= stopValue && playerValue <= stopValue)
    {
      player.setStart(startValue);
      player.setStop(stopValue);
      player.setCurrentSample(playerValue);
    }
  }

  /**
   * Set the status panel.
   * 
   * @param statusPanel The status panel.
   */
  public void setStatusPanel(InternalFormatStatusPanel statusPanel)
  {
    this.statusPanel = statusPanel;
  }

  /**
   * Sets the channel view.
   * 
   * @param channelView The channel view.
   */
  public void setChannelView(ChannelView channelView)
  {
    this.channelView = channelView;
  }

  /**
   * Returns true if there's any selection in the graph. False otherwise.
   * 
   * @return True if selection. False otherwise.
   */
  private boolean isSelection()
  {
    int start = player.getStart();
    int stop = player.getStop();

    return start + stop > 0;
  }

  /**
   * Returns true if <code>x</code> and <code>y</code> is in the graph panel.
   * 
   * @param x A value on the x-axis.
   * @param y A value on the y-axis.
   * @return True if x and y is in the graph area. False otherwise.
   */
  public boolean inView(int x, int y)
  {
    return x > 0 && y > 0 && x < channelView.getGraphWidth() - 1 && y < channelView
        .getGraphHeight() - 1;
  }
}
