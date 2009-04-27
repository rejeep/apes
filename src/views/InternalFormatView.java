package apes.views;

import javax.swing.JPanel;

import apes.models.Player;
import apes.models.InternalFormat;
import apes.lib.Config;

/**
 * Contains one ChannelView per channel in the internal format.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatView extends JPanel
{
  /**
   * Places one ChannelView for each channel on this panel.
   */
  public InternalFormatView()
  {
    InternalFormat internalFormat = Player.getInstance().getInternalFormat();

    for( int i = 0; i < internalFormat.getNumChannels(); i++ )
    {
      add( new ChannelView(Player.getInstance(), internalFormat.getChannel( i ),
                           Config.getInstance().getIntOption( "graphwidth" ),
                           Config.getInstance().getIntOption( "graphheight" )));
    }
  }
}