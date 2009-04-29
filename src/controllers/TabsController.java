package apes.controllers;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import apes.views.ButtonTabPanel;
import apes.views.CloseButton;
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
   * Creates a new <code>TabsController</code>.
   *
   */
  public TabsController()
  {
    this.tabsView = new TabsView();
  }

  /**
   * Closes the clicked tab.
   */
  public void close()
  {
    CloseButton button = (CloseButton)event.getSource();
    int index = tabsView.indexOfTabComponent( button.getButtonTabPanel() );

    if( index != -1 )
    {
      tabsView.remove( index );
    }
  }

  /**
   * Adds a <code>Component</code> with a title as a new tab.
   */
  public void add( Component component, String title )
  {
    tabsView.addTab( title, component );

    ButtonTabPanel buttonTabPanel = new ButtonTabPanel( this, tabsView );
    int index = tabsView.getTabCount() - 1;

    tabsView.setTabComponentAt( index, buttonTabPanel );
    tabsView.setEnabledAt( index, true );
  }

  /**
   * Returns the tab view.
   */
  public TabsView getTabsView()
  {
    return this.tabsView;
  }
}
