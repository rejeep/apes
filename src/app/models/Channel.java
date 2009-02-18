package src.app.models;

import java.util.List;

/**
 * Describes a single channel of audio information.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class Channel
{
  /**
   * Amount of samples in each Samples object in the samplesList.
   */
  public static final int SAMPLES_SIZE = 1000;

  /**
   * A list containing all Samples structure, and thus all audio data of the channel.
   */
  private List<Samples> samplesList;

  /**
   * Constructor which adds a list of samples to the Channel.
   *
   * @param samples All audio data to be added to the Channel.
   */
  public Channel( Samples samples )
  {
    // Go through all samples, divide into chunks of SAMPLES_SIZE.
  }

  /**
   * Returns a Samples object containing all samples in the specified interval, inclusively.
   *
   * @param start Index of the first sample to return.
   * @param stop  Index of the last sample to return.
   * @return A Samples object with all samples from sample start until sample stop.
   */
  public Samples getSamples( int start, int stop )
  {
    // Create new Samples object s
    // For all Samples objects to include entirely
    // Add samples to s
    // Add the remaining samples to s
    return null;
  }

  /**
   * Inserts samples at the selected position.
   *
   * @param start   Position of insertion as sample index.
   * @param samples Sample data to insert into channel.
   */
  public void setSamples( int start, Samples samples )
  {

  }

  /**
   * Replaces selected interval with the specified samples.
   *
   * @param start   Index of the first sample to replace.
   * @param stop    Index of the last sample to replace.
   * @param samples An object containing all sample data to replace selection with.
   */
  public void setSamples( int start, int stop, Samples samples )
  {

  }

}