package com.bufkin.lwjgl_game.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Model implements AutoCloseable {
	private int draw_count;
	private int v_id;
	private int t_id;
	private int i_id;

	public Model(float[] vertices, float[] tex_coords, int[] indices) {
		this.draw_count = indices.length;

		this.v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.v_id);
		glBufferData(GL_ARRAY_BUFFER, this.createBuffer(vertices), GL_STATIC_DRAW);

		this.t_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.t_id);
		glBufferData(GL_ARRAY_BUFFER, this.createBuffer(tex_coords), GL_STATIC_DRAW);

		this.i_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.i_id);

		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();

		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void render() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, this.v_id);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, this.t_id);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.i_id);
		glDrawElements(GL_TRIANGLES, this.draw_count, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}

	private FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void close() throws Exception {
		glDeleteBuffers(this.v_id);
		glDeleteBuffers(this.t_id);
		glDeleteBuffers(this.i_id);
	}
}
