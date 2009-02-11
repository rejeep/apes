import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class WaveFileFormat implements AudioFormatPlugin{

	public void exportFile(InternalFormat internalFormat, String filename) {
			
	}

	// Create a more detailed description of exception
	
	public InternalFormat importFile(String filename) throws IOException{
		ByteBuffer buffer = FileHandler.loadFile(filename);
		
		// Wave do not contain any tags
		Tag tag = null;
		
		// 4 big
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.getInt();
		// 4 little
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.getInt(); 
		// 4 big
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.getInt();
		// 4 big
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.getInt();
		// 4 little
		buffer.order(ByteOrder.LITTLE_ENDIAN);
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
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.getInt();
		// 4 little
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int subChunk2Size = buffer.getInt();
		// little
		byte[] samples = new byte[subChunk2Size];
		
		buffer.get(samples, 0, subChunk2Size);
		
		Samples data = new Samples(samples);
		
		System.out.println("Tags: " + tag);
		System.out.println("Number of channels: " + numChannels);
		System.out.println("Sample rate: " + sampleRate);
		System.out.println("Bits per sample: " + bitsPerSample);
		System.out.println("Samples: " + samples.length);		
		
		return new InternalFormat(tag, numChannels, sampleRate, bitsPerSample, data);
	}
}
