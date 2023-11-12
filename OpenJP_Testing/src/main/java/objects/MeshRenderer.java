package objects;

public class MeshRenderer extends Renderer {

	protected Mesh mesh;
	
	@Deprecated
	public MeshRenderer(Mesh mesh, Transform transform) {
		super(transform);
		this.mesh = mesh;
	}
	public MeshRenderer(Transform transform, Mesh mesh) {
		super(transform);
		this.mesh = mesh;
	}
	
	@Override
	public void render() {
		if (!visible)
			return;
		super.render();
		mesh.render();
	}
}
