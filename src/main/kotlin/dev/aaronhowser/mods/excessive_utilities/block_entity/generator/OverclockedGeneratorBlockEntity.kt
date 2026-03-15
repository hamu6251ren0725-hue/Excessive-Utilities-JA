package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class OverclockedGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GeneratorBlockEntity(ModBlockEntityTypes.OVERCLOCKED_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.OVERCLOCKED

	override fun isValidInput(itemStack: ItemStack): Boolean {
		return itemStack.getBurnTime(RecipeType.SMELTING) > 0
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0 || container == null) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val burnTime = inputStack.getBurnTime(RecipeType.SMELTING)
		if (burnTime <= 0) return false

		fePerTick =

	}
}