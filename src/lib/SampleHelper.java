/**
 * TODO: Comment
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class SampleHelper
{
  public static int millisecondsToSamples( int sampleRate, int milliseconds )
  {
    return ( milliseconds / 1000 ) * sampleRate;
  }

  public static int secondsToSamples( int sampleRate, int seconds )
  {
    return seconds * sampleRate;
  }

  public static int minutesToSamples( int sampleRate, int minutes )
  {
    return ( minutes * 60 ) * sampleRate;
  }

  public static int samplesToMilliseconds( int sampleRate, int samples )
  {
    return ( samples / sampleRate ) * 1000;
  }

  public static int samplesToseconds( int sampleRate, int samples )
  {
    return samples / sampleRate;
  }

  public static int samplesToMinutes( int sampleRate, int samples )
  {
    return ( samples / sampleRate ) / 60;
  }
}