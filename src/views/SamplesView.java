package apes.views;

import apes.models.Channel;
import apes.models.SampleIterator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SamplesView extends JPanel
{
  Channel channel;
  int nrSamples;
  int width, height;
  int centerSample;
  int visibleSamples;
  int[] samples;
  
  public SamplesView( Channel ch, int width, int height )
  {
    super();
    channel = ch;
    this.width = width;
    this.height = height;
    this.setSize( width, height );
    nrSamples = channel.getSamplesSize()*Channel.SAMPLES_SIZE;
    visibleSamples = nrSamples;
    samples = new int[width];
    updateView();
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

  public void updateView()
  {
    SampleIterator iterator = channel.getIterator();
    long sample = 0;
    int x = 0;
    int maxAmp = channel.getMaxAmplitude();
    int minAmp = channel.getMinAmplitude();
    //TODO HACK
    double heightScale = ((float)height/((long)(maxAmp)-(long)(minAmp)))*500; //0.0001f;//(float)height/(maxAmp);
    long widthSamples = Math.round((float)visibleSamples/width);


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

  }


}
