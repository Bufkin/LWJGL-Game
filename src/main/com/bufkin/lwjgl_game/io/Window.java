package com.bufkin.lwjgl_game.io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
	private long window;
	private int width, height;
	private boolean fullScreen;
	private boolean hasResized;
	private GLFWWindowSizeCallback windowSizeCallback;

	private com.bufkin.lwjgl_game.io.Input input;

	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallback() {
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
	}

	private void setLocalCallbacks() {
		this.windowSizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int argWidth, int argHeight) {
				Window.this.width = argWidth;
				Window.this.height = argHeight;
				Window.this.hasResized = true;
			}
		};
		glfwSetWindowSizeCallback(this.window, this.windowSizeCallback);
	}

	public Window() {
		this.setSize(640, 480);
		this.setFullScreen(false);
		this.hasResized = false;
	}

	public void createWindow(String title) {
		this.window = glfwCreateWindow(this.width, this.height, title, 0, 0);

		if (this.window == 0) {
			throw new IllegalStateException("Failed to create window!");
		}

		GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
		assert vid != null;
		glfwSetWindowPos(this.window, (vid.width() - this.width) / 2, (vid.height() - this.height) / 2);
		glfwShowWindow(this.window);
		glfwMakeContextCurrent(this.window);

		this.input = new com.bufkin.lwjgl_game.io.Input(this.window, mouseKeys);
		this.setLocalCallbacks();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void update() {
		this.hasResized = false;
		this.input.update();
		glfwPollEvents();
	}

	public boolean hasResized() {
		return this.hasResized;
	}

	public void cleanUp() {
		this.windowSizeCallback.close();
	}

	public com.bufkin.lwjgl_game.io.Input getInput() {
		return this.input;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.window);
	}

	public void swapBuffers() {
		glfwSwapBuffers(this.window);
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFullScreen() {
		return this.fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public long getWindow() {
		return this.window;
	}
}
