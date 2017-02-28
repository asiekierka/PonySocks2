package pl.asie.ponysocks;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = PonySocks.MODID, version = PonySocks.VERSION, acceptableRemoteVersions="*")
public class PonySocks {
	private static final boolean DEBUG_LOGIN = false;

	public static CreativeTabs tabSocks = new CreativeTabs("tabPonySocks") {
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(sock);
		}

		@Override
		public Item getTabIconItem() {
			return sock;
		}
	};

    @SidedProxy(clientSide = "pl.asie.ponysocks.ClientProxy", serverSide = "pl.asie.ponysocks.CommonProxy")
    public static CommonProxy proxy;

	public static final int[] woolIds = new int[16];

    public static final String MODID = "ponysocks";
    public static final String VERSION = "2.0";
    public static final Random RANDOM = new Random();
	public static final Logger LOGGER = LogManager.getLogger("asietweaks");

	public static ItemSock sock;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		sock = new ItemSock();
		GameRegistry.registerItem(sock, "sockItem");
    }

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
	}

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
		String[] dyes =
				{
						"Black",
						"Red",
						"Green",
						"Brown",
						"Blue",
						"Purple",
						"Cyan",
						"LightGray",
						"Gray",
						"Pink",
						"Lime",
						"Yellow",
						"LightBlue",
						"Magenta",
						"Orange",
						"White"
				};

		for (int i = 0; i < 16; i++) {
			woolIds[i] = OreDictionary.getOreID("dye" + dyes[15 - i]);
		}

		GameRegistry.addShapedRecipe(new ItemStack(sock), "w w", "w w", 'w', Blocks.wool);
		GameRegistry.addRecipe(new RecipeSockColor());
    }
}
