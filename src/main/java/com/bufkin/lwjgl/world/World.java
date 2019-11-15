package com.bufkin.lwjgl.world;

import com.bufkin.lwjgl.collision.AABB;
import com.bufkin.lwjgl.entity.Entity;
import com.bufkin.lwjgl.entity.Player;
import com.bufkin.lwjgl.entity.Transform;
import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World {
    // TODO: Wert anpassen, damit man alles sieht
    private int viewX;
    private int viewY;
    private byte[] tiles;
    private AABB[] boundingBoxes;
    private List<Entity> entities;
    private int width, height;
    private int scale;

    private Matrix4f world;

    public World(String world, Camera camera) {
        try {
            BufferedImage tileSheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));
            BufferedImage entitySheet = ImageIO.read(new File("./levels/" + world + "/entities.png"));

            this.width = tileSheet.getWidth();
            this.height = tileSheet.getHeight();
            this.scale = 32;

            this.world = new Matrix4f().setTranslation((new Vector3f(0)));
            this.world.scale(this.scale);

            int[] colorTileSheet = tileSheet.getRGB(0, 0, this.width, this.height, null, 0, this.width);
            int[] colorEntitySheet = entitySheet.getRGB(0, 0, this.width, this.height, null, 0, this.width);

            this.tiles = new byte[this.width * this.height];
            this.boundingBoxes = new AABB[this.width * this.height];
            this.entities = new ArrayList<>();

            Transform transform;

            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    int red = (colorTileSheet[x + y * this.width] >> 16) & 0xFF;
                    int entityIndex = (colorEntitySheet[x + y * this.width] >> 16) & 0xFF;
                    int entityAlpha = (colorEntitySheet[x + y * this.width] >> 24) & 0xFF;

                    Tile t;
                    try {
                        t = Tile.tiles[red];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        t = null;
                    }

                    if (t != null) {
                        this.setTile(t, x, y);
                    }

                    if (entityAlpha > 0) {
                        transform = new Transform();
                        transform.pos.x = x * 2;
                        transform.pos.y = -y * 2;
                        switch (entityIndex) {
                            // Player
                            case 1:
                                Player player = new Player(transform);
                                this.entities.add(player);
                                camera.getPosition().lerp(transform.pos.mul(-this.scale, new Vector3f()), 0.05f);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            this.entities.add(new Player(new Transform()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public World() {
        this.width = 64;
        this.height = 64;
        this.scale = 16;

        this.tiles = new byte[this.width * this.height];
        this.boundingBoxes = new AABB[this.width * this.height];

        this.world = new Matrix4f().setTranslation((new Vector3f(0)));
        this.world.scale(this.scale);
    }

    public void calculateView(Window window) {
        this.viewX = (window.getWidth() / (this.scale * 2)) + 4;
        this.viewY = (window.getHeight() / (this.scale * 2)) + 4;
    }

    public Matrix4f getWorldMatrix() {
        return this.world;
    }

    public void render(TileRenderer render, Shader shader, Camera camera) {
        int posX = (int) camera.getPosition().x / (this.scale * 2);
        int posY = (int) camera.getPosition().y / (this.scale * 2);

        for (int i = 0; i < this.viewX; i++) {
            for (int j = 0; j < this.viewY; j++) {
                Tile t = this.getTile(i - posX - (this.viewX / 2) + 1, j + posX - (this.viewY / 2));
                if (t != null) {
                    render.renderTile(t, i - posX - (this.viewX / 2) + 1, -j - posY + (this.viewY / 2), shader, this.world, camera);
                }
            }
        }

        for (Entity entity : this.entities) {
            entity.render(shader, camera, this);
        }
    }

    public void update(float delta, Window window, Camera camera) {
        for (Entity entity : this.entities) {
            entity.update(delta, window, camera, this);
        }

        for (int i = 0; i < this.entities.size(); i++) {
            this.entities.get(i).collideWithTiles(this);

            for (int j = i + 1; j < this.entities.size(); j++) {
                this.entities.get(i).collideWithEntity(this.entities.get(j));
            }

            this.entities.get(i).collideWithTiles(this);
        }
    }

    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -this.width * this.scale * 2;
        int h = this.height * this.scale * 2;

        // Collision detection
        if (pos.x > -((float) window.getWidth() / 2) + this.scale)
            pos.x = -((float) window.getWidth() / 2) + this.scale;
        if (pos.x < w + ((float) window.getWidth() / 2) + this.scale)
            pos.x = w + ((float) window.getWidth() / 2) + this.scale;
        if (pos.y < ((float) window.getHeight() / 2) - this.scale)
            pos.y = ((float) window.getHeight() / 2) - this.scale;
        if (pos.y > h - ((float) window.getHeight() / 2) - this.scale)
            pos.y = h - ((float) window.getHeight() / 2) - this.scale;
    }

    private void setTile(Tile tile, int x, int y) {
        this.tiles[x + y * this.width] = tile.getId();

        if (tile.isSolid()) {
            this.boundingBoxes[x + y * this.width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        } else {
            this.boundingBoxes[x + y * this.width] = null;
        }
    }

    private Tile getTile(int x, int y) {
        try {
            return Tile.tiles[this.tiles[x + y * this.width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public AABB getTileBoundingBox(int x, int y) {
        try {
            return this.boundingBoxes[x + y * this.width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public float getScale() {
        return this.scale;
    }
}
