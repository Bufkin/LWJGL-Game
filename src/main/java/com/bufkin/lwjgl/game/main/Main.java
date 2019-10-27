package com.bufkin.lwjgl.game.main;

import com.bufkin.lwjgl.game.Camera;
import com.bufkin.lwjgl.game.Model;
import com.bufkin.lwjgl.game.Shader;
import com.bufkin.lwjgl.game.Timer;
import com.bufkin.lwjgl.game.textures.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {
    
    private final int WIDTH  = 1280;
    private final int HEIGHT = 720;
    
    private Main() {
        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize!");
            System.exit(1);
        }
        
        long window = glfwCreateWindow(this.WIDTH, this.HEIGHT, "Window", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        Camera camera = new Camera(this.WIDTH, this.HEIGHT);
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
        
        Model   model  = new Model(vertices, texture, indices);
        Shader  shader = new Shader("shader");
        Texture tex    = new Texture("./res/test.png");
        
        Matrix4f scale = new Matrix4f()
                                 .translate(new Vector3f(100, 0, 0))
                                 .scale(256);
        Matrix4f target = new Matrix4f();
        camera.setPosition(new Vector3f(-100, 0, 0));
        
        double frame_cap = 1.0 / 60.0;
        
        double frame_time = 0;
        int    frames     = 0;
        
        double time        = Timer.getTime();
        double unprocessed = 0;
        
        while (!glfwWindowShouldClose(window)) {
            boolean can_render = false;
            
            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            
            time = time_2;
            
            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;
                
                target = scale;
                if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
                    glfwSetWindowShouldClose(window, Boolean.TRUE);
                }
                
                glfwPollEvents();
                
                if (frame_time >= 1.0f) {
                    frame_time = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }
            
            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);
                
                tex.bind(0);
                shader.bind();
                shader.setUniform("sampler", 0);
                shader.setUniform("projection", camera.getProjection().mul(target));
                model.render();
                
                glfwSwapBuffers(window);
                frames++;
            }
        }
        glfwTerminate();
    }
    
    public static void main(String[] args) {
        new Main();
    }
}
