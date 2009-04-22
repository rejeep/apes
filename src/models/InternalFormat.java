package apes.models;

import apes.lib.FileHandler;
import java.util.List;
import java.io.IOException;

/**
 * Describes the audio in a format suitable for internal representation in the program.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class InternalFormat
{
  /**
   * Information about where the file were saved to or loaded from.
   */
  private FileStatus fileStatus;
  
  /**
   * Information of all sound file tags.
   */
  private Tags tags;

  /**
   * Amount of samples per second.
   */
  private int sampleRate;

  /**
   * List containing all channels of the sound file.
   */
  private List<Channel> channels;

  /**
   * Constructor setting up the Internal Format according to the supplied data.
   *
   * @param tag         Tag information of the audio file.
   * @param samplerate  Amount of samples per second.
   * @param channelList Channels containing all audio data.
   */
  public InternalFormat( Tags tag, int samplerate, List<Channel> channelList )
  {
    tags = tag;
    sampleRate = samplerate;
    channels = channelList;
  }

  /**
   * The tag object describing all the tags of the audio file.
   *
   * @return All tags of the audio file as a Tag object.
   */
  public Tags getTags()
  {
    return tags;
  }
  
  /**
   * Sets the tags object for this IF.
   *
   * @param tags The tags object.
   */
  public void setTags( Tags tags )
  {
    this.tags = tags;
  }
  
  /**
   * Returns the number of channels.
   *
   * @return The number of channels.
   */
  public int getNumChannels()
  {
    return channels.size();
  }

  /**
   * Returns the channel with index channel.
   *
   * @return The channel of the chosen index.
   */
  public Channel getChannel( int channel )
  {
    return channels.get( channel );
  }

  /**
   * Returns a Samples object containing the specified interval.
   *
   * @param channel Index of the channel to fetch data from.
   * @param start   Start of interval in milliseconds.
   * @param stop    End of interval in milliseconds.
   * @return A Samples object containing all samples of the selected Channel in the specified interval.
   */
  Samples getSamples( int channel, int start, int stop )
  {
    return null;
  }

  /**
   * Inserts samples at the selected position.
   *
   * @param start   Position of insertion as sample index.
   * @param samples Sample data to insert into channel.
   */
  public void setSamples( int channel, int start, Samples samples )
  {
    channels.get( channel ).setSamples( start, samples );
    // Do something about differing length of channels.
  }

  /**
   * Replaces selected interval with the specified samples.
   *
   * @param start   of the interval as sample index.
   * @param stop    of interval as sample index.
   * @param samples An object containing all sample data to replace selection with.
   */
  public void setSamples( int channel, int start, int stop, Samples samples ) throws IndexOutOfBoundsException
  {
    channels.get( channel ).setSamples( start, stop, samples );
    // Do something about differing length of channels.
  }

  /**
   * Returns the sample rate, that is, how many samples make up one second.
   *
   * @return Returns the sample rate.
   */
  public int getSampleRate()
  {
    return sampleRate;
  }

  /**
   * Save file as
   * TODO: add error handling, or some sort of response
   */
  /**
   * Saves the internal format to the specifed location with the specified name.
   * @param filePath The location the file should be saved to.
   * @param fileName The name of the file to be stored.
   */
  public void saveAs(String filePath, String fileName) throws IOException
  {    
    FileHandler.saveObjectToFile( filePath, fileName, this );      
  }

  /**
   * Save file
   * TODO: add error handling or some sort of response
   */
  public void save() throws IOException
  {
    FileHandler.saveObjectToFile( fileStatus.getFilepath(), fileStatus.getFileName(), this);
  }

  /*
  public void savePart()
  {
    //TODO: IMPLEMENT    
  }
  */

  /**
   * Get the <code>FileStatus</code>.
   *
   * @return The <code>FileStatus</code>.
   */
  public FileStatus getFileStatus()
  {
    return fileStatus;
  }

  /**
   * Set a <code>FileStatus</code>.
   *
   * @param newFileStatus The new FileStatus.
   */
  public void setFileStatus( FileStatus fileStatus )
  {
    this.fileStatus = fileStatus;
  }
  
  /**
   * Load file
   * @param filePath Where the file is located.
   * @param fileName The name of the file.
   * @return
   */
  public static InternalFormat load(String filePath, String fileName) throws IOException, ClassNotFoundException
  {
    // TODO: add error handling or some sort of response
    return (InternalFormat) FileHandler.loadObjectFile( filePath, fileName );
  }
  
  /**
   * Returns the amount of time needed to play all samples in the given object, assuming the samplerate of <code>this</code>.
   * @param s The samples to be checked.
   * @return The time needed to play all samples in <code>s</code> given the samplerate of <code>this</code>, in milliseconds.
   */
  public int getPlayTime( Samples s )
  {
    return (Samples.MS_PER_SECOND * s.getSize()) / sampleRate;
  }
}
