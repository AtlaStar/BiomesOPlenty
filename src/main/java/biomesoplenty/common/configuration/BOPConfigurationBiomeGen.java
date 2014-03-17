package biomesoplenty.common.configuration;

import biomesoplenty.api.BOPBiomeHelper;
import biomesoplenty.api.BOPBiomeHelper.BOPBiomeEntry;
import biomesoplenty.api.BOPBiomeHelper.TemperatureType;
import biomesoplenty.common.core.BOPWorld;
import biomesoplenty.common.world.layer.hell.BiomeLayerHellBiomes;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;

public class BOPConfigurationBiomeGen
{
	public static Configuration config;
	
	public static ArrayList<String> disabledBiomes = new ArrayList();
	
	public static boolean oceanGen = false;
	public static boolean frozenOceanGen = false;
	public static boolean deepOceanGen = false;
	public static boolean mushroomIslandGen = false;
	public static boolean mesaPlateauFGen = false;
	public static boolean mesaPlateauGen = false;
	public static boolean jungleGen = false;
	public static boolean megaTaigaGen = false;
	
	public static void addDefaultDisabledBiomes()
	{
	}

	public static void init(File configFile)
	{
		addDefaultDisabledBiomes();
		
		config = new Configuration(configFile);

		try
		{
			config.load();
			
			for (BOPBiomeEntry entry : BOPBiomeHelper.biomeLists[-1 + 1].values())
			{
				BiomeGenBase biome = entry.biome;
				
				String name = biome.biomeName;
				String convertedName = BOPBiomeHelper.convertBiomeName(name);

				if (config.get("Nether Biomes To Generate (There must be at least one from each category)", name, !disabledBiomes.contains(convertedName)).getBoolean(!disabledBiomes.contains(convertedName)))
				{
					BiomeLayerHellBiomes.netherBiomes.add(biome);
				}
			}

			for (BOPBiomeEntry entry : BOPBiomeHelper.biomeLists[0 + 1].values())
			{
				BiomeGenBase biome = entry.biome;
				
				String name = biome.biomeName;
				String convertedName = BOPBiomeHelper.convertBiomeName(name);

				if (config.get("Overworld Biomes To Generate (There must be at least one from each category)", name + " (" + WordUtils.capitalize(entry.temperatureType.toString().toLowerCase()) + ")", !disabledBiomes.contains(convertedName)).getBoolean(!disabledBiomes.contains(convertedName)))
				{
					if (BOPWorld.onlyBiome != null ? entry == BOPWorld.onlyBiome : true)
					{
						entry.addToCorrespondingTemperatureTypeList();
					}
				}
			}
			
			if (BOPWorld.onlyBiome != null)
			{
		        for (TemperatureType temperatureType : BOPBiomeHelper.TemperatureType.values())
		        {
		        	BOPBiomeHelper.getCorrespondingTemperatureTypeList(temperatureType).add(BOPWorld.onlyBiome);
		        }
			}
			else
			{		
				oceanGen = config.get("Special Biomes To Generate", "Ocean", true).getBoolean(true);
				frozenOceanGen = config.get("Special Biomes To Generate", "FrozenOcean", true).getBoolean(true);
				deepOceanGen = config.get("Special Biomes To Generate", "Deep Ocean", true).getBoolean(true);
				mushroomIslandGen = config.get("Special Biomes To Generate", "MushroomIsland", true).getBoolean(true);
				mesaPlateauFGen = config.get("Special Biomes To Generate", "Mesa Plateau F", true).getBoolean(true);
				mesaPlateauGen = config.get("Special Biomes To Generate", "Mesa Plateau", true).getBoolean(true);
				jungleGen = config.get("Special Biomes To Generate", "Jungle", true).getBoolean(true);
				megaTaigaGen = config.get("Special Biomes To Generate", "Mega Taiga", true).getBoolean(true);
			}
		}
		catch (Exception e)
		{
			FMLLog.log(Level.ERROR, e, "Biomes O Plenty has had a problem loading its configuration");
		}
		finally
		{
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
}