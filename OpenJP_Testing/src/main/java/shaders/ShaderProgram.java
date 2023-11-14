package shaders;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL44;

import java.awt.Color;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


/** 
 * Shader program
 */
public class ShaderProgram {

    /** OpenGL program ID */
    private final int programId;

    /** ID of vertex shader */
    private int vertexShaderId;

    /** ID of fragment shader */
    private int fragmentShaderId;

    /** Map of all known uniform names to IDs */
    private HashMap<String, Integer> knownUniforms;

    /** UBO binding point for camera data */
    public static final int CAMERA_UNIFORM_BLOCK = 1;

    /** Name of the shader program */
    private String name;

    /** Currently bound shader in OpenGL */
    public static int currentBoundShader = 0;

    /**
     * Create a new un-named shader program
     * @throws Exception if the shader program could not be created in OpenGL
     */
    public ShaderProgram() throws Exception {
        this("");
    }

    /**
     * Create a new named shader program
     * @param name shader program name
     * @throws Exception if the shader program could not be created in OpenGL
     */
    public ShaderProgram(String name) throws Exception {
        this.name = name;
        programId = GL44.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader "+name);
        }
        knownUniforms = new HashMap<String, Integer>();
    }

    /**
     * Creates a vertex shader from string
     * @param shaderCode GLSL vertex shader code
     * @throws Exception if the shader could not be created
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL44.GL_VERTEX_SHADER);
        // int blockId = GL44.glGetUniformBlockIndex(programId, "Camera");
        // GL44.glUniformBlockBinding(programId, blockId, CAMERA_UNIFORM_BLOCK);
    }
    /**
     * Create a vertex shader from GLSL resource file
     * @param filename resource path
     * @throws Exception if the shader could not be created
     */
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

    /**
     * Creates a fragment shader from string
     * @param shaderCode GLSL fragment shader code
     * @throws Exception if the shader could not be created
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL44.GL_FRAGMENT_SHADER);
    }
    /**
     * Create a fragment shader from GLSL resource file
     * @param filename resource path
     * @throws Exception if the shader could not be created
     */
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

    /**
     * Create a new shader from code string
     * @param shaderCode GLSL code
     * @param shaderType type of shader. (<code>GL*.GL_*_SHADER</code>)
     * @return OpengL ID of the shader
     * @throws Exception if the shader could not be created
     */
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

    /**
     * Link the shader program program
     * @throws Exception if linking failed
     */
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

    /**
     * Bind the shader program for rendering
     */
    public void bind() {
        if(currentBoundShader == programId) return;
        GL44.glUseProgram(programId);
        currentBoundShader = programId;
    }

    /**
     * Unbind the shader program from rendering
     */
    public void unbind() {
    	GL44.glUseProgram(0);
        currentBoundShader = 0;
    }

    /**
     * Disposes of the shader program
     */
    public void dispose() {
        unbind();
        if (programId != 0) {
        	GL44.glDeleteProgram(programId);
        }
    }

    /**
     * Get uniform ID from this shader program. <br><br>
     * Caches uniform IDs on first call for name.
     * @param name uniform name
     * @return Uniform ID OR <code>0</code> if the uniform could not be found
     */
    // @throws UniformException if the uniform could not be found in the shader program
	public int getUniform(String name) {
        if(knownUniforms.containsKey(name)) return knownUniforms.get(name);
		int id = GL44.glGetUniformLocation(programId, name);
		if(id < 0) {
            System.err.println("Could not find uniform '"+name+"' in shader "+this.name);
            // throw new UniformException("Could not find uniform '"+name+"' in shader");
		}
        knownUniforms.put(name, id);
		return id;
	}
	
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
	public void uniformSetBoolean(String name, boolean val) {
        bind();
		Uniform.setBoolean(getUniform(name), val);
	}
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
	public void uniformSetColor(String name, Color val) {
        bind();
		Uniform.setColor(getUniform(name), val);
	}
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
	public void uniformSetColor4(String name, Color val) {
        bind();
		Uniform.setColor4(getUniform(name), val);
	}
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
	public void uniformSetFloat(String name, float val) {
        bind();
		Uniform.setFloat(getUniform(name), val);
	}
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
	public void uniformSetVector3f(String name, Vector3f val) {
        bind();
		Uniform.setVector3f(getUniform(name), val);
	}
    /**
     * Set name uniform value.<br>
     * <br>
     * <b>Binds this shader program</b>
     * @param name uniform name
     * @param val uniform value
     */
    public void uniformSetInt1(String name, int val) {
        bind();
        GL44.glUniform1i(getUniform(name), val);
    }

    /**
     * Get the name of the shader
     * @return Shader name
     */
    public String getName() {
        return name;
    }
}