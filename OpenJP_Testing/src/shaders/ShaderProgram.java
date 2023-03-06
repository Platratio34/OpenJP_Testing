package shaders;

//import static org.lwjgl.opengl.GL30.glGetUniformLocation;
import org.lwjgl.opengl.GL33;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    public ShaderProgram() throws Exception {
        programId = GL33.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL33.GL_VERTEX_SHADER);
    }
    public void createVertexShaderFile(String filename) throws Exception {
    	List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
    	String code = "";
    	for(int i = 0 ; i < lines.size(); i++) {
    		code += lines.get(i) + "\n";
    	}
    	createVertexShader(code);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL33.GL_FRAGMENT_SHADER);
    }
    public void createFragmentShaderFile(String filename) throws Exception {
    	List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
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

	public int getUniform(String name) {
		int id = GL33.glGetUniformLocation(programId, name);
		if(id < 0) {
			System.err.println("Could not find uniform '"+name+"' in shader");
		}
		return id;
	}
}