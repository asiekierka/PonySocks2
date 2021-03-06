/*
 * Copyright (C) 2015, 2017, 2019 Adrian Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.asie.ponysocks.render;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import pl.asie.ponysocks.ItemSock;
import pl.asie.ponysocks.PonySocks;

public class ItemSockColor implements IItemColor {
	@Override
	public int colorMultiplier(ItemStack stack, int layer) {
		if (stack.getItem() instanceof ItemSock) {
			ItemSock sock = (ItemSock) stack.getItem();
			if (sock.isColorable(stack)) {
				return layer >= 2 ? -1 : (0xFF000000 | sock.getColor(stack, layer == 1));
			}
		}
		return -1;
	}
}
