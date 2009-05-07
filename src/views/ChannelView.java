package apes.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.Config;
import apes.models.Channel;
import apes.models.Player;
import apes.models.SampleIterator;
import java.awt.GridLayout;

// TODO: Maybe pass some arguments to updateView.


/**
 * View for all channel graphs.
 *
 * TODO: Some bug when changing in the graph with copy/cut/paste and
 * then drags in it.
 *
 * @author Simon Holm
 */
public class ChannelView extends JPanel implements Runnable
{
  /**
   * Contains a list of all graphs in this channel.
   */
  private Set<ChannelView.Graph> graphs;

  /**
   * The player.
   */
  private Player player;

  /**
   * The fixed mark in the graph.
   */
  private int fixedMark;

  /**
   * The moving mark in the graph.
   */
  private int movingMark;

  /**
   * The channel controller.
   */
  private ChannelController channelController;

  /**
   * A graphs width.
   */
  private int graphWidth;

  /**
   * A graphs height.
   */
  private int graphHeight;

  /**
   * The layout to use.
   */
  private GridLayout layout;

  /**
   * Creates a new <code>ChannelView</code> instance.
   *
   * @param channelController The channel controller.
   * @param player The player.
   */
  public ChannelView( ChannelController channelController, Player player )
  {
    layout = new GridLayout( 0, 1 );
    layout.setVgap( 10 );
    setLayout( layout );

    this.channelController = channelController;
    this.player = player;

    graphs = new HashSet<ChannelView.Graph>();

    Config config = Config.getInstance();
    graphWidth = config.getIntOption( "graph_width" );
    graphHeight = config.getIntOption( "graph_height" );

    new Thread( this ).start();
  }

  /**
   * Adds a channel graph to this view.
   *
   * @param channel The channel.
   */
  public void addChannel( Channel channel )
  {
    // Add one more row to the layout.
    layout.setRows( layout.getRows() + 1 );

    // Create a new graph.
    ChannelView.Graph graph = this.new Graph( this, channel );

    // Add to set of graphs.
    graphs.add( graph );

    // Add to this panel.
    add( graph );
  }

