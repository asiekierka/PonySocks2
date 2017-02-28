package pl.asie.ponysocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

public class RecipeSockColor implements IRecipe {
	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		boolean foundSock = false;
		int dyeCount = 0;

		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack s = crafting.getStackInSlot(i);
			if (s != null && s.getItem() != null) {
				if (s.getItem() instanceof ItemSock) {
					if (foundSock) {
						return false;
					} else {
						foundSock = true;
					}
				} else {
					int[] ids = OreDictionary.getOreIDs(s);
					boolean found = false;
					for (int ii : ids) {
						for (int j : PonySocks.woolIds) {
							if (ii == j) {
								found = true;
								dyeCount++;
								break;
							}
						}
						if (found) {
							break;
						}
					}
					if (!found) {
						return false;
					}
				}
			}
		}
		return dyeCount > 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		List<Integer> dyeColorsLeft = new ArrayList<Integer>();
		List<Integer> dyeColorsRight = new ArrayList<Integer>();
		ItemStack sock = null;
		int sockX = -1;
		int sockY = -1;
		int fWidth = (int) Math.floor(Math.sqrt(crafting.getSizeInventory()));
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack s = crafting.getStackInSlot(i);
			if (s != null && s.getItem() != null) {
				if (s.getItem() instanceof ItemSock) {
					if (sock != null) {
						return null;
					}
					sock = s;
					sockX = i % fWidth;
					sockY = i / fWidth;
				}
			}
		}
		if (sock == null) {
			return null;
		}
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack s = crafting.getStackInSlot(i);
			if (s != null && s.getItem() != null) {
				if (s.getItem() instanceof ItemSock) {
					continue;
				} else {
					int[] ids = OreDictionary.getOreIDs(s);
					boolean found = false;
					for (int ii : ids) {
						for (int j = 0; j < PonySocks.woolIds.length; j++) {
							if (ii == PonySocks.woolIds[j]) {
								int px = i % fWidth;
								int py = i / fWidth;
								if (px < sockX || (px == sockX && py < sockY)) {
									dyeColorsLeft.add(j);
								} else {
									dyeColorsRight.add(j);
								}
								found = true;
								break;
							}
						}
						if (found) {
							break;
						}
					}
					if (!found) {
						return null;
					}
				}
			}
		}

		int[] colorLeft = new int[3];
		int colorLeftCount = 0;
		int colorLeftMax = 0;

		int[] colorRight = new int[3];
		int colorRightCount = 0;
		int colorRightMax = 0;

		for (int i: dyeColorsLeft) {
			float[] col = EntitySheep.fleeceColorTable[i];
			int r = (int)(col[0] * 255.0F);
			int g = (int)(col[1] * 255.0F);
			int b = (int)(col[2] * 255.0F);
			colorLeftMax += Math.max(r, Math.max(g, b));
			colorLeft[0] += r;
			colorLeft[1] += g;
			colorLeft[2] += b;
			colorLeftCount++;
		}

		for (int i: dyeColorsRight) {
			float[] col = EntitySheep.fleeceColorTable[i];
			int r = (int)(col[0] * 255.0F);
			int g = (int)(col[1] * 255.0F);
			int b = (int)(col[2] * 255.0F);
			colorRightMax += Math.max(r, Math.max(g, b));
			colorRight[0] += r;
			colorRight[1] += g;
			colorRight[2] += b;
			colorRightCount++;
		}

		if (colorLeftCount == 0 && colorRightCount == 0) {
			return null;
		}

		NBTTagCompound cpd = sock.getTagCompound();
		if (cpd != null) {
			if (cpd.hasKey("color1")) {
				int c = cpd.getInteger("color1");
				int r = (c >> 16 & 255);
				int g = (c >> 8 & 255);
				int b = (c & 255);
				colorLeftMax += Math.max(r, Math.max(g, b));
				colorLeft[0] += r;
				colorLeft[1] += g;
				colorLeft[2] += b;
				colorLeftCount++;
			}

			if (cpd.hasKey("color2")) {
				int c = cpd.getInteger("color2");
				int r = (c >> 16 & 255);
				int g = (c >> 8 & 255);
				int b = (c & 255);
				colorRightMax += Math.max(r, Math.max(g, b));
				colorRight[0] += r;
				colorRight[1] += g;
				colorRight[2] += b;
				colorRightCount++;
			}
		}

		sock = sock.copy();
		if (sock.getTagCompound() == null) {
			sock.setTagCompound(new NBTTagCompound());
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
			sock.getTagCompound().setInteger("color1", rAvgL << 16 | gAvgL << 8 | bAvgL);
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
			sock.getTagCompound().setInteger("color2", rAvgR << 16 | gAvgR << 8 | bAvgR);
		}

		return sock;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
}
