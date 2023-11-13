package shaders;

/**
 * Collection of materials for a renderer.<br>
 * <br>
 * <b>Shader specific</b>
 */
public class Materials {
	
	/**
	 * Standard uniform name for materials uniform
	 */
	private static final String MATERIAL_UNIFORM_NAME = "materials";
	/**
	 * Maximum number of materials
	 */
	public static final int MAX_MATERIALS = 16;
	
	private Material[] materials;
	private ShaderProgram shader;
	
	/**
	 * Creates a new unbound material collection
	 */
	public Materials() {
		materials = new Material[MAX_MATERIALS];
	}

	/**
	 * Set the shader for the collection
	 * @param shader
	 */
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		
		// Texture2D defTexture = new Texture2D(2,2);
		// defTexture.fill(Color.WHITE);
		// defTexture.updateTexture();
		// Uniform defTextureUniform = new Uniform(shader, "defaultTexture");
		// defTextureUniform.setTexture2D(defTexture);
	}
	
	/**
	 * Set the material in a specific slot.<br><br>
	 * Must be between 0 and <code>MAX_MATERIALS</code>
	 * @param index slot index between 0 and <code>MAX_MATERIALS</code>
	 * @param material material to set
	 */
	public void setMaterial(int index, Material material) {
		if (index < 0 || index >= MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 <= id < " + MAX_MATERIALS + "; id was " + index);
		materials[index] = material;
	}
	/**
	 * Get the material currently in the slot
	 * @param index slot index between 0 and <code>MAX_MATERIALS</code>
	 * @return Current material in slot, may be <code>null</code>
	 */
	public Material getMaterial(int index) {
		if (index < 0 || index >= MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 <= id < " + MAX_MATERIALS + "; id was " + index);
		return materials[index];
	}

	/**
	 * Bind the collection to the set shader<br><br>
	 * <b>Binds the shader set with <code>setShader()</code></b>
	 */
	public void bind() {
		shader.bind();
		for(int i = 0; i < materials.length; i++) {
			if(materials[i] == null) break;
			updateMaterial(i, materials[i]);
		}
	}

	private void updateMaterial(int index, Material material) {
		if (index < 0 || index >= MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 <= id < " + MAX_MATERIALS + "; id was " + index);
		// materials[material.getMatId()] = material;
		material.updateShader(shader, MATERIAL_UNIFORM_NAME + "[" + index + "]", index);
	}
	
}
