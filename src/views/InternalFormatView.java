package apes.views;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import apes.models.Channel;
import apes.models.InternalFormat;
import apes.models.Player;

import apes.models.Samples;

/**
 * 
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatView extends JPanel
{
  private int width;
  private int height;
  
  public InternalFormatView()
  {
    this.width = 800;
    this.height = 300;
    
    setPreferredSize( new Dimension( width, height ) );
  }

  @Override
  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    
    Player player = Player.getInstance();
    InternalFormat internalFormat = player.getInternalFormat();
    Channel channel = internalFormat.getChannel( 0 );
    int size = channel.getSamplesSize();
    Samples samples = null;
    int min = channel.getMinAmplitude();
    int max = channel.getMaxAmplitude();
    long diff = channel.getDiffAmplitude();
    int prev_y = height / 2;
      
    for( int i = 0, j = 0; i < size && j < width; i++, j += 5 )
    {
      try
      {
        samples = channel.getSamples( i );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
      
      long temp = (long)samples.getAverageAmplitude() - min;

      double part = (double)temp / diff;
      int y = (int)( part * height ) / 2;
      
      g.drawLine( j - 1, prev_y, j, y );
      
      prev_y = y;
    }
  }
}