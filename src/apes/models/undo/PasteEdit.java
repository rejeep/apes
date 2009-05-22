package apes.models.undo;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

import apes.models.InternalFormat;
import apes.models.MemoryHandler;


/**
 * PasteEdit records changes which occurs after performing a paste action.
 * PasteEdit provides undo/redo support for PasteAction.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PasteEdit extends AbstractUndoableEdit
{
  private InternalFormat internalFormat;

  private MemoryHandler paste;

  private long start, stop;

  private boolean undoable;

  public PasteEdit(InternalFormat intForm, Point marked, MemoryHandler p)
  {
    internalFormat = intForm;
    start = marked.x;
    stop = start + p.getUsedMemory();
    paste = new MemoryHandler();
    paste.transfer(p, 0, p.getUsedMemory(), 0);
    redo();
  }

  @Override
  public void redo()
  {
    internalFormat.pasteSamples(start, paste);
    paste.dispose();
    undoable = true;
  }

  @Override
  public void undo()
  {
    paste.dispose();
    internalFormat.cutSamples(start, stop, paste);
    undoable = false;
  }

  @Override
  public boolean canRedo()
  {
    return !undoable;
  }

  @Override
  public boolean canUndo()
  {
    return undoable;
  }
}
