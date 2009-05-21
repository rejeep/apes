package apes.controllers;

import apes.lib.PlayerHandler;
import apes.models.Tabs;
import apes.views.tabs.ButtonTabPanel;
import apes.views.tabs.TabCloseButton;
import apes.views.tabs.TabsView;

/**
 * Handles tab actions such as closing or changing a tab.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TabsController extends ApplicationController
{
  /**
   * The tabs model.
   */
  private Tabs tabs;

  /**
   * An instance of the player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * Creates a new <code>TabsController</code> instance.
   * 
   * @param tabs The tabs model.
   */
  public TabsController(Tabs tabs)
  {
    this.tabs = tabs;
    this.playerHandler = PlayerHandler.getInstance();
  }

  /**
   * Returns the tabs model.
   * 
   * @return The tabs model.
   */
  public Tabs getTabs()
  {
    return tabs;
  }

  /**
   * Is called when a tab is closed.
   */
  public void close()
  {
    TabCloseButton tabCloseButton = (TabCloseButton)event.getSource();
    ButtonTabPanel buttonTabPanel = tabCloseButton.getButtonTabPanel();
    TabsView tabsView = buttonTabPanel.getTabsView();

    int index = tabsView.indexOfTabComponent(buttonTabPanel);

    if(index != -1)
    {
      tabs.remove(index);
    }
  }

  /**
   * Is called when a tab is changed.
   */
  public void change()
  {
    TabsView tabsView = (TabsView)event.getSource();
    int index = tabsView.getSelectedIndex();
    Tabs.Tab tab = tabs.get(index);

    // An exception may occur here. But thats what we want. Because
    // this happens when there's no tab.
    playerHandler.setInternalFormat(tab.getInternalFormat());
  }
}
