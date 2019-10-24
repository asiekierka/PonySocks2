/*
 * Copyright (C) 2015, 2017, 2019 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks.recipe;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.asie.ponysocks.ItemUtils;
import pl.asie.ponysocks.PonySocks;

import java.util.Optional;

public class RecipeSockWashHandler {
	@SubscribeEvent
	public void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getWorld().isRemote && !event.getEntityPlayer().isSneaking()) {
			ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
			if (stack.isEmpty()) {
				return;
			}

			if (stack.getItem() == PonySocks.sock && (PonySocks.sock.hasColor(stack, false) || PonySocks.sock.hasColor(stack, true))) {
				IBlockState state = event.getWorld().getBlockState(event.getPos());
				ItemStack cleanStack = new ItemStack(PonySocks.sock, 1, 0);

				int level = state.getValue(BlockCauldron.LEVEL);

				if (level > 0 && state.getBlock() instanceof BlockCauldron && state.getPropertyKeys().contains(BlockCauldron.LEVEL)) {
					event.setCanceled(true);
					stack.shrink(1);
					if (stack.isEmpty()) {
						event.getEntityPlayer().setHeldItem(event.getHand(), cleanStack);
					} else {
						ItemUtils.giveOrSpawnItemEntity(
								event.getEntityPlayer(),
								event.getWorld(),
								new Vec3d(event.getPos()).add(0.5, 1, 0.5),
								cleanStack, 0, 0, 0, 0, true
						);
					}

					event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockCauldron.LEVEL, level - 1));
					event.getEntityPlayer().addStat(StatList.ARMOR_CLEANED);
				}
			}
		}
	}
}
