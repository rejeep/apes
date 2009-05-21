package apes.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.TagsController;
import apes.lib.Language;
import apes.models.Tags;

/**
 * View where tags can be changed.
 */
public class TagsView extends JFrame
{
  /**
   * A language object.
   */
  private Language language;

  /**
   * The head label.
   */
  private JLabel head;

  /**
   * The name.
   */
  private JTextField n;

  /**
   * The track.
   */
  private JTextField t;

  /**
   * The artist.
   */
  private JTextField ar;

  /**
   * The album.
   */
  private JTextField al;

  /**
   * The genre.
   */
  private JTextField g;

  /**
   * The composer.
   */
  private JTextField comp;

  /**
   * The comments.
   */
  private JTextField com;

  /**
   * The copyright.
   */
  private JTextField copy;

  /**
   * Creates a new <code>TagsView</code> instance.
   * 
   * @param tagsController The tags controller.
   * @param tags The tags model.
   */
  public TagsView(TagsController tagsController, Tags tags)
  {
    this.language = Language.getInstance();

    setLayout(new BorderLayout());

    // North panel
    JPanel north = new JPanel();
    north.setLayout(new FlowLayout());
    head = new ApesLabel("tags.title");
    north.add(head);

    // South panel
    JPanel south = new JPanel();
    south.setLayout(new FlowLayout());

    // Save button
    JButton save = new ApesButton("tags.save");
    save.addActionListener(tagsController);
    save.setName("save");
    south.add(save);

    // Close button
    JButton close = new ApesButton("tags.close");
    close.addActionListener(tagsController);
    close.setName("close");
    south.add(close);

    n = new JTextField(tags.get("name"));
    t = new JTextField(tags.get("track"));
    ar = new JTextField(tags.get("artist"));
    al = new JTextField(tags.get("album"));
    g = new JTextField(tags.get("genre"));
    comp = new JTextField(tags.get("composer"));
    com = new JTextField(tags.get("comments"));
    copy = new JTextField(tags.get("copyright"));

    JLabel name = new JLabel(language.get("tags.name"));
    JLabel track = new JLabel(language.get("tags.track"));
    JLabel artist = new JLabel(language.get("tags.artist"));
    JLabel album = new JLabel(language.get("tags.album"));
    JLabel genre = new JLabel(language.get("tags.genre"));
    JLabel composer = new JLabel(language.get("tags.composer"));
    JLabel comments = new JLabel(language.get("tags.comments"));
    JLabel copyright = new JLabel(language.get("tags.copyright"));

    JPanel center = new JPanel();
    center.setBackground(Color.GRAY);
    center.setLayout(new GridLayout(8, 2));
    center.setPreferredSize(new Dimension(200, 170));
    center.add(name);
    center.add(n);
    center.add(track);
    center.add(t);
    center.add(artist);
    center.add(ar);
    center.add(album);
    center.add(al);
    center.add(genre);
    center.add(g);
    center.add(composer);
    center.add(comp);
    center.add(comments);
    center.add(com);
    center.add(copyright);
    center.add(copy);

    // Placing
    add(south, BorderLayout.SOUTH);
    add(new JPanel(), BorderLayout.WEST);
    add(center, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.EAST);
    add(north, BorderLayout.NORTH);

    pack();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  /**
   * Returns the name.
   * 
   * @return The name.
   */
  @Override
  public String getName()
  {
    return n.getText();
  }

  /**
   * Returns the track.
   * 
   * @return The track.
   */
  public String getTrack()
  {
    return t.getText();
  }

  /**
   * Returns the artist.
   * 
   * @return The artist.
   */
  public String getArtist()
  {
    return ar.getText();
  }

  /**
   * Returns the album.
   * 
   * @return The album.
   */
  public String getAlbum()
  {
    return al.getText();
  }

  /**
   * Returns the genre.
   * 
   * @return The genre.
   */
  public String getGenre()
  {
    return g.getText();
  }

  /**
   * Returns the composer.
   * 
   * @return The composer.
   */
  public String getComposer()
  {
    return comp.getText();
  }

  /**
   * Returns the comment.
   * 
   * @return The comments.
   */
  public String getComments()
  {
    return com.getText();
  }

  /**
   * Returns the copyright.
   * 
   * @return The copyright.
   */
  public String getCopyright()
  {
    return copy.getText();
  }
}
