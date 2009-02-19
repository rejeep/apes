package apes.plugins;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import apes.interfaces.AudioFormatPlugin;
import apes.lib.FileHandler;
import apes.models.Channel;
import apes.models.InternalFormat;
import apes.models.Samples;
import apes.models.Tags;

/**
 * Module used for converting .wav-files to the internal format
 * and converting the internal format to .wav-files.
 *
 * @author Simon Holm
 */
public class WaveFileFormat implements AudioFormatPlugin
{

  /**
   * UNINPMEMENTED
   * Converts a file from the internal file format to .wav and stores it on the disk
   *
   * @param internalFormat The file to be converted.
   * @param path           The path to the folder were the file should be saved.
   * @param filename       The name of the file to be saved.
   * @throws Exception
   */
  public void exportFile( InternalFormat internalFormat, String path, String filename ) throws Exception
  {
    //TODO add export capability
    throw new Exception( "Not implemeted yet." );
  }

  // Create a more detailed description of exception
  /**
   * Imports a wave file, converts it to the internal format and returns it.
   *
   * @param path     The path to the folder with the file to be loaded.
   * @param filename The name of the file to be imported.
   * @return Returns the file converted to the internal format
   * @throws Exception Will throw an exception if something bad happens
   */
  public InternalFormat importFile( String path, String filename ) throws Exception
  {
    ByteBuffer buffer = FileHandler.loadFile( path, filename );

    // Wave do not contain any tags
    Tags tag = null;

    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    buffer.getInt();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    buffer.getInt();
    // 2 little
    buffer.getShort();
    // 2 little
    int numChannels = buffer.getShort();
    // 4 little
    int sampleRate = buffer.getInt();
    // 4 little
    buffer.getInt();
    // 2 little
    buffer.getShort();
    // 2 little
    int bitsPerSample = buffer.getShort();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    int subChunk2Size = buffer.getInt();
    // little
    byte[] samples = new byte[subChunk2Size];

    buffer.get( samples, 0, subChunk2Size );

    //TODO remove printing
    //TODO make compatible with internal format

    System.out.println( "Tags: " + tag );
    System.out.println( "Number of channels: " + numChannels );
    System.out.println( "Sample rate: " + sampleRate );
    System.out.println( "Bits per sample: " + bitsPerSample );
    System.out.println( "Samples: " + samples.length );

    // tag sampleRate channels[]
    //list with channels
    List<Channel> channels = new ArrayList<Channel>();

    //TODO Add support for mor than one channel
    if ( numChannels > 1 )
    {
      System.exit( 1 );
    }

    for ( int i = 0; i < numChannels; ++i )
    {
      channels.add( new Channel( new Samples( bitsPerSample, samples ) ) );
    }

    return new InternalFormat( tag, sampleRate, channels );
  }

}
