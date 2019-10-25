/*
 * Copyright (C) 2015, 2017, 2019 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks.recipe.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import pl.asie.ponysocks.recipe.RecipeBase;

import javax.annotation.Nullable;
import java.util.Collections;

public abstract class JEIRecipePony implements IRecipeWrapper {
	protected final RecipeBase recipe;

	public static class Shapeless extends JEIRecipePony implements ICraftingRecipeWrapper {
		public Shapeless(RecipeBase recipe) {
			super(recipe);
		}

		@Nullable
		@Override
		public ResourceLocation getRegistryName() {
			return recipe.getRegistryName();
		}
	}

	public static class Shaped extends JEIRecipePony implements IShapedCraftingRecipeWrapper {
		public Shaped(RecipeBase recipe) {
			super(recipe);
		}

		@Override
		public int getWidth() {
			return recipe.getWidth();
		}

		@Override
		public int getHeight() {
			return recipe.getHeight();
		}
	}

	public static JEIRecipePony create(RecipeBase recipe) {
		return recipe.isShaped() ? new Shaped(recipe) : new Shapeless(recipe);
	}

	public JEIRecipePony(RecipeBase recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, PonySocksJEIPlugin.STACKS.expandRecipeItemStackInputs(recipe.getIngredients()));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(recipe.getRecipeOutputs()));
	}
}
