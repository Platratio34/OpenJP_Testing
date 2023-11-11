package shaders;

import objects.Texture2D;

public class Materials {
	
	private static final String MATERIAL_UNIFORM_NAME = "materials";
	private static final int MAX_MATERIALS = 16;
	private static final String TEXTURES_UNIFORM_NAME = "textures";
	private static final int MAX_TEXTURES = 16;
	
	private Material[] materials;
	private ShaderProgram shader;

	private Texture2D[] textures;
	
	public Materials() {
		materials = new Material[MAX_MATERIALS];
		textures = new Texture2D[MAX_TEXTURES];
	}

	public void setShader(ShaderProgram shader) {
		this.shader = shader;
		
		// Texture2D defTexture = new Texture2D(2,2);
		// defTexture.fill(Color.WHITE);
		// defTexture.updateTexture();
		// Uniform defTextureUniform = new Uniform(shader, "defaultTexture");
		// defTextureUniform.setTexture2D(defTexture);
	}

	public void setMaterial(int index, Material material) {
		if (index < 0 || index >= MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 <= id < " + MAX_MATERIALS + "; id was " + index);
		materials[index] = material;
	}
	public Material getMaterial(int index) {
		if (index < 0 || index >= MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 <= id < " + MAX_MATERIALS + "; id was " + index);
		return materials[index];
	}

	public void bind() {
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
	
	// public void setTexture(int texId, Texture2D texture) {
	// 	textures[texId] = texture;
	// 	Uniform textureUniform = new Uniform(shader, TEXTURES_UNIFORM_NAME + "[" + texId + "]");
	// 	textureUniform.setTexture2D(texture, texId);
	// }

	public Texture2D getTexture(int texId) {
		return textures[texId];
	}
	
}
