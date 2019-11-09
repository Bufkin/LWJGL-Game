package com.bufkin.lwjgl.render;

import com.bufkin.lwjgl.io.Timer;

public class Animation {
    private Texture[] frames;
    private int pointer;
    private double elapsedTime, currentTime, lastTime, fps;

    public Animation(int amount, int fps, String fileName) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / (double) fps;

        this.frames = new Texture[amount];

        for (int i = 0; i < amount; i++) {
            this.frames[i] = new Texture("anim/" + fileName + "_" + i + ".png");
        }
    }

    public void bind() {
        this.bind(0);
    }

    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += this.currentTime - this.lastTime;

        if (this.elapsedTime >= this.fps) {
            this.elapsedTime -= this.fps;
            this.pointer++;
        }

        if (this.pointer >= this.frames.length) this.pointer = 0;

        this.lastTime = this.currentTime;
        this.frames[this.pointer].bind(sampler);
    }
}
