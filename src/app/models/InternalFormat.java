package apes;

import java.util.*;

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
	 * @param tag Tag information of the audio file.
	 * @param channels Channels containing all audio data.
	 * @param samplerate Amount of samples per second.
	 */
	InternalFormat(Tag tag, Channel[] channels, int samplerate)
	{
		
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
	public Channel getChannel(int channel)
	{
		return channels.get(channel);
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
		public final int  SAMPLES_SIZE = 1000;
		
		/**
		 * A list containing all Samples structure, and thus all audio data of the channel. 
		 */
		private List<Samples> samplesList;
	}
}
