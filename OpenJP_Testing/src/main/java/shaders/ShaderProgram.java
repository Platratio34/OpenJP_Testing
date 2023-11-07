package shaders;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import objects.Texture2D;

import java.awt.Color;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;
    private Texture2D defTexture;

    private HashMap<String, Integer> knownUniforms;

    public ShaderProgram() throws Exception {
        programId = GL33.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        defTexture = new Texture2D(2,2);
        defTexture.fill(Color.WHITE);
        defTexture.updateTexture();
        knownUniforms = new HashMap<String, Integer>();
        // int defTextureUniform = getUniform("defaultTexture");
        // Uniform.setTexture2D(defTextureUniform, defTexture);
//        int textureId = GL33.glGenTexture();
//        GL33.glActiveTexture(GL33.GL_TEXTURE0);
//        GL33.glBindTexture()
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL33.GL_VERTEX_SHADER);
    }
    public void createVertexShaderResource(String filename) throws Exception {
        URL res = getClass().getClassLoader().getResource(filename);
        if (res == null) {
            throw new Exception("Could not load vertex shader from resources: " + filename);
            // System.err.println("Could not load vertex shader from resources: "+filename);
            // return;
        }
    	List<String> lines = Files.readAllLines(Paths.get(res.toURI()), StandardCharsets.UTF_8);
    	String code = "";
    	for(int i = 0 ; i < lines.size(); i++) {
    		code += lines.get(i) + "\n";
    	}
    	createVertexShader(code);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL33.GL_FRAGMENT_SHADER);
    }

    public void createFragmentShaderResource(String filename) throws Exception {
        URL res = getClass().getClassLoader().getResource(filename);
        if (res == null) {
            throw new Exception("Could not load fragment shader from resources: " + filename);
            // System.err.println("Could not load fragment shader from resources: "+filename);
            // return;
        }
    	List<String> lines = Files.readAllLines(Paths.get(res.toURI()), StandardCharsets.UTF_8);
    	String code = "";
    	for(int i = 0 ; i < lines.size(); i++) {
    		code += lines.get(i) + "\n";
    	}
    	createFragmentShader(code);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = GL33.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        GL33.glShaderSource(shaderId, shaderCode);
        GL33.glCompileShader(shaderId);

        if (GL33.glGetShaderi(shaderId, GL33.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + GL33.glGetShaderInfoLog(shaderId, 1024));
        }

        GL33.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
    	GL33.glLinkProgram(programId);
        if (GL33.glGetProgrami(programId, GL33.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + GL33.glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
        	GL33.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
        	GL33.glDetachShader(programId, fragmentShaderId);
        }

        GL33.glValidateProgram(programId);
        if (GL33.glGetProgrami(programId, GL33.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL33.glGetProgramInfoLog(programId, 1024));
        }

    }

    public void bind() {
    	GL33.glUseProgram(programId);
    }

    public void unbind() {
    	GL33.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
        	GL33.glDeleteProgram(programId);
        }
    }

	public int getUniform(String name) throws UniformException {
        if(knownUniforms.containsKey(name)) return knownUniforms.get(name);
		int id = GL33.glGetUniformLocation(programId, name);
		if(id < 0) {
            System.err.println("Could not find uniform '"+name+"' in shader");
            // throw new UniformException("Could not find uniform '"+name+"' in shader");
		}
        knownUniforms.put(name, id);
		return id;
	}
	
	public void uniformSetBoolean(String name, boolean val) throws UniformException {
		Uniform.setBoolean(getUniform(name), val);
	}
	public void uniformSetColor(String name, Color val) throws UniformException {
		Uniform.setColor(getUniform(name), val);
	}
	public void uniformSetColor4(String name, Color val) throws UniformException {
		Uniform.setColor4(getUniform(name), val);
	}
	public void uniformSetFloat(String name, float val) throws UniformException {
		Uniform.setFloat(getUniform(name), val);
	}
	public void uniformSetVector3f(String name, Vector3f val) throws UniformException {
		Uniform.setVector3f(getUniform(name), val);
	}
	public void uniformSetInt1(String name, int val) throws UniformException {
		GL33.glUniform1i(getUniform(name), val);
	}
}