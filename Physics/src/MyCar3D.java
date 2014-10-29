import hidrogine.lwjgl.BufferObject;
import hidrogine.lwjgl.Group;
import hidrogine.lwjgl.Material;
import hidrogine.lwjgl.Model3D;
import hidrogine.lwjgl.Object3D;
import hidrogine.lwjgl.ShaderProgram;
import hidrogine.math.BoundingFrustum;
import hidrogine.math.BoundingSphere;
import hidrogine.math.ContainmentType;
import hidrogine.math.IVector3;
import hidrogine.math.Matrix;
import hidrogine.math.Vector3;

public class MyCar3D extends Object3D {

	private float time;
	private static final Matrix IDENTITY = new Matrix().identity();
	/** The Constant ROTATION. */
	private static final Matrix ROTATION = new Matrix();

	public MyCar3D(IVector3 position, Model3D model) {
		super(position, model);
		// TODO Auto-generated constructor stub
	}

	public void addTime(float dt) {
		time += dt;
	}

	@Override
	public void draw(final ShaderProgram program, final BoundingFrustum frustum) {

		final Matrix modelMatrix = getModelMatrix();
		final Model3D model = (Model3D) getModel();
		for (Group g : model.getGroups()) {
			for (BufferObject b : g.getBuffers()) {
				final BoundingSphere sph = new BoundingSphere(b);
				sph.getCenter().add(modelMatrix.getTranslation());
				if (frustum.contains(sph) != ContainmentType.Disjoint) {
					b.bind(program);
					final Material m = b.getMaterial();
					final Matrix matrix = new Matrix().identity();

					if (m.getName().equals("c0")) {
						program.setDiffuseColor(
								(float) (Math.sin(time) + 1) / 2f,
								(float) (Math.cos(time * Math.E / 2) + 1) / 2f,
								(float) (Math.sin(time * Math.PI / 2)
										* Math.cos(time * Math.PI / 2) + 1) / 2f);
					}
					if (g.getName().startsWith("w")
							&& g.getName().length() == 2) {
						final IVector3 center = new Vector3(g.getCenter())
								.multiply(-1f);
						matrix.translate(center).multiply(
								ROTATION.createRotationX(time * 48));
						center.multiply(-1f);
						matrix.translate(center);
					}

					program.setModelMatrix(matrix.multiply(modelMatrix));
					TheQuadExampleMoving.draws++;

					b.draw(program);
					program.setModelMatrix(IDENTITY);
				}
			}
		}
	}
}