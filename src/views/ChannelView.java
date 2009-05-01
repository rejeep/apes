package apes.views;

import apes.models.Channel;
import apes.models.SampleIterator;
import apes.models.Samples;
import apes.models.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

/**
 * TODO: Comment
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
      nrSamples = channel.getSamplesSize()*Channel.SAMPLES_SIZE;
      visibleSamples = nrSamples;
      centerSample = nrSamples/2;
      updateView();
    }
    new Thread(this).start();
  }

  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    Graphics2D g2 = (Graphics2D) g;

    g2.setColor(Color.blue);
    for(int i = 0; i < samples.length-1; ++i)
    {
      g2.drawLine(i, samples[i], i+1, samples[i+1]);
    }

    g2.setColor(Color.black);
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
      g2.setColor(Color.green);
      if(markEnd > 0 && markBeginning > 0)
        g2.fillRect(markBeginning, 0, markEnd-markBeginning, height);
    }
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g2.setColor(Color.black);
    if(nrSamples > 0)
      g2.drawLine(( (int) ((long) width*player.getCurrentSample()/nrSamples) ),0,
                  ( (int) ((long) width*player.getCurrentSample()/nrSamples) ),height);



  }

  public void setZoom(int samples)
  {
    visibleSamples = samples;
  }

  public void setCenter(int sample)
  {
    centerSample = sample;
  }

  public void setChannel(Channel ch)
  {
    channel = ch;
    nrSamples = channel.getSamplesSize()*Channel.SAMPLES_SIZE;
    visibleSamples = nrSamples;
    centerSample = nrSamples/2;
  }

  public void updateView()
  {
    double time = System.currentTimeMillis();

    if(channel == null)
      return;


    int sample = 0;
    int maxAmp = Integer.MIN_VALUE;
    int minAmp = Integer.MAX_VALUE;


    int samplesPerPixel = visibleSamples/width;
    float sampleChunksPerPixel = ((float)samplesPerPixel / Channel.SAMPLES_SIZE);

    if(sampleChunksPerPixel > 0)
    {

      for(int i = 0; i < width; ++i)
      {
        for(int j = 0; j < sampleChunksPerPixel; ++j)
          sample = sample+channel.getSamples( j*i ).getAverageAmplitude( samplesPerPixel % Channel.SAMPLES_SIZE );
        sample = Math.round(sample/sampleChunksPerPixel);
        samples[i] = sample;
        if(sample  > maxAmp)
          maxAmp = sample;
        if(sample < minAmp)
          minAmp = sample;
        sample = 0;
      }

      double heightScale = ((float)height/((long)(maxAmp)-(long)(minAmp)));

      //Lowpass here?

      for(int i = 0; i < samples.length; ++i)
        samples[i] = Math.round((height/2)+(float)(samples[i]*heightScale));

      this.repaint();
      //System.out.println("Time to update view(ms): " + (System.currentTimeMillis() - time));
    }
  }


  public int getZoom()
  {
    return visibleSamples;
  }

  public Point getMarkedSamples()
  {
    if(markBeginning > 0 && markEnd > 0)
    {
      int samplesPerPixel = visibleSamples/width;
      try {
        return new Point((centerSample-visibleSamples/2)+samplesPerPixel*markBeginning,
                         (centerSample-visibleSamples/2)+samplesPerPixel*markEnd);
      } catch (Exception e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
    return null;
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
    //System.out.println(e.getWheelRotation());
    //-1 scroll wheel up
    //1 scroll wheel down
    Point marked = getMarkedSamples();
    //System.out.println("start: " + marked.x + " end: " + marked.y);

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
      //time = System.currentTimeMillis();
      this.repaint();
      try
      {
        Thread.sleep( 17 );   // 60 fps
      } catch ( InterruptedException e )
      {
        e.printStackTrace();
      }
      //System.out.println("Time to repaint(ms): " + (System.currentTimeMillis() - time));
    }
  }
}
