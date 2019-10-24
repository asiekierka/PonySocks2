/*
 * Copyright (C) 2015, 2017, 2019 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks;

import java.util.*;

import com.google.common.collect.Lists;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import pl.asie.ponysocks.recipe.*;

@Mod(modid = PonySocks.MODID, version = PonySocks.VERSION, updateJSON = "https://asie.pl/files/minecraft/update/" + PonySocks.MODID + ".json")
public class PonySocks {
	public static CreativeTabs tabSocks = new CreativeTabs("tabPonySocks") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(sock);
		}
	};

    @SidedProxy(clientSide = "pl.asie.ponysocks.ClientProxy", serverSide = "pl.asie.ponysocks.CommonProxy")
    public static CommonProxy proxy;

	public static final int[] dyeOreIds = new int[16];

    public static final String MODID = "ponysocks";
    public static final String VERSION = "2.0";
	public static final Logger LOGGER = LogManager.getLogger("ponysocks");

	public static ItemSock sock;
	public static Collection<IRecipe> specialDyeRecipes = new ArrayList<>();
	private static List<ItemStack> socksOrdered;
	private static List<ItemStack> socksShuffled;

	private static void initSockLists() {
		if (socksOrdered == null) {
			socksOrdered = new ArrayList<>(256);
			int[] colors = new int[16];
			for (int i = 0; i < 16; i++) {
				colors[i] = RecipeDyeableBase.fromFloats(EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(i)));
			}

			for (int i = 0; i < 256; i++) {
				ItemStack sock = new ItemStack(PonySocks.sock, 1, 0);
				sock.setTagCompound(new NBTTagCompound());
				sock.getTagCompound().setInteger("color1", colors[i & 15]);
				sock.getTagCompound().setInteger("color2", colors[i >> 4]);
				socksOrdered.add(sock);
			}

			socksShuffled = Lists.newArrayList(socksOrdered);
			Collections.shuffle(socksShuffled);
		}
	}

	public static List<ItemStack> getSocksOrdered() {
		initSockLists();
		return socksOrdered;
	}

	public static List<ItemStack> getSocksShuffled() {
		initSockLists();
		return socksShuffled;
	}

	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		try {
			if (Class.forName("com.minelittlepony.model.pony.armor.ModelPonyArmor") != null) {
				throw new RuntimeException("PonySocks 2.2+ requires a newer version of MineLittlePony (>= 3.2.7). Please update!");
			}
		} catch (ClassNotFoundException e) {
			// pass
		}

    	MinecraftForge.EVENT_BUS.register(this);
    	MinecraftForge.EVENT_BUS.register(proxy);
    	MinecraftForge.EVENT_BUS.register(new RecipeSockWashHandler());

		sock = new ItemSock();
		sock.setRegistryName(new ResourceLocation(MODID, "sockitem"));

		proxy.preInit();
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(sock);
    }

    @SubscribeEvent
    public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event){
    	event.getRegistry().register(new RecipeSockCreate(new ResourceLocation(MODID, "sock_create"), "ponysocks:sock"));
	    event.getRegistry().register(new RecipeSockPaint(new ResourceLocation(MODID, "sock_paint"), "ponysocks:sock"));
	    event.getRegistry().register(new RecipeSockClear(new ResourceLocation(MODID, "sock_clear"), "ponysocks:sock"));

	    specialDyeRecipes.add(new RecipeSockRainbow(new ResourceLocation(MODID, "sock_rainbow"), "ponysocks:sock"));

	    for (IRecipe recipe : specialDyeRecipes) {
		    event.getRegistry().register(recipe);
	    }
    }

	@EventHandler
	public void init(FMLInitializationEvent event) {
    	proxy.init();
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
			dyeOreIds[i] = OreDictionary.getOreID("dye" + dyes[15 - i]);
		}

		/* GameRegistry.addShapedRecipe(new ItemStack(sock), "w w", "w w", 'w', Blocks.wool);
		GameRegistry.addRecipe(new RecipeSockColor()); */
    }
}
