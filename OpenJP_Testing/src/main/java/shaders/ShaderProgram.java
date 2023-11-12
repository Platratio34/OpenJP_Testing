package shaders;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL44;

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

    public static final int CAMERA_UNIFORM_BLOCK = 1;

    private Uniform defaultTextUniform;
    private String name;

    public ShaderProgram() throws Exception {
        this("");
    }

    public ShaderProgram(String name) throws Exception {
        this.name = name;
        programId = GL44.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader "+name);
        }
        defTexture = new Texture2D(2,2);
        // defTexture.fill(Color.WHITE);
        // defTexture.updateTexture();
        knownUniforms = new HashMap<String, Integer>();
        // int defTextureUniform = getUniform("defaultTexture");
        // Uniform.setTexture2D(defTextureUniform, defTexture);
//        int textureId = GL44.glGenTexture();
//        GL44.glActiveTexture(GL44.GL_TEXTURE0);
//        GL44.glBindTexture()
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL44.GL_VERTEX_SHADER);
        // int blockid = GL44.glGetUniformBlockIndex(programId, "Camera");
        // GL44.glUniformBlockBinding(programId, blockid, CAMERA_UNIFORM_BLOCK);
    }
    public void createVertexShaderResource(String filename) throws Exception {
        URL res = getClass().getClassLoader().getResource(filename);
        if (res == null) {
            throw new Exception("Could not load vertex shader from resources: " + filename + "; File could not be found");
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
        fragmentShaderId = createShader(shaderCode, GL44.GL_FRAGMENT_SHADER);
    }

    public void createFragmentShaderResource(String filename) throws Exception {
        URL res = getClass().getClassLoader().getResource(filename);
        if (res == null) {
            throw new Exception("Could not load fragment shader from resources: " + filename + "; File could not be found");
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
        int shaderId = GL44.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        GL44.glShaderSource(shaderId, shaderCode);
        GL44.glCompileShader(shaderId);

        if (GL44.glGetShaderi(shaderId, GL44.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + GL44.glGetShaderInfoLog(shaderId, 1024));
        }

        GL44.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        
    	GL44.glLinkProgram(programId);
        if (GL44.glGetProgrami(programId, GL44.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + GL44.glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
        	GL44.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
        	GL44.glDetachShader(programId, fragmentShaderId);
        }

        GL44.glValidateProgram(programId);
        if (GL44.glGetProgrami(programId, GL44.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL44.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        GL44.glUseProgram(programId);
        if (defaultTextUniform == null) {
            defaultTextUniform = new Uniform(this, "defaultTexture");
            defaultTextUniform.setTexture2D(defTexture);
        }
    }

    public void unbind() {
    	GL44.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
        	GL44.glDeleteProgram(programId);
        }
    }

	public int getUniform(String name) throws UniformException {
        if(knownUniforms.containsKey(name)) return knownUniforms.get(name);
		int id = GL44.glGetUniformLocation(programId, name);
		if(id < 0) {
            System.err.println("Could not find uniform '"+name+"' in shader "+this.name);
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
		GL44.glUniform1i(getUniform(name), val);
	}
}