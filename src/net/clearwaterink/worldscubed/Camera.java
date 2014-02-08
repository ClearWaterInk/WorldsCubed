package net.clearwaterink.worldscubed;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private float yaw = 0, pitch = 0;
	private Vector3f position;
	
	public Camera(float x, float y, float z){
		position = new Vector3f(x, y, z);
	}
	
	public void walkForwards(float distance){
		position.x += distance * (float)Math.sin(Math.toRadians(-yaw));
		position.z -= distance * (float)Math.cos(Math.toRadians(-yaw));
    }
	
	public void walkBackwards(float distance){
		position.x -= distance * (float)Math.sin(Math.toRadians(-yaw));
		position.z += distance * (float)Math.cos(Math.toRadians(-yaw));
    }
    
    public void strafeLeft(float distance){
    	position.x += distance * (float)Math.sin(Math.toRadians(-(yaw - 90)));
    	position.z -= distance * (float)Math.cos(Math.toRadians(-(yaw - 90)));
    }

    public void strafeRight(float distance){
    	position.x += distance * (float)Math.sin(Math.toRadians(-(yaw + 90)));
    	position.z -= distance * (float)Math.cos(Math.toRadians(-(yaw + 90)));
    }
    
    public void flyUp(float distance){
    	position.y += distance;
    }
    
    public void flyDown(float distance){
    	position.y -= distance;
    }
	
    public void processMouse(float mouseSpeed) {
        final float MAX_LOOK_UP = 90;
        final float MAX_LOOK_DOWN = -90;
        float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
        float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
        if (yaw + mouseDX >= 360) {
            yaw = yaw + mouseDX - 360;
        } else if (yaw + mouseDX < 0) {
            yaw = 360 - yaw + mouseDX;
        } else {
            yaw += mouseDX;
        }
        if (pitch - mouseDY >= MAX_LOOK_DOWN && pitch - mouseDY <= MAX_LOOK_UP) {
            pitch += -mouseDY;
        } else if (pitch - mouseDY < MAX_LOOK_DOWN) {
            pitch = MAX_LOOK_DOWN;
        } else if (pitch - mouseDY > MAX_LOOK_UP) {
            pitch = MAX_LOOK_UP;
        }
    }
    
	public void lookThrough(){
		glRotatef(pitch + 180, 1, 0, 0);
		glRotatef(yaw, 0, -1, 0);

		glTranslatef(0, -256, 0);
		glTranslatef(position.x, position.y, position.z);
		
		//Display.setTitle(position.x + " " + position.y + " " + position.z);
	}
	
	public void stopJumping(){
		glRotatef(-pitch - 180, 1, 0, 0);
		glRotatef(-yaw, 0, -1, 0);
		
		glTranslatef(-position.x, -position.y, -position.z);
	}
	
	public void setPos(int x, int y, int z){
		position.x = x;
		position.y = y;
		position.z = z;
	}
}
