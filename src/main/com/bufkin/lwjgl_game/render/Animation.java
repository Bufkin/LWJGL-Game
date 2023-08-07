package com.bufkin.lwjgl_game.render;

import com.bufkin.lwjgl_game.io.Timer;

public class Animation {
	private Texture[] frames;
	private int texturePointer;
	private double elapsedTime, currentTime, lastTime, fps;

	public Animation(int amount, int fps, String fileName) {
		this.texturePointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0 / (double) fps;

		this.frames = new Texture[amount];

		for (int i = 0; i < amount; i++) {
			this.frames[i] = new Texture(fileName + "/" + i + ".png");
		}
	}

	public void bind() {
		this.bind(0);
	}

	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += this.currentTime - this.lastTime;

		if (this.elapsedTime >= this.fps) {
			this.elapsedTime = 0;
			this.texturePointer++;
		}

		if (this.texturePointer >= this.frames.length) this.texturePointer = 0;

		this.lastTime = this.currentTime;
		this.frames[this.texturePointer].bind(sampler);
	}
}
