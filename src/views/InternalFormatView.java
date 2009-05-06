package apes.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import apes.controllers.ChannelController;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.views.InternalFormatStatusPanel;
import apes.views.ChannelView;

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
   * The channel view.
   */
  private ChannelView channelView;
  
  /**
   * The status panel.
   */
  private InternalFormatStatusPanel statusPanel;

  /**
   * Places one ChannelView for each channel on this panel.
   *
   * @param internalFormat an <code>InternalFormat</code> value.
   */
  public InternalFormatView( PlayerHandler playerHandler, InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;

    Player player = playerHandler.getPlayer( internalFormat );
    ChannelController channelController = new ChannelController( player );

    statusPanel = new InternalFormatStatusPanel( channelController );
    add( statusPanel );

    channelView = new ChannelView( channelController, player );
    add( channelView );

    setInternalFormat( internalFormat );
  }

  /**
   * Adds some channel views to this pannel.
   *
   * @param internalFormat The internal format.
   */
  private void setInternalFormat( InternalFormat internalFormat )
  {
    // TODO: Should we clear channelView?

    for( int i = 0; i < internalFormat.getNumChannels(); i++ )
    {
      channelView.addChannel( internalFormat.getChannel( i ) );
    }
  }

  /**
   * Returns the internal format for this view.
   *
   * @return The internal format.
   */
  public InternalFormat getInternalFormat()
  {
    return internalFormat;
  }

  /**
   * Updates the view.
   */
  public void updateView()
  {
    channelView.updateView();
  }

  /**
   * Returns this internal format views channel view.
   *
   * @return The channel view.
   */
  public ChannelView getChannelView()
  {
    return channelView;
  }
  
  /**
   * Returns this internal format views status panel.
   *
   * @return The status panel.
   */
  public InternalFormatStatusPanel getStatusPanel()
  {
    return statusPanel;
  }
}