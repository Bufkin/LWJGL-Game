package com.bufkin.lwjgl.game;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Shader {
    private int program, vs, fs;
    
    public Shader(String fileName) {
        program = glCreateProgram();
        
        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(fileName + ".vs"));
        glCompileShader(vs);
        
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }
        
        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(fileName + ".fs"));
        glCompileShader(fs);
        
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }
        
        glAttachShader(program, vs);
        glAttachShader(program, fs);
        
        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");
        
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }
    
    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        
        if (location != -1) {
            glUniform1i(location, value);
        }
    }
    
    public void setUniform(String name, Matrix4f value) {
        int         location = glGetUniformLocation(program, name);
        FloatBuffer buffer   = BufferUtils.createFloatBuffer(4 * 4);
        value.get(buffer);
        
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }
    
    public void bind() {
        glUseProgram(program);
    }
    
    private String readFile(String fileName) {
        StringBuilder  string = new StringBuilder();
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
}
