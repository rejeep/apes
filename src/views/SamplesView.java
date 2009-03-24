package apes.views;

import apes.models.Channel;
import apes.models.SampleIterator;
import apes.models.Samples;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SamplesView extends JPanel
{
  private Channel channel;
  private int nrSamples;
  private int width, height;
  private int centerSample;
  private int visibleSamples;
  private int[] samples;

  public SamplesView( Channel ch, int width, int height )
  {
    super();

    channel = ch;
    this.width = width;
    this.height = height;
    this.setSize( width, height );

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
}
