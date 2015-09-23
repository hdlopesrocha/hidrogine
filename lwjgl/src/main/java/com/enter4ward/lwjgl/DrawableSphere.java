package com.enter4ward.lwjgl;

import org.lwjgl.opengl.GL11;

import com.enter4ward.math.BoundingSphere;
import com.enter4ward.math.Matrix;

public class DrawableSphere {

	BufferObject obj = new BufferObject(false);


	
	public DrawableSphere() {
		
		float[] packedVector = { 0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.866f,-0.5f,0.0f,0.8474f,-0.5309f,0.0f,0.8333f,-0.25f,0.866f,-0.433f,-0.2654f,0.8474f,-0.4598f,0.0833f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.0833f,1.0f,-0.433f,0.866f,-0.25f,-0.4598f,0.8474f,-0.2654f,0.1667f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.1667f,1.0f,-0.5f,0.866f,0.0f,-0.5309f,0.8474f,0.0f,0.25f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.25f,1.0f,-0.433f,0.866f,0.25f,-0.4598f,0.8474f,0.2654f,0.3333f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.3333f,1.0f,-0.25f,0.866f,0.433f,-0.2655f,0.8474f,0.4598f,0.4167f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.4167f,1.0f,0.0f,0.866f,0.5f,0.0f,0.8474f,0.5309f,0.5f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.5f,1.0f,0.25f,0.866f,0.433f,0.2654f,0.8474f,0.4598f,0.5833f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.5833f,1.0f,0.433f,0.866f,0.25f,0.4598f,0.8474f,0.2655f,0.6667f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.6667f,1.0f,0.5f,0.866f,0.0f,0.5309f,0.8474f,0.0f,0.75f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.75f,1.0f,0.433f,0.866f,-0.25f,0.4598f,0.8474f,-0.2654f,0.8333f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.8333f,1.0f,0.25f,0.866f,-0.433f,0.2655f,0.8474f,-0.4598f,0.9167f,0.8333f,0.0f,1.0f,0.0f,0.0f,1.0f,0.0f,0.9167f,1.0f,0.0f,0.866f,-0.5f,0.0f,0.8474f,-0.5309f,1.0f,0.8333f,0.0f,0.5f,-0.866f,0.0f,0.4823f,-0.876f,0.0f,0.6667f,-0.433f,0.5f,-0.75f,-0.438f,0.4823f,-0.7586f,0.0833f,0.6667f,-0.75f,0.5f,-0.433f,-0.7586f,0.4823f,-0.438f,0.1667f,0.6667f,-0.866f,0.5f,0.0f,-0.876f,0.4823f,0.0f,0.25f,0.6667f,-0.75f,0.5f,0.433f,-0.7586f,0.4823f,0.438f,0.3333f,0.6667f,-0.433f,0.5f,0.75f,-0.438f,0.4823f,0.7586f,0.4167f,0.6667f,0.0f,0.5f,0.866f,0.0f,0.4823f,0.876f,0.5f,0.6667f,0.433f,0.5f,0.75f,0.438f,0.4823f,0.7586f,0.5833f,0.6667f,0.75f,0.5f,0.433f,0.7586f,0.4823f,0.438f,0.6667f,0.6667f,0.866f,0.5f,0.0f,0.876f,0.4823f,0.0f,0.75f,0.6667f,0.75f,0.5f,-0.433f,0.7586f,0.4823f,-0.438f,0.8333f,0.6667f,0.433f,0.5f,-0.75f,0.438f,0.4823f,-0.7586f,0.9167f,0.6667f,0.0f,0.5f,-0.866f,0.0f,0.4823f,-0.876f,1.0f,0.6667f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.5f,-0.5f,0.0f,-0.866f,-0.5f,0.0f,-0.866f,0.0833f,0.5f,-0.866f,0.0f,-0.5f,-0.866f,0.0f,-0.5f,0.1667f,0.5f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.0f,0.25f,0.5f,-0.866f,0.0f,0.5f,-0.866f,0.0f,0.5f,0.3333f,0.5f,-0.5f,0.0f,0.866f,-0.5f,0.0f,0.866f,0.4167f,0.5f,0.0f,0.0f,1.0f,0.0f,0.0f,1.0f,0.5f,0.5f,0.5f,0.0f,0.866f,0.5f,0.0f,0.866f,0.5833f,0.5f,0.866f,0.0f,0.5f,0.866f,0.0f,0.5f,0.6667f,0.5f,1.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.75f,0.5f,0.866f,0.0f,-0.5f,0.866f,0.0f,-0.5f,0.8333f,0.5f,0.5f,0.0f,-0.866f,0.5f,0.0f,-0.866f,0.9167f,0.5f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,1.0f,0.5f,0.0f,-0.5f,-0.866f,0.0f,-0.4823f,-0.876f,0.0f,0.3333f,-0.433f,-0.5f,-0.75f,-0.438f,-0.4823f,-0.7586f,0.0833f,0.3333f,-0.75f,-0.5f,-0.433f,-0.7586f,-0.4823f,-0.438f,0.1667f,0.3333f,-0.866f,-0.5f,0.0f,-0.876f,-0.4823f,0.0f,0.25f,0.3333f,-0.75f,-0.5f,0.433f,-0.7586f,-0.4823f,0.438f,0.3333f,0.3333f,-0.433f,-0.5f,0.75f,-0.438f,-0.4823f,0.7586f,0.4167f,0.3333f,0.0f,-0.5f,0.866f,0.0f,-0.4823f,0.876f,0.5f,0.3333f,0.433f,-0.5f,0.75f,0.438f,-0.4823f,0.7586f,0.5833f,0.3333f,0.75f,-0.5f,0.433f,0.7586f,-0.4823f,0.438f,0.6667f,0.3333f,0.866f,-0.5f,0.0f,0.876f,-0.4823f,0.0f,0.75f,0.3333f,0.75f,-0.5f,-0.433f,0.7586f,-0.4823f,-0.438f,0.8333f,0.3333f,0.433f,-0.5f,-0.75f,0.438f,-0.4823f,-0.7586f,0.9167f,0.3333f,0.0f,-0.5f,-0.866f,0.0f,-0.4823f,-0.876f,1.0f,0.3333f,0.0f,-0.866f,-0.5f,0.0f,-0.8474f,-0.5309f,0.0f,0.1667f,-0.25f,-0.866f,-0.433f,-0.2654f,-0.8474f,-0.4598f,0.0833f,0.1667f,-0.433f,-0.866f,-0.25f,-0.4598f,-0.8474f,-0.2654f,0.1667f,0.1667f,-0.5f,-0.866f,0.0f,-0.5309f,-0.8474f,0.0f,0.25f,0.1667f,-0.433f,-0.866f,0.25f,-0.4598f,-0.8474f,0.2654f,0.3333f,0.1667f,-0.25f,-0.866f,0.433f,-0.2655f,-0.8474f,0.4598f,0.4167f,0.1667f,0.0f,-0.866f,0.5f,0.0f,-0.8474f,0.5309f,0.5f,0.1667f,0.25f,-0.866f,0.433f,0.2654f,-0.8474f,0.4598f,0.5833f,0.1667f,0.433f,-0.866f,0.25f,0.4598f,-0.8474f,0.2655f,0.6667f,0.1667f,0.5f,-0.866f,0.0f,0.5309f,-0.8474f,0.0f,0.75f,0.1667f,0.433f,-0.866f,-0.25f,0.4598f,-0.8474f,-0.2654f,0.8333f,0.1667f,0.25f,-0.866f,-0.433f,0.2655f,-0.8474f,-0.4598f,0.9167f,0.1667f,0.0f,-0.866f,-0.5f,0.0f,-0.8474f,-0.5309f,1.0f,0.1667f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.0f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.0833f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.1667f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.25f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.3333f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.4167f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.5f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.5833f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.6667f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.75f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.8333f,0.0f,0.0f,-1.0f,0.0f,0.0f,-1.0f,0.0f,0.9167f,0.0f};
		
		short[] ii = { 
				0,1,2,3,2,4,5,4,6,7,6,8,9,8,10,11,10,12,13,12,14,15,14,16,17,16,18,19,18,20,21,20,22,23,22,24,1,25,26,1,26,2,2,26,27,2,27,4,4,27,28,4,28,6,6,28,29,6,29,8,8,29,30,8,30,10,10,30,31,10,31,12,12,31,32,12,32,14,14,32,33,14,33,16,16,33,34,16,34,18,18,34,35,18,35,20,20,35,36,20,36,22,22,36,37,22,37,24,25,38,39,25,39,26,26,39,40,26,40,27,27,40,41,27,41,28,28,41,42,28,42,29,29,42,43,29,43,30,30,43,44,30,44,31,31,44,45,31,45,32,32,45,46,32,46,33,33,46,47,33,47,34,34,47,48,34,48,35,35,48,49,35,49,36,36,49,50,36,50,37,38,51,52,38,52,39,39,52,53,39,53,40,40,53,54,40,54,41,41,54,55,41,55,42,42,55,56,42,56,43,43,56,57,43,57,44,44,57,58,44,58,45,45,58,59,45,59,46,46,59,60,46,60,47,47,60,61,47,61,48,48,61,62,48,62,49,49,62,63,49,63,50,51,64,65,51,65,52,52,65,66,52,66,53,53,66,67,53,67,54,54,67,68,54,68,55,55,68,69,55,69,56,56,69,70,56,70,57,57,70,71,57,71,58,58,71,72,58,72,59,59,72,73,59,73,60,60,73,74,60,74,61,61,74,75,61,75,62,62,75,76,62,76,63,77,65,64,78,66,65,79,67,66,80,68,67,81,69,68,82,70,69,83,71,70,84,72,71,85,73,72,86,74,73,87,75,74,88,76,75
					   };

		for (int i = 0; i < packedVector.length; i += 8) {
			obj.addVertex(packedVector[i + 0], packedVector[i + 1],
					packedVector[i + 2]);

			obj.addNormal(packedVector[i + 3], packedVector[i + 4],
					packedVector[i + 5]);
			obj.addTexture(packedVector[i + 6], packedVector[i + 7]);

		}

		for (int i = 0; i < ii.length; ++i) {
			obj.addIndex(ii[i]);
		}

		obj.buildBuffer();
	}

	private static final Matrix TEMP_SCALE = new Matrix();
	/**
	 * Draw.
	 *
	 * @param shader
	 *            the shader
	 */
	public final void draw(final ShaderProgram shader, BoundingSphere sph) {
		//System.out.println(min.toString()+" : "+max.toString());
		shader.setAmbientColor(1, 1, 1);
		shader.setMaterialAlpha(0.2f);
		GL11.glDisable(GL11.GL_CULL_FACE);
		shader.setModelMatrix(TEMP_SCALE.createScale(sph.getRadius()).translate(sph));
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		obj.draw(shader);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	

		GL11.glEnable(GL11.GL_CULL_FACE);


	}


}
