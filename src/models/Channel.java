package apes.models;

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
    // Divide samples into chunks of SAMPLES_SIZE.
    for(int i = 0; i < samples.getSize()/SAMPLES_SIZE; i++)
    {
      Samples newSamp = new Samples(Samples.BITS_PER_SAMPLE, SAMPLES_SIZE);
      for(int j = 0; j < SAMPLES_SIZE; j++)
      {

      }
      samplesList.add(newSamp);
    }
  }

  /**
   * Returns a Samples object containing all samples in the specified interval, inclusively.
   *
   * @param start Index of the first sample to return.
   * @param stop  Index of the last sample to return.
   * @return A Samples object with all samples from sample start until sample stop.

   */
  public Samples getSamples( int start, int stop ) throws Exception
  {
    if( start < 0 || stop < 0 || start > stop || stop >= SAMPLES_SIZE * samplesList.size() )
      throw new Exception("Invalid interval");
    int first = start / SAMPLES_SIZE;
    int last = stop / SAMPLES_SIZE;
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
  public void setSamples( int start, int stop, Samples samples ) throws Exception
  { 
    if( start < 0 || stop < 0 || start > stop || stop >= SAMPLES_SIZE * samplesList.size() )
      throw new Exception( "Invalid interval" );
    
    // First and last Samples objects to edit.
    int first = start / SAMPLES_SIZE;
    int last = stop / SAMPLES_SIZE;
    
    int curSamp = 0;
    // Only one Samples object to edit:
    if(first == last)
    {
      setSamplesObject(first, start % SAMPLES_SIZE, SAMPLES_SIZE, curSamp, samples);
      return;
    }
    
    // Edit first Samples object
    setSamplesObject(first, start % SAMPLES_SIZE, SAMPLES_SIZE, curSamp, samples);
    curSamp += start % SAMPLES_SIZE;
    
    // Edit all in-between first and last Samples objects.
    if(first+1 < last)
    {
      setSamplesObjects( first + 1, last - 1, curSamp, samples);
      curSamp += SAMPLES_SIZE;
    }
      
     // Edit last Samples object.
     setSamplesObject(last, 0, stop % SAMPLES_SIZE, curSamp, samples);
      
  }
  
  /**
   * Replaces a range of samples within the selected Samples object.
   * @param sample Index of the Samples object to edit.
   * @param start Index of the first sample to set.
   * @param stop Index of the last sample to set.
   * @param samples The data to use.

   *            samples contain insufficient amount of data.
   */
  private void setSamplesObject( int sample, int start, int stop, int firstSamp, Samples samples ) throws Exception
  {
  }
  
  /**
   * Replaces all samples of Samples objects in the specified range with the specified data.
   * @param first The first samples object to set.
   * @param last The last samples object to set.
   * @param firstSamp Where to start fetching samples from.
   * @param samples Samples object containing data to write.
    
   *          there are too few samples in the <code>samples</code> after <code>firstSamp</code>
   */
  private void setSamplesObjects( int first, int last, int firstSamp, Samples samples ) throws Exception
  {
    // Error checking.
    if( last < first || last < 0 || first < 0 )
      throw new Exception("Bad interval");
    if( firstSamp < 0 || samples.getSize() - firstSamp < (last-first) * SAMPLES_SIZE )
      throw new Exception("bad firstSamp index or samples object size");
      
    // Set data
    int curSamp = firstSamp;
    for(int i = first; i <= last; i++)
    {
      Samples s = samplesList.get(i);
      for(int j = 0; j < SAMPLES_SIZE; j++)
      s.setSample(j, samples.getSample(curSamp++));
    }
  }

}