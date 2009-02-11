package apes;

import java.util.List;

/**
 * Describes the audio in a format suitable for internal representation in the program.
 * @author Daniel Kvick (kvick@student.chalmers.se)
 *
 */
public class InternalFormat 
{
	
	/**
	 * Information of all sound file tags.
	 */
	private Tag tags;
	
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
	 * @param tag Tag information of the audio file.
	 * @param channels Channels containing all audio data.
	 * @param samplerate Amount of samples per second.
	 */
	public InternalFormat(Tag tag, List<Channel> channelList, int samplerate)
	{
		channels = channelList;
	}
	
	/**
	 * The tag object describing all the tags of the audio file.
	 * @return All tags of the audio file as a Tag object.
	 */
	public Tag getTag()
	{
		return tag;
	}
	
	/**
	 * Returns the number of channels.
	 * @return The number of channels.
	 */
	public int getNumChannels()
	{
		return channels.size();
	}
	
	/**
	 * Returns the channel with index channel.
	 * @return The channel of the chosen index.
	 */
	public Channel getChannel( int channel )
	{
		return channels.get( channel );
	}
	
	/**
	 * Returns a Samples object containing the specified interval.
	 * @param channel Index of the channel to fetch data from.
	 * @param start Start of interval in milliseconds.
	 * @param end End of interval in milliseconds.
	 * @return A Samples object containing all samples of the selected Channel in the specified interval.
	 */
	Samples getSamples( int channel, int start, int stop)
	{
	  return channels.get( channel ).getSamples( start*1000*Channel.SAMPLES_SIZE, stop*1000*Channel.SAMPLES_SIZE );
	}
	
	/**
   * Inserts samples at the selected position.
   * @param start Position of insertion in milliseconds.
   * @param samples Sample data to insert into channel.
   */
	public void setSamples( int channel, int start, Samples samples )
	{
	  channels.get( channel ).setSamples( start*1000*Channel.SAMPLES_SIZE, samples );
	}
	
	/**
   * Replaces selected interval with the specified samples.
   * @param start of the interval in milliseconds.
   * @param stop of interval in milliseconds.
   * @param samples An object containing all sample data to replace selection with.
   */
	public void setSamples( int channel, int start, int stop, Samples samples )
	{
	  channels.get( channel ).setSamples( start*1000*Channel.SAMPLES_SIZE, stop*1000*Channel.SAMPLES_SIZE, samples );
	}
	
	/**
	 * Returns the sample rate, that is, how many samples make up one second.
	 * @return Returns the sample rate.
	 */
	public int getSampleRate()
	{
		return sampleRate;
	}
	
	/**
	 * Describes a single channel of audio information.
	 * 
	 * @author Daniel Kvick (kvick@student.chalmers.se)
	 *
	 */
	public class Channel
	{
		/**
		 * Amount of samples in each Samples object in the samplesList.
		 */
		public static final int  SAMPLES_SIZE = 1000;
		
		/**
		 * A list containing all Samples structure, and thus all audio data of the channel. 
		 */
		private List<Samples> samplesList;
		
		/**
		 * Constructor which adds a list of samples to the Channel. 
		 * @param samples All audio data to be added to the Channel.
		 */
		public Channel( Samples samples )
		{
			// Go through all samples, divide into chunks of SAMPLES_SIZE.
		}
		
		/**
		 * Returns a Samples object containing all samples in the specified interval, inclusively. 
		 * @param start Index of the first sample to return.
		 * @param stop Index of the last sample to return.
		 * @return A Samples object with all samples from sample start until sample stop.
		 */
		public Samples getSamples( int start, int stop )
		{
			// Create new Samples object s
			// For all Samples objects to include entirely
				// Add samples to s
			// Add the remaining samples to s
		}
		
		/**
		 * Inserts samples at the selected position.
		 * @param start Position of insertion as sample index.
		 * @param samples Sample data to insert into channel.
		 */
		public void setSamples( int start, Samples samples )
		{
		  
		}
		
		/**
		 * Replaces selected interval with the specified samples.
		 * @param start Index of the first sample to replace.
		 * @param stop Index of the last sample to replace.
		 * @param samples An object containing all sample data to replace selection with.
		 */
		public void setSamples( int start, int stop, Samples samples )
    {
      
    }
		
	}
}
