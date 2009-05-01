package apes.models.undo;

import apes.models.Channel;
import apes.models.Samples;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * PasteEdit records changes which occurs after performing a
 * paste action. PasteEdit provides undo/redo support for
 * PasteAction.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PasteEdit extends AbstractUndoableEdit
{
  
  private Channel channel;
  private Samples[] paste;
  private int start, stop;
  
  public PasteEdit( Channel c, Point marked, Samples[] p )
  {
    channel = c;
    start = marked.x;
    stop = marked.y;
    paste = p;
    redo();
  }
  
  public void redo()
  {
    stop = channel.pasteSamples( start, paste );
  }
  
  public void undo()
  {
    paste = channel.cutSamples( start, stop );
  }
}
