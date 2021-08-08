package com.bufkin.lwjgl_game.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Shader implements AutoCloseable {
  private int program, vs, fs;

  public Shader(String fileName) {
    this.program = glCreateProgram();

    this.vs = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(this.vs, this.readFile(fileName + ".vs"));
    glCompileShader(this.vs);

    if (glGetShaderi(this.vs, GL_COMPILE_STATUS) != 1) {
      System.err.println(glGetShaderInfoLog(this.vs));
      System.exit(1);
    }

    this.fs = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(this.fs, this.readFile(fileName + ".fs"));
    glCompileShader(this.fs);

    if (glGetShaderi(this.fs, GL_COMPILE_STATUS) != 1) {
      System.err.println(glGetShaderInfoLog(this.fs));
      System.exit(1);
    }

    glAttachShader(this.program, this.vs);
    glAttachShader(this.program, this.fs);

    glBindAttribLocation(this.program, 0, "vertices");
    glBindAttribLocation(this.program, 1, "textures");

    glLinkProgram(this.program);
    if (glGetProgrami(this.program, GL_LINK_STATUS) != 1) {
      System.err.println(glGetProgramInfoLog(this.program));
      System.exit(1);
    }

    glValidateProgram(this.program);
    if (glGetProgrami(this.program, GL_VALIDATE_STATUS) != 1) {
      System.err.println(glGetProgramInfoLog(this.program));
      System.exit(1);
    }
  }

  public void setUniform(String name, int value) {
    int location = glGetUniformLocation(this.program, name);

    if (location != -1) {
      glUniform1i(location, value);
    }
  }

  public void setUniform(String name, Vector4f value) {
    int location = glGetUniformLocation(this.program, name);

    if (location != -1) {
      glUniform4f(location, value.x, value.y, value.z, value.w);
    }
  }

  public void setUniform(String name, Matrix4f value) {
    int location = glGetUniformLocation(this.program, name);
    FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
    value.get(buffer);

    if (location != -1) {
      glUniformMatrix4fv(location, false, buffer);
    }
  }

  public void bind() {
    glUseProgram(this.program);
  }

  private String readFile(String fileName) {
    StringBuilder string = new StringBuilder();
    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(new File("./shaders/" + fileName)));
      String line;
      while ((line = br.readLine()) != null) {
        string.append(line);
        string.append("\n");
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return string.toString();
  }

  public void close() throws Exception {
    glDetachShader(this.program, this.vs);
    glDetachShader(this.program, this.fs);
    glDeleteShader(this.vs);
    glDeleteShader(this.fs);
    glDeleteProgram(this.program);
  }
}
