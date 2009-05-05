package apes.controllers;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import apes.views.ChannelView;

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

  public void mousePressed( MouseEvent e )
  {
    ChannelView channelView = (ChannelView)e.getSource();

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
    ChannelView channelView = (ChannelView)e.getSource();

    mouseDown = false;

    channelView.setPlayerRegion();
  }

  public void mouseExited( MouseEvent e )
  {
    ChannelView channelView = (ChannelView)e.getSource();

    int x = e.getX();
    int y = e.getY();

    if( y > 0 && y < channelView.getHeight() )
    {
      if( mouseDown )
      {
        if( x <= 0 )
        {
          channelView.moveMark( 1 );
        }
        else
        {
          channelView.moveMark( channelView.getWidth() - 1 );
        }
      }
    }
  }

  public void mouseDragged( MouseEvent e )
  {
    ChannelView channelView = (ChannelView)e.getSource();

    int x = e.getX();
    int y = e.getY();

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
    ChannelView channelView = (ChannelView)e.getSource();

    // -1 scroll wheel up.
    // 1 scroll wheel down.
    int rotation = e.getWheelRotation();

    Point marked = channelView.getMarkedSamples();
    double time = System.currentTimeMillis();

    channelView.getChannel().scaleSamples( marked.x, marked.y, 1.0f - rotation * 0.1f );
    channelView.updateView();
  }

  public void mouseMoved( MouseEvent e ) {}
  public void mouseEntered( MouseEvent e ) {}
  public void mouseClicked( MouseEvent e ) {}
}
