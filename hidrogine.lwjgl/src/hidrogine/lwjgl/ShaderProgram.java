package hidrogine.lwjgl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ShaderProgram {

    public int mPositionHandle;
    public int mNormalHandle;
    public int mTextureCoordinateHandle;
    private int projectionMatrixLocation = 0;
    private int viewMatrixLocation = 0;
    private int modelMatrixLocation = 0;
    private int cameraPositionLocation = 0;
    private int materialShininessLocation = 0;
    private int materialAlphaLocation = 0;
    private int materialSpecularLocation = 0;

    private int opaqueLocation = 0;
    private int ambientColorLocation = 0;
    private int diffuseColorLocation = 0;

    private int[] lightPositionLocation = new int[10];
    private int[] lightSpecularColorLocation = new int[10];
    /** The model matrix. */
    private Matrix4f modelMatrix = null;
    private Vector3f[] lightPosition = new Vector3f[10];
    private Vector3f[] lightSpecularColor = new Vector3f[10];

    Stack<Matrix4f> matrixStack = new Stack<Matrix4f>();
    private FloatBuffer matrix44Buffer = null;

    private int program = 0;

    public ShaderProgram(String vertexShader, String fragShader)
            throws Exception {
        // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
        modelMatrix = new Matrix4f();
        modelMatrix.setIdentity();
        // Load the vertex shader
        int vsId = this.createShader("vertex.glsl", ARBVertexShader.GL_VERTEX_SHADER_ARB);
        // Load the fragment shader
        int fsId = this.createShader("fragment.glsl", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

        // Create a new shader program that links both shaders
        program = ARBShaderObjects.glCreateProgramObjectARB();

        /*
         * if the vertex and fragment shaders setup sucessfully, attach them to
         * the shader program, link the sahder program (into the GL context I
         * suppose), and validate
         */
        ARBShaderObjects.glAttachObjectARB(program, vsId);
        ARBShaderObjects.glAttachObjectARB(program, fsId);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program,
                ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return;
        }
/*
        // Position information will be attribute 0
        GL20.glBindAttribLocation(program, 0, "gl_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(program, 1, "gl_Color");
        // Textute information will be attribute 2
        GL20.glBindAttribLocation(program, 2, "gl_TextureCoord");
*/
        use();
        ARBShaderObjects.glValidateProgramARB(program);

        // Get matrices uniform locations
        projectionMatrixLocation = ARBShaderObjects.glGetUniformLocationARB(program,
                "projectionMatrix");
        viewMatrixLocation = ARBShaderObjects.glGetUniformLocationARB(program, "viewMatrix");
        modelMatrixLocation = ARBShaderObjects.glGetUniformLocationARB(program, "modelMatrix");
        ambientColorLocation = ARBShaderObjects.glGetUniformLocationARB(program, "ambientColor");
        diffuseColorLocation = ARBShaderObjects.glGetUniformLocationARB(program, "diffuseColor");
        cameraPositionLocation = ARBShaderObjects.glGetUniformLocationARB(program,
                "cameraPosition");
        opaqueLocation = ARBShaderObjects.glGetUniformLocationARB(program, "opaque");

        // material locations
        materialShininessLocation = ARBShaderObjects.glGetUniformLocationARB(program,
                "materialShininess");
        materialAlphaLocation = ARBShaderObjects.glGetUniformLocationARB(program, "materialAlpha");
        materialSpecularLocation = ARBShaderObjects.glGetUniformLocationARB(program,
                "materialSpecular");

        for (int i = 0; i < 10; ++i) {
            lightPositionLocation[i] = ARBShaderObjects.glGetUniformLocationARB(program,
                    "lightPosition[" + i + "]");
            lightSpecularColorLocation[i] = ARBShaderObjects.glGetUniformLocationARB(program,
                    "lightSpecularColor[" + i + "]");

        }
        setMaterialAlpha(1f);
    }

    /**
     * Use default shader.
     */
    public void use() {
        ARBShaderObjects.glUseProgramObjectARB(program);    }

    Matrix4f modelView = new Matrix4f();

    /**
     * Sets the projection matrix.
     */
    public void update(Camera camera) {

        Matrix4f.mul(camera.getViewMatrix(), modelMatrix, modelView);
        // Upload matrices to the uniform variables
        use();

        camera.getProjectionMatrix().store(matrix44Buffer);
        matrix44Buffer.flip();
        ARBShaderObjects.glUniformMatrix4ARB(projectionMatrixLocation, false, matrix44Buffer);

        camera.getViewMatrix().store(matrix44Buffer);
        matrix44Buffer.flip();
        ARBShaderObjects.glUniformMatrix4ARB(viewMatrixLocation, false, matrix44Buffer);

        modelMatrix.store(matrix44Buffer);
        matrix44Buffer.flip();
        ARBShaderObjects.glUniformMatrix4ARB(modelMatrixLocation, false, matrix44Buffer);

        Vector3f cameraPosition = camera.getPosition();
        ARBShaderObjects.glUniform3fARB(cameraPositionLocation, cameraPosition.x,
                cameraPosition.y, cameraPosition.z);

        for (int i = 0; i < 10; ++i) {
            Vector3f position = lightPosition[i];
            Vector3f specularColor = lightSpecularColor[i];
            if (position != null) {
                ARBShaderObjects.glUniform3fARB(lightPositionLocation[i], position.x,
                        position.y, position.z);
            }
            if (specularColor != null) {
                ARBShaderObjects.glUniform3fARB(lightSpecularColorLocation[i],
                        specularColor.x, specularColor.y, specularColor.z);
            }
        }
    }

    public void setMaterialAlpha(float value) {
      
        ARBShaderObjects.glUniform1fARB(materialAlphaLocation, value);
    }

    public void setMaterialShininess(float value) {
        ARBShaderObjects.glUniform1fARB(materialShininessLocation, value);
    }

    public void setAmbientColor(float r, float g, float b) {
        ARBShaderObjects.glUniform3fARB(ambientColorLocation, r, g, b);
    }

    public void setMaterialSpecular(float r, float g, float b) {
        ARBShaderObjects.glUniform3fARB(materialSpecularLocation, r, g, b);
    }

    public void setDiffuseColor(float r, float g, float b) {
        ARBShaderObjects.glUniform3fARB(diffuseColorLocation, r, g, b);
    }

    public void setLightPosition(int index, Vector3f lightPosition) {
        this.lightPosition[index] = lightPosition;
    }

    public void setLightColor(int index, Vector3f lightColor) {
        this.lightSpecularColor[index] = lightColor;
    }

    public void setOpaque(Boolean value) {
        if (value)
            GL11.glEnable(GL11.GL_CULL_FACE);
        else
            GL11.glDisable(GL11.GL_CULL_FACE);

        ARBShaderObjects.glUniform1iARB(opaqueLocation, value ? 1 : 0);
    }

    /*
     * With the exception of syntax, setting up vertex and fragment shaders is
     * the same.
     * 
     * @param the name and path to the vertex shader
     */
    private int createShader(String filename, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader,
                    readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader,
                    ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: "
                        + getLogInfo(shader));

            return shader;
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects
                .glGetObjectParameteriARB(obj,
                        ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();

        FileInputStream in = new FileInputStream(filename);

        Exception exception = null;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Exception innerExc = null;
            try {
                String line;
                while ((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            } catch (Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch (Exception exc) {
                    if (innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }

            if (innerExc != null)
                throw innerExc;
        } catch (Exception exc) {
            exception = exc;
        } finally {
            try {
                in.close();
            } catch (Exception exc) {
                if (exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }

            if (exception != null)
                throw exception;
        }

        return source.toString();
    }

    public void pushMatrix() {
        Matrix4f copy = new Matrix4f();
        Matrix4f.load(modelMatrix, copy);
        matrixStack.push(copy);
    }

    public void popMatrix() {
        if (matrixStack.size() > 0) {
            modelMatrix = matrixStack.pop();
        }
    }

    protected void updateModelMatrix() {
        use();
        modelMatrix.store(matrix44Buffer);
        matrix44Buffer.flip();
        ARBShaderObjects.glUniformMatrix4ARB(modelMatrixLocation, false, matrix44Buffer);
    }

    public void setIdentity() {
        Matrix4f.setIdentity(modelMatrix);
    }

    /**
     * Gets the model matrix.
     *
     * @return the model matrix
     */
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

}
