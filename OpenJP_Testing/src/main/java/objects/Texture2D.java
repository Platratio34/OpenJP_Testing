package objects;

import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.opengl.GL45;

import oldObjects.Triangle;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

/**
 * 2D texture for rendering
 */
public class Texture2D {
	
	private int textureId;
	private long handle;
	
	private Color[] pixels;
	private int width;
	private int height;

	private boolean resident = false;;
	private boolean genMipMaps = false;
	
	/**
	 * Create a new white texture of size.<br>
	 * <br>
	 * Width and height should be multiples of 2.
	 * @param w width
	 * @param h height
	 */
	public Texture2D(int w, int h) {
		//		System.out.println("Created texture with ID "+textureId);
		width = w;
		height = h;
		pixels = new Color[w * h];
		fill(Color.white);

		initGLTexture();
	}
	/**
	 * Create a new texture of size.<br>
	 * <br>
	 * Width and height should be multiples of 2.
	 * @param w width
	 * @param h height
	 * @param pixels pixel colors (ordered x, y)
	 */
	public Texture2D(int w, int h, Color[] pixels) {
		//		System.out.println("Created texture with ID "+textureId);
		width = w;
		height = h;
		this.pixels = pixels;
		
		initGLTexture();
	}
	
	private void initGLTexture() {
		textureId = GL45.glCreateTextures(GL45.GL_TEXTURE_2D);
		GL45.glTextureStorage2D(textureId, 1, GL45.GL_RGBA8, width, height);
		updateTexture();
		// GL45.glTextureSubImage2D(textureId, 0, 0, 0, width, height, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, toByteBuffer());
		// GL45.glGenerateTextureMipmap(textureId);
		GL45.glTextureParameteri(textureId, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		GL45.glTextureParameteri(textureId, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
		// GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, width, height, 0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, buff);

		handle = ARBBindlessTexture.glGetTextureHandleARB(textureId);
		if (handle == 0) {
			System.err.println("Could not get texture handle");
		}
	}
	
	/**
	 * Set the color of a given pixel
	 * @param x x position of pixel
	 * @param y y position of pixel
	 * @param color pixel color
	 */
	public void setPixel(int x, int y, Color color) {
		int i = x + (y*width);
		pixels[i] = color;
	}
	/**
	 * Get the color of a given pixel
	 * @param x x position of pixel
	 * @param y y position of pixel
	 * @return pixel color
	 */
	public Color getPixel(int x, int y) {
		int i = x + (y*width);
		return pixels[i];
	}
	
	/**
	 * Fill all pixels with given color
	 * @param color fill color
	 */
	public void fill(Color color) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	/**
	 * Get the width of the texture
	 * @return pixel width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Get the height of the texture
	 * @return pixel height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Convert the texture to a byte buffer
	 * @return byte buffer of color data
	 */
	public ByteBuffer toByteBuffer() {
		ByteBuffer buff = ByteBuffer.allocateDirect(4*width*height);
		for(int i = 0; i < pixels.length; i++) {
			Color c = pixels[i];
			// System.out.println(c);
			buff.put((byte)c.getRed());
			buff.put((byte)c.getGreen());
			buff.put((byte)c.getBlue());
			buff.put((byte)c.getAlpha());
		}
		buff.flip();
		return buff;
	}
	
	/**
	 * Update the texture in OpenGL
	 */
	public void updateTexture() {
		// bind();
		GL45.glTextureSubImage2D(textureId, 0, 0, 0, width, height, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, toByteBuffer());
		if (genMipMaps)
			GL45.glGenerateTextureMipmap(textureId);
		// System.out.println(buff);
		// GL45.glPixelStorei(GL45.GL_UNPACK_ALIGNMENT, 1);
		// GL45.glGenerateMipmap(GL45.GL_TEXTURE_2D);
		// unbind();
	}
	
	/**
	 * Bind the texture to texture unit
	 * @param index texture unit index (<code>GL*.GL_TEXTURE*</code>)
	 */
	public void activateBind(int index) {
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, textureId);
		GL45.glActiveTexture(GL45.GL_TEXTURE0+index);
	}
	
	/**
	 * Get the OpenGL texture ID
	 * @return texture ID
	 */
	public int getId() {
		return textureId;
	}

	/**
	 * Get bindless texture handle
	 * @return texture handle
	 */
	public long getHandle() {
		return handle;
	}

	/**
	 * Make bindless texture resident
	 */
	public void makeResident() {
		if (resident)
			return;
		ARBBindlessTexture.glMakeTextureHandleResidentARB(handle);
		resident = true;
	}
	/**
	 * Make bindless texture non-resident
	 */
	public void makeNonResident() {
		if (!resident)
			return;
		ARBBindlessTexture.glMakeTextureHandleNonResidentARB(handle);
		resident = false;
	}
	
	/**
	 * Load a texture from .png resource file
	 * @param path resource path
	 * @return Texture from image file
	 * @throws IOException if the file could not be found or read
	 */
	public static Texture2D loadFromPngResource(String path) throws IOException {
		ClassLoader classLoader = Triangle.class.getClassLoader();
		InputStream iS = classLoader.getResourceAsStream(path);
		if (iS == null) {
			throw new IOException("Could not load rescues \""+path+"\"");
		}
		BufferedImage bi = ImageIO.read(iS);
		// int[] data = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
		int w = bi.getWidth();
		int h = bi.getHeight();
		Color[] pixels = new Color[w * h];
		for(int i = 0; i < w*h; i++) {
			// int i = x + (y*width);
			int x = i % w;
			int y = (int)(i / w);
			pixels[i] = new Color(bi.getRGB(x, y));
		}
		return new Texture2D(w,h,pixels);
	}
}
