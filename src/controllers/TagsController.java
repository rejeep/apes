package apes.controllers;

import apes.views.TagsView;
import apes.models.Player;
import apes.models.Tags;
import apes.models.InternalFormat;

/**
 *
 */
public class TagsController extends ApplicationController
{
  /**
   *
   */
  private TagsView tagsView;
  
  /**
   *
   */
  private InternalFormat internalFormat;

  
  /**
   *
   */
  public void edit()
  {
    setInternalFormat();
    
    this.tagsView = new TagsView( this, internalFormat.getTags() );
  }
  
  /**
   *
   */
  public void save()
  {
    // Create new Tags object from view.
    
    Tags tags = null;
    
    internalFormat.setTags(tags);
  }
  
  /**
   *
   */
  private void setInternalFormat()
  {
    this.internalFormat = Player.getInstance().getInternalFormat();
  }
}
