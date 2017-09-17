/*
 * Copyright (C) 2015, 2017 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks.recipe;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import pl.asie.ponysocks.ItemSock;
import pl.asie.ponysocks.PonySocks;

public class RecipeSockPaint extends RecipeDyeableBase {
	public RecipeSockPaint(ResourceLocation location, String group) {
		super(location, group, false, 2, 1);

		ingredients.add(Ingredient.fromItem(Items.DYE));
		ingredients.add(Ingredient.fromItem(PonySocks.sock));
		ingredients.add(Ingredient.fromItem(Items.DYE));
	}

	@Override
	protected boolean hasColor(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		for (int id : ids) {
			if (ArrayUtils.contains(PonySocks.dyeOreIds, id)) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected int getColor(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		for (int id : ids) {
			for (int i = 0; i < 16; i++) {
				if (id == PonySocks.dyeOreIds[i]) {
					return fromFloats(EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(i)));
				}
			}
		}

		throw new RuntimeException("Shouldn't happen!");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int countSocks = 0;
		int countDyes = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack s = inv.getStackInSlot(i);
			if (!s.isEmpty()) {
				if (s.getItem() instanceof ItemSock && PonySocks.sock.isColorable(s)) {
					countSocks++;
				} else if (hasColor(s)) {
					countDyes++;
				} else {
					return false;
				}
			}
		}

		if (countSocks == 1 && countDyes > 0) {
			for (IRecipe recipe : PonySocks.specialDyeRecipes) {
				if (recipe.matches(inv, worldIn)) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
