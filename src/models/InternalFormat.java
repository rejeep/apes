package apes.models;

import java.util.List;

/**
 * Describes the audio in a format suitable for internal representation in the program.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class InternalFormat
{

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
  public Tags getTag()
  {
    return tags;
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
   * @param start   Position of insertion in milliseconds.
   * @param samples Sample data to insert into channel.
   */
  public void setSamples( int channel, int start, Samples samples )
  {
    channels.get( channel ).setSamples( start * 1000 * Channel.SAMPLES_SIZE, samples );
  }

  /**
   * Replaces selected interval with the specified samples.
   *
   * @param start   of the interval in milliseconds.
   * @param stop    of interval in milliseconds.
   * @param samples An object containing all sample data to replace selection with.
   */
  public void setSamples( int channel, int start, int stop, Samples samples )
  {
    
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


}
