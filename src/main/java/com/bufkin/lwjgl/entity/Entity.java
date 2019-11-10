package com.bufkin.lwjgl.entity;

import com.bufkin.lwjgl.collision.AABB;
import com.bufkin.lwjgl.collision.Collision;
import com.bufkin.lwjgl.io.Window;
import com.bufkin.lwjgl.render.Animation;
import com.bufkin.lwjgl.render.Camera;
import com.bufkin.lwjgl.render.Model;
import com.bufkin.lwjgl.render.Shader;
import com.bufkin.lwjgl.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {
    private static Model model;
    protected AABB boundingBox;
    // private Texture texture;
    protected Animation texture;
    protected Transform transform;

    public Entity(Animation animation, Transform transform) {
        this.texture = animation;
        this.transform = transform;
        this.boundingBox = new AABB(new Vector2f(this.transform.pos.x, this.transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

    public void move(Vector2f direction) {
        this.transform.pos.add(new Vector3f(direction, 0));

        this.boundingBox.getCenter().set(this.transform.pos.x, this.transform.pos.y);
    }

    public void collideWithTiles(World world) {
        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = world.getTileBoundingBox(
                        (int) (((this.transform.pos.x / 2) + 0.5f) - (5 / 2)) + i,
                        (int) (((-this.transform.pos.y / 2) + 0.5f) - (5 / 2)) + j
                );
            }
        }

        AABB box;
        box = this.getClosestBox(boxes, null);

        if (box != null) {
            Collision data = this.boundingBox.getCollision(box);
            if (data.isIntersecting) {
                this.boundingBox.correctPosition(box, data);
                this.transform.pos.set(this.boundingBox.getCenter(), 0);
            }

            box = this.getClosestBox(boxes, box);

            data = this.boundingBox.getCollision(box);
            if (data.isIntersecting) {
                this.boundingBox.correctPosition(box, data);
                this.transform.pos.set(this.boundingBox.getCenter(), 0);
            }
        }
    }

    public abstract void update(float delta, Window window, Camera camera, World world);

    private AABB getClosestBox(AABB[] boxes, AABB box) {
        for (AABB aabb : boxes) {
            if (aabb != null) {
                if (box == null) box = aabb;

                Vector2f length1 = box.getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());
                Vector2f length2 = aabb.getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = aabb;
                }
            }
        }
        return box;
    }

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", this.transform.getProjection(target));
        this.texture.bind(0);
        this.model.render();
    }

    public static void initAsset() {
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

        model = new Model(vertices, texture, indices);
    }

    public static void deleteAsset() {
        model = null;
    }
}
