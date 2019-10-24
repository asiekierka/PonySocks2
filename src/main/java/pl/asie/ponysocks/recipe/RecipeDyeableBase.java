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

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import pl.asie.ponysocks.ItemSock;
import pl.asie.ponysocks.PonySocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RecipeDyeableBase extends RecipeBase {
	public static class MixColorResult {
		public final boolean found;
		public final int valueLeft, valueRight;

		public MixColorResult(boolean found, int valueLeft, int valueRight) {
			this.found = found;
			this.valueLeft = valueLeft;
			this.valueRight = valueRight;
		}
	}

	public RecipeDyeableBase(ResourceLocation location, String group, boolean shaped, int width, int height) {
		super(location, group, shaped, width, height);
	}

	protected abstract boolean hasColor(ItemStack stack);
	protected abstract int getColor(ItemStack stack);

	public static int fromFloats(float[] val) {
		return (Math.round(val[0] * 255) << 16) | (Math.round(val[1] * 255) << 8) | Math.round(val[2] * 255);
	}

	protected MixColorResult mixColor(InventoryCrafting crafting) {
		List<Integer> dyeColorsLeft = new ArrayList<Integer>();
		List<Integer> dyeColorsRight = new ArrayList<Integer>();
		boolean found = false;
		int sockX = crafting.getWidth() / 2;
		int sockY = crafting.getHeight() / 2;
		int fWidth = (int) Math.floor(Math.sqrt(crafting.getSizeInventory()));

		int clOut = 0;
		int crOut = 0;

		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack s = crafting.getStackInSlot(i);
			if (!s.isEmpty() && s.getItem() instanceof ItemSock) {
				if (!PonySocks.sock.isColorable(s)) {
					return new MixColorResult(false, 0, 0);
				}

				sockX = i % fWidth;
				sockY = i / fWidth;
				// if (PonySocks.sock.hasColor(s, false)) dyeColorsLeft.add(PonySocks.sock.getColor(s, false));
				// if (PonySocks.sock.hasColor(s, true)) dyeColorsRight.add(PonySocks.sock.getColor(s, true));
				clOut = PonySocks.sock.getColor(s, false);
				crOut = PonySocks.sock.getColor(s, true);
			}
		}
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack s = crafting.getStackInSlot(i);
			if (!s.isEmpty() && hasColor(s)) {
				int color = getColor(s);
				int px = i % fWidth;
				int py = i / fWidth;
				if (px < sockX || (px == sockX && py < sockY)) {
					dyeColorsLeft.add(color);
				} else {
					dyeColorsRight.add(color);
				}
				found = true;
			}
		}

		if (!found) {
			return new MixColorResult(false, 0, 0);
		}

		int[] colorLeft = new int[3];
		int colorLeftCount = 0;
		int colorLeftMax = 0;

		int[] colorRight = new int[3];
		int colorRightCount = 0;
		int colorRightMax = 0;

		for (int i: dyeColorsLeft) {
			int r = (i >> 16) & 0xFF;
			int g = (i >> 8) & 0xFF;
			int b = i & 0xFF;
			colorLeftMax += Math.max(r, Math.max(g, b));
			colorLeft[0] += r;
			colorLeft[1] += g;
			colorLeft[2] += b;
			colorLeftCount++;
		}

		for (int i: dyeColorsRight) {
			int r = (i >> 16) & 0xFF;
			int g = (i >> 8) & 0xFF;
			int b = i & 0xFF;
			colorRightMax += Math.max(r, Math.max(g, b));
			colorRight[0] += r;
			colorRight[1] += g;
			colorRight[2] += b;
			colorRightCount++;
		}

		if (colorLeftCount > 0) {
			int rAvgL = colorLeft[0] / colorLeftCount;
			int gAvgL = colorLeft[1] / colorLeftCount;
			int bAvgL = colorLeft[2] / colorLeftCount;
			float var16 = (float) colorLeftMax / (float) colorLeftCount;
			float var17 = (float) Math.max(rAvgL, Math.max(gAvgL, bAvgL));
			rAvgL = (int) ((float) rAvgL * var16 / var17);
			gAvgL = (int) ((float) gAvgL * var16 / var17);
			bAvgL = (int) ((float) bAvgL * var16 / var17);
			clOut = rAvgL << 16 | gAvgL << 8 | bAvgL;
		}

		if (colorRightCount > 0) {
			int rAvgR = colorRight[0] / colorRightCount;
			int gAvgR = colorRight[1] / colorRightCount;
			int bAvgR = colorRight[2] / colorRightCount;
			float var16 = (float) colorRightMax / (float) colorRightCount;
			float var17 = (float) Math.max(rAvgR, Math.max(gAvgR, bAvgR));
			rAvgR = (int) ((float) rAvgR * var16 / var17);
			gAvgR = (int) ((float) gAvgR * var16 / var17);
			bAvgR = (int) ((float) bAvgR * var16 / var17);
			crOut = rAvgR << 16 | gAvgR << 8 | bAvgR;
		}

		return new MixColorResult(true, clOut, crOut);
	}


	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		MixColorResult result = mixColor(inv);
		if (!result.found) return ItemStack.EMPTY;

		ItemStack sock = new ItemStack(PonySocks.sock, 1, 0);
		sock.setTagCompound(new NBTTagCompound());
		sock.getTagCompound().setInteger("color1", result.valueLeft);
		sock.getTagCompound().setInteger("color2", result.valueRight);
		return sock;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(PonySocks.sock);
	}

	@Override
	public List<ItemStack> getRecipeOutputs() {
		return PonySocks.getSocksShuffled();
	}
}