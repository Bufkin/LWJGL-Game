package com.bufkin.lwjgl_game.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
  private Vector3f position;
  private Matrix4f projection;

  public Camera(int width, int height) {
    this.position = new Vector3f(0, 0, 0);
    this.setProjection(width, height);
  }

  public void setProjection(int width, int height) {
    this.projection = new Matrix4f().setOrtho2D((float) -width / 2, (float) width / 2, (float) -height / 2, (float) height / 2);
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public void addPosition(Vector3f position) {
    this.position.add(position);
  }

  public Vector3f getPosition() {
    return this.position;
  }

  public Matrix4f getUntransformedProjection() {
    return this.projection;
  }

  public Matrix4f getProjection() {
    return this.projection.translate(this.position, new Matrix4f());
  }
}

