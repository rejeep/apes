package apes.views;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import apes.controllers.TabsController;

/**
 * Is a panel that is placed on a tab. A label and a close button is
 * placed on in.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ButtonTabPanel extends JPanel
{
  /**
   * The close button.
   */
  private JButton close;


  /**
   * Creates a new <code>ButtonTabPanel</code>.
   *
   * @param tabsController The tabs controller.
   * @param tabsView The tabs view.
   */
  public ButtonTabPanel( TabsController tabsController, final TabsView tabsView )
  {
    // Use flowlayout.
    setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );

    // Not opaque.
    setOpaque( false );

    // Make JLabel read titles from TabsView.
    JLabel label = new JLabel()
    {
      /**
       * Always return the tab text.
       */
      public String getText()
      {
        int i = tabsView.indexOfTabComponent( ButtonTabPanel.this );

        if( i != -1 )
        {
          return tabsView.getTitleAt( i );
        }

        return null;
      }
    };

    add( label );

    // Close button.
    close = new CloseButton( this );
    close.addActionListener( tabsController );
    close.setName( "close" );
    add( close );
  }

  /**
   * Return the close button.
   *
   * @return The close button.
   */
  public CloseButton getCloseButton()
  {
    return (CloseButton)this.close;
  }
}