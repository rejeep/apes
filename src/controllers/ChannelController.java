package apes.controllers;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import apes.models.Player;
import apes.views.ChannelView;
import apes.views.InternalFormatStatusPanel;


/**
 * Channel controller.
 *
 * TODO: Bug when dragging a selection with mouse 3.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ChannelController extends ApplicationController implements MouseListener, MouseMotionListener, MouseWheelListener
{
  /**
   * True if mouse if pressed. False otherwise.
   *
   * TODO: You can get this information from MouseEvent, but it didn't
   * work well in mouseDragged.
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
  public ChannelController( Player player )
  {
    this.player = player;
  }

  public void mousePressed( MouseEvent e )
  {
    int x = e.getX();

    // Left mouse button.
    if( e.getButton() == MouseEvent.BUTTON1 )
    {
      if( e.getClickCount() == 1 )
      {
        channelView.selectRegion( x, x );
      }
      else
      {
        channelView.selectAll();
      }
    }
    // Middle mouse button (often wheel).
    else if( e.getButton() == MouseEvent.BUTTON2 )
    {
      channelView.deSelectRegion();
    }

    mouseDown = true;

    channelView.repaint();
  }

  public void mouseReleased( MouseEvent e )
  {
    mouseDown = false;

    if( channelView.isSelection() )
    {
      player.setRegion( channelView.getMarkedSamples() );
    }

    updateStatusPanel();
  }

  public void mouseExited( MouseEvent e )
  {
    int y = e.getY();
    int x = e.getX();

    // If the mouse don't exist at the top or bottom.
    if( y > 0 && y < channelView.getGraphHeight() )
    {
      if( mouseDown )
      {
        // If the mouse existed to the left.
        if( x <= 0 )
        {
          channelView.moveMark( 1 );
        }
        else
        {
          channelView.moveMark( channelView.getGraphWidth() - 1 );
        }
      }
    }
  }

  public void mouseDragged( MouseEvent e )
  {
    int y = e.getY();
    int x = e.getX();

    // Is the mouse inside the panel.
    if( channelView.inView( x, y ) )
    {
      if( e.getModifiers() == MouseEvent.BUTTON1_MASK )
      {
        channelView.moveMark( x );
      }
      else if( e.getModifiers() == MouseEvent.BUTTON3_MASK )
      {
        // Is there a selection.
        if( channelView.isSelection() )
        {
          int beginning = channelView.getMarkBeginning();
          int end = channelView.getMarkEnd();

          int fromBeginning = Math.abs( x - beginning );
          int fromEnd = Math.abs( x - end );

          // Are we closer to the beginning mark.
          if( fromBeginning <= fromEnd )
          {
            channelView.moveBeginning( x );
          }
          else
          {
            channelView.moveEnd( x );
          }
        }
      }

      updateStatusPanel();

      channelView.updateView();
    }
  }

  public void mouseWheelMoved( MouseWheelEvent e )
  {
    // -1 scroll wheel up.
    // 1 scroll wheel down.
    int rotation = e.getWheelRotation();

    Point marked = channelView.getMarkedSamples();

    // TODO: We should scale all channels.
    // channelView.getChannel().scaleSamples( marked.x, marked.y, 1.0f - rotation * 0.1f );
    channelView.updateView();
  }

  public void mouseMoved( MouseEvent e ) {}
  public void mouseEntered( MouseEvent e ) {}
  public void mouseClicked( MouseEvent e ) {}


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
    int beginning = statusPanel.getBeginningTextFieldValue();
    int end = statusPanel.getEndTextFieldValue();
    int player = statusPanel.getPlayerTextFieldValue();

    channelView.selectRegion( beginning, end );
    channelView.setMarkPlayer( player );

    channelView.updateView();
  }

  /**
   * Updates the status panel with the values from the graph
   * selection.
   *
   * @param channelView The channel view.
   */
  private void updateStatusPanel()
  {
    int beginning = channelView.getMarkBeginning();
    int end = channelView.getMarkEnd();

    statusPanel.setBeginningTextFieldValue( beginning );
    statusPanel.setEndTextFieldValue( end );
    // TODO: 100 is temp
    statusPanel.setPlayerTextFieldValue( 100 );
  }
  
  /**
   * Set the status panel.
   *
   * @param statusPanel The status panel.
   */
  public void setStatusPanel( InternalFormatStatusPanel statusPanel )
  {
    this.statusPanel = statusPanel;
  }

  /**
   * Sets the channel view.
   *
   * @param channelView The channel view.
   */
  public void setChannelView( ChannelView channelView )
  {
    this.channelView = channelView;
  }
}
