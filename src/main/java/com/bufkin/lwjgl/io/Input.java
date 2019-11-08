package com.bufkin.lwjgl.io;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private long window;
    private boolean keys[];
    private boolean mouseKeys[];

    public Input(long window) {
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];

        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            this.keys[i] = false;
        }
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(this.window, key) == 1;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(this.window, button) == 1;
    }

    public boolean isKeyPressed(int key) {
        return (this.isKeyDown(key) && !this.keys[key]);
    }

    public void update() {
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            this.keys[i] = this.isKeyDown(i);
        }
    }
}
