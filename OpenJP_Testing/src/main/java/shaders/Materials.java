package shaders;

import objects.Texture2D;
import java.awt.Color;

public class Materials {
	
	private static final String MATERIAL_UNIFORM_NAME = "materials";
	private static final int MAX_MATERIALS = 16;
	private static final String TEXTURES_UNIFORM_NAME = "textures";
	private static final int MAX_TEXTURES = 16;
	
	private Material[] materials;
	private ShaderProgram shader;

	private Texture2D defTexture;
	private Texture2D[] textures;
	
	public Materials(ShaderProgram shader) {
		this.shader = shader;

		materials = new Material[MAX_MATERIALS];
		textures = new Texture2D[MAX_TEXTURES];

        defTexture = new Texture2D(2,2);
        defTexture.fill(Color.WHITE);
        defTexture.updateTexture();
        int defTextureUniform = shader.getUniform("defaultTexture");
        Uniform.setTexture2D(defTextureUniform, defTexture);
	}

	public void updateMaterial(Material material) {
		if (material.getMatId() < 1 || material.getMatId() > MAX_MATERIALS)
			throw new IndexOutOfBoundsException(
					"Invalid Material ID: 0 < id < " + MAX_MATERIALS + "; id was " + material.getMatId());
		materials[material.getMatId()] = material;
		material.updateShader(shader, MATERIAL_UNIFORM_NAME + "[" + material.getMatId() + "]");
	}
	
	public void setTexture(int texId, Texture2D texture) {
		textures[texId] = texture;
		int uId = shader.getUniform(TEXTURES_UNIFORM_NAME + "[" + texId + "]");
		Uniform.setTexture2D(uId, texture);
	}

	public Texture2D getTexture(int texId) {
		return textures[texId];
	}
	
}
