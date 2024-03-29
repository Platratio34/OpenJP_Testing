package objects;

/**
 * Renderer for standard mesh.<br><br>
 * <b>Shader specific</b>
 */
public class MeshRenderer extends Renderer {

	/**
	 * The mesh that will be rendered
	 */
	protected Mesh mesh;
	
	@Deprecated
	public MeshRenderer(Mesh mesh, Transform transform) {
		super(transform);
		this.mesh = mesh;
	}
	/**
	 * Create a new renderer for given mesh
	 * @param transform transformation for renderer
	 * @param mesh mesh to render
	 */
	public MeshRenderer(Transform transform, Mesh mesh) {
		super(transform);
		this.mesh = mesh;
	}

	public MeshRenderer(Mesh mesh) {
		super();
		this.mesh = mesh;
	}
	
	@Override
	protected void onRender() {
		mesh.render();
	}
}
