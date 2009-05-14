package apes.models.undo;

import apes.models.InternalFormat;

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
  private byte[] paste;
  private int start, stop;
  private boolean undoable;
  
  public PasteEdit( InternalFormat intForm, Point marked, byte[] p )
  {
    internalFormat = intForm;
    start = marked.x;
    stop = marked.y;
    paste = p;
    redo();
  }
  
  public void redo()
  {
    System.out.println("Redoing paste");
    stop = internalFormat.pasteSamples( start, paste );
    undoable = true;
    System.out.println("Redoing paste");
  }
  
  public void undo()
  {
    paste = internalFormat.cutSamples( start, stop );
    undoable = false;
    System.out.println("Undoing cut");
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
