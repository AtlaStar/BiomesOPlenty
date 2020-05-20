/*******************************************************************************
 * Copyright 2014-2019, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package biomesoplenty.common.world.layer;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.enums.BOPClimates;
import biomesoplenty.common.util.biome.BiomeUtil;
import biomesoplenty.common.world.BOPLayerUtil;
import biomesoplenty.common.world.layer.traits.IAreaTransformer3;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

public enum BOPMixOceansLayer implements IAreaTransformer3, IDimOffset0Transformer
{
    INSTANCE;

    @Override
    public int applyPixel(INoiseRandom context, IArea biomeArea, IArea oceanArea, IArea climateArea, int x, int z)
    {
        int biomeId = biomeArea.get(x, z);
        int oceanId = oceanArea.get(x, z);
        int climateVal = climateArea.get(x, z);
        BOPClimates climate = BOPClimates.lookup(climateVal);

        if (!BOPLayerUtil.isOcean(biomeId))
        {
            return biomeId;
        }
        else
        {
        	switch (climate)
            {
                case ICE_CAP:
                    oceanId = BOPLayerUtil.FROZEN_OCEAN;
                    break;

                case TUNDRA:
                case WET_BOREAL:
                case DRY_BOREAL:
                    oceanId = BOPLayerUtil.COLD_OCEAN;
                    break;

                case WARM_TEMPERATE:
                case SUBTROPICAL:
                case MEDITERRANEAN:
                case SAVANNA:
                    oceanId = BOPLayerUtil.LUKEWARM_OCEAN;
                    break;

                case TROPICAL:
                case HOT_DESERT:
                    oceanId = BOPLayerUtil.WARM_OCEAN;
                    break;

                case WASTELAND:
                    if (BOPBiomes.wasteland.isPresent())
                    {
                        oceanId = BiomeUtil.GetBiomeIDFromRegistry(BOPBiomes.wasteland.get().getRegistryName());
                    }
                    // Fallthrough

                default:
                    oceanId = BOPLayerUtil.OCEAN;
                    break;
            }
        	
            // When far from land, warm oceans should become lukewarm and frozen oceans should become cold
            /*for (int xOff = -8; xOff <= 8; xOff += 4)
            {
                for (int zOff = -8; zOff <= 8; zOff += 4)
                {
                    int offsetBiomeId = biomeArea.get(x + xOff, z + zOff);

                    if (!BOPLayerUtil.isOcean(offsetBiomeId))
                    {
                        if (oceanId == BOPLayerUtil.WARM_OCEAN)
                        {
                            return BOPLayerUtil.LUKEWARM_OCEAN;
                        }

                        if (oceanId == BOPLayerUtil.FROZEN_OCEAN)
                        {
                            return BOPLayerUtil.COLD_OCEAN;
                        }
                    }
                }
            }*/

            if (biomeId == BOPLayerUtil.DEEP_OCEAN)
            {
                if (oceanId == BOPLayerUtil.WARM_OCEAN)
                {
                    return BOPLayerUtil.DEEP_WARM_OCEAN;
                }

                if (oceanId == BOPLayerUtil.LUKEWARM_OCEAN)
                {
                    return BOPLayerUtil.DEEP_LUKEWARM_OCEAN;
                }

                if (oceanId == BOPLayerUtil.OCEAN)
                {
                    return BOPLayerUtil.DEEP_OCEAN;
                }

                if (oceanId == BOPLayerUtil.COLD_OCEAN)
                {
                    return BOPLayerUtil.DEEP_COLD_OCEAN;
                }

                if (oceanId == BOPLayerUtil.FROZEN_OCEAN)
                {
                    return BOPLayerUtil.DEEP_FROZEN_OCEAN;
                }

                if (BOPBiomes.wasteland.isPresent() && oceanId == BiomeUtil.GetBiomeIDFromRegistry(BOPBiomes.wasteland.get().getRegistryName()))
                {
                    return BiomeUtil.GetBiomeIDFromRegistry(BOPBiomes.wasteland.get().getRegistryName());
                }
            }

            return oceanId;
        }
    }
}
