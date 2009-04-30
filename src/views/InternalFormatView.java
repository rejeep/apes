package apes.views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import apes.lib.Config;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;

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
   * The player handler.
   */
  private PlayerHandler playerHandler;
  
  /**
   * Places one ChannelView for each channel on this panel.
   *
   * @param internalFormat an <code>InternalFormat</code> value
   */
  public InternalFormatView( PlayerHandler playerHandler, InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;
    this.playerHandler = playerHandler;
    
    setInternalFormat( internalFormat );
  }
  
  /**
   * Adds some channel views to this pannel.
   *
   * @param internalFormat The internal format.
   */
  private void setInternalFormat( InternalFormat internalFormat )
  {
    channelViews.clear();
    
    Player player = playerHandler.getPlayer( internalFormat );

    for( int i = 0; i < internalFormat.getNumChannels(); i++ )
    {
      channelViews.add( new ChannelView( player, internalFormat.getChannel( i ),
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
  
  /**
   * Returns the internal format for this view.
   */
  public InternalFormat getInternalFormat()
  {
    return this.internalFormat;
  }
}