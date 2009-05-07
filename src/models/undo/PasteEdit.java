package apes.models.undo;

import apes.models.InternalFormat;
import apes.models.Channel;
import apes.models.Samples;

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
  private Samples[][] paste;
  private int start, stop;
  
  public PasteEdit( InternalFormat intForm, Point marked, Samples[][] p )
  {
    internalFormat = intForm;
    start = marked.x;
    stop = marked.y;
    paste = p;
    redo();
  }
  
  public void redo()
  {
    stop = internalFormat.pasteSamples( start, paste );
  }
  
  public void undo()
  {
    paste = internalFormat.cutSamples( start, stop );
  }
}
