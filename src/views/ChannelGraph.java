package apes.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;

import apes.lib.Config;
import apes.models.Channel;
import apes.models.SampleIterator;
import apes.models.Player;

/**
 * TODO: Comment
 *
 * @author Simon Holm
 */
public class ChannelGraph extends JPanel
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
   * The fixed mark in the graph.
   */
  private int fixedMark;

  /**
   * The moving mark in the graph.
   */
  private int movingMark;

  /**
   *
   */
  private float samplesPerPixel;
  

  /**
   * Creates a new <code>ChannelGraph</code> instance.
   *
   * @param channel The channel.
   * @param player The player.
   * @param width Preferred width.
   * @param height Preferred height.
   */
  public ChannelGraph( Channel channel, Player player, int width, int height )
  {
    Dimension size = new Dimension( width, height );

    setPreferredSize( size );
    setSize( size );

    this.player = player;
    this.channel = channel;

    samples = new int[width];

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
        g2.drawLine(i, samples[i]+(getHeight()/2), i+1,  samples[i+1]+(getHeight()/2));
    else
      for(int i = 0; i < samples.length; ++i)
        g2.drawLine(i, -samples[i]+(getHeight()/2), i,  samples[i]+(getHeight()/2));

    g2.setColor(Color.decode(Config.getInstance().getOption("color_lines")));
    g2.drawLine(0,getHeight()/2,getWidth(),getHeight()/2);
    g2.drawLine(0,getHeight(),getWidth(),getHeight());
    g2.drawLine(0,0,getWidth(),0);

    if(getMarkBeginning() > 0)
      g2.drawLine(getMarkBeginning(),0,getMarkBeginning(),getHeight());
    if(getMarkEnd() > 0)
      g2.drawLine(getMarkEnd(),0,getMarkEnd(),getHeight());

    if(getMarkBeginning() > 0)
    {
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
      g2.setColor(Color.decode(Config.getInstance().getOption("color_selection")));
      if(getMarkEnd() > 0 && getMarkBeginning() > 0)
        g2.fillRect(getMarkBeginning(), 0, getMarkEnd()-getMarkBeginning(), getHeight());
    }
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g2.setColor(Color.decode(Config.getInstance().getOption("color_play")));
    if(nrSamples > 0)
      g2.drawLine(( (int) ((long) getWidth()*player.getCurrentSample()/nrSamples) ),0,
                  ( (int) ((long) getWidth()*player.getCurrentSample()/nrSamples) ),getHeight());
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

    samplesPerPixel = visibleSamples/getWidth();

    Point index;
    int prevSample = 0;

    if(samplesPerPixel < 1)
    {
      SampleIterator iterator = channel.getIterator();
      int j = 0;
      for(int i = 0; i < getWidth(); ++i)
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
    else if(channel.getSamplesSize() > getWidth())
    {
      for(int i = 0; i < getWidth(); ++i)
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
      for(int i = 0; i < getWidth(); ++i)
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

    double heightScale = (((float)getHeight()/2)/((long)(maxAmp)-(long)(minAmp)));

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
      int samplesPerPixel = visibleSamples/getWidth();
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
  
  /**
   * Returns the position of the fixed mark.
   *
   * @return The fixed marks position.
   */
  public int getFixedMark()
  {
    return fixedMark;
  }
  
  /**
   * Returns the position of the moving mark.
   *
   * @return The moving marks position.
   */
  public int getMovingMark()
  {
    return movingMark;
  }

  /**
   * Sets the fixed marks position.
   *
   * @param x The x-axis position.
   */
  public void setFixedMark( int x )
  {
    fixedMark = x;
  }

  /**
   * Sets the moving marks position.
   *
   * @param x The x-axis position.
   */
  public void setMovingMark( int x )
  {
    movingMark = x;
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
  
  /**
   * Returns the player mark on the x-axis in pixels.
   *
   * @return The player mark.
   */
  public int getMarkPlayer()
  {
    // To be implemented...
    return -1;
  }
}