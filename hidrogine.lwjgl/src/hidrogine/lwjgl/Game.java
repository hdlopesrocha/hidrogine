package hidrogine.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * The Class Game.
 */
public abstract class Game {

    // Shader variables
    /** The program. */
    protected ShaderProgram program;

    /** The camera. */
    private Camera camera;

    
    public Camera getCamera() {
        return camera;
    }

    /**
     * Instantiates a new game.
     */
    public Game(int w, int h) {
        try {
            LibraryLoader.loadNativeLibraries();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                    .withForwardCompatible(true).withProfileCore(true);
            camera = new Camera(w, h);
            Display.setDisplayMode(new DisplayMode(w, h));
            Display.setTitle("Game");
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
        GL11.glViewport(0, 0, w, h);

        program = new ShaderProgram("vertex.glsl", "fragment.glsl");
        setup();
        while (!Display.isCloseRequested()) {
            // Do a single loop (logic/render)
            update();
            program.update(camera);
            draw();
            // Force a maximum FPS of about 60
            Display.sync(60);
            // Let the CPU synchronize with the GPU if GPU is tagging behind
            Display.update();
        }

        Display.destroy();

    }

    /**
     * Sets the camera.
     *
     * @param c
     *            the new camera
     */
    public void setCamera(Camera c) {
        camera = c;
        GL11.glViewport(0, 0, c.getWidth(), c.getHeight());
    }

    /**
     * Use default shader.
     */
    public void useDefaultShader() {
        program.use();
    }

    /**
     * Update.
     */
    public abstract void update();

    /**
     * Draw.
     */
    public abstract void draw();

    /**
     * Setup.
     */
    public abstract void setup();

}
