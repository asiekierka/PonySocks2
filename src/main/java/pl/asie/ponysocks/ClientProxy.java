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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.asie.ponysocks.render.IStackedArmor;
import pl.asie.ponysocks.render.ItemSockColor;
import pl.asie.ponysocks.render.ModelArmorSockBiped;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class ClientProxy extends CommonProxy {
	private static final ModelResourceLocation[] modelTypes = {
			new ModelResourceLocation("ponysocks:sockitem","inventory"),
			new ModelResourceLocation("ponysocks:sockitem","inventory_rainbow")
	};

	private static ModelBiped modelArmorHuman, modelArmorPony;

	public static ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot armorSlot, ModelBiped parentModel) {
		ModelBiped targetModel = null;

		if (!(parentModel.getClass().getName().startsWith("com.minelittlepony"))) {
			if (armorSlot == EntityEquipmentSlot.FEET) {
				if (modelArmorHuman == null) {
					modelArmorHuman = new ModelArmorSockBiped();
				}

				targetModel = modelArmorHuman;
			}
		} else {
			if (armorSlot == EntityEquipmentSlot.FEET) {
				if (modelArmorPony == null) {
					MethodHandle sockCreationHandle;

					try {
						Class.forName("com.minelittlepony.model.armour.ModelPonyArmor");

						try {
							sockCreationHandle = MethodHandles.lookup().unreflectConstructor(Class.forName("pl.asie.ponysocks.render.ModelArmorSock").getConstructor());
						} catch (Exception ee) {
							throw new RuntimeException(ee);
						}
					} catch (Exception e) {
						PonySocks.LOGGER.error("Using an unsupported version of Mine Little Pony - sock rendering will not work correctly!", e);

						try {
							sockCreationHandle = MethodHandles.lookup().unreflectConstructor(Class.forName("pl.asie.ponysocks.render.ModelArmorSockBiped").getConstructor());
						} catch (Exception ee) {
							throw new RuntimeException(ee);
						}
					}

					try {
						modelArmorPony = (ModelBiped) sockCreationHandle.invoke();
					} catch (Throwable t) {
						throw new RuntimeException(t);
					}
				}

				targetModel = modelArmorPony;
			}
		}

		if (targetModel != null) {
			((IStackedArmor) targetModel).setArmorStack(stack);
		}
		return targetModel;
	}

	@Override
	public void preInit() {
		ModelLoader.setCustomMeshDefinition(PonySocks.sock, stack -> modelTypes[stack.getMetadata() % modelTypes.length]);
		ModelBakery.registerItemVariants(PonySocks.sock, modelTypes);
	}

	@Override
	public void init() {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemSockColor(), PonySocks.sock);
	}
}
