package apes.models.undo;

import apes.models.InternalFormat;
import apes.models.MemoryHandler;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * PasteEdit records changes which occurs after performing a paste
 * action. PasteEdit provides undo/redo support for PasteAction.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PasteEdit extends AbstractUndoableEdit
{
  private InternalFormat internalFormat;

  private MemoryHandler paste;

  private int start, stop;

  private boolean undoable;

  public PasteEdit(InternalFormat intForm, Point marked, MemoryHandler p)
  {
    internalFormat = intForm;
    start = marked.x;
    stop = marked.y;
    paste = new MemoryHandler();
    paste.transfer(p, 0, (int)(p.getUsedMemory() - 1), 0);
    redo();
  }

  public void redo()
  {
    internalFormat.pasteSamples(start, paste);
    paste.dispose();
    undoable = true;
  }

  public void undo()
  {
    paste.dispose();
    internalFormat.cutSamples(start, stop, paste);
    undoable = false;
  }

  public boolean canRedo()
  {
    return !undoable;
  }

  public boolean canUndo()
  {
    return undoable;
  }
}
