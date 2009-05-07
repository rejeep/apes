package apes.models;

import java.awt.Point;
import java.io.IOException;
import java.util.List;

import apes.lib.FileHandler;
import java.util.Observable;

/**
 * Describes the audio in a format suitable for internal
 * representation in the program.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class InternalFormat extends Observable
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
   * Length of each channel in samples.
   */
  int sampleAmount;

  /**
   * Constructor setting up the Internal Format according to the
   * supplied data.
   *
   * @param tags        Tag information of the audio file.
   * @param samplerate  Amount of samples per second.
   * @param channelList Channels containing all audio data.
   */
  public InternalFormat( Tags tags, int samplerate, List<Channel> channelList )
  {
    if(tags == null)
    {
      this.tags = new Tags();
    }
    else
    {
      this.tags = tags;
    }
    sampleRate = samplerate;
    channels = channelList;
    
    // Assume equal length of channels. NullPointerException if incorrect or no channels.
    Channel c = channels.get(0);
    
    int sampleCount = 0;
    for( int i = 0; i < c.getSamplesSize(); i++ )
      sampleCount += c.getSamples( i ).getSize();
    sampleAmount = sampleCount;
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
   * Returns the number of samples in each channel.
   * @return <code>sampleAmount</code>.
   */
  public int getSampleAmount()
  {
    return sampleAmount;
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
   * @return A Samples object containing all samples of the selected
   * Channel in the specified interval.
   */
  Samples getSamples( int channel, int start, int stop )
  {
    return null;
  }

  /**
   * Returns the sample rate, that is, how many samples make up one
   * second.
   *
   * @return Returns the sample rate.
   */
  public int getSampleRate()
  {
    return sampleRate;
  }

  /**
   * Returns a chunk of data in PCM format. The chunk contains <code>amount</code> samples from each channel starting from absolute index <code>index</code>. 
   * @param index The first sample to include in the chunk, as an absolute index.
   * @param amount Amount of samples in the chunk. If there are fewer than <code>amount</code> samples after <code>index</code>, all remaining samples are included.
   * @return A byte array containing the requested data.
   */
  public byte[] getChunk( int index, int amount )
  {
    // Get memory.
    if( index + amount > getSampleAmount() )
      amount = getSampleAmount() - index;
    byte[] retChunk = new byte[ channels.size() * amount * Samples.BYTES_PER_SAMPLE ];
    
    SampleIterator[] sampIts = new SampleIterator[channels.size()];
    for( int i = 0; i < channels.size(); i++ )
    {
      Channel c = channels.get(i);
      Point point = c.findAbsoluteIndex( index );
      sampIts[i] = c.getIteratorFromIndex( point.x, point.y );
    }
      
    for( int j = 0; j < amount; j++ )
    {
      for( int i = 0; i < channels.size(); i++ )
      {
        int value = sampIts[i].next();
        for( int k = 0; k < Samples.BYTES_PER_SAMPLE; k++ )
        {
          retChunk[i * amount * Samples.BYTES_PER_SAMPLE + j * Samples.BYTES_PER_SAMPLE + k] = (byte)((value >> (k * 8)) & 0xff);
        }
      }
    }
    
    return retChunk;
  }
  /**
   * Save file as
   * TODO: add error handling, or some sort of response
   */
  /**
   * Saves the internal format to the specifed location with the
   * specified name.
   *
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
   * @param fileStatus The new FileStatus.
   */
  public void setFileStatus( FileStatus fileStatus )
  {
    this.fileStatus = fileStatus;
  }

  /**
   * Load file
   *
   * @param filePath Where the file is located.
   * @param fileName The name of the file.
   *
   * @return Returns an internal format.
   */
  public static InternalFormat load(String filePath, String fileName) throws IOException, ClassNotFoundException
  {
    // TODO: add error handling or some sort of response
    return (InternalFormat) FileHandler.loadObjectFile( filePath, fileName );
  }

  /**
   * Returns the amount of time needed to play all samples in the
   * given object, assuming the samplerate of <code>this</code>.
   *
   * @param s The samples to be checked.
   * @return The time needed to play all samples in <code>s</code>
   * given the samplerate of <code>this</code>, in milliseconds.
   */
  public int getPlayTime( Samples s )
  {
    return (Samples.MS_PER_SECOND * s.getSize()) / sampleRate;
  }
  
  /**
   * Removes the samples in the specified interval from the selected channel and returns a Samples[] containing Samples containing these samples. Ensures that all Channels retain equal length.
   * @param c The Channel to cut from.
   * @param start First sample to cut away.
   * @param stop Last sample to cut away.
   * @return An array containing Samples objects of the data removed from the selected channel.
   */
  public Samples[] cutSamples( Channel c, int start, int stop )
  {
    if( !channels.contains( c ) )
      return null;
    
    Samples[] samples = c.cutSamples( start, stop );
    
    // Fix length.
    c.adjustPadding( stop - start + 1 );
    
    return samples;
  }
  
  /**
   * Inserts the provided samples at the specified index. Ensures that all Channels retain equal length.
   * @param c Channel to insert into.
   * @param start Index to insert at.
   * @param samples Samples to insert at start.
   * @return Index of the first sample after the inserted samples.
   */
  public int pasteSamples( Channel c, int start, Samples[] samples )
  {
    if( !channels.contains( c ) )
      return -1;
    
    int retVal = c.pasteSamples( start, samples );
    
    // fix length.
    int addedLength = retVal - start;
    int newPadding = c.adjustPadding( -addedLength );
   
    // This channel too long, pad all others.
    if( newPadding < 0 )
    {
      newPadding *= -1;
      for( Channel channel : channels )
        if( channel != c )
          channel.adjustPadding( newPadding );
    }
    
    return retVal;
  }
}
