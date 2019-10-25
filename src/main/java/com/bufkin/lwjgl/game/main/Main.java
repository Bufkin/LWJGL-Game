package com.bufkin.lwjgl.game.main;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import static org.lwjgl.glfw.GLFW.*;

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

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
            GL46.glBegin(GL46.GL_QUADS);
            GL46.glColor4f(1, 0, 0, 0);
            GL46.glVertex2f(-0.5f, 0.5f);
            GL46.glVertex2f(0.5f, 0.5f);
            GL46.glVertex2f(0.5f, -0.5f);
            GL46.glVertex2f(-0.5f, -0.5f);
            GL46.glEnd();

            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}
