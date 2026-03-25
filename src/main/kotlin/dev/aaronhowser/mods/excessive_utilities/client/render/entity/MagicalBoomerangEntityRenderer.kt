package dev.aaronhowser.mods.excessive_utilities.client.render.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.entity.MagicalBoomerangEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class MagicalBoomerangEntityRenderer(
	context: EntityRendererProvider.Context
) : EntityRenderer<MagicalBoomerangEntity>(context) {

	private val itemRenderer: ItemRenderer = context.itemRenderer
	private val boomerangStack: ItemStack = ModItems.MAGICAL_BOOMERANG.getDefaultInstance()

	override fun render(
		entity: MagicalBoomerangEntity,
		entityYaw: Float,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int
	) {
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)

		val age = entity.tickCount + partialTick
		val rotationDegrees = Mth.wrapDegrees(age * 20f)

		poseStack.withPose {
			poseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees))
			poseStack.mulPose(Axis.XP.rotationDegrees(90f))

			itemRenderer.renderStatic(
				boomerangStack,
				ItemDisplayContext.GROUND,
				packedLight,
				OverlayTexture.NO_OVERLAY,
				poseStack,
				bufferSource,
				entity.level(),
				entity.id
			)
		}
	}

	override fun getTextureLocation(entity: MagicalBoomerangEntity): ResourceLocation = TextureAtlas.LOCATION_BLOCKS

}