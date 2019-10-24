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

import mezz.jei.api.*;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.asie.ponysocks.PonySocks;
import pl.asie.ponysocks.recipe.RecipeBase;

import java.util.Collections;

@JEIPlugin
public class PonySocksJEIPlugin implements IModPlugin {
	public static IStackHelper STACKS;

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.useNbtForSubtypes(PonySocks.sock);
	}

	@Override
	public void register(IModRegistry registry) {
		STACKS = registry.getJeiHelpers().getStackHelper();

		for (IRecipe recipe : ForgeRegistries.RECIPES) {
			if (recipe instanceof RecipeBase) {
				registry.addRecipes(Collections.singletonList(recipe), VanillaRecipeCategoryUid.CRAFTING);
			}
		}

		registry.handleRecipes(RecipeBase.class, JEIRecipePony::create, VanillaRecipeCategoryUid.CRAFTING);
	}
}
