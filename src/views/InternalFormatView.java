package apes.views;

import javax.swing.JPanel;

import apes.controllers.ChannelController;
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
    
    // The controller must know some views.
    channelController.setStatusPanel( statusPanel );
    channelController.setChannelView( channelView ); 

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
}