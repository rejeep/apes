package apes.views;

import apes.models.Channel;
import apes.models.Player;
import apes.models.SampleIterator;
import apes.lib.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Views a single channel.
 *
 * @author Simon Holm
 */
public class ChannelView extends JPanel
  implements MouseListener,
  MouseWheelListener,
  MouseMotionListener,
  Runnable
{
  private Channel channel;
  private int nrSamples;
  private int width, height;
  private int centerSample;
  private int visibleSamples;
  private int[] samples;
  private Player player;
  private int markBeginning;
  private int markEnd;
  private boolean mousePressed;
  private boolean movEdgeLeft;
  private float samplesPerPixel;

  /**
   * Constructor to create a view for a channel.
   * @param player A player to keep track of what is played. 
   * @param ch A channel contianing the data to be viewed.
   * @param width The width of the view.
   * @param height The height of the view.
   */
  public ChannelView( Player player, Channel ch, int width, int height )
  {
    super();
    this.addMouseListener( this );
    this.addMouseWheelListener( this );
    this.addMouseMotionListener( this );
    this.player = player;
    this.channel = ch;
    this.width = width;
    this.height = height;
    this.setPreferredSize( new Dimension( width, height ) );
    markBeginning = -1;
    markEnd       = -1;
    samples = new int[width];
    mousePressed = false;
    movEdgeLeft = true;

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

    if(markBeginning > 0)
      g2.drawLine(markBeginning,0,markBeginning,height);
    if(markEnd > 0)
      g2.drawLine(markEnd,0,markEnd,height);

    if(markBeginning > 0)
    {
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
      g2.setColor(Color.decode(Config.getInstance().getOption("color_selection")));
      if(markEnd > 0 && markBeginning > 0)
        g2.fillRect(markBeginning, 0, markEnd-markBeginning, height);
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
    if(markBeginning > 0 && markEnd > 0)
    {
      int samplesPerPixel = visibleSamples/width;
      try {
        int beginning = (centerSample-visibleSamples/2)+samplesPerPixel*markBeginning, end = (centerSample-visibleSamples/2)+samplesPerPixel*markEnd; 
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
   * Removes marking.
   */
  public void unmark()
  {
    markBeginning = markEnd = -1;
    repaint();
  }

  //@Override
  public void mouseClicked(MouseEvent e) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  //@Override
  public void mousePressed(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1)
    {
      int x = e.getX();
      int y = e.getY();

      if(x < width && x > 0 && y < height)
      {
        markBeginning = x;
        markEnd = x;
        this.repaint();
      }
    }
    mousePressed = true;
  }

  //@Override
  public void mouseReleased(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1)
    {
      int x = e.getX();
      int y = e.getY();

      if( x < width && x > 0 && y < height)
      {
        if( x > markEnd )
        {
          markEnd = x;
          movEdgeLeft = false;
        }
        else if( x < markBeginning )
        {
          markBeginning = x;
          movEdgeLeft = true;
        }
        else
        {
          if(movEdgeLeft)
            markBeginning = x;
          else
            markEnd = x;
        }
      }

      this.repaint();
    }
    else if( e.getButton() == MouseEvent.BUTTON3)
    {
      markBeginning = -1;
      markEnd = -1;
      this.repaint();
    }
    mousePressed = false;
    
    player.setRegion( getMarkedSamples() );
  }

  //@Override
  public void mouseEntered(MouseEvent e) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  //@Override
  public void mouseExited(MouseEvent e) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  //@Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    //-1 scroll wheel up
    //1 scroll wheel down
    Point marked = getMarkedSamples();

    double time = System.currentTimeMillis();
    channel.scaleSamples( marked.x, marked.y, 1.0f-e.getWheelRotation()*0.1f );
    this.updateView();
  }

  //@Override
  public void mouseDragged( MouseEvent mouseEvent )
  {
    int x = mouseEvent.getX();
    int y = mouseEvent.getY();

    if(x < width && x > 0 && x > markEnd && y < height)
    {
      markEnd = x;
      movEdgeLeft = false;
    }
    else if(x < width && x > 0 && x < markBeginning && y < height)
    {
      markBeginning = x;
      movEdgeLeft = true;
    }
    if(x < width && x > 0 && y < height)
    {
      //left movement
      if(movEdgeLeft)
        markBeginning = x;
      else
        markEnd = x;
    }

    this.repaint();
  }

  //@Override
  public void mouseMoved( MouseEvent mouseEvent )
  {

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
}
