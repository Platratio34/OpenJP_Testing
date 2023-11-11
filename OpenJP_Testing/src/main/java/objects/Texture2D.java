package objects;

import org.lwjgl.opengl.GL33;
import java.awt.Color;
import java.nio.ByteBuffer;

public class Texture2D {
	
	private int textureId;
	
	private Color[] pixels;
	private int width;
	private int height;
	
	public Texture2D(int w, int h) {
		textureId = GL33.glGenTextures();
//		System.out.println("Created texture with ID "+textureId);
		width = w;
		height = h;
		pixels = new Color[w*h];
		fill(Color.white);
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
	
	public void updateTexture() {
		bind();
		ByteBuffer buff = ByteBuffer.allocateDirect(4*width*height);
		for(int i = 0; i < pixels.length; i++) {
			Color c = pixels[i];
			buff.put((byte)c.getRed());
			buff.put((byte)c.getGreen());
			buff.put((byte)c.getBlue());
			buff.put((byte)c.getAlpha());
		}
		buff.flip();
		GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, buff);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
		GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
		unbind();
	}
	public void bind() {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
	}
	public void unbind() {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
	}
	public void activateBind(int index) {
		GL33.glActiveTexture(GL33.GL_TEXTURE0+index);
		bind();
	}
	
	public int getId() {
		return textureId;
	}
}
