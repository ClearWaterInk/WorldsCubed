package net.clearwaterink.worldscubed.world;

import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.clearwaterink.worldscubed.WorldsCubed;

import org.lwjgl.BufferUtils;

public class World {
	private final int CHUNK_WIDTH = 336, CHUNK_HEIGHT = 256;
	
	private FloatBuffer vcBuffer;
	private Generator generator;
	private WorldsCubed wc;
	
	private int count = 0;
	private int cubes = 0;
	
	public int chunk[][][] = new int[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_WIDTH];
	
	public World(WorldsCubed wc) {
		this.wc = wc;
		generator = new Generator(this, 12, CHUNK_WIDTH, CHUNK_HEIGHT);
		vcBuffer = BufferUtils.createFloatBuffer(50000000);
	}
	
	public void createWorld() {
		generator.generateWorld();
		
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_WIDTH; z++) {
					if (chunk[x][y][z] > 0) {
						cubes += 1;
					}
					if (chunk[x][y][z] == 1) {
						createMesh(x, y, z, 0.0f, 0.0f, 0.0f);
					} else if(chunk[x][y][z] == 2) {
						createMesh(x, y, z, 0.0f, 0.7f, 0.0f);
					}
				}
			}
		}

		vcBuffer.flip();
		wc.setCubes(cubes);
	}
	
	public void render() {
		drawBlocks();
	}
	
	/**
	 * Adds a block into the VBO
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	private void createMesh(int x, int y, int z, float r, float g, float b) {
		/*
		 * This checks to make the block's face is touching another block. If not don't show the face.
		 * TEMP: All values added to color will be removed. They are light place holders. "Psuedo-light"
		 */
		// Left
		if (!checkLeft(x, y, z)) {
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.19f).put(g + 0.19f).put(b + 0.19f);
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.19f).put(g + 0.19f).put(b + 0.19f);
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.19f).put(g + 0.19f).put(b + 0.19f);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.19f).put(g + 0.19f).put(b + 0.19f);
			count += 4;
		}

		// Back
		if (!checkBack(x, y, z)) {
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.09f).put(g + 0.09f).put(b + 0.09f);
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.09f).put(g + 0.09f).put(b + 0.09f);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.09f).put(g + 0.09f).put(b + 0.09f);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.09f).put(g + 0.09f).put(b + 0.09f);
			count += 4;
		}

		// Front
		if (!checkFront(x, y, z)){
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.04f).put(g + 0.04f).put(b + 0.04f);
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.04f).put(g + 0.04f).put(b + 0.04f);
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.04f).put(g + 0.04f).put(b + 0.04f);
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.04f).put(g + 0.04f).put(b + 0.04f);
			count += 4;
		}

		// Top
		if (!checkTop(x, y, z)) {
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.15f).put(g + 0.15f).put(b + 0.15f);
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.15f).put(g + 0.15f).put(b + 0.15f);
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.15f).put(g + 0.15f).put(b + 0.15f);
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put( 0.5f + z); vcBuffer.put(r + 0.15f).put(g + 0.15f).put(b + 0.15f);
			count += 4;
		}
		
		// Bottom
		if (!checkBottom(x, y, z)) {
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r).put(g).put(b);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r).put(g).put(b);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r).put(g).put(b);
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put( 0.5f + z); vcBuffer.put(r).put(g).put(b);
			count += 4;
		}

		// Right
		if (!checkRight(x, y, z)) {
			vcBuffer.put( 0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.06f).put(g + 0.06f).put(b + 0.06f);
			vcBuffer.put(-0.5f + x).put(-0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.06f).put(g + 0.06f).put(b + 0.06f);
			vcBuffer.put(-0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.06f).put(g + 0.06f).put(b + 0.06f);
			vcBuffer.put( 0.5f + x).put( 0.5f + y).put(-0.5f + z); vcBuffer.put(r + 0.06f).put(g + 0.06f).put(b + 0.06f);
			count += 4;
		}
	}
	
	
	private void drawBlocks() {
		IntBuffer ib = BufferUtils.createIntBuffer(1);

		glGenBuffersARB(ib);
		int vcHandle = ib.get(0);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vcBuffer, GL_STATIC_DRAW_ARB);
		glVertexPointer(3, GL_FLOAT, 6 << 2, 0 << 2);
		glColorPointer(3, GL_FLOAT, 6 << 2, 3 << 2);
		
		glDrawArrays(GL_QUADS, 0, count);
		
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		
		// cleanup VBO handles
		ib.put(0, vcHandle);
		glDeleteBuffersARB(ib);
	}
	
	//Block checking
	private boolean checkLeft(int x, int y, int z){
		int dz = z + 1;
		if(dz > CHUNK_WIDTH - 1){
			return false;
		}else if(chunk[x][y][dz] != 0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkRight(int x, int y, int z){
		int dz = z - 1;
		if(dz < 0){
			return false;
		}else if(chunk[x][y][dz] != 0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkFront(int x, int y, int z){
		int dx = x + 1;
		if(dx > CHUNK_WIDTH - 1){
			return false;
		}else if(chunk[dx][y][z] != 0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkBack(int x, int y, int z){
		int dx = x - 1;
		if(dx < 0){
			return false;
		}else if(chunk[dx][y][z] != 0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkBottom(int x, int y, int z){
		int dy = y + 1;
		if(dy > CHUNK_HEIGHT - 1){
			return false;
		}else if(chunk[x][dy][z] != 0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkTop(int x, int y, int z){
		int dy = y - 1;
		if(dy < 0){
			return false;
		}else if(chunk[x][dy][z] != 0){
			return true;
		}else{
			return false;
		}
	}
}