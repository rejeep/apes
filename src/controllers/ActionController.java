package apes.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.EventObject;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
 * <p>However. If there is no method named action. A method defined in
 * {@link ApplicationController#methodMissing ApplicationController}
 * called methodMissing is called. This is handy for dynamic action
 * calling. So in your controller, you can override that method to get
 * some behavior.</p>
 *
 * <p>Two variables are by default accessible in the controllers.
 * <ul>
 *   <li>event - Is the event that occurred.</li>
 *   <li>name - Is the name of the component.</li>
 * </ul>
 * </p>
 *
 * <p>If the above do not work, start by reading the comments in
 * {@link ApplicationController ApplicationController}.</p>
 *
 * <p>If you done that and it still doesn't work, make sure that this
 * class implements the listener class you want to use. Say for
 * example that you want to add a <code>ComponentListener</code> to
 * your controller. Then this class must implement ComponentListener
 * and have all methods from that interface. In this case:
 * <ul>
 *   <li>void componentHidden(ComponentEvent e)</li>
 *   <li>void componentMoved(ComponentEvent e)</li>
 *   <li>void componentResized(ComponentEvent e)</li>
 *   <li>void componentShown(ComponentEvent e)</li>
 * </ul>
 *
 * And in each of these methods you should first set the event
 * instance variable to the event that is passed to the method. And
 * then call {@link ActionController#callActionByName callActionByName}.</p>
 *
 * One more thing to check. Is your controller initialized? Because if
 * you send null to addActionListener, you don't get any error.
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
  
  /**
   * The name of the component.
   */
  protected String name;

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
    // The action that is to be called is the method with the same
    // name as the name for the component that triggered the event.
    this.name = ((Component)event.getSource()).getName();

    try
    {
      callAction( name );
    }
    catch( NoSuchMethodException e )
    {
      try
      {
        callAction( "methodMissing" );
      }
      catch( Exception ex )
      {
        ex.printStackTrace();
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Call that method on this (the controller the view is connected
   * to) class.
   *
   * @param name a <code>String</code> value
   *
   * @exception NoSuchMethodException If method does not exits.
   * @exception Exception If any other error occurs.
   */
  private void callAction( String name ) throws NoSuchMethodException, Exception
  {
    Method method = this.getClass().getMethod( name );

    method.invoke( this );
  }
}