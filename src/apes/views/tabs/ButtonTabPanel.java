package apes.views.tabs;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import apes.controllers.TabsController;


/**
 * Is a panel that is placed on a tab. A label and a close button is placed on
 * in.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ButtonTabPanel extends JPanel
{
  private TabsView tabsView;

  /**
   * Creates a new <code>ButtonTabPanel</code> instance.
   * 
   * @param tabsView The tabs view.
   */
  public ButtonTabPanel(final TabsView tabsView, TabsController tabsController)
  {
    this.tabsView = tabsView;

    // Use flowlayout.
    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    // Not opaque.
    setOpaque(false);

    // Make JLabel read titles from TabsView.
    JLabel label = new JLabel()
    {
      /**
       * Always return the tab text.
       */
      @Override
      public String getText()
      {
        int i = tabsView.indexOfTabComponent(ButtonTabPanel.this);

        if(i != -1)
        {
          return tabsView.getTitleAt(i);
        }

        return null;
      }
    };

    add(label);

    // Close button.
    TabCloseButton close = new TabCloseButton(this);
    close.addActionListener(tabsController);
    close.setName("close");
    add(close);
  }

  /**
   * Return the tabs view that this panel is added on.
   * 
   * @return The tabs view.
   */
  public TabsView getTabsView()
  {
    return tabsView;
  }
}