  /**
   * Updates the view and all graphs in it.
   */
  public void updateView()
  {
    for( ChannelView.Graph graph : graphs )
    {
      graph.updateView();
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
    // Check so that x1 and x2 is in the graph.
    if( x1 < 0 )
    {
      x1 = 0;
    }
    if( x1 > graphWidth )
    {
      x1 = graphWidth - 1;
    }

    if( x2 < 0 )
    {
      x2 = 0;
    }
    if( x2 > graphWidth )
    {
      x2 = graphWidth - 1;
    }

    fixedMark = x1;
    movingMark = x2;
  }

  /**
   * Removes selection.
   */
  public void deSelectRegion()
  {
    fixedMark = -1;
    movingMark = -1;
  }

  /**
   * Selects the whole visible graph.
   */
  public void selectAll()
  {
    fixedMark = 1;
    movingMark = graphWidth - 1;
  }

  /**
   * Moves mark to <code>x</code>.
   *
   * @param x Where to move on the x-axis.
   */
  public void moveMark( int x )
  {
    if( fixedMark == -1 )
    {
      fixedMark = x;
    }

    movingMark = x;
  }

  /**
   * Moves the beginning mark to x.
   *
   * @param x The new mark on the x-axis.
   */
  public void moveBeginning( int x )
  {
    if( fixedMark < movingMark )
    {
      fixedMark = movingMark;
    }

    movingMark = x;
  }

  /**
   * Moves the end mark to x.
   *
   * @param x The new mark on the x-axis.
   */
  public void moveEnd( int x )
  {
    if( fixedMark > movingMark )
    {
      fixedMark = movingMark;
    }

    movingMark = x;
  }

  /**
   * Returns true if there's any selection in the graph. False
   * otherwise.
   *
   * @return True if any selection. False otherwise.
   */
  public boolean isSelection()
  {
    return fixedMark != -1 || movingMark != -1;
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
    return x > 0 && y > 0 && x < graphWidth - 1 && y < graphHeight - 1;
  }

  /**
   * Returns the graph width.
   *
   * @return The graph width.
   */
  public int getGraphWidth()
  {
    return graphWidth;
  }

  /**
   * Returns the graph height.
   *
   * @return The graph height.
   */
  public int getGraphHeight()
  {
    return graphHeight;
  }

  /**
   * Returns the mark that is the most to the left.
   *
   * @return The position of the mark in pixels.
   */
  public int getMarkBeginning()
  {
    return Math.min( fixedMark, movingMark );
  }

  /**
   * Returns the mark that is the most to the right.
   *
   * @return The position of the mark in pixels.
   */
  public int getMarkEnd()
  {
    return Math.max( fixedMark, movingMark );
  }

  // TODO:
  public int getMarkPlayer()
  {
    return 1;
  }

  // TODO:
  public void setMarkPlayer( int pixels )
  {

  }

  public void run()
  {
    while( true )
    {
      this.repaint();

      try
      {
        Thread.sleep( 17 ); // 60 fps
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace();
      }
    }
  }

  // TODO: Ohh, dear lord... Temp!
  // This method should be in here and not in the graph.
  public Point getMarkedSamples()
  {
    for( ChannelView.Graph graph : graphs )
    {
      if( graph != null )
      {
        return graph.getMarkedSamples();
      }
    }

    return null;
  }

  public int samplesToPixels(long samples)
  {
    return 0;//graphWidth*samples/nrSamples;
  }

  public int millisecondsToPixels(long milliseconds)
  {
    return 0;
  }

  public int secondsToPixels(long seconds)
  {
    return 0;
  }

  public long pixelsToSamples(int pixels)
  {
    return 0;
  }

  public long pixelsToMilliseconds(int pixels)
  {
    return 0;
  }

  public long pixelsToSecnods(int pixels)
  {
    return 0;
  }



  /**
   * TODO: Comment
   *
   * @author Simon Holm
   */
  public class Graph extends JPanel
  {
    /**
     * The channel.
     */
    private Channel channel;

    /**
     *
     */
    private int nrSamples;

    /**
     *
     */
    private int centerSample;

    /**
     *
     */
    private int visibleSamples;

    /**
     *
     */
    private int[] samples;

    /**
     *
     */
    private float samplesPerPixel;

    /**
     * The view that this graph is placed on.
     */
    private ChannelView channelView;


    /**
     * Creates a new <code>ChannelGraph</code> instance.
     *
     * @param channelView The view that this graph is placed on.
     * @param channel The channel.
     */
    public Graph( ChannelView channelView, Channel channel )
    {
      // Set graph size.
      Dimension size = new Dimension( graphWidth, graphHeight );
      setPreferredSize( size );
      setSize( size );

      // Set events for the graph.
      addMouseListener( channelController );
      addMouseMotionListener( channelController );
      addMouseWheelListener( channelController );

      this.channelView = channelView;
      this.channel = channel;

      // TODO: What does this do?
      samples = new int[graphWidth];

      if(channel != null)
      {
        for(int i = 0; i < channel.getSamplesSize(); ++i)
          nrSamples += channel.getSamples(i).getSize();

        visibleSamples = nrSamples;
        centerSample = nrSamples/2;
        updateView();
      }
    }

    /**
     * Returns the view that this graph is placed on.
     *
     * @return The view.
     */
    public ChannelView getChannelView()
    {
      return channelView;
    }

    /**
     * Draws the view.
     * @param g A graphics object.
     */
    public void paintComponent( Graphics g )
    {
      super.paintComponent( g );
      Graphics2D g2 = (Graphics2D) g;

      setBackground(g2);
      drawGraph(g2);
      drawLines(g2);
      drawMarkers(g2);
      drawSelection(g2);
      drawPlayMarker(g2);
      drawRuler(g2);
    }

    private void drawRuler(Graphics2D g2)
    {

    }

    private void drawPlayMarker(Graphics2D g2)
    {
      g2.setColor(Color.decode(Config.getInstance().getOption("color_play")));
      if(nrSamples > 0)
        g2.drawLine(( (int) ((long) graphWidth*player.getCurrentSample()/nrSamples) ),0,
                    ( (int) ((long) graphWidth*player.getCurrentSample()/nrSamples) ),graphHeight);
    }

    private void drawSelection(Graphics2D g2)
    {
      if(getMarkBeginning() > 0)
      {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(Color.decode(Config.getInstance().getOption("color_selection")));
        if(getMarkEnd() > 0 && getMarkBeginning() > 0)
          g2.fillRect(getMarkBeginning(), 0, getMarkEnd()-getMarkBeginning(), graphHeight);
      }
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawMarkers(Graphics2D g2)
    {
      if(getMarkBeginning() > 0)
        g2.drawLine(getMarkBeginning(),0,getMarkBeginning(),graphHeight);
      if(getMarkEnd() > 0)
        g2.drawLine(getMarkEnd(),0,getMarkEnd(),graphHeight);
    }

    private void drawLines(Graphics2D g2)
    {
      g2.setColor(Color.decode(Config.getInstance().getOption("color_lines")));
      g2.drawLine(0,graphHeight/2,graphWidth,graphHeight/2);
      g2.drawLine(0,graphHeight,graphWidth,graphHeight);
      g2.drawLine(0,0,graphWidth,0);
    }

    private void drawGraph(Graphics2D g2)
    {
      g2.setColor(Color.decode(Config.getInstance().getOption("color_graph")));
      if(samplesPerPixel < 1)
        for(int i = 0; i < samples.length-1; ++i)
          g2.drawLine(i, samples[i]+(graphHeight/2), i+1,  samples[i+1]+(graphHeight/2));
      else
        for(int i = 0; i < samples.length; ++i)
          g2.drawLine(i, -samples[i]+(graphHeight/2), i,  samples[i]+(graphHeight/2));
    }

    private void setBackground(Graphics2D g2)
    {
      g2.setBackground(Color.decode(Config.getInstance().getOption("color_background")));
      g2.clearRect(0,0, graphWidth-1, graphHeight-1);
    }

    /**
     * Sets the level of zoom.
     * @param samples The number of samples to be viewed in the view.
     */
    public void setZoom(int samples)
    {
      visibleSamples = samples;
    }

    /**
     * Sets the center of the view.
     * @param sample The sample that should be in the center of the view.
     */
    public void setCenter(int sample)
    {
      centerSample = sample;
    }

    /**
     * Updates the view form the channel and repaints it.
     */
    public void updateInternalFormat()
    {
      if(channel == null)
        return;

      nrSamples = 0;
      for(int i = 0; i < channel.getSamplesSize(); ++i)
        nrSamples += channel.getSamples(i).getSize();

      //visibleSamples = nrSamples;
      //centerSample = nrSamples/2;

      int maxAmp = Integer.MIN_VALUE;
      int minAmp = Integer.MAX_VALUE;

      samplesPerPixel = visibleSamples/graphWidth;

      Point index;
      int prevSample = 0;

      if(samplesPerPixel < 1)
      {
        SampleIterator iterator = channel.getIterator();
        int j = 0;
        for(int i = 0; i < graphWidth; ++i)
        {
          if(j == 0 && iterator.hasNext())
            samples[i] = iterator.next();
          j += samplesPerPixel;
          if(j > 1)
            j = 0;

          if(samples[i]  > maxAmp)
            maxAmp = samples[i];
          if(samples[i] < minAmp)
            minAmp = samples[i];
        }

      }
      else if(channel.getSamplesSize() > graphWidth)
      {
        for(int i = 0; i < graphWidth; ++i)
        {
          index = channel.findAbsoluteIndex(Math.round(i*samplesPerPixel));

          for(int j = prevSample; j < index.x; ++j)
          {
            samples[i] += channel.getSamples( j ).getAverageAmplitude( channel.getSamples( j ).getSize() );
          }

          if(samples[i]  > maxAmp)
            maxAmp = samples[i];
          if(samples[i] < minAmp)
            minAmp = samples[i];

          prevSample = index.x;
        }
      }
      else
      {
        SampleIterator iterator = channel.getIterator();
        for(int i = 0; i < graphWidth; ++i)
        {
          for(int j = 0; j < samplesPerPixel && iterator.hasNext(); ++j)
            samples[i] += iterator.next();

          samples[i] = Math.round(samples[i]/samplesPerPixel);

          if(samples[i]  > maxAmp)
            maxAmp = samples[i];
          if(samples[i] < minAmp)
            minAmp = samples[i];
        }

      }

      double heightScale = (((float)graphHeight/2)/((long)(maxAmp)-(long)(minAmp)));

      //Lowpass here? / Highpass here?

      for(int i = 0; i < samples.length; ++i)
        samples[i] = Math.round((float)(samples[i]*heightScale));

      this.repaint();
    }

    /**
     * Returns the level of zoom.
     * @return Number of samples visible.
     */
    public int getZoom()
    {
      return visibleSamples;
    }

    /**
     * Returns the interval of marked samples.
     * point.x = the start of the marked area
     * point.y = the end of the marked area
     * @return The intervall.
     */
    public Point getMarkedSamples()
    {
      if(getMarkBeginning() > 0 && getMarkEnd() > 0)
      {
        int samplesPerPixel = visibleSamples/graphWidth;
        try {
          int beginning = (centerSample-visibleSamples/2)+samplesPerPixel*getMarkBeginning(), end = (centerSample-visibleSamples/2)+samplesPerPixel*getMarkEnd();
          if( beginning <= end )
            return new Point( beginning, end );
          else
            return new Point( end, beginning );
        } catch (Exception e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
      return null;
    }
  }
  
  // TODO:
  public void updatePlayer()
  {
    
  }
}