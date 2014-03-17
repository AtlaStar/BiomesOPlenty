package biomesoplenty.common.world.features;

import biomesoplenty.api.BOPBlockHelper;
import biomesoplenty.common.world.decoration.BOPDecorationManager;
import biomesoplenty.common.world.generation.WorldGeneratorBOP;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

public class WorldGenWasteland4 extends WorldGeneratorBOP
{
    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        //TODO:      isAirBlock()
        while (world.isAirBlock(x, y, z) && y > 2)
        {
            --y;
        }

        Block var6 = world.getBlock(x, y, z);
        Block var95 = world.getBlock(x - 1, y, z);
        Block var96 = world.getBlock(x + 1, y, z);
        Block var97 = world.getBlock(x, y, z - 1);
        Block var98 = world.getBlock(x, y, z + 1);

        if (var6 != BOPBlockHelper.get("driedDirt") || var95 != BOPBlockHelper.get("driedDirt") || var96 != BOPBlockHelper.get("driedDirt") || var97 != BOPBlockHelper.get("driedDirt") || var98 != BOPBlockHelper.get("driedDirt") )
            return false;
        else
        {
            for (int var7 = -2; var7 <= 2; ++var7)
            {
                for (int var8 = -2; var8 <= 2; ++var8)
                {
                    //TODO:  isAirBlock()                                       isAirBlock()
                    if (world.isAirBlock(x + var7, y - 1, z + var8) && world.isAirBlock(x + var7, y - 2, z + var8))
                    {
                        return false;
                    }
                }
            }

            int var999 = random.nextInt(2);

            if (var999 == 0)
            {
                //TODO: setBlock()
                this.func_150515_a(world, x, y - 1, z, Blocks.air);
                this.func_150515_a(world, x + 1, y - 1, z, Blocks.air);
                this.func_150515_a(world, x - 1, y - 1, z, Blocks.air);
                this.func_150515_a(world, x, y - 1, z + 1, Blocks.air);
                this.func_150515_a(world, x, y - 1, z - 1, Blocks.air);
                this.func_150515_a(world, x, y - 2, z, Blocks.air);
                return true;
            }
            if (var999 == 1)
            {
                this.func_150515_a(world, x, y - 1, z, Blocks.air);
                return true;
            }

            return true;
        }
    }

    @Override
    public void setupGeneration(World world, Random random, BiomeGenBase biome, String featureName, int x, int z)
    {
        for (int i = 0; i < (Integer)BOPDecorationManager.getBiomeFeatures(biome.biomeID).getFeature(featureName); i++)
        {
            int randX = x + random.nextInt(16) + 8;
            int randZ = z + random.nextInt(16) + 8;
            int randY = random.nextInt(world.getHeightValue(randX, randZ) * 2);

            this.generate(world, random, randX, randY, randZ);
        }
    }
}
