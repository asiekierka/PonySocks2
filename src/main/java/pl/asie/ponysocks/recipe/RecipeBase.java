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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collections;
import java.util.List;

public abstract class RecipeBase extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	protected final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final String group;
	private final boolean shaped;
	private final int width, height;

	public RecipeBase(ResourceLocation location, String group, boolean shaped, int width, int height) {
		this.group = group;
		setRegistryName(location);
		this.shaped = shaped;
		this.width = width;
		this.height = height;
	}

	protected boolean matchesShaped(InventoryCrafting inv) {
		for (int iy = 0; iy <= inv.getHeight() - 2; iy++) {
			for (int ix = 0; ix <= inv.getWidth() - 3; ix++) {
				boolean foundMistake = false;
				innerLoop: for (int y = 0; y < 2; y++) {
					for (int x = 0; x < 3; x++) {
						Ingredient ingredient = ingredients.get(y*3 + x);
						if (!ingredient.apply(inv.getStackInRowAndColumn(ix+x, iy+y))) {
							foundMistake = true;
							break innerLoop;
						}
					}
				}

				if (!foundMistake) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isShaped() {
		return shaped;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public boolean canFit(int width, int height) {
		return shaped ? (width * height) >= (this.width * this.height) : width >= this.width && height >= this.height;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return getRecipeOutput();
	}

	public List<ItemStack> getRecipeOutputs() {
		return Collections.singletonList(getRecipeOutput());
	}
}
