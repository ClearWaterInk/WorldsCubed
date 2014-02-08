package net.clearwaterink.worldscubed;

import static org.lwjgl.opengl.GL11.*;

import java.text.NumberFormat;
import java.util.Locale;

import net.clearwaterink.worldscubed.render.Terrain;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class WorldsCubed {
	public Camera camera;
	public Terrain terrain;
	
	//private String title = "Worlds³";
	private boolean wire = false;
	private int cubes = 0;
	private long fps = 0;
	private long lastFPS = 0;
	private long lastFrame = 0;
	
	public WorldsCubed() {
		camera = new Camera(0, 170, -10);
		terrain = new Terrain(this);
		start();
	}
	
	private void start() {
		try{
			Display.setDisplayMode(new DisplayMode(720,480));
			Display.setFullscreen(true);
			Display.setResizable(true);
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		getDelta();
		lastFPS = getTime();
		
    	Mouse.setGrabbed(true);
		while(!Display.isCloseRequested()){
    		glLoadIdentity();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    		
    		processCamera();
			if(wire){
				glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
			}else{
				glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
			}
			
			monitorInput();
    		
			terrain.render();
			
			Display.update();
		}
		Display.destroy();
		System.exit(0);
	}
	
	private void processCamera(){
		camera.processMouse(1);
		camera.lookThrough();
	}
	
	private void monitorInput(){
		float dt = getDelta();
    	float movementSpeed = 0.015f;
    	try{
    		Keyboard.create();
    	}catch(Exception e){
    		System.out.print(e);
    	}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			camera.walkForwards(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			camera.walkBackwards(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			camera.strafeLeft(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			camera.strafeRight(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			camera.flyUp(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			camera.flyDown(movementSpeed * dt);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)){
			wire = !wire;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			System.exit(0);
		}
		updateFPS();
	}
	
	private void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(60.0f, ((float) Display.getWidth()) / ((float) Display.getHeight()), 0.1f, 256.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glShadeModel(GL_SMOOTH);
		glClearColor(0.2f, 0.4f, 0.8f, 0.0f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glEnable(GL_CULL_FACE);
        Display.setVSyncEnabled(false);
        glEnableClientState(GL11.GL_VERTEX_ARRAY);
        glEnableClientState(GL11.GL_COLOR_ARRAY);
	}
	
	public static void main(String[] args) {new WorldsCubed();}
	
	//FPS
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps + " Cubes: " + 
					NumberFormat.getNumberInstance(Locale.US).format(cubes));
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void setCubes(int cubes) {
		this.cubes = cubes;
	}
}
