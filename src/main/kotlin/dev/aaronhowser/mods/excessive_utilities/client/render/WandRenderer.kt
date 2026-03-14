package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.RenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.BuildersWandItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.neoforged.neoforge.client.event.RenderHighlightEvent
import java.util.function.Predicate

object WandRenderer {

	fun renderTargetBlocks(event: RenderHighlightEvent.Block) {
		val player = AaronClientUtil.localPlayer ?: return

		val builderWand = getHeldWand(player, isBuilder = true)
		val destructionWand = getHeldWand(player, isBuilder = false)

		if (builderWand == null && destructionWand == null) return

		val level = player.level()
		val hit = event.target
		val pos = hit.blockPos
		val face = hit.direction

		val poseStack = event.poseStack

		if (builderWand != null) {
			renderBuilderWand(level, pos, face, builderWand, player, poseStack)
		}

	}

	private fun renderBuilderWand(
		level: Level,
		clickedPos: BlockPos,
		face: Direction,
		wandStack: ItemStack,
		player: Player,
		poseStack: PoseStack
	) {
		val clickedState = level.getBlockState(clickedPos)
		if (clickedState.isAir) return
		val blockItem = clickedState.block.asItem()
		if (blockItem == Items.AIR) return

		val amountInInventory = player.inventory.countItem(blockItem)

		val maxAmount = wandStack.get(ModDataComponents.AMOUNT_BLOCKS) ?: return
		val amountCanPlace = minOf(maxAmount, amountInInventory)

		val positions = BuildersWandItem.getPositions(
			level,
			clickedPos,
			clickedState,
			face,
			amountCanPlace
		)

		for (pos in positions) {
			val offset = clickedPos.center.vectorTo(pos.center)

			RenderUtil.renderCubeThroughWalls(poseStack, offset, 1f, 0xFFFFFFFF.toInt())
		}
	}

	private fun renderDestructionWand(destructionWand: ItemStack, event: RenderHighlightEvent.Block) {
		val amount = destructionWand.get(ModDataComponents.AMOUNT_BLOCKS) ?: return

	}

	private fun getHeldWand(player: Player, isBuilder: Boolean): ItemStack? {
		val predicate = if (isBuilder) {
			Predicate<ItemStack> { it.isItem(ModItems.BUILDERS_WAND) || it.isItem(ModItems.CREATIVE_BUILDERS_WAND) }
		} else {
			Predicate<ItemStack> { it.isItem(ModItems.DESTRUCTION_WAND) || it.isItem(ModItems.CREATIVE_DESTRUCTION_WAND) }
		}

		val mainHand = player.mainHandItem
		if (predicate.test(mainHand)) {
			return mainHand
		}

		val offHand = player.offhandItem
		if (predicate.test(offHand)) {
			return offHand
		}

		return null
	}

}