package com.bufkin.lwjgl.world;

public class Tile {
    public static Tile[] tiles = new Tile[16];
    public static byte not = 0;

    public static final Tile grass = new Tile("grass");
    public static final Tile test2 = new Tile("checker").setSolid();

    private byte id;
    private boolean solid;
    private String texture;

    public Tile(String texture) {
        this.id = not;
        not++;
        this.texture = texture;
        this.solid = false;

        if (tiles[this.id] != null) {
            throw new IllegalStateException("Tiles at: [" + this.id + "] is already being used!");
        }
        tiles[this.id] = this;
    }

    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public byte getId() {
        return this.id;
    }

    public String getTexture() {
        return this.texture;
    }
}
