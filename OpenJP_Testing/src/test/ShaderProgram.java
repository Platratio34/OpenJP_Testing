package test;

import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.glGetUniformLocation;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
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
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
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
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }
    public void createShaderFile(String filename, int shaderType) throws Exception {
    	List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
    	String code = "";
    	for(int i = 0 ; i < lines.size(); i++) {
    		code += lines.get(i) + "\n";
    	}
    	createShader(code, shaderType);
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

	public int getUniform(String name) {
		return glGetUniformLocation(programId, name);
	}
}