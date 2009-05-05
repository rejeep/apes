package apes.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.Config;
import apes.models.Channel;
import apes.models.Player;
import apes.models.SampleIterator;


/**
 * Views a single channel.
 *
 * TODO: width and height are set in this class. But component already
 * have those. Can we use them?
 *
 * TODO: Comment
 *
 * TODO: Some bug when changing in the graph with copy/cut/paste and
 * then drags in it.
 *
 * @author Simon Holm
 */
public class ChannelView extends JPanel implements Runnable
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
   * The player for this channel.
   */
  private Player player;

  /**
   *
   */
  private int fixedMark;

  /**
   *
   */
  private int movingMark;

  /**
   *
   */
  private float samplesPerPixel;

  /**
   * The panel height.
   */
  private int height;

  /**
   * The panel width.
   */
  private int width;
  
  
  /**
   * Constructor to create a view for a channel.
   *
   * @param player A player to keep track of what is played.
   * @param ch A channel contianing the data to be viewed.
   * @param width The width of the view.
   * @param height The height of the view.
   */
  public ChannelView( ChannelController channelController, Channel ch, int width, int height )
  {
    setPreferredSize( new Dimension( width, height ) );

    this.height = height;
    this.width = width;
    
    this.channel = ch;
    this.player = channelController.getPlayer();
    
    // Set events for this panel.
    addMouseListener( channelController );
    addMouseMotionListener( channelController );
    addMouseWheelListener( channelController );    

    deSelectRegion();

    samples = new int[width];

    if(ch != null)
    {
      for(int i = 0; i < channel.getSamplesSize(); ++i)
        nrSamples += channel.getSamples(i).getSize();

      visibleSamples = nrSamples;
      centerSample = nrSamples/2;
      updateView();
    }
    new Thread(this).start();
  }

  /**
   * Draws the view.
   * @param g A graphics object.
   */
  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    Graphics2D g2 = (Graphics2D) g;

    g2.setBackground(Color.decode(Config.getInstance().getOption("color_background")));
    g2.clearRect(0,0, getWidth()-1, getHeight()-1);

    g2.setColor(Color.decode(Config.getInstance().getOption("color_graph")));
    if(samplesPerPixel < 1)
      for(int i = 0; i < samples.length-1; ++i)
        g2.drawLine(i, samples[i]+(height/2), i+1,  samples[i+1]+(height/2));
    else
      for(int i = 0; i < samples.length; ++i)
        g2.drawLine(i, -samples[i]+(height/2), i,  samples[i]+(height/2));

    g2.setColor(Color.decode(Config.getInstance().getOption("color_lines")));
    g2.drawLine(0,height/2,width,height/2);
    g2.drawLine(0,height,width,height);
    g2.drawLine(0,0,width,0);

    if(getMarkBeginning() > 0)
      g2.drawLine(getMarkBeginning(),0,getMarkBeginning(),height);
    if(getMarkEnd() > 0)
      g2.drawLine(getMarkEnd(),0,getMarkEnd(),height);

    if(getMarkBeginning() > 0)
    {
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
      g2.setColor(Color.decode(Config.getInstance().getOption("color_selection")));
      if(getMarkEnd() > 0 && getMarkBeginning() > 0)
        g2.fillRect(getMarkBeginning(), 0, getMarkEnd()-getMarkBeginning(), height);
    }
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g2.setColor(Color.decode(Config.getInstance().getOption("color_play")));
    if(nrSamples > 0)
      g2.drawLine(( (int) ((long) width*player.getCurrentSample()/nrSamples) ),0,
                  ( (int) ((long) width*player.getCurrentSample()/nrSamples) ),height);
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
   * Returns the channel of the view.
   * @return Returns <code>channel</code>.
   */
  public Channel getChannel()
  {
    return channel;
  }

  /**
   * Sets the channel of the view.
   * @param ch The channel to be viewed.
   */
  public void setChannel(Channel ch)
  {
    channel = ch;
    nrSamples = channel.getSamplesSize()*Channel.SAMPLES_SIZE;
    visibleSamples = nrSamples;
    centerSample = nrSamples/2;
  }

  /**
   * Updates the view form the channel and repaints it.
   */
  public void updateView()
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

    samplesPerPixel = visibleSamples/width;

    Point index;
    int prevSample = 0;

    if(samplesPerPixel < 1)
    {
      SampleIterator iterator = channel.getIterator();
      int j = 0;
      for(int i = 0; i < width; ++i)
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
    else if(channel.getSamplesSize() > width)
    {
      for(int i = 0; i < width; ++i)
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
      for(int i = 0; i < width; ++i)
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

    double heightScale = (((float)height/2)/((long)(maxAmp)-(long)(minAmp)));

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
      int samplesPerPixel = visibleSamples/width;
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
    movingMark = getWidth() - 1;
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
      int temp = movingMark;
      movingMark = fixedMark;
      fixedMark = temp;
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
      int temp = movingMark;
      movingMark = fixedMark;
      fixedMark = temp;
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
    return x > 0 && y > 0 && x < width - 1 && y < height - 1;
  }

  /**
   * Returns the graph width.
   *
   * @return The graph width.
   */
  public int getWidth()
  {
    return width;
  }

  /**
   * Returns the graph height.
   *
   * @return The graph height.
   */
  public int getHeight()
  {
    return height;
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
}
