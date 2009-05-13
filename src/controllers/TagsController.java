package apes.controllers;

import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Tags;
import apes.views.TagsView;

/**
 * This class handles events related to tags.
 */
public class TagsController extends ApplicationController
{
  /**
   * The view.
   */
  private TagsView tagsView;

  /**
   * The internal format that the tags should be edited on.
   */
  private InternalFormat internalFormat;
  
  /**
   * 
   */
  private PlayerHandler playerHandler;

  /**
   * Creates a new <code>TagsController</code> instance.
   */
  public TagsController()
  {
    playerHandler = PlayerHandler.getInstance();
  }

  /**
   * Creates a new tags view (frame) where tags can be edited.
   */
  public void edit()
  {
    internalFormat = playerHandler.getInternalFormat();
    this.tagsView = new TagsView( this, internalFormat.getTags() );
  }

  /**
   * Closes the tags window.
   */
  public void close()
  {
    tagsView.setVisible( false );
    tagsView.dispose();
  }

  /**
   * Saves the new tag values to the tag.
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

    internalFormat.setTags( tags );
  }
}