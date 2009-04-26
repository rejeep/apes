package apes.controllers;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.models.undo.CopyEdit;
import apes.views.InternalFormatView;
import apes.models.undo.ChangeEdit;
import apes.models.undo.PasteEdit;

/**
 * Controller for the internal format.
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;

  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  /**
   * Creates a new <code>InternalFormatController</code> instance.
   *
   * @param undoManager The undo manager.
   * @param internalFormatView The internal format view.
   */
  public InternalFormatController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.undoManager = undoManager;
    this.internalFormatView = internalFormatView;
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
   * Performs a delete.
   */
  public void delete()
  {
    
  }
}