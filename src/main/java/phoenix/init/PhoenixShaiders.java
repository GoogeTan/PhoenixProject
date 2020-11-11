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
        programs.put("Colorax", ShaderLoader.loadProgram(getFile(("/assets/hydramod/shaders/colorax/final.vsh")), getFile("/assets/hydramod/shaders/colorax/final.fsh")));
        programs.put("Warp1", ShaderLoader.loadProgram(getFile("/assets/hydramod/shaders/warp1/final.vsh"), getFile("/assets/hydramod/shaders/warp1/final.fsh")));
        programs.put("Glow", ShaderLoader.loadProgram(getFile("/assets/hydramod/shaders/glow/final.vsh"), getFile("/assets/hydramod/shaders/glow/final.fsh")));
        programs.put("Break", ShaderLoader.loadProgram(getFile("/assets/hydramod/shaders/break/final.vsh"), getFile("/assets/hydramod/shaders/break/final.fsh")));
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
