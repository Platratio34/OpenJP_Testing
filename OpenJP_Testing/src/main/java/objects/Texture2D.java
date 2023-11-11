package objects;

import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.opengl.GL45;
import java.awt.Color;
import java.nio.ByteBuffer;

public class Texture2D {
	
	private int textureId;
	private long handle;
	
	private Color[] pixels;
	private int width;
	private int height;
	
	public Texture2D(int w, int h) {
		textureId = GL45.glCreateTextures(GL45.GL_TEXTURE_2D);
//		System.out.println("Created texture with ID "+textureId);
		width = w;
		height = h;
		pixels = new Color[w*h];
		fill(Color.white);
		
		GL45.glTextureStorage2D(textureId, 1, GL45.GL_RGBA8, width, height);
		updateTexture();
		// GL45.glTextureSubImage2D(textureId, 0, 0, 0, width, height, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, toByteBuffer());
		// GL45.glGenerateTextureMipmap(textureId);
		// GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		// GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
		// GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, width, height, 0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, buff);

		handle = ARBBindlessTexture.glGetTextureHandleARB(textureId);
		if (handle == 0) {
			System.err.println("Could not get texture handle");
		}
		// updateTexture();
	}
	
	public void setPixel(int x, int y, Color color) {
		int i = x + (y*width);
		pixels[i] = color;
	}
	public Color getPixel(int x, int y) {
		int i = x + (y*width);
		return pixels[i];
	}
	
	public void fill(Color color) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
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
	
	public void updateTexture() {
		// bind();
		GL45.glTextureSubImage2D(textureId, 0, 0, 0, width, height, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, toByteBuffer());
		GL45.glGenerateTextureMipmap(textureId);
		// System.out.println(buff);
		// GL45.glPixelStorei(GL45.GL_UNPACK_ALIGNMENT, 1);
		// GL45.glGenerateMipmap(GL45.GL_TEXTURE_2D);
		// unbind();
	}
	public void bind() {
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, textureId);
	}
	public void unbind() {
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
	}
	public void activateBind(int index) {
		bind();
		GL45.glActiveTexture(GL45.GL_TEXTURE0+index);
	}
	
	public int getId() {
		return textureId;
	}

	public long getHandle() {
		return handle;
	}

	public void makeResident() {
		// if (ARBBindlessTexture.glIsImageHandleResidentARB(handle))
		// 	return;
		ARBBindlessTexture.glMakeTextureHandleResidentARB(handle);
	}
}
