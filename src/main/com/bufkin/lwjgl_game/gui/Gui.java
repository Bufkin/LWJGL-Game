package com.bufkin.lwjgl_game.gui;

import com.bufkin.lwjgl_game.assets.Assets;
import com.bufkin.lwjgl_game.io.Window;
import com.bufkin.lwjgl_game.render.Camera;
import com.bufkin.lwjgl_game.render.Shader;
import com.bufkin.lwjgl_game.render.TileSheet;
import org.joml.Matrix4f;

public class Gui {
	private Shader shader;
	private Camera camera;
	private TileSheet sheet;

	public Gui(Window window) {
		this.shader = new Shader("gui");
		this.camera = new Camera(window.getWidth(), window.getHeight());
		this.sheet = new TileSheet("test.png", 3);
	}

	public void resizeCamera(Window window) {
		this.camera.setProjection(window.getWidth(), window.getHeight());
	}

	public void render() {
		Matrix4f mat = new Matrix4f();
		this.camera.getProjection().scale(87, mat);
		mat.translate(-2, -2, 0);
		this.shader.bind();

		this.shader.setUniform("projection", mat);

		this.sheet.bindTile(this.shader, 0, 1);
		this.sheet.bindTile(this.shader, 4);
		// this.shader.setUniform("color", new Vector4f(0, 0, 0, 0.4f));

		Assets.getModel().render();
	}
}
