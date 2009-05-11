package apes.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.EventObject;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>This class handles all controller actions. All controllers
 * should extend ApplicationController, that in turn extend this
 * class. This means that all controllers will handle events the same
 * way.</p>
 *
 * <h4>Usage</h4>
 *
 * <p>If you want to connect an event to a <code>Component</code>, Say
 * for example a <code>JButton</code>, you add a listener as
 * usual:</p>
 *
 * <pre>JButton button = new JButton("Some button");
 *button.addActionListener(controller);</pre>
 *
 * <p>Note that the event should <strong>always</strong> go to the
 * controller.</p>
 *
 * <p>After this there's one more thing to do. And that is to give the
 * Component a name with the {@link java.awt.Component#setName(String
 * name) setName} method.</p>
 * <pre>button.setName("action");</pre>
 *
 * <p>The name of the method that will be called in the controller,
 * that was connected to the component, is the one with the same name
 * as the component name. This however, is only true, if there's a
 * method in the controller with that name, taking no arguments. So
 * for the above example the <code>action</code> method in the
 * <code>controller</code> controller will be called when
 * <code>button</code> is clicked.</p>
 *
 * <p>However. If there is no method named action in the controller. A
 * method defined in {@link ApplicationController#methodMissing
 * ApplicationController} called <code>methodMissing</code> is
 * called. This is handy for dynamic action calling. So in your
 * controller, you can override this method to get some other
 * behavior.</p>
 *
 * <p>Before a method is called, {@link
 * ApplicationController#beforeFilter beforeFilter} is called. And
 * after the method is called, {@link
 * ApplicationController#afterFilter afterFilter} is
 * called. However. The action is only called if there's no exception
 * thrown in the before filter. This can be used to not call an action
 * on some condition in the before filter.</p>
 *
 * <p>Two variables are by default available in the controllers.
 * <ul>
 *   <li>event - Is the event that was fired.</li>
 *   <li>name - Is the name of the component that the event was fired on.</li>
 * </ul>
 * </p>
 *
 * <h4>Troubleshooting</h4>
 *
 * <p>If the above does not work. Start by reading the comments in
 * {@link ApplicationController ApplicationController}.</p>
 *
 * <p>If you done that and it still doesn't work, make sure that this
 * class implements the listener class you want to use. Say for
 * example that you want to add a <code>ComponentListener</code> to
 * your controller. Then this class must implement
 * <code>ComponentListener</code> and have all methods from that
 * interface. In this case:</p>
 * <ul>
 *   <li>void componentHidden(ComponentEvent e)</li>
 *   <li>void componentMoved(ComponentEvent e)</li>
 *   <li>void componentResized(ComponentEvent e)</li>
 *   <li>void componentShown(ComponentEvent e)</li>
 * </ul>
 *
 * <p>And in each of these methods you should first set the event
 * instance variable to the event that is passed to the method. And
 * then call {@link ActionController#callActionByName callActionByName}.</p>
 *
 * <p>One more thing to check is if your controller is created or not.
 * Because if you send null to <code>addActionListener</code>, you don't get any
 * errors.</p>
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public abstract class ActionController implements ActionListener, ChangeListener
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
      try
      {
        callAction( "beforeFilter" );
        callAction( name );
      }
      catch( InvocationTargetException e ) {}
      
      callAction( "afterFilter" );
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
  private void callAction( String name ) throws NoSuchMethodException, InvocationTargetException, Exception
  {
    Method method = this.getClass().getMethod( name );

    method.invoke( this );
  }
}