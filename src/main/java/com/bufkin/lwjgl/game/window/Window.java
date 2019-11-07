package com.bufkin.lwjgl.game.window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;
    private int width, height;


    private boolean fullScreen;

    public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    public Window() {
        this.setSize(640, 480);
        this.setFullScreen(false);
    }

    public void createWindow(String title) {
        this.window = glfwCreateWindow(this.width, this.height, title, 0, 0);

        if (this.window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }

        GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(this.window,
                (vid.width() - this.width) / 2,
                (vid.height() - this.height) / 2
        );
        glfwShowWindow(this.window);
        glfwMakeContextCurrent(this.window);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(this.window);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFullScreen() {
        return this.fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }
}
