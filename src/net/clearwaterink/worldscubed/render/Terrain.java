package net.clearwaterink.worldscubed.render;

import net.clearwaterink.worldscubed.WorldsCubed;
import net.clearwaterink.worldscubed.World.World;

public class Terrain {
	private World world;
	private WorldsCubed wc;
	
	public Terrain(WorldsCubed wc) {
		this.wc = wc;
		world = new World(this.wc);
		world.createWorld();
	}
	
	public void render() {
		world.render();
	}
}
