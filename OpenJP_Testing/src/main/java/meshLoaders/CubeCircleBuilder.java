package meshLoaders;

import org.joml.Vector3f;

import util.MeshData;

public class CubeCircleBuilder {
    
    private MeshDataBuilder builder;
    private int rows;

    private static final float PI = 3.141592653f;

    public static MeshData buildSphere(int rows) {
        return new CubeCircleBuilder().build(rows);
    }

    public MeshData build(int rows) {
        builder = new MeshDataBuilder();
        this.rows = rows;
        
        makeFace(0, 0);
        makeFace(PI*0.5f, 0);
        makeFace(PI, 0);
        makeFace(PI*1.5f, 0);
        makeFace(0, PI*0.5f);
        makeFace(0, PI*-0.5f);

        return builder.build("MESH");
    }
    
    private void makeFace(float rotY, float rotZ) {
        int[][] indices = new int[rows + 1][rows + 1];
        float rs = 2f / (float) (rows);

        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= rows; j++) {
                Vector3f point = new Vector3f((i * rs) - 1f, (j * rs) - 1f, 1f).normalize();
                // points.add(point);
                point.rotateY(rotY);
                point.rotateX(rotZ);
                builder.addNormal(point);
                indices[i][j] = builder.addVertex(point);
                builder.addColor(0, 0, 0);
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                // System.out.println(i + "," + j);

                int v0 = indices[i][j];
                int v1 = indices[i + 1][j];
                int v2 = indices[i + 1][j + 1];
                int v3 = indices[i][j + 1];
                // System.out.println(points.get(v0) + "," + points.get(v1) + "," + points.get(v2) + "," + points.get(v3));

                builder.addTri(v0, v1, v2);
                builder.addTri(v2, v3, v0);
            }
        }
    }
    
}
