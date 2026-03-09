package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class SunCrystalBEWLR : BlockEntityWithoutLevelRenderer(
	Minecraft.getInstance().blockEntityRenderDispatcher,
	Minecraft.getInstance().entityModels
) {

	val itemRenderer: ItemRenderer = Minecraft.getInstance().itemRenderer

	override fun renderByItem(
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val bakedModel = itemRenderer.getModel(stack, null, null, 0)

		val outlineBuffer = buffer.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS))
		itemRenderer.renderModelLists(
			bakedModel,
			stack,
			packedLight,
			packedOverlay,
			poseStack,
			outlineBuffer
		)

		val glowBuffer = buffer.

	}

}