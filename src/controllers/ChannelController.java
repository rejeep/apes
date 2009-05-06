package apes.controllers;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

import apes.models.Player;
import apes.views.ApesButton;
import apes.views.ChannelView;
import apes.views.ChannelView.Graph;
import apes.views.InternalFormatStatusPanel;
import apes.views.InternalFormatView;

import apes.views.InternalFormatView;

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
   * The x-axis position on the graph.
   */
  private int x;

  /**
   * The y-axis position on the graph.
   */
  private int y;

  /**
   * The channel view.
   */
  private ChannelView channelView;

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
    setup( e );

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
    setup( e );

    mouseDown = false;

    if( channelView.isSelection() )
    {
      player.setRegion( channelView.getMarkedSamples() );
    }
  }

  public void mouseExited( MouseEvent e )
  {
    setup( e );
    
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
    setup( e );

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

      channelView.repaint();
    }
  }

  public void mouseWheelMoved( MouseWheelEvent e )
  {
    setup( e );

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
   * Does some basic setup for an event. Like set up variables etc...
   *
   * @param e The <code>MouseEvent</code>.
   */
  private void setup( MouseEvent e )
  {
    ChannelView.Graph graph = (ChannelView.Graph)e.getSource();
    channelView = graph.getChannelView();

    x = e.getX();
    y = e.getY();
  }
  
  /**
   * Is called when the refresh button in the panel is pressed.
   *
   * TODO: In InternalFormatController? But how...?
   */
  public void refresh()
  {
    ApesButton button = (ApesButton)event.getSource();
    JPanel panel = (JPanel)button.getParent();
    InternalFormatStatusPanel statusPanel = (InternalFormatStatusPanel)panel.getParent();
    InternalFormatView internalFormatView = (InternalFormatView)statusPanel.getParent();
    ChannelView channelView = (ChannelView)internalFormatView.getChannelView();
     
    int beginning = statusPanel.getBeginningTextFieldValue();
    int end = statusPanel.getEndTextFieldValue();
    int player = statusPanel.getPlayerTextFieldValue();
    
    channelView.selectRegion( beginning, end );
    channelView.setMarkPlayer( player );
    
    channelView.updateView();
  }
}
