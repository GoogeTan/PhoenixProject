package phoenix.init;

import phoenix.Phoenix;
import phoenix.utils.ShaderLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PhoenixShaiders
{
    private static HashMap<String, Integer> programs = new HashMap<>();
    public static void initShaders()
    {
        //programs.put("normal", ShaderLoader.loadProgram(getFile(("/assets/phoenix/shaders/normal_vert.glsl")), getFile("/assets/phoenix/shaders/normal_frag.glsl")));
    }

    public static int getProgram(String name)
    {
        if(programs.containsKey(name))
        {
            return programs.get(name);
        }
        return 0;
    }

    private static InputStream getFile(String path)
    {
        return PhoenixShaiders.class.getResourceAsStream(path);
    }

    private static InputStream getFileShader(String path)
    {
        return PhoenixShaiders.class.getResourceAsStream("/assets/" + Phoenix.MOD_ID + "/shaders/" + path);
    }
}
