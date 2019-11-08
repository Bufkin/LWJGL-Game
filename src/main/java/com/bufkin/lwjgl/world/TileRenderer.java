package com.bufkin.lwjgl.world;

import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Model;
import com.bufkin.lwjgl.render.Shader;
import com.bufkin.lwjgl.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {
    private HashMap<String, Texture> tileTextures;
    private Model model;

    public TileRenderer() {
        this.tileTextures = new HashMap<>();
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

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!this.tileTextures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    this.tileTextures.put(tex, new Texture(tex + ".png"));
                }
            }
        }
    }

    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
        shader.bind();
        if (this.tileTextures.containsKey(tile.getTexture())) {
            this.tileTextures.get(tile.getTexture()).bind(0);
        }
        Matrix4f tile_position = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(world, target);
        target.mul(tile_position);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        this.model.render();
    }
}
