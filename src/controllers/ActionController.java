package apes.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.EventObject;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * <p>This class handles all actions. All controller should extend
 * ApplicationController, who in turn extend from this class. This
 * means that all controllers will handle events the same way.</p>
 *
 * <p>If you want to connect an event to a <code>Component</code>, Say
 * for example a JButton, you add a listener as usual:</p>
 *
 * <pre>JButton button = new JButton("Some button");
 *button.addActionListener(controller);</pre>
 *
 * <p>Note that the event should <b>always</b> go to the
 * controller.</p>
 *
 * <p>After this there's one more thing to do. And that is to give the
 * Component a name with the <code>{@link
 * java.awt.Component#setName(String name) setName}</code> method.</p>
 * <pre>button.setName("action");</pre>
 *
 * <p>The name is the action that is called in the controller that was
 * connected to the event. So for the above example the "action"
 * method will be called when the button is clicked.</p>
 *
 * <p>If the above do not work, start by reading the comments in
 * {@link ApplicationController ApplicationController}.</p>
 *
 * <p>If you done that and it still doesn't work, make sure that this
 * class implements the listener class you want to use. Say for
 * example that you want to add a <code>ComponentListener</code> to
 * your controller. Then this class must implement ComponentListener
 * and have all methods from that interface. In this case:
 * <pre>void componentHidden(ComponentEvent e)
 *void componentMoved(ComponentEvent e)
 *void componentResized(ComponentEvent e)
 *void componentShown(ComponentEvent e)</pre>
 *
 * And in each of these methods you should first set the event
 * instance variable to the event that is passed to the method. And
 * then call {@link ActionController#callActionByName callActionByName}.</p>
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ActionController implements ActionListener, ChangeListener
{
  /**
   * The event that was fired. Controllers can via this variable get
   * the Component who fired the event.
   */
  protected EventObject event;

  public void actionPerformed( ActionEvent e )
  {
    this.event = e;

    callActionByName();
  }

  public void stateChanged( ChangeEvent e )
  {
    this.event = e;

    callActionByName();
  }

  /**
   * Will call the method in the controller with the same name as the
   * components name.
   */
  private void callActionByName()
  {
    // The action is determined by the component name.
    String action = ( ( Component ) ( event.getSource() ) ).getName();

    try
    {
      // Call that method on this (the controller the view is
      // connected to) class.
      Method method = this.getClass().getMethod( action );
      method.invoke( this );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}