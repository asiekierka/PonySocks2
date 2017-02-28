package pl.asie.ponysocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSock extends ItemArmor {
	public static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("Sock", 4, new int[]{1, 1, 1, 1}, 5);
	protected static boolean IS_SECOND_PHASE;

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public ItemSock() {
		super(MATERIAL, 0, 3);
		setCreativeTab(PonySocks.tabSocks);
		setUnlocalizedName("ponysocks.sock");
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if ("overlay".equals(type)) {
			return "ponysocks:textures/armor/sock_layer_fake.png";
		} else {
			if (IS_SECOND_PHASE) {
				return "ponysocks:textures/armor/sock_layer_1.png";
			} else {
				return "ponysocks:textures/armor/sock_layer_0.png";
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[3];
		icons[0] = register.registerIcon("ponysocks:icon_sock_layer_0");
		icons[1] = register.registerIcon("ponysocks:icon_sock_layer_1");
		icons[2] = register.registerIcon("ponysocks:icon_sock_overlay");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getRenderPasses(int metadata) {
		return 3;
	}

	@Override
	public int getColor(ItemStack stack) {
		return getColor(stack, IS_SECOND_PHASE);
	}

	private int getColor(ItemStack stack, boolean passTwo) {
		NBTTagCompound compound = stack.getTagCompound();
		String key = passTwo ? "color2" : "color1";

		if (compound != null && compound.hasKey(key)) {
			return compound.getInteger(key);
		} else {
			return 16777215;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return pass == 2 ? 16777215 : getColor(stack, pass == 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		return icons[pass % 3];
	}
}
