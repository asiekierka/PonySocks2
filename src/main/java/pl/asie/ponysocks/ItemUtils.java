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

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ItemUtils {
	private ItemUtils() {

	}

	public static boolean canMerge(ItemStack source, ItemStack target) {
		return equals(source, target, false, true, true);
	}

	public static boolean equalsMeta(ItemStack source, ItemStack target) {
		if (source.isEmpty()) {
			return target.isEmpty();
		}
		return equals(source, target, false, !source.getItem().isDamageable(), false);
	}

	public static boolean equals(ItemStack source, ItemStack target, boolean matchStackSize, boolean matchDamage, boolean matchNBT) {
		return equals(source, target, matchStackSize, matchDamage, matchNBT, matchNBT);
	}

	public static boolean equals(ItemStack source, ItemStack target, boolean matchStackSize, boolean matchDamage, boolean matchNBT, boolean matchCaps) {
		if (source == target) {
			return true;
		} else if (source.isEmpty()) {
			return target.isEmpty();
		} else {
			if (source.getItem() != target.getItem()) {
				return false;
			}

			if (matchStackSize && source.getCount() != target.getCount()) {
				return false;
			}

			if (matchDamage && source.getItemDamage() != target.getItemDamage()) {
				return false;
			}

			if (matchNBT) {
				if (source.hasTagCompound() != target.hasTagCompound()) {
					return false;
				} else if (source.hasTagCompound() && !source.getTagCompound().equals(target.getTagCompound())) {
					return false;
				}
			}

			if (matchCaps) {
				if (!source.areCapsCompatible(target)) {
					return false;
				}
			}

			return true;
		}
	}

	public static EntityItem giveOrSpawnItemEntity(EntityPlayer player, World world, Vec3d loc, ItemStack stack, float mXm, float mYm, float mZm, float randomness, boolean emitEquipSound) {
		EntityItem entityItem = createItemEntity(world, loc, stack, mXm, mYm, mZm, randomness);
		if (player.inventory != null) {
			EntityItemPickupEvent event = new EntityItemPickupEvent(player, entityItem);
			if (!MinecraftForge.EVENT_BUS.post(event) && event.getResult() != Event.Result.DENY) {
				if (emitEquipSound ? player.addItemStackToInventory(stack) : player.inventory.addItemStackToInventory(stack)) {
					return null;
				}
			}
		}

		world.spawnEntity(entityItem);
		return entityItem;
	}

	private static EntityItem createItemEntity(World world, Vec3d loc, ItemStack stack, float mXm, float mYm, float mZm, float randomness) {
		EntityItem entityItem = new EntityItem(world, loc.x, loc.y, loc.z, stack);
		entityItem.setDefaultPickupDelay();
		if (randomness <= 0.0f) {
			entityItem.motionX = mXm;
			entityItem.motionY = mYm;
			entityItem.motionZ = mZm;
		} else {
			entityItem.motionX = ((1.0f - randomness) + (((world.rand.nextDouble() - 0.5) * 2.0f) * randomness)) * mXm;
			entityItem.motionY = ((1.0f - randomness) + (((world.rand.nextDouble() - 0.5) * 2.0f) * randomness)) * mYm;
			entityItem.motionZ = ((1.0f - randomness) + (((world.rand.nextDouble() - 0.5) * 2.0f) * randomness)) * mZm;
		}
		return entityItem;
	}

	public static EntityItem spawnItemEntity(World world, Vec3d loc, ItemStack stack, float mXm, float mYm, float mZm, float randomness) {
		EntityItem entityItem = createItemEntity(world, loc, stack, mXm, mYm, mZm, randomness);
		world.spawnEntity(entityItem);
		return entityItem;
	}
}
