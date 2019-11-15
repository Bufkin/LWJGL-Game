package com.bufkin.lwjgl.game;

import com.bufkin.lwjgl.assets.Assets;
import com.bufkin.lwjgl.gui.Gui;
import com.bufkin.lwjgl.io.Timer;
import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Shader;
import com.bufkin.lwjgl.world.TileRenderer;
import com.bufkin.lwjgl.world.World;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private Main() {
        Window.setCallbacks();

        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize!");
            System.exit(1);
        }

        Window window = new Window();
        window.setSize(this.WIDTH, this.HEIGHT);
        window.createWindow("Game");

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Camera camera = new Camera(this.WIDTH, this.HEIGHT);
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();
        Assets.initAsset();

        Shader shader = new Shader("shader");
        World world = new World("test_level", camera);
        world.calculateView(window);

        Gui gui = new Gui(window);

        double frame_cap = 1.0 / 60.0;
        double frame_time = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!window.shouldClose()) {
            boolean can_render = false;

            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;

            time = time_2;

            while (unprocessed >= frame_cap) {
                if (window.hasResized()) {
                    camera.setProjection(window.getWidth(), window.getHeight());
                    gui.resizeCamera(window);
                    world.calculateView(window);
                    glViewport(0, 0, window.getWidth(), window.getHeight());
                }

                unprocessed -= frame_cap;
                can_render = true;

                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(), true);
                }

                world.correctCamera(camera, window);
                world.update((float) frame_cap, window, camera);
                window.update();

                if (frame_time >= 1.0f) {
                    frame_time = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);
                world.render(tiles, shader, camera);
                gui.render();
                window.swapBuffers();
                frames++;
            }
        }
        Assets.deleteAsset();
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}
