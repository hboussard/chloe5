package fr.inra.sad.bagap.aparapi.buffer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.io.*;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class Buffer {

	public static void main(final String[] _args) throws IOException {
		final File file;
		try {
			
			System.out.println("buffer");
			
			file = new File(Buffer.class.getResource("/bretagne.tif").toURI());
			
			

			ImageInputStream stream = ImageIO.createImageInputStream(file);

			// File or input stream
			ImageReader reader = ImageIO.getImageReaders(stream).next();
			reader.setInput(stream);

			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle sourceRegion = new Rectangle(10000, 10000, 1000, 1000); 
			param.setSourceRegion(sourceRegion); // Set region

			BufferedImage inputImage = reader.read(0, param); // Will read only the region specified

			System.out.println(inputImage);

			int height = inputImage.getHeight();
			int width = inputImage.getWidth();

			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);

			short[] inBytes = ((DataBufferShort) inputImage.getRaster().getDataBuffer()).getData();
			short[] outBytes = ((DataBufferUShort) outputImage.getRaster().getDataBuffer()).getData();
			
			
			new ImageBuffer(11).applySlidingWindow(inBytes, outBytes, width, height, 51);	
			File outputfile = new File("C:/Hugues/temp/image1.tif");
			ImageIO.write(outputImage, "tif", outputfile);
			
			new ImageBuffer(7).applySlidingWindow(inBytes, outBytes, width, height, 51);	
			outputfile = new File("C:/Hugues/temp/image2.tif");
			ImageIO.write(outputImage, "tif", outputfile);
			
			new ImageBuffer(5).applySlidingWindow(inBytes, outBytes, width, height, 51);	
			outputfile = new File("C:/Hugues/temp/image3.tif");
			ImageIO.write(outputImage, "tif", outputfile);

			
			//new ImageBuffer().applySlidingWindow(inBytes, outBytes, width, height);	

		} catch (URISyntaxException e) {
			throw new IllegalStateException("could not get testcard", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
	}

	final static class ImageBuffer {

		private int width, height;

		private short imageIn[], imageOut[];
		
		private int windowSize;

		private int value;
		
		public ImageBuffer(int value){
			this.value = value;
		}
		
		public void processPixel(int x, int y, int w, int h) {

			int count = 0;
			int mid = windowSize / 2;
			for (int dx = -mid; dx <= mid; dx += 1) {
				if(((x + dx) >= 0) && ((x + dx) < width)){
					for (int dy = -mid; dy <= mid; dy += 1) {
						if(((y + dy) >= 0) && ((y + dy) < height)){
							final int v = imageIn[((y + dy) * w) + (x + dx)];
							if (v == value) {
								count++;
							}
						}
					}
				}
			}
			imageOut[(y * w) + x] = (short) count;

		}

		private void execute() {
			for (int y = 0; y < height - 2; y++) {
				for (int x = 0; x < width; x++) {
					run(x, y);
				}
			}
		}

		//private void export(){
        //	ImageIO...
        //}

		public void run(int x, int y) {
			// System.out.println(height+" "+width+" "+x+" "+y);
			// final int x = getGlobalId(0) % (width * 3);
			// final int y = getGlobalId(0) / (width * 3);

			if ((x > 3) && (x < ((width * 3) - 3)) && (y > 1) && (y < (height - 1))) {
				processPixel(x, y, width, height);
			}

		}

		public void applySlidingWindow(short[] _imageIn, short[] _imageOut, int _width, int _height, int _windowSize) {

			long begin = System.currentTimeMillis();
			imageIn = _imageIn;
			imageOut = _imageOut;
			width = _width;
			height = _height;
			windowSize = _windowSize;
			execute();
			long end = System.currentTimeMillis();
			System.out.println(end - begin + " " + _width + " " + _height);

			//export();
		}
	}
}
