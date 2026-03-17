package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.ResturbedMobSpawnerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.Spawner
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class ResturbedMobSpawnerBlock : Block(Properties.ofFullCopy(Blocks.SPAWNER)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return ResturbedMobSpawnerBlockEntity(pos, state)
	}

	override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			ModBlockEntityTypes.RESTURBED_MOB_SPAWNER.get(),
			ResturbedMobSpawnerBlockEntity::tick
		)
	}

	override fun appendHoverText(stack: ItemStack, context: Item.TooltipContext, tooltipComponents: List<Component?>, tooltipFlag: TooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
		Spawner.appendHoverText(stack, tooltipComponents, "SpawnData")
	}

}