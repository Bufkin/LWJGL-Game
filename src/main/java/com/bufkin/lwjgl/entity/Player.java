package com.bufkin.lwjgl.entity;

import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Model;
import com.bufkin.lwjgl.render.Shader;
import com.bufkin.lwjgl.render.Texture;
import com.bufkin.lwjgl.world.World;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private Model model;
    private Texture texture;
    private Transform transform;

    public Player() {
        float[] vertices = new float[]{
                -1.0f, 1.0f, 0,     // TOP LEFT     0
                1.0f, 1.0f, 0,      // TOP RIGHT    1
                1.0f, -1.0f, 0,     // BOTTOM RIGHT 2
                -1.0f, -1.0f, 0,    // BOTTOM LEFT  3
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

        this.model = new Model(vertices, texture, indices);
        this.texture = new Texture("test.png");

        this.transform = new Transform();
        this.transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta, Window window, Camera camera, World world) {
        if (window.getInput().isKeyDown(GLFW_KEY_A)) {
            this.transform.pos.add(new Vector3f(-10 * delta, 0, 0));
        }

        if (window.getInput().isKeyDown(GLFW_KEY_D)) {
            this.transform.pos.add(new Vector3f(10 * delta, 0, 0));
        }

        if (window.getInput().isKeyDown(GLFW_KEY_W)) {
            this.transform.pos.add(new Vector3f(0, 10 * delta, 0));
        }

        if (window.getInput().isKeyDown(GLFW_KEY_S)) {
            this.transform.pos.add(new Vector3f(0, -10 * delta, 0));
        }

        camera.setPosition(this.transform.pos.mul(-world.getScale(), new Vector3f()));
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", this.transform.getProjection(camera.getProjection()));
        this.texture.bind(0);
        this.model.render();
    }
}
