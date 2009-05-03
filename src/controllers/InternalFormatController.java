package apes.controllers;

import java.awt.Point;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.models.InternalFormat;
import apes.models.Samples;
import apes.models.Channel;
import apes.models.undo.ChangeEdit;
import apes.models.undo.CopyEdit;
import apes.models.undo.CutEdit;
import apes.models.undo.DeleteEdit;
import apes.models.undo.PasteEdit;
import apes.plugins.WaveFileFormat;
import apes.views.InternalFormatView;
import apes.views.ChannelView;


/**
 * Controller for the internal format.
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  /**
   * The main panel tabs.
   */
  private TabsController tabsController;


  /**
   * Values copied or cut.
   */
  private Samples[] clipboard;
  
  /**
   * Creates a new <code>InternalFormatController</code>.
   *
   * @param undoManager The undo manager.
   * @param tabsController The tabs controller.
   */
  public InternalFormatController( UndoManager undoManager, TabsController tabsController )
  {
    this.tabsController = tabsController;
    this.undoManager = undoManager;
  }

  /**
   * A piece was copied from the graph.
   */
  public void copy()
  {
   ChannelView firstSelection = getFirstSelectedChannelView();
    
    // No selection?
    if( firstSelection == null )
      return;
    
    Point marked = firstSelection.getMarkedSamples();
    if( marked == null || marked.x == marked.y )
      return;
    
    clipboard = firstSelection.getChannel().copySamples( marked.x, marked.y );
  }

  /**
   * A piece was cut from the graph.
   */
  public void cut()
  {
    InternalFormatView ifView = getCurrentInternalFormatView();
    InternalFormat intForm = ifView.getInternalFormat();
    ChannelView firstSelection = getFirstSelectedChannelView();
    
    // No selection?
    if( firstSelection == null )
      return;
    
    Point marked = firstSelection.getMarkedSamples();
    if( marked == null || marked.x == marked.y )
      return;
    
    CutEdit edit = new CutEdit( intForm, firstSelection.getChannel(), marked );

    undoManager.addEdit( edit );
    
    clipboard = edit.getCutout();
    
    ifView.updateView();
  }

  /**
   * A piece was pasted to the graph.
   */
  public void paste()
  {
    if( clipboard == null )
      return;
    
    InternalFormatView ifView = getCurrentInternalFormatView();
    InternalFormat intForm = ifView.getInternalFormat();
    ChannelView firstSelection = getFirstSelectedChannelView();
    
    // No selection?
    if( firstSelection == null )
      return;
    
    Point marked = firstSelection.getMarkedSamples();
    if( marked == null )
      return;
    
    UndoableEdit edit = new PasteEdit( intForm, firstSelection.getChannel(), marked, clipboard );

    undoManager.addEdit( edit );
    
    ifView.updateView();
  }

  /**
   * Performs a delete.
   */
  public void delete()
  {
    InternalFormatView ifView = getCurrentInternalFormatView();
    InternalFormat intForm = ifView.getInternalFormat();
    ChannelView firstSelection = getFirstSelectedChannelView();
    
    // No selection?
    if( firstSelection == null )
      return;
    
    Point marked = firstSelection.getMarkedSamples();
    if( marked == null || marked.x == marked.y )
      return;
    
    CutEdit edit = new CutEdit( intForm, firstSelection.getChannel(), marked );

    undoManager.addEdit( edit );
    
    ifView.updateView();
  }

  /**
   * Performs some kind of change.
   */
  public void change()
  {
    UndoableEdit edit = new ChangeEdit();

    undoManager.addEdit( edit );
  }

  /**
   * Performs an undo.
   */
  public void undo()
  {
    undoManager.undo();
    getCurrentInternalFormatView().updateView();
  }

  /**
   * Performs a redo.
   */
  public void redo()
  {
    undoManager.redo();
    getCurrentInternalFormatView().updateView();
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    WaveFileFormat wav = new WaveFileFormat();

    try
    {
      // Chose file.
      final JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory( new File( "." ) );
      fc.showOpenDialog( null );
      File file = fc.getSelectedFile();
      String name = file.getName();

      // Set internal format.
      InternalFormat internalFormat = wav.importFile( file.getParent(), name );

      // Add the view to a new tab.
      tabsController.add( internalFormat, name );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns the <code>InternalFormatView</code> that is active. null
   * is returned if no tab is open.
   *
   * @return The currently active <code>InternalFormatView</code>.
   */
  public InternalFormatView getCurrentInternalFormatView()
  {
    return (InternalFormatView) tabsController.getTabsView().getSelectedComponent();
  }
  
  /**
   * Returns a reference to the ChannelView of the lowest index such that there is a selection. 
   * @return First ChannelView with a selection. Returns null if no ChannelView contains a selection.
   */
  public ChannelView getFirstSelectedChannelView()
  {
    // Get all Channels
    List<ChannelView> channelViews = getCurrentInternalFormatView().getChannelViews();
    
    // Fins first selection.
    ChannelView firstSelection = null;
    for( ChannelView c : channelViews )
      if( c.getMarkedSamples() != null )
      {
        firstSelection = c;
        break;
      }
    return firstSelection;
  }
}