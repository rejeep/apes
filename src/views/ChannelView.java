package apes.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.Config;
import apes.models.Channel;
import apes.models.Player;
import apes.models.SampleIterator;


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
   * The number of samples for each of the channels.
   */
  private int nrSamples;

  /**
   * The center sample of the channels
   */
  private int centerSample;

  /**
   * The number of visible samples in each channel
   */
  private int visibleSamples;

  /**
   * The position of the mouse in the x-axis
   */
  private int mousePosX;

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
   * Updates the view.
   */
  public void updateInternalFormat()
  {
    for( ChannelView.Graph graph : graphs )
    {
      graph.updateGraph();
    }
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

  /**
   * Transform a position in a channel to pixels in the graph based on samples.
   * @param samples The position in the channel in samples.
   * @return -1 if the sample is outside the graph otherwise where in the graph.
   */
  public int samplesToPixels(int samples)
  {
    int firstVisibleSample = centerSample - visibleSamples/2;
    int lastVisibelSample  = centerSample + visibleSamples/2;

    if(samples < firstVisibleSample || samples > lastVisibelSample)
      return -1;

    int properSamples = samples - firstVisibleSample;

    float ratio = (float)properSamples / visibleSamples;

    return Math.round(ratio * graphWidth);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on milliseconds.
   * @param milliseconds The position in the channel in milliseconds.
   * @return -1 if the time is outside the graph otherwise where in the graph.
   */
  public int millisecondsToPixels(int milliseconds)
  {
    int sampleRate = player.getInternalFormat().getSampleRate();
    int samples = Math.round((milliseconds / 1000.0f) * sampleRate);
    return samplesToPixels(samples);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on seconds.
   * @param seconds The position in the channel in seconds.
   * @return -1 if the time is outside the graph otherwise where in the graph.
   */
  public int secondsToPixels(int seconds)
  {
    int sampleRate = player.getInternalFormat().getSampleRate();
    int samples = seconds * sampleRate;
    return samplesToPixels(samples);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on minutes.
   * @param minutes The position in the channel in seconds.
   * @return -1 if the time is outside the graph otherwise where in the graph.
   */
  public int minutesToPixels(int minutes)
  {
    int sampleRate = player.getInternalFormat().getSampleRate();
    int samples = (minutes*60) * sampleRate;
    return samplesToPixels(samples);
  }

  /**
   * Transform a number of pixels to samples in the channel.
   * @param pixels How many pixels in the graph in the x-axis
   * @return The absolute samples in the channel, -1 if outside the graph.
   */
  public int pixelsToSamples(int pixels)
  {
    if(pixels < 0 || pixels > graphWidth)
      return -1;
    int firstVisibleSample = centerSample - visibleSamples/2;
    int samplesPerPixel = visibleSamples / graphWidth;
    int samples = pixels*samplesPerPixel + firstVisibleSample;
    return samples;
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * @param pixels How many pixels in the graph in the x-axis
   * @return The millisecnods in the channel, -1 if outside the graph.
   */
  public int pixelsToMilliseconds(int pixels)
  {
    int samples = pixelsToSamples(pixels);
    int sampleRate = player.getInternalFormat().getSampleRate();
    int milliseconds = (samples / sampleRate) * 1000;
    return milliseconds;
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * @param pixels How many pixels in the graph in the x-axis
   * @return The seconds in the channel, -1 if outside the graph.
   */
  public int pixelsToSeconds(int pixels)
  {
    int samples = pixelsToSamples(pixels);
    int sampleRate = player.getInternalFormat().getSampleRate();
    int seconds = (samples / sampleRate);
    return seconds;
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * @param pixels How many pixels in the graph in the x-axis
   * @return The minutes in the channel, -1 if outside the graph.
   */
  public int pixelsToMinutes(int pixels)
  {
    int samples = pixelsToSamples(pixels);
    int sampleRate = player.getInternalFormat().getSampleRate();
    int minutes = (samples / sampleRate) / 60;
    return minutes;
  }

  public void setMousePosX( int mousePosX )
  {
    this.mousePosX = mousePosX;
  }

  /**
   * Is called when the player is updated. Then each graph should
   * be updated.
   */
  public void updatePlayer()
  {
    for( ChannelView.Graph graph : graphs )
    {
      graph.repaint();
    }
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
        updateGraph();
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
      drawStatus(g2);
    }

    private void drawStatus(Graphics2D g2)
    {
      //inchannelController;
      int seconds = pixelsToSeconds( mousePosX );
      g2.drawString( "( " + seconds + " s)", 0, graphHeight );
    }

    private void drawRuler(Graphics2D g2)
    {
      g2.setColor(Color.decode(Config.getInstance().getOption("color_ruler")));
      int rulerWidth = Config.getInstance().getIntOption("ruler_width");
      g2.fillRect(0,0, graphWidth-1, rulerWidth );
      for(int i = 0; i < graphWidth; i += graphWidth/10)
        g2.drawLine( i, 0, i, rulerWidth+5);

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
      if(getMarkStart() > 0)
      {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(Color.decode(Config.getInstance().getOption("color_selection")));
        if(getMarkStop() > 0 && getMarkStart() > 0)
          g2.fillRect(getMarkStart(), 0, getMarkStop()-getMarkStart(), graphHeight);
      }
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawMarkers(Graphics2D g2)
    {
      if(getMarkStart() > 0)
        g2.drawLine(getMarkStart(),0,getMarkStart(),graphHeight);
      if(getMarkStop() > 0)
        g2.drawLine(getMarkStop(),0,getMarkStop(),graphHeight);
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
    public void updateGraph()
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
     * Returns the start mark position in pixels.
     *
     * @return The start mark position.
     */
    private int getMarkStart()
    {
      return samplesToPixels( player.getStart() );
    }

    /**
     * Returns the stop mark position in pixels.
     *
     * @return The stop mark position.
     */
    private int getMarkStop()
    {
      return samplesToPixels( player.getStop() );
    }
  }
}