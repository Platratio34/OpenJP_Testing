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

public class BinMesh {

	private static final int TYPE_FLOAT = 0x01;
	private static final int TYPE_UINT = 0x02;

	private static final int SECTION_END = 0x00;
	private static final int SECTION_VERT = 0x01;
	private static final int SECTION_COLOR = 0x02;
	private static final int SECTION_NORM = 0x03;
	private static final int SECTION_INDICES = 0x04;

	public static byte[] floatToBin(float f) {
		int bits = Float.floatToIntBits(f);
		return new byte[] {
				(byte) (bits >> 24),
				(byte) (bits >> 16),
				(byte) (bits >> 8),
				(byte) (bits)
		};
	}

	public static float binToFloat(byte[] bytes) {
		int intBits = bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}

	public static float binToFloat(byte b0, byte b1, byte b2, byte b3) {
		int intBits = b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
    
	public static byte[] uIntToBin(int n) {
		return new byte[] {
				(byte) (n >> 24),
				(byte) (n >> 16),
				(byte) (n >> 8),
				(byte) (n)
		};
	}

	public static int binToUInt(byte[] bytes) {
		return binToUInt(bytes[0], bytes[1], bytes[2], bytes[3]);
	}

	public static int binToUInt(byte b0, byte b1, byte b2, byte b3) {
		return b0 << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b3 & 0xFF);
	}
    
	
    public static byte[] meshToBin(String str) {
        
		String[] components = str.split(";");
		int sI = 0;
		int vI = 0;
		int cI = 1;
		int nI = 2;
		int mI = -1;
		
		if(components[0].contains("format=")) {
			sI++;
			String[] parts = components[0].substring(8).split(",");
			// vI = -1;
			cI = -1;
			nI = -1;
			for(int i = 0; i < parts.length; i++) {
				if(parts[i].equals("vertex")) {
					vI = i;
				} else if(parts[i].equals("color")) {
					cI = i;
				} else if(parts[i].equals("normal")) {
					nI = i;
				} else if(parts[i].equals("mat")) {
					mI = i;
				}
			}
		}
		
		int vlength = components.length - sI;
		float[] verts = new float[vlength];
		float[] colors = new float[vlength];
		float[] normals = new float[vlength];
		
		int type = 0;
		int arrI = 0;
        for (int i = sI; i < components.length; i++) {
			String[] parts = components[i].split(",");
            if (type == vI) {
                verts[arrI] = Float.parseFloat(parts[0]);
                verts[arrI + 1] = Float.parseFloat(parts[1]);
                verts[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("v=%f,%f,%f; ", verts[arrI], verts[arrI+1], verts[arrI+2]));
            } else if (type == cI) {
                colors[arrI] = Float.parseFloat(parts[0]);
                colors[arrI + 1] = Float.parseFloat(parts[1]);
                colors[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("c=%f,%f,%f; ", colors[arrI], colors[arrI+1], colors[arrI+2]));
            } else if (type == nI) {
                normals[arrI] = Float.parseFloat(parts[0]);
                normals[arrI + 1] = Float.parseFloat(parts[1]);
                normals[arrI + 2] = Float.parseFloat(parts[2]);
                //				System.out.print(String.format("n=%f,%f,%f; ", normals[arrI], normals[arrI+1], normals[arrI+2]));
            } else if (type == mI) {
                colors[arrI] = Float.parseFloat(parts[0]);
            }

            type = (type + 1) % 3;
            if (type == 0) {
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
		out.write(vlength / 0x100);
		out.write(vlength % 0x100);
		
		// Write vertices
		for (int i = 0; i < vlength; i++) {
			byte[] b = floatToBin(verts[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// Section header for colors
		out.write(SECTION_COLOR);
		out.write(TYPE_FLOAT);
		out.write(vlength / 0x100);
		out.write(vlength % 0x100);
		
		// Write colors
		for (int i = 0; i < vlength; i++) {
			byte[] b = floatToBin(colors[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}
		
		// Section header for normals
		out.write(SECTION_NORM);
		out.write(TYPE_FLOAT);
		out.write(vlength / 0x100);
		out.write(vlength % 0x100);
		
		// Write normals
		for (int i = 0; i < vlength; i++) {
			byte[] b = floatToBin(normals[i]);
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
    
	public static void meshResourceToBin(String src, String dest) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(src)));
		} catch (NullPointerException e) {
			System.err.println("Could not load mesh from resource \"" + src + "\"");
			return;
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
	
	public static Mesh meshFromBin(byte[] bytes) {
		if (bytes[0] != 'M' || bytes[1] != 'E' || bytes[2] != 'S' || bytes[3] != 'H') { // Check that this is a mesh file
			System.err.println("Could not load mesh; file type mismatch");
			return null;
		}

		float[] vertices = null;
		float[] colors = null;
		float[] normals = null;

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
				}
				if (cSection == SECTION_COLOR) {
					colors = arr;
				}
				if (cSection == SECTION_NORM) {
					normals = arr;
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
		return mesh;
	}

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