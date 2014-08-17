import hidrogine.lwjgl.Game;
import hidrogine.lwjgl.VertexData;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Class TheQuadExampleMoving.
 */
public class TheQuadExampleMoving extends Game {

    /**
     * Instantiates a new the quad example moving.
     *
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public TheQuadExampleMoving(int width, int height) {
        super(width, height);
    }

    // Entry point for the application
    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        new TheQuadExampleMoving(800,600);
    }

    // Quad variables
    /** The vao id. */
    private int vaoId = 0;

    /** The vbo id. */
    private int vboId = 0;

    /** The vboi id. */
    private int vboiId = 0;

    /** The indices count. */
    private int indicesCount = 0;

    /** The vertices. */
    private VertexData[] vertices = null;

    /** The vertices byte buffer. */
    private ByteBuffer verticesByteBuffer = null;

    // Texture variables
    /** The tex ids. */
    private int[] texIds = new int[2];

    /** The texture selector. */
    private int textureSelector = 0;
    // Moving variables

    /** The model pos. */
    private Vector3f modelPos = null;

    /** The model angle. */
    private Vector3f modelAngle = null;

    /** The model scale. */
    private Vector3f modelScale = null;

    /** The camera pos. */
    private Vector3f cameraPos = null;

    /**
     * Setup quad.
     */
    private void setupQuad() {
        // We'll define our quad using 4 vertices of the custom 'TexturedVertex'
        // class
        VertexData v0 = new VertexData();
        v0.setXYZ(-0.5f, 0.5f, 0);
        v0.setST(0, 0);
        VertexData v1 = new VertexData();
        v1.setXYZ(-0.5f, -0.5f, 0);
        v1.setST(0, 1);
        VertexData v2 = new VertexData();
        v2.setXYZ(0.5f, -0.5f, 0);
        v2.setST(1, 1);
        VertexData v3 = new VertexData();
        v3.setXYZ(0.5f, 0.5f, 0);
        v3.setST(1, 0);

        vertices = new VertexData[] { v0, v1, v2, v3 };

        // Put each 'Vertex' in one FloatBuffer
        verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length
                * VertexData.stride);
        FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
        for (int i = 0; i < vertices.length; i++) {
            // Add position, color and texture floats to the buffer
            verticesFloatBuffer.put(vertices[i].getElements());
        }
        verticesFloatBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = { 0, 1, 2, 2, 3, 0 };
        indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer,
                GL15.GL_STREAM_DRAW);

        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, VertexData.positionElementCount,
                GL11.GL_FLOAT, false, VertexData.stride,
                VertexData.positionByteOffset);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, VertexData.colorElementCount,
                GL11.GL_FLOAT, false, VertexData.stride,
                VertexData.colorByteOffset);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, VertexData.textureElementCount,
                GL11.GL_FLOAT, false, VertexData.stride,
                VertexData.textureByteOffset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        // Create a new VBO for the indices and select it (bind) - INDICES
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Set the default quad rotation, scale and position values
        modelPos = new Vector3f(0, 0, 0);
        modelAngle = new Vector3f(0, 0, 0);
        modelScale = new Vector3f(1, 1, 1);
        cameraPos = new Vector3f(0, 0, -1);

        this.exitOnGLError("setupQuad");
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#setup()
     */
    @Override
    public void setup() {
        texIds = new int[2];
        texIds[0] = loadPNGTexture("stGrid1.png", GL13.GL_TEXTURE0);
        texIds[1] = loadPNGTexture("stGrid2.png", GL13.GL_TEXTURE0);
        this.exitOnGLError("setupTexture");
        
     
        this.setupQuad();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#update()
     */
    @Override
    public void update() {
        // -- Input processing
        float rotationDelta = 15f;
        float scaleDelta = 0.1f;
        float posDelta = 0.1f;
        Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta,
                scaleDelta);
        Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta,
                -scaleDelta);

        while (Keyboard.next()) {
            // Only listen to events where the key was pressed (down event)
            if (!Keyboard.getEventKeyState())
                continue;

            // Switch textures depending on the key released
            switch (Keyboard.getEventKey()) {
            case Keyboard.KEY_1:
                textureSelector = 0;
                break;
            case Keyboard.KEY_2:
                textureSelector = 1;
                break;
            }

            // Change model scale, rotation and translation values
            switch (Keyboard.getEventKey()) {
            // Move
            case Keyboard.KEY_UP:
                modelPos.y += posDelta;
                break;
            case Keyboard.KEY_DOWN:
                modelPos.y -= posDelta;
                break;
            // Scale
            case Keyboard.KEY_P:
                Vector3f.add(modelScale, scaleAddResolution, modelScale);
                break;
            case Keyboard.KEY_M:
                Vector3f.add(modelScale, scaleMinusResolution, modelScale);
                break;
            // Rotation
            case Keyboard.KEY_LEFT:
                modelAngle.z += rotationDelta;
                break;
            case Keyboard.KEY_RIGHT:
                modelAngle.z -= rotationDelta;
                break;
            }
        }

        // -- Update matrices
        // Reset view and model matrices
        setViewMatrix(new Matrix4f());
        setModelMatrix(new Matrix4f());

        // Translate camera
        Matrix4f.translate(cameraPos, getViewMatrix(), getViewMatrix());

        // Scale, translate and rotate model
        Matrix4f.scale(modelScale, getModelMatrix(), getModelMatrix());
        Matrix4f.translate(modelPos, getModelMatrix(), getModelMatrix());
        Matrix4f.rotate(this.degreesToRadians(modelAngle.z), new Vector3f(0, 0,
                1), getModelMatrix(), getModelMatrix());
        Matrix4f.rotate(this.degreesToRadians(modelAngle.y), new Vector3f(0, 1,
                0), getModelMatrix(), getModelMatrix());
        Matrix4f.rotate(this.degreesToRadians(modelAngle.x), new Vector3f(1, 0,
                0), getModelMatrix(), getModelMatrix());

        updateMatrices();

        GL20.glUseProgram(0);

        this.exitOnGLError("logicCycle");
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#draw()
     */
    @Override
    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        useDefaultShader();

        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIds[textureSelector]);

        // Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Bind to the index VBO that has all the information about the order of
        // the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount,
                GL11.GL_UNSIGNED_BYTE, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);

        this.exitOnGLError("renderCycle");
    }

}
