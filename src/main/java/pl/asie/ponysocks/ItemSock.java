/*
 * Copyright (C) 2015, 2017 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.EnumHelper;
import pl.asie.ponysocks.render.ModelArmorSock;
import pl.asie.ponysocks.render.ModelArmorSockBiped;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;

public class ItemSock extends ItemArmor {
	public static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("sock", "ponysocks:sock", 4, new int[]{1, 1, 1, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

	public ItemSock() {
		super(MATERIAL, 0, EntityEquipmentSlot.FEET);
		setCreativeTab(PonySocks.tabSocks);
		setUnlocalizedName("ponysocks.sock");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 1; i < 2; i++) {
				ItemStack stack = new ItemStack(this, 1, 0);
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("type", i);
				items.add(stack);
			}

			items.addAll(PonySocks.getSocksOrdered());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nullable
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot armorSlot, ModelBiped parentModel) {
		return ClientProxy.getArmorModel(entity, stack, armorSlot, parentModel);
	}

	public int getColor(ItemStack stack, boolean passTwo) {
		NBTTagCompound compound = stack.getTagCompound();
		String key = passTwo ? "color2" : "color1";

		if (compound != null && compound.hasKey(key)) {
			return compound.getInteger(key);
		} else {
			return 16777215;
		}
	}

	public boolean hasColor(ItemStack stack, boolean passTwo) {
		NBTTagCompound compound = stack.getTagCompound();
		String key = passTwo ? "color2" : "color1";

		return compound != null && compound.hasKey(key);
	}

	public boolean isColorable(ItemStack stack) {
		return stack.getMetadata() == 0;
	}

	@Override
	public int getMetadata(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("type") ? stack.getTagCompound().getInteger("type") : 0;
	}
}
