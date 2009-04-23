package apes.controllers.undo;

import javax.swing.undo.UndoManager;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoableEdit;
import apes.models.undo.CutEdit;

/**
 * Cut action. Handles the cutting in the graph.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CutController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;

  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  public CutController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.undoManager = undoManager;
  }

  /**
   * A piece was cut from the graph.
   */
  public void cut()
  {
    UndoableEdit edit = new CutEdit();

    undoManager.addEdit( edit );
  }
}
