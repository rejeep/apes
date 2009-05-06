package apes.views;

import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.models.Channel;
import apes.models.Player;
import java.awt.Point;


/**
 * View for a single channel.
 *
 * TODO: Some bug when changing in the graph with copy/cut/paste and
 * then drags in it.
 *
 * @author Simon Holm
 */
public class ChannelView extends JPanel implements Runnable
{
  /**
   * The graph.
   */
  private ChannelGraph channelGraph;

  /**
   * The panel.
   */
  private ChannelPanel channelPanel;

  /**
   * TODO: Fix comment
   * Constructor to create a view for a channel.
   *
   * @param channelController a <code>ChannelController</code> value
   * @param channel a <code>Channel</code> value
   * @param width The width of the view.
   * @param height The height of the view.
   */
  public ChannelView( ChannelController channelController, Channel channel, int width, int height )
  {
    Player player = channelController.getPlayer();

    channelGraph = new ChannelGraph( channel, player, width, height );
    channelPanel = new ChannelPanel();

    add( channelGraph );
    add( channelPanel );

    // Set events for this panel.
    addMouseListener( channelController );
    addMouseMotionListener( channelController );
    addMouseWheelListener( channelController );

    deSelectRegion();

    new Thread( this ).start();
  }

  //@Override
  public void run()
  {
    //double time;
    while(true)
    {
      this.repaint();
      try
      {
        Thread.sleep( 17 );   // 60 fps
      } catch ( InterruptedException e )
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Selects a region.
   *
   * TODO: Check so that x1 and x2 is in graph?
   *
   * @param x1 Mark 1.
   * @param x2 Mark 2.
   */
  public void selectRegion( int x1, int x2  )
  {
    setFixedMark( x1 );
    setMovingMark( x2 );
  }

  /**
   * Removes selection.
   */
  public void deSelectRegion()
  {
    setFixedMark( -1 );
    setMovingMark( -1 );
  }

  /**
   * Selects the whole visible graph.
   */
  public void selectAll()
  {
    setFixedMark( 1 );
    setMovingMark( getGraphWidth() - 1 );
  }

  /**
   * Moves mark to <code>x</code>.
   *
   * @param x Where to move on the x-axis.
   */
  public void moveMark( int x )
  {
    if( getFixedMark() == -1 )
    {
      setFixedMark( x );
    }

    setMovingMark( x );
  }

  /**
   * Moves the beginning mark to x.
   *
   * @param x The new mark on the x-axis.
   */
  public void moveBeginning( int x )
  {
    int movingMark = getMovingMark();
    
    if( getFixedMark() < movingMark )
    {
      setFixedMark( movingMark );
    }

    setMovingMark( x );
  }

  /**
   * Moves the end mark to x.
   *
   * @param x The new mark on the x-axis.
   */
  public void moveEnd( int x )
  {
    int movingMark = getMovingMark();
    
    if( getFixedMark() > movingMark )
    {
      setFixedMark( movingMark );
    }

    setMovingMark( x );
  }

  /**
   * Returns true if there's any selection in the graph. False
   * otherwise.
   *
   * @return True if any selection. False otherwise.
   */
  public boolean isSelection()
  {
    return getFixedMark() != -1 || getMovingMark() != -1;
  }

  /**
   * Returns true if <code>x</code> and <code>y</code> is in the graph
   * panel.
   *
   * @param x A value on the x-axis.
   * @param y A value on the y-axis.
   * @return True if x and y is in the graph area. False otherwise.
   */
  public boolean inView( int x, int y )
  {
    return x > 0 && y > 0 && x < getGraphWidth() - 1 && y < getGraphHeight() - 1;
  }

  // DELEGATORS:
  public int getGraphWidth()          { return channelGraph.getWidth(); }
  public int getGraphHeight()         { return channelGraph.getHeight(); }
  public int getMarkBeginning()       { return channelGraph.getMarkBeginning(); }
  public int getMarkEnd()             { return channelGraph.getMarkEnd(); }
  private int getFixedMark()          { return channelGraph.getFixedMark(); }
  private int getMovingMark()         { return channelGraph.getMovingMark(); }
  public Point getMarkedSamples()     { return channelGraph.getMarkedSamples(); }
  public Channel getChannel()         { return channelGraph.getChannel(); }
  public void updateView()            { channelGraph.updateView(); }
  private void setFixedMark( int x )  { channelGraph.setFixedMark( x ); }
  private void setMovingMark( int x ) { channelGraph.setMovingMark( x ); }
  public void setZoom( int samples )  { channelGraph.setZoom( samples ); }
}