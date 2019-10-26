package com.bufkin.lwjgl.game.main;

import com.bufkin.lwjgl.game.Model;
import com.bufkin.lwjgl.game.textures.Texture;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    
    private Main() {
        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize!");
            System.exit(1);
        }
        
        long window = glfwCreateWindow(1280, 720, "Window", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glEnable(GL_TEXTURE_2D);
        
        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,     // TOP LEFT     0
                0.5f, 0.5f, 0,      // TOP RIGHT    1
                0.5f, -0.5f, 0,     // BOTTOM RIGHT 2
                -0.5f, -0.5f, 0,    // BOTTOM LEFT  3
        };
        
        float[] texture = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };
        
        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        
        Model   model = new Model(vertices, texture, indices);
        Texture tex   = new Texture("./res/test.png");
        
        while (!glfwWindowShouldClose(window)) {
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
                glfwSetWindowShouldClose(window, Boolean.TRUE);
            }
            
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            
            tex.bind();
            model.render();
            
            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }
    
    public static void main(String[] args) {
        new Main();
    }
}
