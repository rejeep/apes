package apes.views.tabs;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JTabbedPane;

import apes.controllers.TabsController;
import apes.models.Tabs;
import apes.views.InternalFormatView;

/**
 * The tabs view is the pane with all tabs.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TabsView extends JTabbedPane implements Observer
{
  /**
   * The tabs controller.
   */
  private TabsController tabsController;

  /**
   * Creates a new <code>TabsView</code> instance.
   * 
   * @param tabsController The tabs controller.
   */
  public TabsView(TabsController tabsController)
  {
    this.tabsController = tabsController;

    // Listen to change events.
    setName("change");
    addChangeListener(tabsController);
  }

  /**
   * Updates this view. Is called when a tab has been added or
   * removed.
   * 
   * @param observable The tabs model class.
   * @param object The tab that was added or removed.
   */
  public void update(Observable observable, Object object)
  {
    Tabs tabs = (Tabs)observable;
    Tabs.Tab tab = (Tabs.Tab)object;

    // This means that we should add the tab.
    if(tabs.contains(tab))
    {
      addTab(tab);
    }
    else
    {
      remove(tab.getIndex());
    }
  }

  /**
   * Adds a tab to the view.
   * 
   * @param tab The tab to add.
   */
  public void addTab(Tabs.Tab tab)
  {
    int index = tab.getIndex();
    String title = tab.getTitle();
    InternalFormatView internalFormatView = tab.getInternalFormatView();

    addTab(title, internalFormatView);
    setSelectedIndex(index);

    ButtonTabPanel buttonTabPanel = new ButtonTabPanel(this, tabsController);
    setTabComponentAt(index, buttonTabPanel);
  }
}
