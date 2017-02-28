package pl.asie.ponysocks;

import net.minecraft.client.renderer.entity.RenderBiped;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	@Override
	public int getRenderIndexArmor(String prefix) {
		if (prefix == null) {
			return 0;
		}

		for (int i = 0; i < RenderBiped.bipedArmorFilenamePrefix.length; i++) {
			if (prefix.equals(RenderBiped.bipedArmorFilenamePrefix[i])) {
				return i;
			}
		}
		return RenderingRegistry.addNewArmourRendererPrefix(prefix);
	}

	@Override
	public void registerRenderers() {
		PonyRenderListener prl = new PonyRenderListener();
		MinecraftForge.EVENT_BUS.register(prl);
		FMLCommonHandler.instance().bus().register(prl);
	}
}
