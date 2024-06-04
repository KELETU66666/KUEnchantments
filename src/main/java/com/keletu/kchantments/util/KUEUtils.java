package com.keletu.kchantments.util;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class KUEUtils {
    @Nullable
    public static BlockCoord randomBlock(World world, EntityPlayer player, int radius, Predicate<BlockCoord> isValid) {
        int x = (int) player.posX + world.rand.nextInt(radius) - world.rand.nextInt(radius);
        int y = (int) player.posY + world.rand.nextInt(radius) - world.rand.nextInt(radius);
        int z = (int) player.posZ + world.rand.nextInt(radius) - world.rand.nextInt(radius);

        BlockCoord block = new BlockCoord(x, y, z);
        return isValid.test(block) ? block : null;
    }

    public static boolean hasNonSolidNeighbor(World world, BlockCoord coord)
    {
        for (BlockCoord n : coord.getNearby())
        {
            if (n.isAir(world) || !n.getBlock(world).isOpaqueCube(n.getBlock(world).getDefaultState()))
                return true;
        }
        return false;
    }
}
