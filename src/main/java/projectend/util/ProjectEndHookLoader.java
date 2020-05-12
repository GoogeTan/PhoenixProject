package projectend.util;

import hooklib.minecraft.HookLoader;
import hooklib.minecraft.PrimaryClassTransformer;

public class ProjectEndHookLoader extends HookLoader
{
    // включает саму HookLib'у. Делать это можно только в одном из HookLoader'ов. При желании, можно включить hooklib.minecraft.HookLibPlugin и не указывать здесь это вовсе.
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks()
    {
       registerHookContainer("projectend.util.MyHooks");
    }
}

