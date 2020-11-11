package phoenix.utils;


import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderLoader
{
        public static int loadProgram(InputStream vertexShaderPath, InputStream fragmentShaderPath) {
            StringBuilder vertexShader = new StringBuilder(), fragmentShader = new StringBuilder();
            int vertexShaderID = 0, fragmentShaderID = 0, program = 0;
            String vertexBuffer = "", fragmentBuffer = "";

            //Читаем шейдеры
            try {
                BufferedReader vertexShaderReader = new BufferedReader(new InputStreamReader(vertexShaderPath));
                BufferedReader fragmentShaderReader = new BufferedReader(new InputStreamReader(fragmentShaderPath));
                while ((vertexBuffer = vertexShaderReader.readLine()) != null) {
                    vertexShader.append(vertexBuffer);
                    vertexShader.append("\n");
                }

                while ((fragmentBuffer = fragmentShaderReader.readLine()) != null) {
                    fragmentShader.append(fragmentBuffer);
                    fragmentShader.append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Создание шейдера...");

            //Создаём шейдеры
            vertexShaderID = createShader(vertexShader.toString(), ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragmentShaderID = createShader(fragmentShader.toString(), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

            System.out.println("Создание программы...");

            //Создаём новую программу
            program = ARBShaderObjects.glCreateProgramObjectARB();

            System.out.println("Прикрепление шейдера к программе");

            //Прикрепляем шейдеры к программе
            ARBShaderObjects.glAttachObjectARB(program, vertexShaderID);
            ARBShaderObjects.glAttachObjectARB(program, fragmentShaderID);


            System.out.println("Линкуем программу...");

            ARBShaderObjects.glLinkProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println("Ошибка линковки программы!");
            }

            System.out.println("Проверяем программу...");

            ARBShaderObjects.glValidateProgramARB(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println("Ошибка проверки шейдерной программы!");
            }

            System.out.println("Шейдер загружен!");

            return program;
        }

        public static int loadProgram(String vertexShaderPath, String fragmentShaderPath) {
            StringBuilder vertexShader = new StringBuilder();
            StringBuilder fragmentShader = new StringBuilder();
            String vertexBuffer = "";
            String fragmentBuffer = "";
            try {
                BufferedReader vertexShaderReader = new BufferedReader(new FileReader(vertexShaderPath));
                BufferedReader fragmentShaderReader = new BufferedReader(new FileReader(fragmentShaderPath));
                while ((vertexBuffer = vertexShaderReader.readLine()) != null) {
                    vertexShader.append(vertexBuffer);
                    vertexShader.append("\n");
                }

                while ((fragmentBuffer = fragmentShaderReader.readLine()) != null) {
                    fragmentShader.append(fragmentBuffer);
                    fragmentShader.append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return createProgram(vertexShader.toString(), fragmentShader.toString());
        }

        public static int createProgram(String vertexShader, String fragmentShader) {
            int program = GL20.glCreateProgram();

            int vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            int fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

            GL20.glShaderSource(vertexShaderID, vertexShader);
            GL20.glShaderSource(fragmentShaderID, fragmentShader);

            GL20.glCompileShader(vertexShaderID);

            if (GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                System.err.println("Failed to compile vertex shader!!!");
                System.err.println(GL20.glGetShaderInfoLog(vertexShaderID, 100));
            }

            GL20.glCompileShader(fragmentShaderID);

            if (GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                System.err.println("Failed to compile fragment shader!!!");
                System.err.println(GL20.glGetShaderInfoLog(fragmentShaderID, 100));
            }

            GL20.glAttachShader(program, vertexShaderID);
            GL20.glAttachShader(program, fragmentShaderID);

            GL20.glLinkProgram(program);
            GL20.glValidateProgram(program);
            return program;
        }

        public static int createShader(String shader, int shaderType) {
            int shaderId = 0;
            try {
                shaderId = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
                if (shaderId == 0) {
                    return 0;
                }
                ARBShaderObjects.glShaderSourceARB(shaderId, shader);
                ARBShaderObjects.glCompileShaderARB(shaderId);
                if (ARBShaderObjects.glGetObjectParameteriARB(shaderId, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                    throw new RuntimeException("Error creating shader: " + ARBShaderObjects.glGetInfoLogARB(shaderId, ARBShaderObjects.glGetObjectParameteriARB(shaderId, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));

                return shaderId;
            } catch (Exception exc) {
                ARBShaderObjects.glDeleteObjectARB(shaderId);
                throw exc;
            }
        }
}
