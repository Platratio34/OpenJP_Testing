package shaders;

public class Materials {
	
	private Material[] materials;
	private ShaderProgram shader;
	private final String MATERIAL_UNIFORM_NAME = "materials";
	private final int MAX_MATERIALS = 16;
	
	public Materials(ShaderProgram shader) {
		this.shader = shader;
	}

	public void updateMaterial(Material material) {
		if(material.getMatId() < 1 || material.getMatId() > MAX_MATERIALS) throw new IndexOutOfBoundsException("Invalid Material ID: 0 < id < "+MAX_MATERIALS+"; id was "+material.getMatId());
		materials[material.getMatId()] = material;
		material.updateShader(shader, MATERIAL_UNIFORM_NAME+"["+material.getMatId()+"]");
	}
	
	
}
