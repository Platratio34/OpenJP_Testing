package objects;

import org.joml.Matrix4f;

import game.Component;
import shaders.Materials;
import shaders.ShaderProgram;
import shaders.Uniform;

/**
 * Generic renderer<br><br>
 * <b>Shader specific</b>
 */
public abstract class Renderer extends Component {
    
	/**
	 * Transform used for rendering
	 */
	public Transform transform;
	/**
	 * Shader to render with
	 */
	protected ShaderProgram shader;
	
	/**
	 * Uniform for transformation matrix
	 */
	protected Uniform matrixUniform;
	/**
	 * Uniform for vertex based (non material) coloring
	 */
	protected Uniform useColorsUniform;

	/**
	 * If the renderer will be drawn
	 */
	protected boolean visible = true;

	/**
	 * Material collection
	 */
	public Materials materials;
	
	/**
	 * Create a new renderer with given transform
	 * @param transform renderer transformation
	 */
	public Renderer(Transform transform) {
		this.transform = transform;
		materials = new Materials();
	}

	/**
	 * Set the shader to be used by the renderer<br><br>
	 * Updates uniform pointers and material collection shader.
	 * @param shader shader to be used
	 */
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		matrixUniform = new Uniform(shader, "transformMatrix");
		useColorsUniform = new Uniform(shader, "useColor");
		materials.setShader(shader);
	}
	
	/**
	 * Checks if the renderer should be drawn
	 * @return If renderer is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set if the renderer should be drawn
	 * @param visible if renderer should be drawn
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Render the renderer if it is visible<br><br>
	 * Calls <code>onRender()<br> for child classes
	 */
    public void render() {
        if (!visible)
            return;
        shader.bind();
        Matrix4f matrix = transform.getTransformMatrix();
        matrixUniform.setMatrix4f(matrix);
        if (materials.getMaterial(0) != null) {
            // colorsUniform.setColorArray(colors);
            useColorsUniform.setBoolean(false);
            materials.bind();
		} else {
			useColorsUniform.setBoolean(true);
		}
		onRender();
    }
	/**
	 * Called when the renderer is rendered.<br><br>
	 * Shader will already be bound, and generic uniforms will be set.
	 */
	protected abstract void onRender();
	
	@Override
	public void onStart() {
		
	}
	@Override
	public void onTick() {
		
	}
	@Override
	public void onCleanup() {
		
	}
    
}
