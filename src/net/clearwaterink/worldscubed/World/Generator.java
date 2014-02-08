package net.clearwaterink.worldscubed.World;

import java.util.Random;

public class Generator {
	private Random rand = null;
	private World world;
	
	private double fre;
	private int see, chunkWidth = 0, chunkHeight = 0;
	
	public Generator(World world, int seed, int chunkWidth, int chunkHeight) {
		this.world = world;
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		rand = new Random();
		rand.setSeed(seed);
		see = rand.nextInt();
		fre = rand.nextDouble();
	}
	
	public void generateWorld() {
		createTerrain(20.0, 0);
	}
	
	private void createTerrain(double zoom, double p){
		int octaves = 2;
		System.out.print("New");
		for(int z = 0; z < chunkWidth; z++) {
			for(int x = 0; x < chunkWidth; x++) {
				double getnoise = 0;
				for(int a = 0; a < octaves - 1; a++){
					double frequency = Math.pow(fre, a);
					double amplitude = Math.pow(p, a);
					getnoise += noise(((double)x) * frequency / zoom,((double)z) / zoom * frequency) * amplitude;
				}
				int color = (int)((getnoise * 128.0) + 128.0);
				if(color > 255)
					color = 255;
				if(color <= 0)
					color = 1;
				
				int y = (int)(((float)chunkWidth / 16.0f) * (float)color) / 16;
				
				drawXZ(x, y, z);
				//System.out.println(x + " " + y + " " + (int)(((float)chunkWidth / 16.0f) * (float)color) / 16);
			}
		}
	}
	
	private void drawXZ(int x, int y, int z){
		for(int s = 0; s < y; s++){
			System.out.println(x + " " + s + " " + z);
			world.chunk[x][((chunkHeight - 1) - s) - 128][z] = 2;
		}
	}
	
	private double noise(double x, double y){
		double floorx = (double)((int)x);
		double floory = (double)((int)y);
		double s, t, u, v;
		
		s = findnoise2(floorx, floory);
		
		t = findnoise2(floorx + 1,floory);
		
		u = findnoise2(floorx, floory + 1);
		
		v = findnoise2(floorx + 1, floory + 1);
		
		double int1 = interpolate(s, t, x - floorx);
		double int2 = interpolate(u, v, x - floorx);
		return interpolate(int1, int2, y - floory);
	}
	
	private double findnoise2(double x, double y){
		int n = (int)x + (int)y * see;
		n = (n << 13)^n;
		int nn = (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
		return 1.0 - ((double)nn / 1073741824.0);
	}

	private double interpolate(double a, double b, double x){
		double ft = x * 3.14159265358979382626233897;
		double f = (1.0 - Math.cos(ft)) * 0.5;
		return a * (1.0 - f) + b * f;
	}
}
