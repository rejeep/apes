package apes.views;

import javax.swing.JPanel;

import apes.lib.Config;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.views.ChannelView;
import java.util.List;
import java.util.ArrayList;

/**
 * Contains one ChannelView per channel in the internal format.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatView extends JPanel
{
  /**
   * Internal format.
   */
  private InternalFormat internalFormat;
  
  /**
   * List of all channel views.
   */
  private List<ChannelView> channelViews = new ArrayList<ChannelView>();
  
  /**
   * Places one ChannelView for each channel on this panel.
   *
   * @param internalFormat an <code>InternalFormat</code> value
   */
  public InternalFormatView( InternalFormat internalFormat )
  {
    setInternalFormat( internalFormat );
  }
  
  /**
   * Adds some channel views to this pannel.
   *
   * @param internalFormat The internal format.
   */
  public void setInternalFormat( InternalFormat internalFormat )
  {
    channelViews.clear();

    for( int i = 0; i < internalFormat.getNumChannels(); i++ )
    {
      channelViews.add( new ChannelView( Player.getInstance(), internalFormat.getChannel( i ),
                                         Config.getInstance().getIntOption( "graph_width" ),
                                         Config.getInstance().getIntOption( "graph_height" ) ) );
      add( channelViews.get( i ) );
    }

    updateView();
  }

  /**
   * Updates the view by telling each channel view to do so.
   */
  public void updateView()
  {
    for( ChannelView channelView : channelViews )
    {
      channelView.updateView();
    }
  }
}