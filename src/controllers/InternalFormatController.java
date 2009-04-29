package apes.controllers;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.undo.ChangeEdit;
import apes.models.undo.CopyEdit;
import apes.models.undo.DeleteEdit;
import apes.models.undo.PasteEdit;
import apes.plugins.WaveFileFormat;
import apes.views.InternalFormatView;

/**
 * Controller for the internal format.
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * Internal format.
   */
  private InternalFormat internalFormat;

  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;

  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  /**
   * The main panel tabs.
   */
  private TabsController tabsController;


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
    this.internalFormatView = new InternalFormatView();
  }

  /**
   * A piece was copied from the graph.
   */
  public void copy()
  {
    UndoableEdit edit = new CopyEdit();

    undoManager.addEdit( edit );
  }

  /**
   * A piece was cut from the graph.
   */
  public void cut()
  {
    UndoableEdit edit = new CopyEdit();

    undoManager.addEdit( edit );
  }

  /**
   * A piece was pasted to the graph.
   */
  public void paste()
  {
    UndoableEdit edit = new PasteEdit();

    undoManager.addEdit( edit );
  }

  /**
   * Performs a delete.
   */
  public void delete()
  {
    UndoableEdit edit = new DeleteEdit();

    undoManager.addEdit( edit );
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
  }

  /**
   * Performs a redo.
   */
  public void redo()
  {
    undoManager.redo();
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    WaveFileFormat wav = new WaveFileFormat();

    try
    {
      // Get the internal format.
      internalFormat = wav.importFile( ".", "test.wav" );
      
      // Set the new internal format for the player.
      Player.getInstance().setInternalFormat( internalFormat );
      
      // Set the new internal format for the view.
      internalFormatView.setInternalFormat( internalFormat );

      // Add the view to a new tab.
      tabsController.add( internalFormatView, internalFormat.getFileStatus().getFileName() );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}