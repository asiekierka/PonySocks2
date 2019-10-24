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

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;
import pl.asie.ponysocks.ItemSock;
import pl.asie.ponysocks.PonySocks;

public class RecipeSockRainbow extends RecipeBase {
	public RecipeSockRainbow(ResourceLocation location, String group) {
		super(location, group, true, 3, 3);

		ingredients.add(new OreIngredient("dyeRed"));
		ingredients.add(Ingredient.EMPTY);
		ingredients.add(new OreIngredient("dyeLime"));

		ingredients.add(new OreIngredient("dyeOrange"));
		ingredients.add(Ingredient.fromStacks(new ItemStack(PonySocks.sock, 1, 0)));
		ingredients.add(new OreIngredient("dyeLightBlue"));

		ingredients.add(new OreIngredient("dyeYellow"));
		ingredients.add(Ingredient.EMPTY);
		ingredients.add(new OreIngredient("dyePurple"));
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return matchesShaped(inv);
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack stack = new ItemStack(PonySocks.sock, 1, 0);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("type", 1);
		return stack;
	}
}
