package apes.plugins;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import apes.interfaces.AudioFormatPlugin;
import apes.lib.FileHandler;
import apes.views.ProgressView;
import apes.models.FileStatus;
import apes.models.InternalFormat;
import apes.models.Tags;

/**
 * Module used for converting .wav-files to the internal format and
 * converting the internal format to .wav-files.
 *
 * @author Simon Holm
 */
public class WaveFileFormat implements AudioFormatPlugin
{

  /**
   * Magic number of samples to read and write.
   */
  private final static int IO_CHUNK_SIZE = 100000;
  /**
   * TODO: Comment
   */
  public String getName()
  {
    return "Wave";
  }

  /**
   * TODO: Comment
   */
  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "Support for .wav files.");
    map.put("sv", "Stöd för .wav filer.");
    return map;
  }

  /**
   * Returns the extension of the file format
   *
   * @return extension
   */
  public String getExtension()
  {
    return "wav";
  }
  
  /**
   * Converts a file from the internal file format to .wav and stores
   * it on the disk
   *
   * @param internalFormat The file to be converted.
   * @param path The path to the folder were the file should be saved.
   * @param fileName The name of the file to be saved.
   * @throws Exception
   */
  public void exportFile( InternalFormat internalFormat, String path, String name ) throws IOException
  {
    exportFile(internalFormat, new File(path, name) ); 
  }

  /**
   * Converts a file from the internal file format to .wav and stores
   * it on the disk
   *
   * @param internalFormat The file to be converted.
   * @param file File to write to
   * @throws Exception
   */
  public void exportFile( InternalFormat internalFormat, File file ) throws IOException
  {
    exportFile( internalFormat, file, 0, internalFormat.getSampleAmount());
  }
  
  /**
   * 
   * @param internalFormat The file to be converted.
   * @param file File to write to
   * @param start Start of interval to copy from in internal format in samples
   * @param stop  End of interval to copy from in internal format in samples
   * @throws Exception
   */
  public void exportFile( InternalFormat internalFormat, File file, long startS, long stopS ) throws IOException
  {
    System.out.println("WAnt to export " + file + " start " + startS + " stop " + stopS);
    ByteBuffer data; // contians data to be exported

    //TODO; Add better support for different headers

    byte[] chunkID       = {'R','I','F','F'};
    int    chunkSize;
    byte[] format        = {'W','A','V','E'};
    int    subchunk1ID   = 0x666d7420; // fmt
    int    subchunk1Size = 16;
    short  audioFormat   = 1;
    short  numChannels   = (short)internalFormat.getNumChannels();
    int    sampleRate    = internalFormat.getSampleRate();
    int    byteRate      = sampleRate * numChannels * (internalFormat.bytesPerSample);
    short  blockAlign    = (short)(numChannels * (internalFormat.bytesPerSample));
    short  bitsPerSample = (short)internalFormat.bitsPerSample;
    byte[] subchunk2ID   = {'d','a','t','a'};
    int    subchunk2Size;

    long numSamples = stopS-startS;

    subchunk2Size = (int)(numSamples * numChannels * internalFormat.bytesPerSample);
    chunkSize = 4+(8+subchunk1Size)+(8+subchunk2Size);
    data = ByteBuffer.wrap( new byte[44] );

    // Start copy data

    data.order( ByteOrder.BIG_ENDIAN );
    data.put( chunkID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( chunkSize );
    data.order( ByteOrder.BIG_ENDIAN );
    data.put( format );
    data.putInt( subchunk1ID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( subchunk1Size );
    data.putShort( audioFormat );
    data.putShort( numChannels );
    data.putInt( sampleRate );
    data.putInt( byteRate );
    data.putShort( blockAlign );
    data.putShort( bitsPerSample );
    data.order( ByteOrder.BIG_ENDIAN );
    data.put( subchunk2ID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( subchunk2Size );

    FileOutputStream fStream = new FileOutputStream( file );

    fStream.write( data.array() );
    int written = 0;

    while( written < numSamples )
    while( written < numSamples)
    {
      byte[] bytes = internalFormat.getChunk( startS + written, IO_CHUNK_SIZE );
      if( bytes == null )
        bytes = internalFormat.getChunk( startS + written, (int)(numSamples - written) );
      written += IO_CHUNK_SIZE;
      fStream.write(bytes);
      System.out.println("Writing to "+ file.getName());
    }
    fStream.close();
  }

  //TODO: Create a more detailed description of exception
  /**
   * Imports a wave file, converts it to the internal format and
   * returns it.
   *
   * @param path The path to the folder with the file to be loaded.
   * @param filename The name of the file to be imported.
   * @return Returns the file converted to the internal format
   * @throws Exception Will throw an exception if something bad
   * happens
   */
  // TODO: Rewrite
  public InternalFormat importFile( String path, String filename ) throws IOException
  {
    ProgressView progress = ProgressView.getInstance();

    //ByteBuffer buffer = FileHandler.loadFile( path, filename );
    //buffer.order( ByteOrder.LITTLE_ENDIAN );

    File file = new File( path, filename );
    FileInputStream fStream = new FileInputStream( file );
    DataInputStream dStream = new DataInputStream( fStream );

    // Wave do not contain any tags
    Tags tag = null;
    dStream.skip( 22 );

    // 2 little
    int numChannels = bigToLittleEndian(dStream.readShort());

    // 4 little
    int sampleRate = bigToLittleEndian(dStream.readInt());

    dStream.skip(6);

    // 2 little
    // TODO: Dangerous => Should be used!
    int bitsPerSample = bigToLittleEndian(dStream.readShort());
    if(bitsPerSample != 16)
    {
      System.out.println("STUPID PROGGRAMMER WAS HERE(WaveFileFormat)");
      System.exit(1);

    }

    dStream.skip( 4 );

    // 4 little
    int subChunk2Size = bigToLittleEndian(dStream.readInt());

    InternalFormat internalFormat = new InternalFormat( tag, sampleRate, numChannels );
    InternalFormat internalFormat = new InternalFormat( tag, sampleRate, numChannels, bitsPerSample );
    internalFormat.setFileStatus( new FileStatus( path, filename ) );

    int written = 0;
    byte b[] = new byte[IO_CHUNK_SIZE];

    progress.setMaximum(subChunk2Size);
    while( written < subChunk2Size )
    {
      progress.setValue(written);
      
      int read = dStream.read(b);
      if( read < IO_CHUNK_SIZE )
      {
        byte[] bTemp = new byte[read];
        System.arraycopy( b, 0, bTemp, 0, read );
        b = bTemp;
      }

      internalFormat.insertSamples( written, b );
      written += b.length;
    }
    progress.reset();
    
    dStream.close();
    return internalFormat;
  }

  private static int bigToLittleEndian(int bigendian)
  {
    ByteBuffer buf = ByteBuffer.allocate(4);

    buf.order(ByteOrder.BIG_ENDIAN);
    buf.putInt(bigendian);

    buf.order(ByteOrder.LITTLE_ENDIAN);
    return buf.getInt(0);
  }

  private static int bigToLittleEndian(short bigendian)
  {
    ByteBuffer buf = ByteBuffer.allocate(2);

    buf.order(ByteOrder.BIG_ENDIAN);
    buf.putShort(bigendian);

    buf.order(ByteOrder.LITTLE_ENDIAN);
    return buf.getShort(0);
  }
}
