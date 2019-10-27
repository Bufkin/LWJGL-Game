package com.bufkin.lwjgl.game.window;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;
    private int  width, height;
    
    public Window() {
    
    }
    
    public void createWindow(String title) {
        this.window = glfwCreateWindow(this.width, this.height, title, 0, 0);
        
        if (this.window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }
        
        glfwShowWindow(this.window);
        glfwMakeContextCurrent(this.window);
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
}
