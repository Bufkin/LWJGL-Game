package com.bufkin.lwjgl_game.render;

import org.joml.Matrix4f;

public class TileSheet {
	private Texture texture;
	private Matrix4f scale;
	private Matrix4f translation;
 
	private int amountOfTiles;

	public TileSheet(String texture, int amountOfTiles) {
		this.texture = new Texture("sheets/" + texture);

		this.scale = new Matrix4f().scale(1.0f / (float) amountOfTiles);
		this.translation = new Matrix4f();

		this.amountOfTiles = amountOfTiles;
	}

	public void bindTile(Shader shader, int x, int y) {
		this.scale.translate(x, y, 0, this.translation);

		shader.setUniform("sampler", 0);
		shader.setUniform("texModifier", this.translation);
		this.texture.bind(0);
	}

	public void bindTile(Shader shader, int tile) {
		int x = tile % this.amountOfTiles;
		int y = tile / this.amountOfTiles;
		this.bindTile(shader, x, y);
	}
}
