package apes.views;

import java.util.Observable;
import java.util.Observer;
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
public class InternalFormatView extends JPanel implements Observer
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
   * The channel controller.
   */
  private ChannelController channelController;

  /**
   * Zoom in and out by this much.
   */
  public final static int ZOOM = 2;

  /**
   * Max possible zoom.
   */
  public final static int MAX_ZOOM = 10;
  
  /**
   * Places one ChannelView for each channel on this panel.
   *
   * @param internalFormat an <code>InternalFormat</code> value.
   */
  public InternalFormatView( InternalFormat internalFormat )
  {
    this.internalFormat = internalFormat;
    
    Player player = PlayerHandler.getInstance().setInternalFormat( internalFormat );

    // Observe these.
    internalFormat.addObserver( this );
    player.addObserver( this );

    channelController = new ChannelController( player );

    channelView = new ChannelView( channelController, player );
    statusPanel = new InternalFormatStatusPanel( internalFormat.getSampleRate(), channelController, player );

    add( statusPanel );
    add( channelView );

    // The controller must know some views.
    channelController.setStatusPanel( statusPanel );
    channelController.setChannelView( channelView );

    setInternalFormat( internalFormat );
  }

  /**
   * Returns the internal format connected to this view.
   *
   * @return The internal format.
   */
  public InternalFormat getInternalFormat()
  {
    return internalFormat;
  }
  
  /**
   * Adds some channel views to this pannel.
   *
   * @param internalFormat The internal format.
   */
  private void setInternalFormat( InternalFormat internalFormat )
  {
    for( int i = 0; i < internalFormat.getNumChannels(); i++ )
    {
      channelView.addChannel( internalFormat.getChannel( i ) );
    }
  }

  public void update( Observable o, Object arg )
  {
    if( o instanceof InternalFormat )
    {
      channelView.updateInternalFormat();
    }

    if( o instanceof Player )
    {
      channelView.updatePlayer();
      statusPanel.updatePlayer();
    }
  }

  /**
   * Updates all.
   */
  public void updateAll()
  {
    channelView.updateInternalFormat();
    channelView.updatePlayer();
    statusPanel.updatePlayer();
  }

  // DELEGATORS
  public int getZoom() { return channelView.getZoom(); }
  public void setZoom( int samples ) { channelView.setZoom( samples ); }
  public void setCenter( int sample ) { channelView.setCenter( sample ); }
}