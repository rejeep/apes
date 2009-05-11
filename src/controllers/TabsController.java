package apes.controllers;

import java.util.HashMap;
import java.util.Map;

import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.views.ButtonTabPanel;
import apes.views.CloseButton;
import apes.views.InternalFormatView;
import apes.views.TabsView;

/**
 * Handles all tab actions.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TabsController extends ApplicationController
{
  /**
   * The tab view.
   */
  private TabsView tabsView;

  /**
   * The player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * Must have this to keep track of which internal format to get rid
   * of when closing a tab.
   */
  private Map<CloseButton, InternalFormat> buttons;

  /**
   * Keeps track of which view an internal format is connected to.
   */
  private Map<InternalFormat, InternalFormatView> internalFormats;

  /**
   * Creates a new <code>TabsController</code>.
   */
  public TabsController( PlayerHandler playerHandler )
  {
    this.playerHandler = playerHandler;
    
    this.tabsView = new TabsView();
    this.tabsView.addChangeListener( this );
    this.tabsView.setName( "change" );

    buttons = new HashMap<CloseButton, InternalFormat>();
    internalFormats = new HashMap<InternalFormat, InternalFormatView>();
  }

  /**
   * Closes the pressed tab and removes the player for that tab in the
   * player handler.
   *
   * NOTE: We don't need to set the new internalFormat. The close
   * action will also trigger the change action (See {@link
   * TabsController#change change}, which in turn sets the new
   * internal format.
   */
  public void close()
  {
    CloseButton closeButton = (CloseButton)event.getSource();
    int index = tabsView.indexOfTabComponent( closeButton.getButtonTabPanel() );

    if( index != -1 )
    {
      tabsView.remove( index );
    }

    // The internal format.
    InternalFormat internalFormat = buttons.get( closeButton );
      
    // Remove player from player handler.
    playerHandler.remove( internalFormat );
    
    // Remove from map that keeps track of buttons and internalformats.
    buttons.remove( closeButton );
    
    // Remove connection between internal format and it's view.
    internalFormats.remove( internalFormat );
  }

  /**
   * Adds an <code>internalFormatView</code> to a new tab.
   *
   * @param internalFormat The internal format to create a view from.
   * @param title The tab title (name).
   */
  public void add( InternalFormat internalFormat, String title )
  {
    // Set the new internal format.
    playerHandler.setInternalFormat( internalFormat );

    // Create a view over the new internal format and fix observers.
    Player player = playerHandler.getCurrentPlayer();
    InternalFormatView internalFormatView = new InternalFormatView( player, internalFormat );
    internalFormat.addObserver( internalFormatView );
    player.addObserver( internalFormatView );

    // Add the tab to the pane.
    tabsView.addTab( title, internalFormatView );

    // Get the tab index (the last tab).
    int index = tabsView.getTabCount() - 1;

    // Add panel component to the tab.
    ButtonTabPanel buttonTabPanel = new ButtonTabPanel( this, tabsView );
    tabsView.setTabComponentAt( index, buttonTabPanel );

    // Add button to buttons map so that we can remove player when
    // closing the tab.
    buttons.put( buttonTabPanel.getCloseButton(), internalFormat );

    // Select the added tab.
    tabsView.setSelectedIndex( index );
    
    // Adds internal format and view connection.
    internalFormats.put( internalFormat, internalFormatView );
  }

  /**
   * Returns the tab view.
   */
  public TabsView getTabsView()
  {
    return tabsView;
  }

  /**
   * Is called when something in the tabs pane changes. For example a
   * tab is changed or closed.
   */
  public void change()
  {
    // NullPointerException occurs if the last tab is closed. Then we
    // don't want to do anything.
    try
    {
      TabsView tabsView = (TabsView)event.getSource();
      InternalFormatView internalFormatView = (InternalFormatView)tabsView.getSelectedComponent();
      InternalFormat internalFormat = internalFormatView.getInternalFormat();

      playerHandler.setInternalFormat( internalFormat );
    }
    catch( NullPointerException e ) {}
  }
  
  /**
   * TODO: Comment
   *
   * @return an <code>InternalFormatView</code> value
   */
  public InternalFormatView getCurrentInternalFormatView()
  {
    return internalFormats.get( playerHandler.getInternalFormat() );
  }
}
