/*
 * Copyright (C) 2015, 2017 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks.render;

import com.minelittlepony.model.pony.armor.ModelPonyArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import pl.asie.ponysocks.PonySocks;

public class ModelArmorSock extends ModelPonyArmor implements IStackedArmor {
	private static final ResourceLocation[] LAYERS = new ResourceLocation[] {
			new ResourceLocation("ponysocks:textures/models/armor/sock_layer_0_pony.png"),
			new ResourceLocation("ponysocks:textures/models/armor/sock_layer_1_pony.png"),
			new ResourceLocation("ponysocks:textures/models/armor/sock_rainbow_pony.png")
	};

	private ItemStack stack;

	public ModelArmorSock() {
		super();
		init(0.0F, 0.025F);
	}

	public void setArmorStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (stack.getMetadata() > 0) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(LAYERS[Math.min(LAYERS.length - 1, 1 + stack.getMetadata())]);
			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			return;
		}

		for (int i = 0; i < 2; i++) {
			int color = PonySocks.sock.getColor(stack, i > 0);
			GlStateManager.color(
					(float) ((color >> 16) & 0xFF) / 255.0f,
					(float) ((color >> 8) & 0xFF) / 255.0f,
					(float) (color & 0xFF) / 255.0f,
					1.0f
			);
			Minecraft.getMinecraft().getTextureManager().bindTexture(LAYERS[i]);
			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
}
