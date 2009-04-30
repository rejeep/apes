package apes.controllers;

import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Tags;
import apes.views.TagsView;

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
  private PlayerHandler playerHandler;

  /**
   *
   */
  public TagsController( PlayerHandler playerHandler )
  {
    this.playerHandler = playerHandler;
  }

  /**
   *
   */
  public void edit()
  {
    this.tagsView = new TagsView( this, getInternalFormat().getTags() );
  }

  /**
   *
   */
  public void close()
  {
    tagsView.setVisible( false );
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

    getInternalFormat().setTags( tags );
  }

  /**
   *
   */
  public InternalFormat getInternalFormat()
  {
    return playerHandler.getInternalFormat();
  }
}
