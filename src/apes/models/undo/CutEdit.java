package apes.models.undo;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

import apes.models.InternalFormat;
import apes.models.MemoryHandler;

/**
 * CutEdit records changes which occurs after performing a cut action.
 * CutEdit provides undo/redo support for CutAction.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CutEdit extends AbstractUndoableEdit
{
  private MemoryHandler cutout;

  private InternalFormat internalFormat;

  private int start, stop;

  private boolean undoable;

  /**
   * Constructs the CutEdit and performs the cut.
   * 
   * @param c Channel to be affected.
   * @param marked A point where [x,y] describes the interval to cut
   *          as absolute indexes.
   */
  public CutEdit(InternalFormat intForm, Point marked)
  {
    internalFormat = intForm;
    start = marked.x;
    stop = marked.y;
    cutout = new MemoryHandler();
    redo();
  }

  /**
   * Performs the action of cutting the selected interval from the
   * selected Channel.
   */
  public void redo()
  {
    cutout.dispose();
    internalFormat.cutSamples(start, stop, cutout);
    undoable = true;
  }

  /**
   * Undoes the cutting by pasting the cutout into the file at
   * selected index.
   */
  public void undo()
  {
    internalFormat.pasteSamples(start, cutout);
    cutout.dispose();
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

  /**
   * Returns an array of Samples containing all samples cut from the
   * Channel.
   * 
   * @return Returns cutout.
   */
  public MemoryHandler getCutout()
  {
    return cutout;
  }
}
