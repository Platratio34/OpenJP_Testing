package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gizmos.GizmoMesh;
import objects.Mesh;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

/**
 * Static class containing functions for creating and reading binary mesh files
 */
public class BinMesh {

	/** Float type code */
	private static final int TYPE_FLOAT = 0x01;
	/** Unsigned integer type code */
	private static final int TYPE_UINT = 0x02;

	/** End section id */
	private static final int SECTION_END = 0x00;
	/** Vertex section id */
	private static final int SECTION_VERT = 0x01;
	/** Color section id */
	private static final int SECTION_COLOR = 0x02;
	/** Normal section id */
	private static final int SECTION_NORM = 0x03;
	/** Index section id */
	private static final int SECTION_INDICES = 0x04;
	/** UV section id */
	private static final int SECTION_UV = 0x05;

	/**
	 * Convert a float to byte array<br><br>
	 * Uses IEEE 754 floating-point "single format"
	 * @param f float to convert
	 * @return 4 byte array representing the float
	 */
	public static byte[] floatToBin(float f) {
		int bits = Float.floatToIntBits(f);
		return new byte[] {
				(byte) (bits >> 24),
				(byte) (bits >> 16),
				(byte) (bits >> 8),
				(byte) (bits)
		};
	}
	/**
	 * Convert a byte array to a float<br><br>
	 * Uses IEEE 754 floating-point "single format"
	 * @param bytes 4 byte array representing the float
	 * @return Float represented by the bytes
	 */
	public static float binToFloat(byte[] bytes) {
		int intBits = bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
	/**
	 * Convert a bytes to a float.<br><br>
	 * Uses IEEE 754 floating-point "single format"
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Float represented by the bytes
	 */
	public static float binToFloat(byte b0, byte b1, byte b2, byte b3) {
		int intBits = b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
    
	/**
	 * Convert an unsigned integer to bytes
	 * @param n unsigned integer to convert
	 * @return 4 byte array representing the integer
	 */
	public static byte[] uIntToBin(int n) {
		return new byte[] {
				(byte) (n >> 24),
				(byte) (n >> 16),
				(byte) (n >> 8),
				(byte) (n)
		};
	}
	/**
	 * Convert byte array to unsigned integer
	 * @param bytes 4 byte array representing the integer
	 * @return Unsigned integer represented by the bytes
	 */
	public static int binToUInt(byte[] bytes) {
		return binToUInt(bytes[0], bytes[1], bytes[2], bytes[3]);
	}
	/**
	 * Convert bytes to unsigned integer
	 * @param b0 high byte (<code>0xff000000</code>)
	 * @param b1 medium high byte (<code>0x00ff0000</code>)
	 * @param b2 medium low byte (<code>0x0000ff00</code>)
	 * @param b3 low byte (<code>0x000000ff</code>)
	 * @return Unsigned integer represented by the bytes
	 */
	public static int binToUInt(byte b0, byte b1, byte b2, byte b3) {
		return b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
	}
    
	/**
	 * Convert a mesh string to byte array
	 * @param str mesh string
	 * @return Byte array representing the mesh
	 */
    public static byte[] meshToBin(String str) {
        
		String[] components = str.split(";");
		int startLine = 0;
		int vertexSection = 0;
		int colorSection = 1;
		int normalSection = 2;
		int matSection = -1;
		int uvSection = -1;

		int numTypes = 0;
		
		if(components[0].contains("format=")) {
			startLine++;
			String[] parts = components[0].substring(7).split(",");
			vertexSection = -1;
			colorSection = -1;
			normalSection = -1;
			for(int i = 0; i < parts.length; i++) {
				if(parts[i].equals("vertex")) {
					vertexSection = i;
					numTypes++;
				} else if(parts[i].equals("color")) {
					colorSection = i;
					numTypes++;
				} else if(parts[i].equals("normal")) {
					normalSection = i;
					numTypes++;
				} else if(parts[i].equals("mat")) {
					matSection = i;
					numTypes++;
				} else if(parts[i].equals("uv")) {
					uvSection = i;
					numTypes++;
				}
			}
		}
		
		int cLength = (components.length - startLine)/numTypes;
		float[] verts = new float[cLength*3];
		float[] colors = new float[cLength*3];
		float[] normals = new float[cLength*3];
		float[] uvs = new float[cLength*2];
		int uvsI = 0;
		
		int section = 0;
		int arrI = 0;
        for (int i = startLine; i < components.length; i++) {
			String[] parts = components[i].split(",");
            if (section == vertexSection) {
                verts[arrI] = Float.parseFloat(parts[0]);
                verts[arrI + 1] = Float.parseFloat(parts[1]);
                verts[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("v=%f,%f,%f; ", verts[arrI], verts[arrI+1], verts[arrI+2]));
            } else if (section == colorSection) {
                colors[arrI] = Float.parseFloat(parts[0]);
                colors[arrI + 1] = Float.parseFloat(parts[1]);
                colors[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("c=%f,%f,%f; ", colors[arrI], colors[arrI+1], colors[arrI+2]));
            } else if (section == normalSection) {
                normals[arrI] = Float.parseFloat(parts[0]);
                normals[arrI + 1] = Float.parseFloat(parts[1]);
                normals[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("n=%f,%f,%f; ", normals[arrI], normals[arrI+1], normals[arrI+2]));
            } else if (section == matSection) {
                colors[arrI] = Float.parseFloat(parts[0]);
            } else if (section == uvSection) {
                uvs[uvsI] = Float.parseFloat(parts[0]);
                uvs[uvsI + 1] = Float.parseFloat(parts[1]);
				uvsI += 2;
                //				System.out.print(String.format("n=%f,%f,%f; ", normals[arrI], normals[arrI+1], normals[arrI+2]));
            }

            section = (section + 1) % numTypes;
            if (section == 0) {
                arrI += 3;
                //				System.out.println("");
            }
        }
        
        // ArrayList<Byte> bytes = new ArrayList<Byte>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Write file header
        out.write('M');
        out.write('E');
        out.write('S');
		out.write('H');
		
		// Section header for vertices
		out.write(SECTION_VERT);
		out.write(TYPE_FLOAT);
		out.write(verts.length / 0x100);
		out.write(verts.length % 0x100);
		
		// Write vertices
		for (int i = 0; i < verts.length; i++) {
			byte[] b = floatToBin(verts[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// Section header for colors
		out.write(SECTION_COLOR);
		out.write(TYPE_FLOAT);
		out.write(colors.length / 0x100);
		out.write(colors.length % 0x100);
		
		// Write colors
		for (int i = 0; i < colors.length; i++) {
			byte[] b = floatToBin(colors[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// Section header for normals
		out.write(SECTION_NORM);
		out.write(TYPE_FLOAT);
		out.write(normals.length / 0x100);
		out.write(normals.length % 0x100);
		
		// Write normals
		for (int i = 0; i < normals.length; i++) {
			byte[] b = floatToBin(normals[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		if(uvSection != -1) {
			// Section header for uvs
			out.write(SECTION_UV);
			out.write(TYPE_FLOAT);
			out.write(uvs.length / 0x100);
			out.write(uvs.length % 0x100);
			
			// Write normals
			for (int i = 0; i < uvs.length; i++) {
				byte[] b = floatToBin(uvs[i]);
				out.write(b[0]);
				out.write(b[1]);
				out.write(b[2]);
				out.write(b[3]);
			}
		}
		
		// File end section header
		out.write(SECTION_END);
		out.write(0x00);
		out.write(0x00);
		out.write(0x00);

		return out.toByteArray();
    }
    
	/**
	 * Convert a plain text mesh resource file to binary file
	 * @param src resource path to plain text mesh file
	 * @param dest non-resource path to binary mesh file
	 * @throws IOException If either file could not be found or accessed
	 */
	public static void meshResourceToBin(String src, String dest) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(src)));
		} catch (NullPointerException e) {
			throw new IOException("Could not load mesh from resource \"" + src + "\"");
		}
		String line = "";
		String str = "";
		while (line != null) {
			str += line;
			line = br.readLine();
		}
		br.close();

		byte[] bytes = meshToBin(str);

		FileOutputStream outputStream = new FileOutputStream(dest);
		for (int i = 0; i < bytes.length; i++) {
			outputStream.write(bytes[i]);
		}
		outputStream.close();
	}
	
	/**
	 * Create a mesh from byte array
	 * @param bytes bytes from binary mesh file
	 * @return Mesh created from file OR <code>null</code> if a mesh could not be created
	 */
	public static Mesh meshFromBin(byte[] bytes) {
		if (bytes[0] != 'M' || bytes[1] != 'E' || bytes[2] != 'S' || bytes[3] != 'H') { // Check that this is a mesh file
			System.err.println("Could not load mesh; file type mismatch");
			return null;
		}

		float[] vertices = null;
		float[] colors = null;
		float[] normals = null;
		float[] uvs = null;

		int cSection = (int) bytes[4];
		int pointer = 4;
		while (cSection != SECTION_END) {
			byte dType = bytes[pointer + 1];
			int length = (bytes[pointer + 2] * 0x100) + bytes[pointer + 3];
			// System.out.println(cSection + " " + dType + " " + length);
			pointer += 4; // move to start of data

			if (dType == TYPE_FLOAT) { // float
				float[] arr = new float[length];
				int aI = 0;
				for (int i = pointer; i < pointer + (length * 4); i += 4) {
					float f = binToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
					arr[aI] = f;
					aI++;
				}
				if (cSection == SECTION_VERT) {
					vertices = arr;
				} else if (cSection == SECTION_COLOR) {
					colors = arr;
				} else if (cSection == SECTION_NORM) {
					normals = arr;
				} else if (cSection == SECTION_UV) {
					uvs = arr;
				}
			}
			pointer += length*4; // move pointer to next header
			cSection = (int) bytes[pointer];
		}
		if (vertices == null || colors == null || normals == null) { // A section was not present
			System.err.println("Could not load mesh, missing sections:");
			if (vertices == null) System.err.println("Missing vertex section");
			if (colors == null) System.err.println("Missing color section");
			if (normals == null) System.err.println("Missing normal section");
			return null;
		}
		Mesh mesh = new Mesh(vertices);
		mesh.setColors(colors);
		mesh.setNormals(normals);
		if(uvs != null) {
			mesh.setUVs(uvs);
		}
		return mesh;
	}

	/**
	 * Create a mesh from binary mesh resource file
	 * @param src binary resource file
	 * @return Mesh created from binary file
	 * @throws IOException If the file could not be found or read
	 */
	public static Mesh meshFromBinResource(String src) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream(src);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int b = is.read();
		while (b != -1) {
			bos.write(b);
			b = is.read();
		}
		return meshFromBin(bos.toByteArray());
	}
	
	/**
	 * Convert a gizmo mesh string to byte array
	 * @param str gizmo mesh string
	 * @return Byte array representing the mesh
	 */
	public static byte[] gizmoToBin(String str) {
        
		String[] components = str.split(";");
		
		ArrayList<Float> verts = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

        boolean vertex = false;
        boolean index = false;
        boolean indexed = false;
		
		for(int i = 0; i < components.length; i++) {
            if(components[i].contains("vertex")) {
                vertex = true;
                index = false;
                continue;
            } else if(components[i].contains("index")) {
                vertex = false;
                index = true;
                indexed = true;
                continue;
            }
			String[] parts = components[i].split(",");
            if(vertex) {
                verts.add(Float.parseFloat(parts[0]));
                verts.add(Float.parseFloat(parts[1]));
                verts.add(Float.parseFloat(parts[2]));
            }
            if(index) {
                indices.add(Integer.parseInt(parts[0]));
                indices.add(Integer.parseInt(parts[1]));
            }
		}

		float[] vA = new float[verts.size()];
		int[] iA = null;
        for(int i = 0; i < verts.size(); i++) {
            vA[i] = verts.get(i);
        }
        if(indexed) {
            iA = new int[indices.size()];
            for(int i = 0; i < indices.size(); i++) {
                iA[i] = indices.get(i);
            }
        }
        
        // ArrayList<Byte> bytes = new ArrayList<Byte>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Write file header
        out.write('G');
        out.write('I');
        out.write('Z');
		out.write('M');
		
		// Section header for vertices
		out.write(SECTION_VERT);
		out.write(TYPE_FLOAT);
		out.write(vA.length / 0x100);
		out.write(vA.length % 0x100);
		
		// Write vertices
		for (int i = 0; i < vA.length; i++) {
			byte[] b = floatToBin(vA[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// Section header for colors
		out.write(SECTION_INDICES);
		out.write(TYPE_UINT);
		out.write(iA.length / 0x100);
		out.write(iA.length % 0x100);
		
		// Write colors
		for (int i = 0; i < iA.length; i++) {
			byte[] b = uIntToBin(iA[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// File end section header
		out.write(SECTION_END);
		out.write(0x00);
		out.write(0x00);
		out.write(0x00);

		return out.toByteArray();
    }
    
	/**
	 * Convert a plain text gizmo mesh resource file to binary file
	 * @param src resource path to plain text gizmo mesh file
	 * @param dest non-resource path to binary gizmo mesh file
	 * @throws IOException If either file could not be found or accessed
	 */
	public static void gizmoResourceToBin(String src, String dest) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(src)));
		} catch (NullPointerException e) {
			System.err.println("Could not load gizmo from resource \"" + src + "\"");
			return;
		}
		String line = "";
		String str = "";
		while (line != null) {
			str += line;
			line = br.readLine();
		}
		br.close();

		byte[] bytes = gizmoToBin(str);

		FileOutputStream outputStream = new FileOutputStream(dest);
		for (int i = 0; i < bytes.length; i++) {
			outputStream.write(bytes[i]);
		}
		outputStream.close();
	}
	
	/**
	 * Create a gizmo mesh from byte array
	 * @param bytes bytes from binary gizmo mesh file
	 * @return Gizmo mesh created from file OR <code>null</code> if a mesh could not be created
	 */
	public static GizmoMesh gizmoFromBin(byte[] bytes) {
		if (bytes[0] != 'G' || bytes[1] != 'I' || bytes[2] != 'Z' || bytes[3] != 'M') { // Check that this is a gizmo file
			System.err.println("Could not load gizmo; file type mismatch");
			return null;
		}

		float[] vertices = null;
		int[] indices = null;

		int cSection = (int) bytes[4];
		int pointer = 4;
		while (cSection != SECTION_END) {
			byte dType = bytes[pointer + 1];
			int length = (bytes[pointer + 2] * 0x100) + bytes[pointer + 3];
			// System.out.println(cSection + " " + dType + " " + length);
			pointer += 4; // move to start of data

			if (dType == TYPE_FLOAT) {
				float[] arr = new float[length];
				int aI = 0;
				for (int i = pointer; i < pointer + (length * 4); i += 4) {
					float f = binToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
					arr[aI] = f;
					aI++;
				}
				if (cSection == SECTION_VERT) {
					vertices = arr;
				}
			} else if (dType == TYPE_UINT) {
				int[] arr = new int[length];
				int aI = 0;
				for (int i = pointer; i < pointer + (length * 4); i += 4) {
					int n = binToUInt(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
					arr[aI] = n;
					aI++;
				}
				if (cSection == SECTION_INDICES) {
					indices = arr;
				}
			}
			pointer += length*4; // move pointer to next header
			cSection = (int) bytes[pointer];
		}
		if (vertices == null) { // A section was not present
			System.err.println("Could not load gizmo, missing sections:");
			if (vertices == null)
				System.err.println("Missing vertex section");
			return null;
		}
		if (indices != null) {
			return new GizmoMesh(vertices, indices);
		}
		return new GizmoMesh(vertices);
	}

	/**
	 * Create a gizmo mesh from binary gizmo mesh resource file
	 * @param src binary resource file
	 * @return Gizmo mesh created from binary file
	 * @throws IOException If the file could not be found or read
	 */
	public static GizmoMesh gizmoFromBinResource(String src) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream(src);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int b = is.read();
		while (b != -1) {
			bos.write(b);
			b = is.read();
		}
		return gizmoFromBin(bos.toByteArray());
	}

}
