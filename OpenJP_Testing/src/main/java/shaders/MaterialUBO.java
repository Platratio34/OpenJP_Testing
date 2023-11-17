package shaders;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryStack;

import objects.Texture2D;
import objects.UBO;

public class MaterialUBO extends UBO {

    private static final long SIZE = VEC4_SIZE + FLOAT_SIZE + INT_SIZE + VEC2_SIZE + VEC2_SIZE + VEC2_SIZE
            + SAMPLER_2D_SIZE;
    
    private static final long COLOR_POS = 0;
    private static final long SMOOTHNESS_POS = VEC4_SIZE;

    private static final long TEXTURED_POS = VEC4_SIZE + FLOAT_SIZE;
    private static final long TEX_SCALE_POS = VEC4_SIZE + FLOAT_SIZE + INT_SIZE;
    private static final long TEX_OFFSET_POS = VEC4_SIZE + FLOAT_SIZE + INT_SIZE + VEC2_SIZE;
    
    private static final long TEXTURE_POS = VEC4_SIZE + FLOAT_SIZE + INT_SIZE + VEC2_SIZE + VEC2_SIZE + VEC2_SIZE;

    public MaterialUBO() {
        super(SIZE, ShaderProgram.MATERIAL_UNIFORM_BLOCK_BASE);
        // try (MemoryStack stack = MemoryStack.stackPush()) {
        //     IntBuffer b = stack.mallocInt(3);
        //     GL45.glGetIntegerv(GL45.GL_MAX_FRAGMENT_UNIFORM_BLOCKS, b);
        //     System.out.println(b.get(0));
        // }
    }
    
    public void setColor(Color color) {
        set(COLOR_POS, color);
    }

    public void setSmoothness(float smoothness) {
        set(SMOOTHNESS_POS, smoothness);
    }

    public void setTextured(boolean textured) {
        set(TEXTURED_POS, textured);
    }

    public void setTextureScale(Vector2f scale) {
        set(TEX_SCALE_POS, scale);
    }

    public void setTextureOffset(Vector2f scale) {
        set(TEX_OFFSET_POS, scale);
    }
    
    public void setTexture(Texture2D texture) {
        set(TEXTURE_POS, texture.getHandle());
    }
    
}
