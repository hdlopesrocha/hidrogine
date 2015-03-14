package hidrogine.lwjgl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import hidrogine.math.IBufferBuilder;
import hidrogine.math.Group;
import hidrogine.math.IBufferObject;
import hidrogine.math.IObject3D;
import hidrogine.math.ITextureLoader;
import hidrogine.math.Material;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import hidrogine.math.Model3D;

import org.lwjgl.opengl.GL11;

// TODO: Auto-generated Javadoc
/**
 * The Class Model3D.
 */
public class LWJGLModel3D extends Model3D {

	/**
	 * Instantiates a new model3 d.
	 *
	 * @param geometry
	 *          the geometry
	 * @param scale
	 *          the scale
	 * @param builder
	 *          the builder
	 * @throws FileNotFoundException
	 *           the file not found exception
	 */

	public LWJGLModel3D(String filename, float scale, Quaternion quat, IBufferBuilder builder)
			throws FileNotFoundException {
		super(new FileInputStream(filename), scale, builder, quat);

		loadTextures();
	}

	public LWJGLModel3D(String filename, float scale, IBufferBuilder builder)
			throws FileNotFoundException {
		super(new FileInputStream(filename), scale, builder, new Quaternion().identity());

		loadTextures();
	}
	
	/**
	 * Load textures.
	 */
	public void loadTextures() {
		
		
		for (final Material m : materials.values()) {

			m.load(new ITextureLoader() {

				@Override
				public int load() {

					return TextureLoader.loadTexture(m.filename);
				}
			});
		}
	}

	/**
	 * Draw.
	 *
	 * @param obj
	 *          the obj
	 * @param shader
	 *          the shader
	 * @param handler
	 *          the handler
	 */
	public void draw(IObject3D obj, ShaderProgram shader, DrawHandler handler) {
		for (Group g : groups) {
			for (IBufferObject ib : g.getBuffers()) {
				BufferObject b = (BufferObject) ib;

				b.bind(shader);
				Matrix mat = handler.onDraw(obj, g, b);
				if (mat != null) {
					shader.setModelMatrix(mat);
					b.draw(shader);
				}
			}
		}

	}
}