package meshLoaders;

import java.io.BufferedReader;
import java.io.IOException;

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

    public static MeshData parseFile(BufferedReader fileReader) throws IOException {
        MeshDataBuilder builder = new MeshDataBuilder();

        String line = fileReader.readLine();
        while (line != null) {
            line = line.trim();

            if (line.length() == 0 || line.startsWith("#"))
                continue;

            if (line.startsWith(OBJ_VERTEX_TEXTURE)) {
                float[] vals = parseFloatList(line, 2, OBJ_VERTEX_TEXTURE.length());
                builder.addUV(vals[0], vals[1]);
            } else if (line.startsWith(OBJ_VERTEX_NORMAL)) {
                float[] vals = parseFloatList(line, 3, OBJ_VERTEX_NORMAL.length());
                builder.addNormal(vals[0], vals[1], vals[2]);
            } else if (line.startsWith(OBJ_VERTEX)) {
                float[] vals = parseFloatList(line, 3, OBJ_VERTEX.length());
                builder.addVertex(vals[0], vals[1], vals[2]);
            } else if (line.startsWith(OBJ_FACE)) {

            } else if (line.startsWith(OBJ_GROUP_NAME)) {

            } else if (line.startsWith(OBJ_SMOOTHING_GROUP)) {

            } else if (line.startsWith(OBJ_POINT)) {

            } else if (line.startsWith(OBJ_LINE)) {

            } else if (line.startsWith(OBJ_MAPLIB)) {

            } else if (line.startsWith(OBJ_USEMAP)) {

            } else if (line.startsWith(OBJ_USEMTL)) {

            } else if (line.startsWith(OBJ_MTLLIB)) {

            } else {
                System.err.println("Unknown line in OBJ: \"" + line + "\"");
            }
            fileReader.close();

            line = fileReader.readLine();
        }

        return builder.build();
    }
    
    private static float[] parseFloatList(String str, int length, int charOffset) {
        float[] arr = new float[length];
        str = str.substring(charOffset);
        String[] parts = str.split(" ");
        for (int i = 0; i < length && i < parts.length; i++) {
            arr[i] = Float.parseFloat(parts[i]);
        }
        return arr; 
    }
}
