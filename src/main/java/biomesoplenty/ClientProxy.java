package biomesoplenty;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.util.MathHelper;
import biomesoplenty.api.BOPItemHelper;
import biomesoplenty.client.particles.EntityDandelionFX;
import biomesoplenty.client.particles.EntityMagicTreeFX;
import biomesoplenty.client.particles.EntityPixieTrailFX;
import biomesoplenty.client.particles.EntitySteamFX;
import biomesoplenty.client.render.blocks.BambooRenderer;
import biomesoplenty.client.render.blocks.FoliageRenderer;
import biomesoplenty.client.render.blocks.GraveRenderer;
import biomesoplenty.client.render.blocks.PlantsRenderer;
import biomesoplenty.client.render.blocks.RenderUtils;
import biomesoplenty.client.render.blocks.SmallBlockRenderer;
import biomesoplenty.client.render.entities.RenderDart;
import biomesoplenty.common.entities.projectiles.EntityDart;
import biomesoplenty.common.entities.projectiles.EntityMudball;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy 
{
	public static Minecraft minecraft = Minecraft.getMinecraft();
	
	//Client Only
	@Override
	public void registerRenderers()
	{
        RenderUtils.foliageModel = RenderingRegistry.getNextAvailableRenderId();
        RenderUtils.plantsModel = RenderingRegistry.getNextAvailableRenderId();
        RenderUtils.bonesModel = RenderingRegistry.getNextAvailableRenderId();
        RenderUtils.graveModel = RenderingRegistry.getNextAvailableRenderId();
        RenderUtils.bambooModel = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderDart());
		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderSnowball(BOPItemHelper.get("mudball"), 0));
		
        RenderingRegistry.registerBlockHandler(new FoliageRenderer());
		RenderingRegistry.registerBlockHandler(new PlantsRenderer());
		RenderingRegistry.registerBlockHandler(new SmallBlockRenderer());
        RenderingRegistry.registerBlockHandler(new GraveRenderer());
        RenderingRegistry.registerBlockHandler(new BambooRenderer());
	}
	
	@Override
	public void spawnParticle(String string, double x, double y, double z)
	{
		EntityFX entityfx = null;
		
		Random rand = new Random();

		if (string == "mud") 
		{
			entityfx = new EntityBreakingFX(minecraft.theWorld, x, y, z, BOPItemHelper.get("mudball"));
		} 
		else if (string == "dart") 
		{
			entityfx = new EntityBreakingFX(minecraft.theWorld, x, y, z, BOPItemHelper.get("dart"), 0);
		} 
		else if (string == "poisondart") 
		{
			entityfx = new EntityBreakingFX(minecraft.theWorld, x, y, z, BOPItemHelper.get("dart"), 1);
		} 
		else if (string == "dandelion") 
		{
			entityfx = new EntityDandelionFX(minecraft.theWorld, x, y, z, 2.0F);
		} 
		else if (string == "steam") 
		{
			entityfx = new EntitySteamFX(minecraft.theWorld, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		else if (string == "magictree") 
		{
			entityfx = new EntityMagicTreeFX(minecraft.theWorld, x, y, z, MathHelper.getRandomDoubleInRange(rand, -0.03, 0.03), -0.02D, MathHelper.getRandomDoubleInRange(rand, -0.03, 0.03));
		}
		else if (string == "pixietrail") 
		{
			entityfx = new EntityPixieTrailFX(minecraft.theWorld, x, y, z, MathHelper.getRandomDoubleInRange(rand, -0.03, 0.03), -0.02D, MathHelper.getRandomDoubleInRange(rand, -0.03, 0.03));
		}

		minecraft.effectRenderer.addEffect(entityfx);
	}
	
	@Override
	public int addArmor(String armor)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}
}