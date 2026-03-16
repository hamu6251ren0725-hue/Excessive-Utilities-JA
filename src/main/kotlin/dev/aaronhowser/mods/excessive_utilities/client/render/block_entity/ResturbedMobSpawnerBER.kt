package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block_entity.ResturbedMobSpawnerBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.SpawnerRenderer

class ResturbedMobSpawnerBER(
	context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<ResturbedMobSpawnerBlockEntity> {

	private val entityRenderer = context.entityRenderer

	override fun render(
		blockEntity: ResturbedMobSpawnerBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val level = blockEntity.level ?: return
		val spawner = blockEntity.spawner
		val entity = spawner.getOrCreateDisplayEntity(level, blockEntity.blockPos)

		if (entity != null) {
			SpawnerRenderer.renderEntityInSpawner(
				partialTick,
				poseStack,
				bufferSource,
				packedLight,
				entity,
				entityRenderer,
				spawner.getoSpin(),
				spawner.getoSpin()
			)
		}

	}

}