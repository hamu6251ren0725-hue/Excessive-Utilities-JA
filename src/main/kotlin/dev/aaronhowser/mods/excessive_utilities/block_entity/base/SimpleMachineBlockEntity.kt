package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.RangedWrapper

abstract class SimpleMachineBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer, MenuProvider {

	protected val energyStorage = EnergyStorage(MAX_ENERGY)

	protected open val container: ImprovedSimpleContainer =
		object : ImprovedSimpleContainer(this, 3) {
			override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
				val level = level ?: return false

				return when (slot) {
					INPUT_SLOT -> isValidInput(stack)
					UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
					else -> false
				}
			}
		}

	protected val containerData: ContainerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					CURRENT_ENERGY_DATA_INDEX -> energyStorage.energyStored
					PROGRESS_DATA_INDEX -> progress
					else -> -1
				}
			}

			override fun set(index: Int, value: Int) {
				// Unused
			}
		}

	override fun getContainers(): List<Container> = listOf(container)
	fun getItemHandler(direction: Direction?): IItemHandlerModifiable =
		object : RangedWrapper(InvWrapper(container), INPUT_SLOT, OUTPUT_SLOT + 1) {
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				return if (slot == OUTPUT_SLOT) {
					super.extractItem(slot, amount, simulate)
				} else {
					ItemStack.EMPTY
				}
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				return if (slot == OUTPUT_SLOT) {
					stack
				} else {
					super.insertItem(slot, stack, simulate)
				}
			}
		}

	abstract val litProperty: Property<Boolean>
	abstract fun isValidInput(stack: ItemStack): Boolean
	abstract fun tryCraft(level: ServerLevel)

	protected var progress = 0

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		val amountSpeedUpgrades = container.getItem(UPGRADE_SLOT).count

		for (i in 0..amountSpeedUpgrades) {
			tryCraft(level)
			updateBlockState(level)
		}
	}

	protected fun updateBlockState(level: ServerLevel) {
		val wasLit = blockState.getValue(litProperty)
		val shouldBeLit = progress > 0

		if (wasLit != shouldBeLit) {
			val newState = blockState.setValue(litProperty, shouldBeLit)
			level.setBlockAndUpdate(blockPos, newState)
		}
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(PROGRESS_NBT, progress)
		tag.saveItems(container, registries)
		tag.saveEnergy(ENERGY_NBT, energyStorage, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		progress = tag.getInt(PROGRESS_NBT)
		tag.loadItems(container, registries)
		tag.loadEnergy(ENERGY_NBT, energyStorage, registries)
	}

	companion object {
		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
		const val UPGRADE_SLOT = 2

		const val CONTAINER_DATA_SIZE = 2
		const val CURRENT_ENERGY_DATA_INDEX = 0
		const val PROGRESS_DATA_INDEX = 1

		const val PROGRESS_NBT = "Progress"
		const val ENERGY_NBT = "Energy"

		const val MAX_ENERGY = 128_000
	}

}