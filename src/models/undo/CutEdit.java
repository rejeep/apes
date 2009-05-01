package apes.models.undo;

import apes.models.Samples;
import apes.models.Channel;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

/**
 * CutEdit records changes which occurs after performing a
 * cut action. CutEdit provides undo/redo support for CutAction.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CutEdit extends AbstractUndoableEdit
{
  private Samples[] cutout;
  private Channel channel;
  private int start, stop;
  
  /**
   * Constructs the CutEdit and performs the cut.
   * @param c Channel to be affected.
   * @param marked A point where [x,y] describes the interval to cut as absolute indexes.
   */
  public CutEdit( Channel c, Point marked )
  {
    channel = c;
    start = marked.x;
    stop = marked.y;
    
    redo();
  }
  
  /**
   * Performs the action of cutting the selected interval from the selected Channel.
   */
  public void redo()
  {
    cutout = channel.cutSamples( start, stop );
  }
  
  /**
   * Undoes the cutting by pasting the cutout into the file at selected index. 
   */
  public void undo()
  {
    channel.pasteSamples( start, cutout );
    cutout = null;
  }
  
  /**
   * Returns an array of Samples containing all samples cut from the Channel.
   * @return Returns cutout.
   */
  public Samples[] getCutout()
  {
    return cutout;
  }
}
