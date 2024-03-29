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

	/*
	 * Number converter functions
	 */
    
	/*
	 * Mesh converter functions
	 */

	/**
	 * Convert a mesh string to byte array
	 * @param str mesh string
	 * @return Byte array representing the mesh
	 * 
	 * @deprecated use <code>Mesh.meshDataFromString(String)</code> and <code>BinMesh.meshDataToBin(MeshData)</code> instead
	 */
	@Deprecated
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
			byte[] b = Binary.floatToBin(verts[i]);
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
			byte[] b = Binary.floatToBin(colors[i]);
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
			byte[] b = Binary.floatToBin(normals[i]);
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
				byte[] b = Binary.floatToBin(uvs[i]);
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

		byte[] bytes = meshDataToBin(Mesh.meshDataFromString(str));

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
	 * 
	 * @deprecated use <code>BinMesh.binToMeshData</code> and <code>Mesh(MeshData)</code> instead
	 */
	@Deprecated
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
					float f = Binary.binToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
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
			pointer += length * 4; // move pointer to next header
			cSection = (int) bytes[pointer];
		}
		if (vertices == null || colors == null || normals == null) { // A section was not present
			System.err.println("Could not load mesh, missing sections:");
			if (vertices == null)
				System.err.println("Missing vertex section");
			if (colors == null)
				System.err.println("Missing color section");
			if (normals == null)
				System.err.println("Missing normal section");
			return null;
		}
		Mesh mesh = new Mesh(vertices);
		mesh.setColors(colors);
		mesh.setNormals(normals);
		if (uvs != null) {
			mesh.setUVs(uvs);
		}
		return mesh;
	}
	
	/**
	 * Create a mesh from binary mesh resource file
	 * @param src binary resource file
	 * @return Mesh Data from binary file
	 * @throws IOException If the file could not be found or read
	 */
	public static MeshData meshDataFromBinResource(String src) throws IOException {
		ClassLoader classLoader = BinMesh.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream(src);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int b = is.read();
		while (b != -1) {
			bos.write(b);
			b = is.read();
		}
		// return meshFromBin(bos.toByteArray());
		return binToMeshData(bos.toByteArray());
	}

	/**
	 * Create a mesh from binary mesh resource file
	 * @param src binary resource file
	 * @return Mesh created from binary file
	 * @throws IOException If the file could not be found or read
	 */
	public static Mesh meshFromBinResource(String src) throws IOException {
		return new Mesh(meshDataFromBinResource(src));
	}
	
	/*
	 * Gizmo converter functions
	 */

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
			byte[] b = Binary.floatToBin(vA[i]);
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
			byte[] b = Binary.uIntToBin(iA[i]);
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
					float f = Binary.binToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
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
					int n = Binary.binToInt(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
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

	/*
	 * Mesh Data binary converters
	 */

	/**
	 * Converts mesh data to byte array
	 * @param mesh MeshData to convert
	 * @return Byte array representing the mesh
	 */
	public static byte[] meshDataToBin(MeshData mesh) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Write file header
        out.write(mesh.type.charAt(0));
        out.write(mesh.type.charAt(1));
        out.write(mesh.type.charAt(2));
		out.write(mesh.type.charAt(3));
		
		// Section header for vertices
		out.write(SECTION_VERT);
		out.write(TYPE_FLOAT);
		out.write(mesh.vertices.length / 0x100);
		out.write(mesh.vertices.length % 0x100);
		
		// Write vertices
		for (int i = 0; i < mesh.vertices.length; i++) {
			byte[] b = Binary.floatToBin(mesh.vertices[i]);
			out.write(b[0]);
			out.write(b[1]);
			out.write(b[2]);
			out.write(b[3]);
		}

		if(mesh.indices != null) {
			// Section header for colors
			out.write(SECTION_INDICES);
			out.write(TYPE_UINT);
			out.write(mesh.indices.length / 0x100);
			out.write(mesh.indices.length % 0x100);
			
			// Write colors
			for (int i = 0; i < mesh.indices.length; i++) {
				byte[] b = Binary.uIntToBin(mesh.indices[i]);
				out.write(b[0]);
				out.write(b[1]);
				out.write(b[2]);
				out.write(b[3]);
			}
		}
		
		if(mesh.colors != null) {
			// Section header for colors
			out.write(SECTION_COLOR);
			out.write(TYPE_FLOAT);
			out.write(mesh.colors.length / 0x100);
			out.write(mesh.colors.length % 0x100);
			
			// Write colors
			for (int i = 0; i < mesh.colors.length; i++) {
				byte[] b = Binary.floatToBin(mesh.colors[i]);
				out.write(b[0]);
				out.write(b[1]);
				out.write(b[2]);
				out.write(b[3]);
			}
		}
		
		if(mesh.normals != null) {
			// Section header for normals
			out.write(SECTION_NORM);
			out.write(TYPE_FLOAT);
			out.write(mesh.normals.length / 0x100);
			out.write(mesh.normals.length % 0x100);
			
			// Write normals
			for (int i = 0; i < mesh.normals.length; i++) {
				byte[] b = Binary.floatToBin(mesh.normals[i]);
				out.write(b[0]);
				out.write(b[1]);
				out.write(b[2]);
				out.write(b[3]);
			}
		}
		
		if(mesh.uvs != null) {
			// Section header for uvs
			out.write(SECTION_UV);
			out.write(TYPE_FLOAT);
			out.write(mesh.uvs.length / 0x100);
			out.write(mesh.uvs.length % 0x100);
			
			// Write normals
			for (int i = 0; i < mesh.uvs.length; i++) {
				byte[] b = Binary.floatToBin(mesh.uvs[i]);
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
	 * Convert a byte array to mesh data
	 * @param bytes byte array representing a mesh
	 * @return MeshData created from byte array
	 */
	public static MeshData binToMeshData(byte[] bytes) {
		// if (bytes[0] != 'M' || bytes[1] != 'E' || bytes[2] != 'S' || bytes[3] != 'H') { // Check that this is a mesh file
		// 	System.err.println("Could not load mesh; file type mismatch");
		// 	return null;
		// }
		MeshData mesh = new MeshData();
		// float[] vertices = null;
		// float[] colors = null;
		// float[] normals = null;
		// float[] uvs = null;

		mesh.type = "" + (char)bytes[0] + (char)bytes[1] + (char)bytes[2] + (char)bytes[3];

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
					float f = Binary.binToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
					arr[aI] = f;
					aI++;
				}
				if (cSection == SECTION_VERT) {
					mesh.vertices = arr;
				} else if (cSection == SECTION_COLOR) {
					mesh.colors = arr;
				} else if (cSection == SECTION_NORM) {
					mesh.normals = arr;
				} else if (cSection == SECTION_UV) {
					mesh.uvs = arr;
				}
			} else if (dType == TYPE_UINT) {
				int[] arr = new int[length];
				int aI = 0;
				for (int i = pointer; i < pointer + (length * 4); i += 4) {
					int n = Binary.binToInt(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
					arr[aI] = n;
					aI++;
				}
				if (cSection == SECTION_INDICES) {
					mesh.indices = arr;
				}
			}
			pointer += length*4; // move pointer to next header
			cSection = (int) bytes[pointer];
		}
		return mesh;
	}

	public static void meshDataToFile(MeshData data, String dest) throws IOException {
		byte[] bytes = meshDataToBin(data);
		FileOutputStream outputStream = new FileOutputStream(dest);
		for (int i = 0; i < bytes.length; i++) {
			outputStream.write(bytes[i]);
		}
		outputStream.close();		
    }
}
