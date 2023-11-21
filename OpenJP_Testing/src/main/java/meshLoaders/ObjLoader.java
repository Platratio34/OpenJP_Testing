package meshLoaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import util.MeshData;

public class ObjLoader {
    
    private final static String OBJ_VERTEX_TEXTURE = "vt";
    private final static String OBJ_VERTEX_NORMAL = "vn";
    private final static String OBJ_VERTEX = "v";
    private final static String OBJ_FACE = "f";
    private final static String OBJ_GROUP_NAME = "g";
    private final static String OBJ_OBJECT_NAME = "o";
    private final static String OBJ_SMOOTHING_GROUP = "s";
    private final static String OBJ_POINT = "p";
    private final static String OBJ_LINE = "l";
    private final static String OBJ_MAPLIB = "maplib";
    private final static String OBJ_USEMAP = "usemap";
    private final static String OBJ_MTLLIB = "mtllib";
    private final static String OBJ_USEMTL = "usemtl";
    private final static String MTL_NEWMTL = "newmtl";
    private final static String MTL_KA = "Ka";
    private final static String MTL_KD = "Kd";
    private final static String MTL_KS = "Ks";
    private final static String MTL_TF = "Tf";
    private final static String MTL_ILLUM = "illum";
    private final static String MTL_D = "d";
    private final static String MTL_D_DASHHALO = "-halo";
    private final static String MTL_NS = "Ns";
    private final static String MTL_SHARPNESS = "sharpness";
    private final static String MTL_NI = "Ni";
    private final static String MTL_MAP_KA = "map_Ka";
    private final static String MTL_MAP_KD = "map_Kd";
    private final static String MTL_MAP_KS = "map_Ks";
    private final static String MTL_MAP_NS = "map_Ns";
    private final static String MTL_MAP_D = "map_d";
    private final static String MTL_DISP = "disp";
    private final static String MTL_DECAL = "decal";
    private final static String MTL_BUMP = "bump";
    private final static String MTL_REFL = "refl";
    private final static String MTL_REFL_TYPE_SPHERE = "sphere";
    private final static String MTL_REFL_TYPE_CUBE_TOP = "cube_top";
    private final static String MTL_REFL_TYPE_CUBE_BOTTOM = "cube_bottom";
    private final static String MTL_REFL_TYPE_CUBE_FRONT = "cube_front";
    private final static String MTL_REFL_TYPE_CUBE_BACK = "cube_back";
    private final static String MTL_REFL_TYPE_CUBE_LEFT = "cube_left";
    private final static String MTL_REFL_TYPE_CUBE_RIGHT = "cube_right";

    private static Vector3f arrToV3(float[] arr) {
        return new Vector3f(arr[0], arr[1], arr[2]);
    }
    private static Vector2f arrToV2(float[] arr) {
        return new Vector2f(arr[0], arr[1]);
    }

    public static MeshData parseFile(BufferedReader fileReader) throws IOException {
        MeshDataBuilder builder = new MeshDataBuilder();

        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
        ArrayList<Vector2f> uvs = new ArrayList<Vector2f>();

        String line = fileReader.readLine();
        int lineN = 0;
        int cMat = -1;
        while (line != null) {
            line = line.trim();
            // System.out.println(lineN+" | "+line);

            if (line.length() == 0 || line.startsWith("#")) {
                // comment
            } else if (line.startsWith(OBJ_VERTEX_TEXTURE)) {
                float[] vals = parseFloatList(line, 2, OBJ_VERTEX_TEXTURE.length());
                // builder.addUV(vals[0], vals[1]);
                uvs.add(arrToV2(vals));
                // System.out.println("Added vertex texture #"+(uvs.size()-1));
            } else if (line.startsWith(OBJ_VERTEX_NORMAL)) {
                float[] vals = parseFloatList(line, 3, OBJ_VERTEX_NORMAL.length());
                // builder.addNormal(vals[0], vals[1], vals[2]);
                normals.add(arrToV3(vals));
                // System.out.println("Added vertex normal #"+(normals.size()-1));
            } else if (line.startsWith(OBJ_VERTEX)) {
                float[] vals = parseFloatList(line, 3, OBJ_VERTEX.length());
                // builder.addVertex(vals[0], vals[1], vals[2]);
                vertices.add(arrToV3(vals));
                // System.out.println("Added vertex #"+(vertices.size()-1));
            } else if (line.startsWith(OBJ_FACE)) {
                String[] verts = line.substring(OBJ_FACE.length()).trim().split(" ");
                int[] indices = new int[verts.length];
                for (int i = 0; i < verts.length; i++) {
                    int[] comp = parseIntList(verts[i], 3, 0, "/");
                    indices[i] = builder.addVertex(vertices.get(comp[0]-1));
                    if (comp[1] >= 0) builder.addUV(uvs.get(comp[1]-1));
                    if (comp[2] >= 0) builder.addNormal(normals.get(comp[2]-1));
                    if (cMat >= 0) builder.addColor(cMat, 0, 0);
                }
                builder.addTri(indices[0], indices[1], indices[2]);
                if (verts.length == 4) {
                    builder.addTri(indices[2], indices[3], indices[0]);
                } else if (verts.length == 5) {
                    builder.addTri(indices[2], indices[3], indices[0]);
                    builder.addTri(indices[0], indices[3], indices[4]);
                }
                else if (verts.length == 6) {
                    builder.addTri(indices[2], indices[3], indices[4]);
                    builder.addTri(indices[4], indices[5], indices[0]);
                } else if(verts.length != 3) {
                    System.out.println("? "+verts.length);
                }
            } else if (line.startsWith(OBJ_GROUP_NAME)) {

            } else if (line.startsWith(OBJ_OBJECT_NAME)) {

            } else if (line.startsWith(OBJ_SMOOTHING_GROUP)) {

            } else if (line.startsWith(OBJ_POINT)) {

            } else if (line.startsWith(OBJ_LINE)) {

            } else if (line.startsWith(OBJ_MAPLIB)) {

            } else if (line.startsWith(OBJ_USEMAP)) {

            } else if (line.startsWith(OBJ_USEMTL)) {
                cMat++;
            } else if (line.startsWith(OBJ_MTLLIB)) {

            } else {
                System.err.println("Unknown line in OBJ: \"" + line + "\"");
            }

            line = fileReader.readLine();
            lineN++;
        }
        fileReader.close();

        return builder.build();
    }
    
    private static float[] parseFloatList(String str, int length, int charOffset) {
        float[] arr = new float[length];
        str = str.substring(charOffset).trim();
        String[] parts = str.split(" ");
        for (int i = 0; i < length && i < parts.length; i++) {
            arr[i] = Float.parseFloat(parts[i]);
        }
        return arr;
    }
    private static int[] parseIntList(String str, int length, int charOffset) {
        return parseIntList(str, length, charOffset, " "); 
    }
    private static int[] parseIntList(String str, int length, int charOffset, String splitString) {
        int[] arr = new int[length];
        str = str.substring(charOffset).trim();
        String[] parts = str.split(splitString);
        for (int i = 0; i < length && i < parts.length; i++) {
            try {
                arr[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                arr[i] = -1;
            }
        }
        return arr; 
    }
}
