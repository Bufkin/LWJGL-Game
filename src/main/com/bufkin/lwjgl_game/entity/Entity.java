package com.bufkin.lwjgl_game.entity;

import com.bufkin.lwjgl_game.assets.Assets;
import com.bufkin.lwjgl_game.collision.AABB;
import com.bufkin.lwjgl_game.collision.Collision;
import com.bufkin.lwjgl_game.io.Window;
import com.bufkin.lwjgl_game.render.Animation;
import com.bufkin.lwjgl_game.render.Camera;
import com.bufkin.lwjgl_game.render.Shader;
import com.bufkin.lwjgl_game.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


public abstract class Entity {
  private AABB boundingBox;
  private Animation[] animations;
  private int use_animation;

  Transform transform;

  Entity(int max_animations, Transform transform) {
    this.animations = new Animation[max_animations];
    this.transform = transform;
    this.use_animation = 0;
    this.boundingBox = new AABB(new Vector2f(this.transform.pos.x, this.transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
  }

  void setAnimation(int index, Animation animation) {
    this.animations[index] = animation;
  }

  void useAnimation(int index) {
    this.use_animation = index;
  }

  void move(Vector2f direction) {
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
    this.animations[this.use_animation].bind(0);
    Assets.getModel().render();
  }

  public void collideWithEntity(Entity entity) {
    Collision collision = this.boundingBox.getCollision(entity.boundingBox);

    if (collision.isIntersecting) {
      collision.distance.x /= 2;
      collision.distance.y /= 2;

      this.boundingBox.correctPosition(entity.boundingBox, collision);
      this.transform.pos.set(this.boundingBox.getCenter().x, this.boundingBox.getCenter().y, 0);

      entity.boundingBox.correctPosition(this.boundingBox, collision);
      entity.transform.pos.set(entity.boundingBox.getCenter().x, entity.boundingBox.getCenter().y, 0);
    }
  }
}
