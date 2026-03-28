package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.block.EUFurnaceBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.menu.simple_machine.SimpleMachineMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import kotlin.jvm.optionals.getOrNull

class EUFurnaceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SimpleMachineBlockEntity<SmeltingRecipe>(ModBlockEntityTypes.FURNACE.get(), pos, blockState) {

	override val litProperty: Property<Boolean> = EUFurnaceBlock.LIT

	override fun isValidInput(stack: ItemStack): Boolean {
		val level = level ?: return false

		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, SingleRecipeInput(stack), level)
			.isPresent
	}

	override fun getFePerTick(recipe: RecipeHolder<SmeltingRecipe>): Int {
		return ServerConfig.CONFIG.furnaceGeneratorFePerTick.get()
	}

	override fun getRecipeDuration(recipe: RecipeHolder<SmeltingRecipe>): Int {
		return ServerConfig.CONFIG.furnaceTicksPerRecipe.get()
	}

	override fun getRecipe(level: Level): RecipeHolder<SmeltingRecipe>? {
		val input = SingleRecipeInput(container.getItem(INPUT_SLOT))
		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, input, level)
			.getOrNull()
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return SimpleMachineMenu(containerId, playerInventory, container, containerData)
	}

}