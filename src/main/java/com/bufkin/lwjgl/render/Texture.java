package com.bufkin.lwjgl.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture {
    private int id, width, height;

    public Texture(String fileName) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File("./res/" + fileName));
            this.width = bi.getWidth();
            this.height = bi.getHeight();

            int[] pixels_raw;
            pixels_raw = bi.getRGB(0, 0, this.width, this.height, null, 0, this.width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(this.width * this.height * 4);

            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < this.width; j++) {
                    int pixel = pixels_raw[i * this.width + j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF));  // RED
                    pixels.put((byte) ((pixel >> 8) & 0xFF));   // GREEN
                    pixels.put((byte) (pixel & 0xFF));          // BLUE
                    pixels.put((byte) ((pixel >> 24) & 0xFF));  // ALPHA
                }
            }
            pixels.flip();
            this.id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, this.id);
        }
    }
}
