package com.bufkin.lwjgl.game;

import com.bufkin.lwjgl.io.Timer;
import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Shader;
import com.bufkin.lwjgl.world.Tile;
import com.bufkin.lwjgl.world.TileRenderer;
import com.bufkin.lwjgl.world.World;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private Main() {
        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize!");
            System.exit(1);
        }

        Window window = new Window();
        window.setSize(this.WIDTH, this.HEIGHT);
        window.createWindow("Game");

        GL.createCapabilities();

        Camera camera = new Camera(this.WIDTH, this.HEIGHT);
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();

       /* float[] vertices = new float[]{
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

        Model model = new Model(vertices, texture, indices);*/
        Shader shader = new Shader("shader");
        World world = new World();

        world.setTile(Tile.test2, 0, 0);
        world.setTile(Tile.test2, 63, 63);

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
                unprocessed -= frame_cap;
                can_render = true;

                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(), true);
                }

                if (window.getInput().isKeyDown(GLFW_KEY_A)) {
                    camera.getPosition().sub(new Vector3f(-5, 0, 0));
                }

                if (window.getInput().isKeyDown(GLFW_KEY_D)) {
                    camera.getPosition().sub(new Vector3f(5, 0, 0));
                }

                if (window.getInput().isKeyDown(GLFW_KEY_W)) {
                    camera.getPosition().sub(new Vector3f(0, 5, 0));
                }

                if (window.getInput().isKeyDown(GLFW_KEY_S)) {
                    camera.getPosition().sub(new Vector3f(0, -5, 0));
                }

                world.correctCamera(camera, window);
                window.update();

                if (frame_time >= 1.0f) {
                    frame_time = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);

//                shader.bind();
//                shader.setUniform("sampler", 0);
//                shader.setUniform("projection", camera.getProjection().mul(target));
//                model.render();
//                tex.bind(0);

                world.render(tiles, shader, camera, window);

                window.swapBuffers();
                frames++;
            }
        }
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }
}
