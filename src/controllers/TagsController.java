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
  
  public void close()
  {
    tagsView.setVisible(false);
    tagsView.dispose();
  }

  /**
   *
   */
  public void save()
  {
    // Create new Tags object from view.
    Tags tags = new Tags();
    tags.put( "name", tagsView.getName() );
    tags.put( "track", tagsView.getTrack() );
    tags.put( "artist", tagsView.getArtist() );
    tags.put( "album", tagsView.getAlbum() );
    tags.put( "genre", tagsView.getGenre() );
    tags.put( "composer", tagsView.getComposer() );
    tags.put( "comments", tagsView.getComments() );
    tags.put( "copyright", tagsView.getCopyright() );
    
    System.out.println(tags);
//    System.out.println(s);
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
