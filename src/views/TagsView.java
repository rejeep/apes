package apes.views;

import javax.swing.JFrame;
import apes.models.Tags;
import javax.swing.JButton;
import apes.controllers.TagsController;

/**
 *
 */
public class TagsView extends JFrame
{
  /**
   *
   */
  private Tags tags;
  
  /**
   * Tags controller.
   */
  private TagsController tagsController;

  /**
   *
   */
  public TagsView( TagsController tagsController, Tags tags )
  {
    this.tagsController = tagsController;
    this.tags = tags;

    // Present current Tags object.
    
    JButton save = new JButton("tags.save");
    save.addActionListener( tagsController );
    save.setName( "save" );
    add(save);
    
    setVisible( true );
    setSize(100, 100);
  }
}
