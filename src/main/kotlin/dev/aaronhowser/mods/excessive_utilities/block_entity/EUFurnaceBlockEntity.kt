package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block.EUFurnaceBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.furnace.EUFurnaceMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
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
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.jvm.optionals.getOrNull

class EUFurnaceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SimpleMachineBlockEntity(ModBlockEntityTypes.FURNACE.get(), pos, blockState) {

	override val litProperty: Property<Boolean> = EUFurnaceBlock.LIT

	override fun isValidInput(stack: ItemStack): Boolean {
		val level = level ?: return false

		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, SingleRecipeInput(stack), level)
			.isPresent
	}

	override fun getGpUsage(): Double {
		val level = level ?: return 0.0
		val hasRecipe = getRecipe(level) != null
		if (!hasRecipe) return 0.0

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		return SpeedUpgradeItem.getGpCost(amountUpgrades)
	}

	override fun tryCraft(level: ServerLevel) {
		val recipe = getRecipe(level)
		if (recipe == null) {
			progress = 0
			level.setBlockAndUpdate(blockPos, blockState.setValue(EUFurnaceBlock.LIT, false))
			return
		}

		val feRequired = ServerConfig.CONFIG.furnaceGeneratorFePerTick.get()
		if (energyStorage.energyStored < feRequired) {
			level.setBlockAndUpdate(blockPos, blockState.setValue(EUFurnaceBlock.LIT, false))
			return
		}

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		progress += amountUpgrades

		val maxProgress = ServerConfig.CONFIG.furnaceTicksPerRecipe.get()

		if (progress >= maxProgress) {
			val inputStack = container.getItem(INPUT_SLOT)
			val newOutput = recipe.value.assemble(SingleRecipeInput(inputStack), level.registryAccess())

			val stackInOutput = container.getItem(OUTPUT_SLOT)
			if (stackInOutput.isEmpty) {
				container.setItem(OUTPUT_SLOT, newOutput)
			} else if (stackInOutput.isItem(newOutput.item)) {
				stackInOutput.grow(newOutput.count)
			}

			inputStack.shrink(1)
			energyStorage.extractEnergy(feRequired, false)

			progress = 0
		}
	}

	private fun getRecipe(level: Level): RecipeHolder<SmeltingRecipe>? {
		val input = SingleRecipeInput(container.getItem(INPUT_SLOT))
		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, input, level)
			.getOrNull()
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return EUFurnaceMenu(containerId, playerInventory, container, containerData)
	}

	companion object {

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: EUFurnaceBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}

		fun getEnergyCapability(transmitter: EUFurnaceBlockEntity, direction: Direction?): IEnergyStorage {
			return transmitter.energyStorage
		}
	}

}