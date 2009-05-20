package apes.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.Config;
import apes.models.Player;
import apes.models.SampleIterator;
import apes.models.InternalFormat;
import apes.lib.SampleHelper;

/**
 * View for all channel graphs.
 * 
 * @author Simon Holm
 * @author Johan Andersson (johandy@student.chalmers.se)
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
   * The internal format
   */
  private InternalFormat internalFormat;

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
   * The sample rate.
   */
  private int sampleRate;

  /**
   * List of possible time units.
   */
  private static enum Unit {
    SAMPLES, MILLISECONDS, SECONDS, MINUTES
  };

  /**
   * Some graph colors.
   */
  private Color colorRuler, colorPlay, colorSelection, colorLine, colorGraph,
      colorBackground, colorDots, colorStatus;

  /**
   * Creates a new <code>ChannelView</code> instance.
   * 
   * @param channelController The channel controller.
   * @param player The player.
   */
  public ChannelView(InternalFormat internalFormat, ChannelController channelController, Player player)
  {
    // Set layout.
    layout = new GridLayout(0, 1);
    layout.setVgap(10);
    setLayout(layout);

    // Set some instance variables.
    this.internalFormat = internalFormat;
    this.sampleRate = internalFormat.getSampleRate();
    this.channelController = channelController;
    this.player = player;

    graphs = new HashSet<ChannelView.Graph>();

    // Set start zoom.
    int numSamples = internalFormat.getSampleAmount();
    setZoom(numSamples);
    setCenter(numSamples / 2);

    // Set configuration options.
    Config config = Config.getInstance();
    graphWidth = config.getIntOption("graph_width");
    graphHeight = config.getIntOption("graph_height");
    colorRuler = Color.decode(config.getOption("color_ruler"));
    colorPlay = Color.decode(config.getOption("color_play"));
    colorSelection = Color.decode(config.getOption("color_selection"));
    colorLine = Color.decode(config.getOption("color_lines"));
    colorGraph = Color.decode(config.getOption("color_graph"));
    colorBackground = Color.decode(config.getOption("color_background"));
    colorDots = Color.decode(config.getOption("color_dots"));
    colorStatus = Color.decode(config.getOption("color_status"));

    new Thread(this).start();
  }

  /**
   * Adds a channel graph to this view.
   * 
   * @param channel The channel.
   */
  public void addChannel(int channel)
  {
    // Add one more row to the layout.
    layout.setRows(layout.getRows() + 1);

    // Create a new graph.
    ChannelView.Graph graph = this.new Graph(this, channel);

    // Add to set of graphs.
    graphs.add(graph);

    // Add to this panel.
    add(graph);
  }

  /**
   * Updates the view.
   */
  public void updateInternalFormat()
  {
    for(ChannelView.Graph graph : graphs)
    {
      double d = System.currentTimeMillis();
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

  /**
   * Run in a thread.
   */
  public void run()
  {
    while(true)
    {
      this.repaint();

      try
      {
        Thread.sleep(17); // 60 fps
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Transform a position in a channel to pixels in the graph based on
   * samples.
   * 
   * @param samples The position in the channel in samples.
   * @return -1 if the sample is outside the graph otherwise where in
   *         the graph.
   */
  public int samplesToPixels(int samples)
  {
    int firstVisibleSample = getFirstVisibleSample();
    int lastVisibelSample = getLastVisibleSample();

    if(samples < firstVisibleSample || samples > lastVisibelSample)
      return -1;

    int properSamples = samples - firstVisibleSample;

    float ratio = (float)properSamples / visibleSamples;

    return Math.round(ratio * graphWidth);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on
   * milliseconds.
   * 
   * @param milliseconds The position in the channel in milliseconds.
   * @return -1 if the time is outside the graph otherwise where in
   *         the graph.
   */
  public int millisecondsToPixels(int milliseconds)
  {
    int samples = SampleHelper.millisecondsToSamples(sampleRate, milliseconds);

    return samplesToPixels(samples);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on
   * seconds.
   * 
   * @param seconds The position in the channel in seconds.
   * @return -1 if the time is outside the graph otherwise where in
   *         the graph.
   */
  public int secondsToPixels(int seconds)
  {
    int samples = SampleHelper.secondsToSamples(sampleRate, seconds);

    return samplesToPixels(samples);
  }

  /**
   * Transform a position in a channel to pixels in the graph based on
   * minutes.
   * 
   * @param minutes The position in the channel in seconds.
   * @return -1 if the time is outside the graph otherwise where in
   *         the graph.
   */
  public int minutesToPixels(int minutes)
  {
    int samples = SampleHelper.millisecondsToSamples(sampleRate, minutes);

    return samplesToPixels(samples);
  }

  /**
   * Transform a number of pixels to samples in the channel.
   * 
   * @param pixels How many pixels in the graph in the x-axis
   * @return The absolute samples in the channel, -1 if outside the
   *         graph.
   */
  public int pixelsToSamples(int pixels)
  {
    if(pixels < 0 || pixels > graphWidth)
    {
      return -1;
    }

    int firstVisibleSample = getFirstVisibleSample();
    float samplesPerPixel = (float)visibleSamples / graphWidth;
    int samples = Math.round( ( pixels * samplesPerPixel ) + firstVisibleSample);

    return samples;
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * 
   * @param pixels How many pixels in the graph in the x-axis
   * @return The millisecnods in the channel, -1 if outside the graph.
   */
  public int pixelsToMilliseconds(int pixels)
  {
    int samples = pixelsToSamples(pixels);

    return SampleHelper.samplesToMilliseconds(sampleRate, samples);
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * 
   * @param pixels How many pixels in the graph in the x-axis
   * @return The seconds in the channel, -1 if outside the graph.
   */
  public int pixelsToSeconds(int pixels)
  {
    int samples = pixelsToSamples(pixels);

    return SampleHelper.samplesToSeconds(sampleRate, samples);
  }

  /**
   * Transform a number of pixels to milliseconds in the channel.
   * Observ that the numbers are rounded down.
   * 
   * @param pixels How many pixels in the graph in the x-axis
   * @return The minutes in the channel, -1 if outside the graph.
   */
  public int pixelsToMinutes(int pixels)
  {
    int samples = pixelsToSamples(pixels);

    return SampleHelper.samplesToMinutes(sampleRate, samples);
  }

  /**
   * Set the x position of the mouse.
   * 
   * @param mousePosX The x position.
   */
  public void setMousePosX(int mousePosX)
  {
    this.mousePosX = mousePosX;
  }

  /**
   * Is called when the player is updated. Then each graph should be
   * updated.
   */
  public void updatePlayer()
  {
    for(ChannelView.Graph graph : graphs)
    {
      graph.repaint();
    }
  }

  /**
   * Returns the level of zoom.
   * 
   * @return Number of samples visible.
   */
  public int getZoom()
  {
    return visibleSamples;
  }

  /**
   * Sets the level of zoom.
   * 
   * @param samples The number of samples to be viewed in the view.
   */
  public void setZoom(int samples)
  {
    visibleSamples = samples;
  }

  /**
   * Sets the center of the view.
   * 
   * @param sample The sample that should be in the center of the
   *          view.
   */
  public void setCenter(int sample)
  {
    centerSample = sample;
  }

  /**
   * Returns the center position.
   * 
   * @return The center position.
   */
  public int getCenter()
  {
    return centerSample;
  }

  /**
   * Returns the first visible sample.
   * 
   * @return The first visible sample.
   */
  public int getFirstVisibleSample()
  {
    return centerSample - ( visibleSamples / 2 );
  }

  /**
   * Returns the last visible sample.
   * 
   * @return The last visible sample.
   */
  public int getLastVisibleSample()
  {
    return centerSample + ( visibleSamples / 2 );
  }

  /**
   * This is a graph over a channel.
   * 
   * @author Simon Holm
   * @author Johan Andersson (johandy@student.chalmers.se)
   */
  public class Graph extends JPanel
  {
    /**
     * The channel.
     */
    private int channel;

    /**
     * Contains all sample amplitudes that should be drawn in the
     * graph.
     */
    private int[] samples;

    /**
     * How many samples are there per pixel.
     */
    private float samplesPerPixel;

    /**
     * The view that this graph is placed on.
     */
    private ChannelView channelView;

    /**
     * A graphics object.
     */
    private Graphics2D g2;

    /**
     * The current time unit.
     */
    private Unit timeUnit;

    /**
     * Creates a new <code>ChannelGraph</code> instance.
     * 
     * @param channelView The view that this graph is placed on.
     * @param channel The channel.
     */
    public Graph(ChannelView channelView, int channel)
    {
      // Set graph size.
      Dimension size = new Dimension(graphWidth, graphHeight);
      setPreferredSize(size);
      setSize(size);

      // Set events for the graph.
      addMouseListener(channelController);
      addMouseMotionListener(channelController);
      addMouseWheelListener(channelController);

      this.channelView = channelView;
      this.channel = channel;

      updateGraph();
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
     * 
     * @param g A graphics object.
     */
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g2 = (Graphics2D)g;

      drawBackground();
      drawGraph();
      drawLines();
      // Do before markers.
      drawSelection();
      drawMarkers();
      drawPlayMarker();
      drawRuler();
      drawStatus();
    }

    /**
     * Draws a status indication on the graph telling where the mouse
     * is placed. The time is in seconds or milliseconds if zoomed in.
     */
    private void drawStatus()
    {
      int time = getTime(mousePosX);;
      String unit = getUnit();

      g2.setColor(colorStatus);
      g2.drawString("( " + time + " " + unit + " )", 3, graphHeight - 3);
    }

    /**
     * Draws a ruler with time marks. The time is in seconds or
     * milliseconds if zoomed in.
     */
    private void drawRuler()
    {
      g2.setColor(colorRuler);

      int rulerWidth = 3;
      g2.fillRect(0, 0, graphWidth - 1, rulerWidth);

      for(int i = 0; i < graphWidth; i += graphWidth / 10)
      {
        g2.drawLine(i, 0, i, rulerWidth + 3);

        int time = getTime(i);

        g2.drawString("" + time, i, 20);
      }
    }

    /**
     * Draws the player marker.
     */
    private void drawPlayMarker()
    {
      int player = getMarkPlayer();

      if(player == 0)
      {
        player++;
      }

      g2.setColor(colorPlay);
      g2.drawLine(player, 0, player, graphHeight);
    }

    /**
     * Draws the selection.
     */
    private void drawSelection()
    {
      int start = getMarkStart();
      int stop = getMarkStop();

      if(start >= 0 && stop > 0)
      {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(colorSelection);
        g2.fillRect(start, 0, stop - start, graphHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
      }
    }

    /**
     * Draws the selection markers (start and end of selection).
     */
    private void drawMarkers()
    {
      int start = getMarkStart();
      int stop = getMarkStop();

      if(start != stop)
      {
        if(start >= 0)
        {
          g2.drawLine(start + 1, 0, start + 1, graphHeight);
        }

        if(stop > 0)
        {
          g2.drawLine(stop - 1, 0, stop - 1, graphHeight);
        }
      }
    }

    /**
     * Draws some lines.
     */
    private void drawLines()
    {
      g2.setColor(colorLine);
      g2.drawLine(0, graphHeight / 2, graphWidth, graphHeight / 2);
      g2.drawLine(0, graphHeight, graphWidth, graphHeight);
      g2.drawLine(0, 0, graphWidth, 0);
    }

    /**
     * Draws the actual graph.
     */
    private void drawGraph()
    {
      // If the player cursor is at the end of the screen. Then go to
      // the next page.
      int start = getFirstVisibleSample();
      int stop = getLastVisibleSample();
      int currentSample = player.getCurrentSample();

      if(visibleSamples > 100000 && currentSample > stop)
      {
        setCenter(visibleSamples + start + visibleSamples / 2);

        updateGraph();
      }

      // Repaint it.
      g2.setColor(colorGraph);

      int half = graphHeight / 2;

      if(samplesPerPixel < 1)
      {
        float jump = (float)graphWidth / samples.length;
        int pixel = 0;

        for(int i = 1; i < samples.length; i++)
        {
          int x1 = pixel;
          int x2 = Math.round(pixel + jump);
          int y1 = half + samples[i - 1];
          int y2 = half + samples[i];

          g2.drawLine(x1, y1, x2, y2);

          if(graphWidth / samples.length > 5)
          {
            g2.setColor(colorDots);
            g2.fillOval(x2 - 3, y2 - 3, 6, 6);
            g2.setColor(colorGraph);
          }

          pixel += Math.round(jump);
        }
      }
      else
      {
        for(int i = 0; i < graphWidth; i++)
        {
          int y1 = half + samples[i];
          int y2 = half - samples[i];

          g2.drawLine(i, y1, i, y2);
        }
      }
    }

    /**
     * Draws a background.
     */
    private void drawBackground()
    {
      g2.setBackground(colorBackground);
      g2.clearRect(0, 0, graphWidth - 1, graphHeight - 1);
    }

    /**
     * Returns an appropriate time depending on the zoom. The
     * <code>timeUnit</code> variable is also set to the correct
     * unit.
     * 
     * @param pixels The pixel position.
     * @return The time.
     */
    private int getTime(int pixels)
    {
      int time = -1;
      int diff = SampleHelper.samplesToMilliseconds(sampleRate, visibleSamples);

      // If diff is larger than five minutes.
      if(diff > 5 * 1000 * 60)
      {
        time = pixelsToMinutes(pixels);
        timeUnit = Unit.MINUTES;
      }
      // If diff is larger than one minute.
      else if(diff > 10 * 1000)
      {
        time = pixelsToSeconds(pixels);
        timeUnit = Unit.SECONDS;
      }
      // If diff is larger than one tenth of a second.
      else if(diff > 100)
      {
        time = pixelsToMilliseconds(pixels);
        timeUnit = Unit.MILLISECONDS;
      }
      else
      {
        time = pixelsToSamples(pixels);
        timeUnit = Unit.SAMPLES;
      }

      return time;
    }

    /**
     * Returns the current unit as a string.
     * 
     * @return The unit.
     */
    private String getUnit()
    {
      String[] units = { "smp", "ms", "s", "min" };

      return units[timeUnit.ordinal()];
    }

    /**
     * Recalculates the view and repaints it.
     */
    public void updateGraph()
    {
      int amount = internalFormat.getSampleAmount();
      if(visibleSamples > amount)
      {
        visibleSamples = amount;
        centerSample = visibleSamples / 2;
      }

      samplesPerPixel = visibleSamples / graphWidth;

      // If there are less samples per pixel than 1. Or if there are
      // equally many samples as there are pixels.
      if(samplesPerPixel <= 1)
      {
        int firstVisibleSample = getFirstVisibleSample();
        ByteBuffer bytes = ByteBuffer.wrap(internalFormat.getSamples(firstVisibleSample, firstVisibleSample + visibleSamples));
        samples = new int[visibleSamples];

        for(int i = 0; i < samples.length; i++)
        {
          int index = (int) ( internalFormat.samplesToBytes(i) + channel * internalFormat.bytesPerSample );

          switch(internalFormat.bytesPerSample)
          {
          case 2:
            samples[i] = bytes.getShort(index);
            break;
          case 4:
            samples[i] = bytes.getInt(index);
            break;

          default:
            System.err.println("BAD BYTES PER SAMPLE IN CHANNEL VIEW WHILE UPDATING GRAPH");
            System.exit(1);
          }
        }
      }
      // If there are more samples per pixel than 1.
      else
      {
        samples = new int[graphWidth];

        int jump = Math.round((float)visibleSamples / graphWidth);
        int firstVisibleSample = getFirstVisibleSample();

        for(int i = 0; i < samples.length; i++)
        {
          int start = firstVisibleSample + ( i * jump );
          int sample = internalFormat.getAverageAmplitude(channel, start, jump);

          samples[i] = sample;
        }
      }

      // Set min and max amplitude.
      int maxAmp = Short.MIN_VALUE;
      int minAmp = Short.MAX_VALUE;
      for(int i = 0; i < samples.length; i++)
      {
        int amplitude = samples[i];

        if(amplitude > maxAmp)
        {
          maxAmp = amplitude;
        }
        if(amplitude < minAmp)
        {
          minAmp = amplitude;
        }
      }

      // Fix sample amplitudes so that they are in correct scale.
      // double heightScale = (double)( (float)( graphHeight / 2 ) / (
      // maxAmp - minAmp ) );

      float scale = ( (float)graphHeight / 2 ) / (float) ( Math.abs(minAmp) + Math.abs(maxAmp) );
      for(int i = 0; i < samples.length; i++)
      {
        samples[i] = Math.round( ( samples[i] * scale ));
      }

      repaint();
    }

    /**
     * Returns the start mark position in pixels.
     * 
     * @return The start mark position.
     */
    private int getMarkStart()
    {
      return samplesToPixels(player.getStart());
    }

    /**
     * Returns the stop mark position in pixels.
     * 
     * @return The stop mark position.
     */
    private int getMarkStop()
    {
      return samplesToPixels(player.getStop());
    }

    /**
     * Returns the player mark position in pixels.
     * 
     * @return The player mark position.
     */
    private int getMarkPlayer()
    {
      return samplesToPixels(player.getCurrentSample());
    }
  }
}
