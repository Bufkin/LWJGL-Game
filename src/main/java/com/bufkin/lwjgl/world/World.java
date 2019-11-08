package com.bufkin.lwjgl.world;

import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class World {
    // TODO: Wert anpassen, damit man alles sieht
    private final int view = 24;
    private byte[] tiles;
    private int width, height;
    private int scale;

    private Matrix4f world;

    public World() {
        this.width = 1024;
        this.height = 1024;
        this.scale = 16;

        this.tiles = new byte[this.width * this.height];

        this.world = new Matrix4f().setTranslation((new Vector3f(0)));
        this.world.scale(this.scale);
    }

    public void render(TileRenderer render, Shader shader, Camera camera, Window window) {
        int posX = ((int) camera.getPosition().x + (window.getWidth() / 2)) / (this.scale * 2);
        int posY = ((int) camera.getPosition().y - (window.getHeight() / 2)) / (this.scale * 2);

        for (int i = 0; i < this.view; i++) {
            for (int j = 0; j < this.view; j++) {
                Tile t = this.getTile(i - posX, j + posX);
                if (t != null) {
                    render.renderTile(t, i - posX, -j - posY, shader, this.world, camera);
                }
            }
        }
    }

    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -this.width * this.scale * 2;
        int h = this.height * this.scale * 2;

        // Collision detection
        if (pos.x > -(window.getWidth() / 2) + this.scale)
            pos.x = -(window.getWidth() / 2) + this.scale;
        if (pos.x < w + (window.getWidth() / 2) + this.scale)
            pos.x = w + (window.getWidth() / 2) + this.scale;
        if (pos.y < (window.getHeight() / 2) - this.scale)
            pos.y = (window.getHeight() / 2) - this.scale;
        if (pos.y > h - (window.getHeight() / 2) - this.scale)
            pos.y = h - (window.getHeight() / 2) - this.scale;
    }

    public void setTile(Tile tile, int x, int y) {
        this.tiles[x + y * this.width] = tile.getId();
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[this.tiles[x + y * this.width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public float getScale() {
        return this.scale;
    }
}
