package apes.views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.Config;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import java.util.Set;
import java.util.HashSet;
import java.awt.Point;

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
  private List<ChannelView> channelViews;

  /**
   * The player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * The channel controller.
   */
  private ChannelController channelController;
  

  /**
   * Places one ChannelView for each channel on this panel.
   *
   * @param internalFormat an <code>InternalFormat</code> value.
   */
  public InternalFormatView( PlayerHandler playerHandler, InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;
    this.playerHandler = playerHandler;
    this.channelViews = new ArrayList<ChannelView>();
    this.channelController = new ChannelController();

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
      // Create view
      ChannelView channelView = new ChannelView( player, internalFormat.getChannel( i ),
                                                 Config.getInstance().getIntOption( "graph_width" ),
                                                 Config.getInstance().getIntOption( "graph_height" ) );
      
      // Set controller
      channelView.setController( channelController );
      
      // Add to list of channel views
      channelViews.add( channelView );
      
      // Add to this panel
      add( channelView );
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
   *
   * @return The internal format.
   */
  public InternalFormat getInternalFormat()
  {
    return this.internalFormat;
  }

  /**
   * Return a set of all channel views that are selected.
   *
   * @return A set of all selected channel views.
   */
  public Set<ChannelView> getSelectedChannels()
  {
    Set<ChannelView> selected = new HashSet<ChannelView>();

    for( ChannelView channelView : channelViews )
    {
      Point point = channelView.getMarkedSamples();

      if( point != null )
      {
        selected.add( channelView );
      }
    }

    return selected;
  }

  /**
   * Returns the list of all ChannelViews.
   * @return The list of all ChannelViews.
   */
  public List<ChannelView> getChannelViews()
  {
    return channelViews;
  }
}