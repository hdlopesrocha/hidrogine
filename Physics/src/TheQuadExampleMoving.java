import hidrogine.lwjgl.Box;
import hidrogine.lwjgl.DrawHandler;
import hidrogine.lwjgl.Game;
import hidrogine.lwjgl.Group;
import hidrogine.lwjgl.Material;
import hidrogine.lwjgl.Model3D;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

// TODO: Auto-generated Javadoc
/**
 * The Class TheQuadExampleMoving.
 */
public class TheQuadExampleMoving extends Game {

    
    /**
     * Instantiates a new the quad example moving.
     */
    public TheQuadExampleMoving() {
        super(1280, 720);
    }

    // Entry point for the application
    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        new TheQuadExampleMoving();
    }

    /** The box. */
    Model3D car;

    DrawHandler boxHandler;

    float time=0;
    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#setup()
     */
    @Override
    public void setup() {
        camera.lookAt(0, 0, 3, 0, 0, 0);
        car = new Model3D("car.mat", "car.geo", 1f);
        program.setLightPosition(0, new Vector3f(3, 3, 3));
        program.setAmbientColor(0, 0, 0);
        program.setDiffuseColor(1, 1, 1);
        program.setMaterialShininess(1000);
        program.setLightColor(0, new Vector3f(1,1,1) );
        
        boxHandler = new DrawHandler() {


            @Override
            public void onDraw(Group group, Material material) {
                if(material.getName().equals("c0")){
                    program.setDiffuseColor((float)(Math.sin(time)+1)/2f,(float) (Math.cos(time*Math.E/2)+1)/2f,(float)(Math.sin(time*Math.PI/2)*Math.cos(time*Math.PI/2)+1)/2f );
                }   
            }
        };
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#update()
     */
    @Override
    public void update() {
        time+=0.003f;
        
        
        
        float sense = 0.03f;
        if(Mouse.isButtonDown(0)){
            camera.rotate(0, 1, 0, Mouse.getDX()*sense*0.2f);
            camera.rotate(1, 0, 0, -Mouse.getDY()*sense*0.2f);
           
        }
  
        

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            camera.rotate(0, 0, 1, -sense);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            camera.rotate(0, 0, 1, sense);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.move(-sense, 0, 0);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.move(sense, 0, 0);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.move(0, 0, -sense);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.move(0, 0, sense);
        }
        program.setLightPosition(0, camera.getPosition());

        GL20.glUseProgram(0);
    }

    
    
    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.lwjgl.Game#draw()
     */
    @Override
    public void draw() {

        program.setIdentity();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // box.draw(shader);
        useDefaultShader();
        program.setOpaque(true);

        program.pushMatrix();
        program.getModelMatrix().rotate(time, new Vector3f(0,0,1));
        
        car.draw(program,boxHandler);
        program.setOpaque(false);
        car.draw(program,boxHandler);
        program.popMatrix();
        GL20.glUseProgram(0);
        
    }

}
