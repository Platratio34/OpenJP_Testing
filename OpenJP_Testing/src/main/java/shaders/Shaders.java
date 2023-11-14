package shaders;

import java.util.HashMap;

public class Shaders {
    
    private static HashMap<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();

    private static final String DEFAULT_VERTEX = "shaders/vertex.vs";

    private static ShaderProgram mainShader;

    /**
     * Create a new named shader with specified frag shader.<br>
     * <br>
     * Uses default vertex shader.
     * @param name shader name
     * @param fragPath resource path to fragment shader
     * @return If the shader was loaded
     */
    public static boolean loadShader(String name, String fragPath) {
        if (shaders.containsKey(name)) {
            System.err.println("[WARN] Shader " + name + " was already loaded");
            return true;
        }
        try {
            ShaderProgram shader = new ShaderProgram(name);
            shader.createVertexShaderResource(DEFAULT_VERTEX);
            shader.createFragmentShaderResource(fragPath);
            shader.link();
            shaders.put(name, shader);
            if (name == "main") {
                mainShader = shader;
            }
            System.out.println("Loaded shader " + name);
            shader.bind();
        } catch (Exception e) {
            System.err.println("Error on loading shader " + name + " with frag " + fragPath);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Get shader by name
     * @param name shader name
     * @return Specified shader OR <code>null</code> if the shader does not exist
     */
    public static ShaderProgram getShader(String name) {
        if (!shaders.containsKey(name)) {
            System.err.println("Shader with name \"" + name + "\" does not exist");
            return null;
        }
        return shaders.get(name);
    }

    /**
     * Disposes of all cached shaders and clears map
     */
    public static void dispose() {
        for (ShaderProgram shader : shaders.values()) {
            shader.dispose();
        }
        shaders.clear();
    }

    /**
     * The shader that is marked as main
     * @return
     */
    public static ShaderProgram getMainShader() {
        return mainShader;
    }
}
