package net.clearwaterink.worldscubed.world;

import net.clearwaterink.worldscubed.tools.Octave;

public class Generator {
	private Octave octave;
	private World world;
	
	private int chunkWidth = 0, chunkHeight = 0;
	
	public Generator(World world, int seed, int chunkWidth, int chunkHeight) {
		this.world = world;
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		octave = new Octave(seed);
	}
	
	public void generateWorld() {
		addBedrock();
		createTerrain(50.0);
	}
	
	private void addBedrock() {
		for (int x = 0; x < chunkWidth; x++) {
			for (int z = 0; z < chunkWidth; z++) {
				world.chunk[x][255][z] = 1;
			}
		}
	}
	
	private void createTerrain(double zoom){
		int octaves = 2;
		for(int z = 0; z < chunkWidth; z++) {
			for(int x = 0; x < chunkWidth; x++) {
				double getnoise = 0;
				for(int a = 0; a < octaves - 1; a++){
					double frequency = Math.pow(1, a);
					getnoise += octave.noise(((double)x) * frequency / zoom,((double)z) / zoom * frequency);
				}
				int color = (int)((getnoise * 128.0) + 128.0);
				if(color > 255)
					color = 255;
				if(color <= 0)
					color = 1;
				
				int y = (int)(((float)chunkWidth / 16.0f) * (float)color) / chunkWidth;
				
				addXZ(x, y, z);
			}
		}
	}
	
	private void addXZ(int x, int y, int z){
		for(int s = 0; s < y; s++){
			world.chunk[x][((chunkHeight - 1) - s) - 128][z] = 2;
		}
	}
}
