/*******************************************************************************
 * Copyright 2014-2020, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package biomesoplenty.api.enums;

import biomesoplenty.init.ModBiomes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraftforge.common.BiomeManager.BiomeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public enum BOPClimates
{
    ICE_CAP (BiomeType.ICY),
    TUNDRA (BiomeType.ICY),
    WET_BOREAL (BiomeType.COOL),
    DRY_BOREAL (BiomeType.COOL),
    WET_TEMPERATE (BiomeType.COOL),
    DRY_TEMPERATE (BiomeType.WARM),
    COOL_TEMPERATE (BiomeType.COOL),
    WARM_TEMPERATE (BiomeType.WARM),
    SUBTROPICAL (BiomeType.WARM),
    TROPICAL (BiomeType.DESERT),
    MEDITERRANEAN (BiomeType.WARM),
    SAVANNA (BiomeType.DESERT),
    HOT_DESERT (BiomeType.DESERT),
    WASTELAND (null),
    NETHER (null);

    private static Set<Biome> registeredBiomes = Sets.newHashSet();
    public final BiomeType biomeType;
    private int totalBiomesWeight;
    private int totalIslandBiomesWeight;

    private ArrayList<WeightedBiomeEntry> landBiomes = Lists.newArrayList();
    private ArrayList<WeightedBiomeEntry> islandBiomes = Lists.newArrayList();

    BOPClimates(BiomeType biomeType)
    {
        this.biomeType = biomeType;
    }

    public static ImmutableSet<Biome> getRegisteredBiomes() {
        return ImmutableSet.copyOf(registeredBiomes);
    }

    public BOPClimates addBiome(int weight, Biome biome)
    {
        return this.addBiome(new WeightedBiomeEntry(weight, biome));
    }

    public BOPClimates addBiome(WeightedBiomeEntry biomeEntry)
    {
        registeredBiomes.add(biomeEntry.biome);
        this.totalBiomesWeight += biomeEntry.weight;
        this.landBiomes.add(biomeEntry);
        return this;
    }

    public BOPClimates addIslandBiome(int weight, Biome biome)
    {
        return this.addIslandBiome(new WeightedBiomeEntry(weight, biome));
    }

    public BOPClimates addIslandBiome(WeightedBiomeEntry biomeEntry)
    {
        registeredBiomes.add(biomeEntry.biome);
        this.totalIslandBiomesWeight += biomeEntry.weight;
        this.islandBiomes.add(biomeEntry);
        return this;
    }

    public Biome getRandomBiome(INoiseRandom context, Biome fallback)
    {
        if (this.totalBiomesWeight == 0)
            return fallback;

        int weight = context.nextRandom(this.totalBiomesWeight);
        Iterator<WeightedBiomeEntry> iterator = this.landBiomes.iterator();
        WeightedBiomeEntry item;
        do
        {
            item = iterator.next();
            weight -= item.weight;
        }
        while (weight >= 0);
        return item.biome;
    }

    public Biome getRandomIslandBiome(INoiseRandom context, Biome fallback)
    {
        if (this.totalIslandBiomesWeight == 0)
            return fallback;

        int weight = context.nextRandom(this.totalIslandBiomesWeight);
        Iterator<WeightedBiomeEntry> iterator = this.islandBiomes.iterator();
        WeightedBiomeEntry item;
        do
        {
            item = iterator.next();
            weight -= item.weight;
        }
        while (weight >= 0);
        return item.biome;
    }

    public Biome getRandomOceanBiome(INoiseRandom context, boolean deep)
    {
        return (deep ? Biomes.DEEP_OCEAN : Biomes.OCEAN);
    }

    public ImmutableList<WeightedBiomeEntry> getLandBiomes()
    {
        return this.landBiomes.isEmpty() ? ImmutableList.of(this.getDefaultWeightedBiomeEntry()) : ImmutableList.copyOf(this.landBiomes);
    }

    public ImmutableList<WeightedBiomeEntry> getIslandBiomes()
    {
        return this.islandBiomes.isEmpty() ? ImmutableList.of(this.getDefaultWeightedBiomeEntry()) : ImmutableList.copyOf(this.islandBiomes);
    }

    private WeightedBiomeEntry getDefaultWeightedBiomeEntry()
    {
        return new WeightedBiomeEntry(100, Biomes.OCEAN);
    }

    private static BOPClimates[] values = BOPClimates.values();

    public static BOPClimates lookup(int i) {return values[i];}

    // map temperature and rainfall to climates
    // temperature values from 0 (cold) to 8 (hot) and rainfall values from 0 (wet) to 11 (dry), index is (temperatureValue * 12) + rainfallValue
    // we will contrive to make any combination equally likely, so the overall rarity of each climate is in proportion to the number of times it appears in the array
    private static final BOPClimates[] climateMapping = new BOPClimates[]
    {
    	WET_BOREAL,		TUNDRA,			ICE_CAP,		ICE_CAP,		ICE_CAP,		ICE_CAP,		ICE_CAP,		ICE_CAP,		ICE_CAP,		ICE_CAP,		TUNDRA,			DRY_BOREAL,
    	WET_BOREAL,		WET_BOREAL,		TUNDRA,			TUNDRA,			TUNDRA,			TUNDRA,			TUNDRA,			TUNDRA,			TUNDRA,			TUNDRA,			DRY_BOREAL,		DRY_BOREAL,
    	WET_TEMPERATE,	WET_BOREAL,		WET_BOREAL,		WET_BOREAL,		WET_BOREAL,		WET_BOREAL,		DRY_BOREAL,		DRY_BOREAL,		DRY_BOREAL,		DRY_BOREAL,		DRY_BOREAL,		DRY_TEMPERATE,
    	WET_TEMPERATE,	WET_TEMPERATE,	WET_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	DRY_TEMPERATE,	DRY_TEMPERATE,	DRY_TEMPERATE,
    	WET_TEMPERATE,	WET_TEMPERATE,	WET_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	COOL_TEMPERATE,	DRY_TEMPERATE,	DRY_TEMPERATE,	DRY_TEMPERATE,
    	SUBTROPICAL,	SUBTROPICAL,	SUBTROPICAL,	WARM_TEMPERATE, WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	SAVANNA,		SAVANNA,		SAVANNA,
    	TROPICAL,		TROPICAL,		SUBTROPICAL,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	WARM_TEMPERATE,	SAVANNA,		HOT_DESERT,		HOT_DESERT,
    	TROPICAL,		TROPICAL,		SUBTROPICAL,	SUBTROPICAL,	MEDITERRANEAN,	MEDITERRANEAN,	MEDITERRANEAN,	MEDITERRANEAN,	SAVANNA,		SAVANNA,		HOT_DESERT,		HOT_DESERT,
    	TROPICAL,		TROPICAL,		TROPICAL,		SUBTROPICAL,	MEDITERRANEAN,	MEDITERRANEAN,	MEDITERRANEAN,	MEDITERRANEAN,	SAVANNA,		HOT_DESERT,		HOT_DESERT,		WASTELAND
    };

    public static int[] getClimateMappingInts()
    {
        // 9 temperature values, 12 rainfall values, 12 * 9 = 108
        int[] out = new int[108];
        for (int i = 0; i < 108; i++)
        {
            out[i] = climateMapping[i].ordinal();
        }
        return out;
    }

    public static ImmutableSet<Biome> getOverworldBiomes()
    {
        Set<Biome> set = Sets.newHashSet();

        for (BOPClimates climate : BOPClimates.values())
        {
            if (climate == BOPClimates.NETHER)
            {
                continue;
            }

            set.addAll(climate.getLandBiomes().stream().map(weightedBiomeEntry -> weightedBiomeEntry.biome).collect(Collectors.toSet()));
            set.addAll(climate.getIslandBiomes().stream().map(weightedBiomeEntry -> weightedBiomeEntry.biome).collect(Collectors.toSet()));
        }

        ModBiomes.subBiomes.values().forEach(weightedSubBiome -> set.add(weightedSubBiome.biome));
        return ImmutableSet.copyOf(set);
    }

    public static class WeightedBiomeEntry
    {
        public final int weight;
        public final Biome biome;

        public WeightedBiomeEntry(int weight, Biome biome)
        {
            this.weight = weight;
            this.biome = biome;
        }
    }

    // for debugging purposes
    public static void printWeights()
    {
        for (BOPClimates climate : BOPClimates.values())
        {
            for (WeightedBiomeEntry entry : climate.landBiomes)
            {
                System.out.println(climate.name()+" "+entry.biome.getName()+" "+entry.weight);
            }
        }
    }

}
