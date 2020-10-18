package phoenix.world;

import net.minecraft.util.math.BlockPos;

public class BlockPosUtils
{
    public static double distanceTo(BlockPos first, BlockPos second)
    {
        return Math.sqrt((first.getX() - second.getX()) * (first.getX() - second.getX()) +
                         (first.getX() - second.getY()) * (first.getY() - second.getY()) +
                         (first.getX() - second.getZ()) * (first.getZ() - second.getZ()));
    }
}
