package meshLoaders;

import java.util.ArrayList;

import org.joml.Vector3f;

import util.MeshData;

public class MeshDataBuilder {
    
    private ArrayList<Float> vertices;
    private ArrayList<Integer> indices;
    private ArrayList<Float> colors;
    private ArrayList<Float> normals;
    private ArrayList<Float> uvs;

    public MeshDataBuilder() {
        vertices = new ArrayList<Float>();
        indices = new ArrayList<Integer>();
        colors = new ArrayList<Float>();
        normals = new ArrayList<Float>();
        uvs = new ArrayList<Float>();
    }

    private float[] convert(ArrayList<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
    
    public MeshData build() {
        return build("");
    }

    public MeshData build(String type) {
        MeshData data = new MeshData();
        if (vertices.size() > 0) {
            data.vertices = convert(vertices);
        }
        if (indices.size() > 0) {
            int[] ind = new int[indices.size()];
            for (int i = 0; i < ind.length; i++) {
                ind[i] = indices.get(i);
            }
            data.indices = ind;
        }
        if (colors.size() > 0) {
            data.colors = convert(colors);
        }
        if (normals.size() > 0) {
            data.normals = convert(normals);
        }
        if (uvs.size() > 0) {
            data.uvs = convert(uvs);
        }
        // System.out.println(data.indices.length);
        data.type = type;
        return data;
    }

    public int addVertex(float x, float y, float z) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
        return (vertices.size() / 3) - 1;
    }

    public int addVertex(Vector3f vector) {
        vertices.add(vector.x);
        vertices.add(vector.y);
        vertices.add(vector.z);
        return (vertices.size() / 3) - 1;
    }

    public void addLine(int p0, int p1) {
        indices.add(p0);
        indices.add(p1);
    }

    public void addTri(int v0, int v1, int v2) {
        // addLine(v0, v1);
        // addLine(v1, v2);
        // addLine(v2, v0);
        indices.add(v0);
        indices.add(v1);
        indices.add(v2);
    }

    public void addColor(float r, float g, float b) {
        colors.add(r);
        colors.add(g);
        colors.add(b);
    }

    public void addNormal(float x, float y, float z) {
        normals.add(x);
        normals.add(y);
        normals.add(z);
    }
    public void addNormal(Vector3f normal) {
        normals.add(normal.x);
        normals.add(normal.y);
        normals.add(normal.z);
    }

    public void addUV(float x, float y) {
        uvs.add(x);
        uvs.add(y);
    }

    public void setVertices(ArrayList<Vector3f> points) {
        // this.vertices =;
        vertices = new ArrayList<Float>();
        for (int i = 0; i < points.size(); i++) {
            addVertex(points.get(i));
        }
    }
}
