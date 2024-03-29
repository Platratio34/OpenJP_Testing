package shaders;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import objects.UBO;

/**
 * Uniform Buffer Object for camera data
 */
public class CameraUBO extends UBO {

    /**
     * Create and allocate a UBO for camera data, binding it to <code>ShaderProgram.CAMERA_UNIFORM_BLOCK</code>
     */
    public CameraUBO() {
        super(2*MAT4_SIZE+VEC4_SIZE, ShaderProgram.CAMERA_UNIFORM_BLOCK);
    }

    /**
     * Set the projection matrix in the UBO
     * @param projection projection matrix
     */
    public void setProjectionMatrix(Matrix4f projection) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			projection.get(buffer);
            buffer.rewind();
            set(0, buffer);
            // System.out.println("Projection matrix set");
		} catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Set the transformation matrix in the UBO
     * @param transform transformation matrix
     */
    public void setTransformMatrix(Matrix4f transform) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			transform.get(buffer);
            buffer.rewind();
            set(MAT4_SIZE, buffer);
            // System.out.println("Transform matrix set");
		} catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Set the camera position in the UBO
     * @param transform camera position
     */
    public void setPosition(Vector3f position) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4);
			// matrix.get(buffer);
            // FloatBuffer fb = buffer.asFloatBuffer();
            buffer.put(position.x);
            buffer.put(position.y);
            buffer.put(position.z);
            buffer.rewind();
            set(MAT4_SIZE*2, buffer);
            // System.out.println("Position set");
		} catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
