package apes.views;

import apes.models.Channel;
import apes.models.SampleIterator;
import apes.models.Samples;
import apes.models.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

public class SamplesView extends JPanel implements MouseListener, MouseWheelListener
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

  public SamplesView( Player player, Channel ch, int width, int height )
  {
    super();
    this.addMouseListener(this);
    this.addMouseWheelListener(this);
    this.player = player;
    this.channel = ch;
    this.width = width;
    this.height = height;
    this.setSize( width, height );
    markBeginning = -1;
    markEnd       = -1;
    samples = new int[width];

    if(ch == null)
    {
      visibleSamples = 0;
      nrSamples = 0;
    }
    else
    {
      nrSamples = channel.getSamplesSize()*Channel.SAMPLES_SIZE;
      visibleSamples = nrSamples;
      centerSample = nrSamples/2;
      updateView();
    }
  }

  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    Graphics2D g2 = (Graphics2D) g;

    for(int i = 0; i < samples.length-1; ++i)
    {
      g2.drawLine(i, samples[i], i+1, samples[i+1]);
    }
    g2.drawLine(0,height/2,width,height/2);
    g2.drawLine(0,height,width,height);
    g2.drawLine(0,0,width,0);

    if(markBeginning > 0)
      g2.drawLine(markBeginning,0,markBeginning,height);
    if(markEnd > 0)
    g2.drawLine(markEnd,0,markEnd,height);

    if(markEnd > 0 && markBeginning > 0)
    {
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
      g2.setColor(Color.blue);
      g2.fillRect(markBeginning, 0, markEnd-markBeginning, height);
    }  
   // player.
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
  }

  public void updateView()
  {
    if(channel == null)
    {
      visibleSamples = 0;
      nrSamples = 0;
    }

    //SampleIterator iterator = channel.getIterator();
    int sample = 0;
    int x = 0;
    int maxAmp = channel.getMaxAmplitude();
    int minAmp = channel.getMinAmplitude();

    //TODO HACK
    double heightScale = ((float)height/((long)(maxAmp)-(long)(minAmp)))*500; //0.0001f;//(float)height/(maxAmp);
    int widthSamples = visibleSamples/width;

    /*
    int start;
    int stop;

    if(centerSample-visibleSamples/2 > 0)
      start = centerSample - visibleSamples/2;
    else
      start = 0;


    for(int i = 0; i < width; ++i)
    {
      try
      {
        System.out.println("start: "+start);
        System.out.println("start+widthSamples = "+start+widthSamples);
        System.out.println("nrSampls: "+nrSamples);
        sample = channel.getSamples( start, start+widthSamples );
        samples[i] = Math.round((height/2)+(float)(sample.getAverageAmplitude()*heightScale));
      } catch ( Exception e )
      {
        e.printStackTrace();
      }

      start += widthSamples;
    }
            */
    SampleIterator iterator = channel.getIterator();
    
    if(centerSample-visibleSamples/2 > 0)
      for(int i = 0; i < centerSample-visibleSamples/2; ++i)
        if(iterator.hasNext())
          iterator.next();
        else
          return;

    for(int i = 0; i < width; ++i)
    {
      for(int j = 0; j < widthSamples; ++j)
        if(iterator.hasNext())
          sample = sample+iterator.next();
        else
          return;

      samples[i] = Math.round((height/2)+(float)(sample*heightScale));
      sample = 0;
    }

    this.repaint();
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
      System.out.println(e.getX() + " " + e.getY() );
      if(x < width && x > 0 && y < height)
      {
        markBeginning = x;          
        markEnd = -1;
        this.repaint();
      }
    } 
  }

  //@Override
  public void mouseReleased(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1)
    {
      int x = e.getX();
      int y = e.getY();

      if(x < width && x > 0 && x >= markBeginning && markBeginning != -1 && y < height)
        markEnd = x;
      else if(x < width && x > 0 && x <= markBeginning && markBeginning != -1 && y < height)
      {
        markEnd = markBeginning;
        markBeginning = x;
      }
      this.repaint();
    }
    else if( e.getButton() == MouseEvent.BUTTON3)
    {
      markBeginning = -1;
      markEnd = -1;
      this.repaint();
    }
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
    System.out.println(e.getWheelRotation());
    //-1 scroll wheel up
    //1 scroll wheel down
    if(e.getWheelRotation() > 0)
    {
    }
    else
    {}
  }
}
