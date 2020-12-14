package phoenix.utils;

import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class BlockPosUtils
{
    public static double distanceTo(BlockPos first, BlockPos second)
    {
        return Math.sqrt((first.getX() - second.getX()) * (first.getX() - second.getX()) +
                (first.getX() - second.getY()) * (first.getY() - second.getY()) +
                (first.getX() - second.getZ()) * (first.getZ() - second.getZ()));
    }

    public static boolean isNear(BlockPos pos, Collection<BlockPos> poses, int range)
    {
        for(BlockPos pos1 : poses)
            if(distanceTo(pos, pos1) < range)
                return false;
        return true;
    }
}
