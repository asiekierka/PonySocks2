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
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import pl.asie.ponysocks.PonySocks;

public class RecipeSockCreate extends RecipeDyeableBase {
	public RecipeSockCreate(ResourceLocation location, String group) {
		super(location, group, true, 3, 2);

		ingredients.add(Ingredient.fromItem(Item.getItemFromBlock(Blocks.WOOL)));
		ingredients.add(Ingredient.EMPTY);
		ingredients.add(Ingredient.fromItem(Item.getItemFromBlock(Blocks.WOOL)));
		ingredients.add(Ingredient.fromItem(Item.getItemFromBlock(Blocks.WOOL)));
		ingredients.add(Ingredient.EMPTY);
		ingredients.add(Ingredient.fromItem(Item.getItemFromBlock(Blocks.WOOL)));
	}

	@Override
	protected boolean hasColor(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.WOOL);
	}

	@Override
	protected int getColor(ItemStack stack) {
		return fromFloats(EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(stack.getMetadata())));
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return matchesShaped(inv);
	}
}
