package pl.asie.ponysocks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by asie on 10/30/15.
 */
public class PonyRenderListener {
	private float partialTicks;
	private Method shouldRenderPassMethod, func_82408_cMethod;
	private Field f;

	public PonyRenderListener() {
		try {
			func_82408_cMethod = RendererLivingEntity.class.getDeclaredMethod("func_82408_c", EntityLivingBase.class, int.class, float.class);
			func_82408_cMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			f = RendererLivingEntity.class.getDeclaredField("renderPassModel");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				f = RendererLivingEntity.class.getDeclaredField("field_77046_h");
				f.setAccessible(true);
			} catch (NoSuchFieldException ee) {
				ee.printStackTrace();
			}
		}

		try {
			shouldRenderPassMethod = RendererLivingEntity.class.getDeclaredMethod("shouldRenderPass", EntityLivingBase.class, int.class, float.class);
			shouldRenderPassMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			try {
				shouldRenderPassMethod = RendererLivingEntity.class.getDeclaredMethod("func_77032_a", EntityLivingBase.class, int.class, float.class);
				shouldRenderPassMethod.setAccessible(true);
			} catch (NoSuchMethodException ee) {
				ee.printStackTrace();
			}
		}
	}
	protected float getDeathMaxRotation(EntityLivingBase p_77037_1_)
	{
		return 90.0F;
	}

	protected void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_)
	{
		GL11.glTranslatef((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
	}

	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
	{
		GL11.glRotatef(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

		if (p_77043_1_.deathTime > 0)
		{
			float f3 = ((float)p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt_float(f3);

			if (f3 > 1.0F)
			{
				f3 = 1.0F;
			}

			GL11.glRotatef(f3 * this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
		}
		else
		{
			String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getCommandSenderName());

			if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || !((EntityPlayer)p_77043_1_).getHideCape()))
			{
				GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}
	private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_)
	{
		float f3;

		for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F)
		{
			;
		}

		while (f3 >= 180.0F)
		{
			f3 -= 360.0F;
		}

		return p_77034_1_ + p_77034_3_ * f3;
	}

	protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_)
	{
		return (float)p_77044_1_.ticksExisted + p_77044_2_;
	}

	@SubscribeEvent
	public void onRenderStart(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;
		}
	}

	private float playerPT;
	private boolean playerPTPresent;

	@SubscribeEvent
	public void onRenderStart(RenderPlayerEvent.Pre event) {
		playerPT = event.partialRenderTick;
		playerPTPresent = true;
	}

	@SubscribeEvent
	public void onEntityRenderPost(RenderLivingEvent.Post event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			ItemStack stack = player.getCurrentArmor(0);
			if (stack != null && stack.getItem() instanceof ItemSock) {
				ItemSock.IS_SECOND_PHASE = true;

				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				try {
					float p_76986_9_ = playerPTPresent ? playerPT : partialTicks;
					playerPTPresent = false;

					float f2 = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, p_76986_9_);
					float f3 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, p_76986_9_);
					float f4;

					if (player.isRiding() && player.ridingEntity instanceof EntityLivingBase)
					{
						EntityLivingBase entitylivingbase1 = (EntityLivingBase)player.ridingEntity;
						f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset, p_76986_9_);
						f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

						if (f4 < -85.0F)
						{
							f4 = -85.0F;
						}

						if (f4 >= 85.0F)
						{
							f4 = 85.0F;
						}

						f2 = f3 - f4;

						if (f4 * f4 > 2500.0F)
						{
							f2 += f4 * 0.2F;
						}
					}

					this.renderLivingAt(player, event.x, event.y, event.z);
					float f13 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_76986_9_;
					f4 = this.handleRotationFloat(player, p_76986_9_);
					this.rotateCorpse(player, f4, f2, p_76986_9_);
					float f5 = 0.0625F;
					GL11.glEnable(GL12.GL_RESCALE_NORMAL);
					GL11.glScalef(-1.0F, -1.0F, 1.0F);
					GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
					float f6 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * p_76986_9_;
					float f7 = player.limbSwing - player.limbSwingAmount * (1.0F - p_76986_9_);

					if (player.isChild())
					{
						f7 *= 3.0F;
					}

					if (f6 > 1.0F)
					{
						f6 = 1.0F;
					}

					ModelBase renderPassModelT = (ModelBase) f.get(event.renderer);
					if (renderPassModelT.getClass() != ModelBiped.class) {
						GL11.glTranslatef(0, 0.25F + 0.125F, 0);
						GL11.glScalef(0.75F, 0.75F, 0.75F);
					} else {
						GL11.glScalef(1 / 1.0625F, 1 / 1.0625F, 1 / 1.0625F);
						GL11.glTranslatef(0, 0.09675F, 0);
					}

					shouldRenderPassMethod.invoke(event.renderer, player, 3, p_76986_9_);
					ModelBase renderPassModel = (ModelBase) f.get(event.renderer);

					renderPassModel.setLivingAnimations(player, f7, f6, p_76986_9_);
					renderPassModel.render(player, f7, f6, f4, f3 - f2, f13, f5);
					func_82408_cMethod.invoke(event.renderer, player, 3, p_76986_9_);
					renderPassModel.render(player, f7, f6, f4, f3 - f2, f13, f5);
				} catch (Exception e) {
					e.printStackTrace();
				}

				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glPopMatrix();

				ItemSock.IS_SECOND_PHASE = false;
			}
		}
	}
}
