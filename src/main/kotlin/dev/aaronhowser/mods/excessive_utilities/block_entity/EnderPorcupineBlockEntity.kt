package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

class EnderPorcupineBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENDER_PORCUPINE.get(), pos, blockState) {

	var minimumOffset: BlockPos = BlockPos.ZERO
		set(value) {
			field = value
			setChanged()
			updateOffsets()
		}

	var maximumOffset: BlockPos = BlockPos.ZERO
		set(value) {
			field = value
			setChanged()
			updateOffsets()
		}

	var allOffsets: List<BlockPos> = emptyList()
		private set

	private fun updateOffsets() {
		allOffsets = BlockPos.betweenClosed(minimumOffset, maximumOffset).toList()
	}

	// Every second, move to the next offset in the list
	fun getCurrentOffset(): Vec3i {
		val level = level ?: return Vec3i.ZERO
		val second = level.gameTime % 20

		val volume = allOffsets.size
		if (volume == 0) return Vec3i.ZERO

		val index = (second % volume).toInt()

		return allOffsets[index]
	}

	override fun getGpUsage(): Double {
		val linkPos = linkedPosition ?: return 0.0
		return linkPos.distManhattan(blockPos).toDouble()
	}

	fun getItemHandler(direction: Direction?): IItemHandler? {
		val level = level ?: return null
		val linkPos = linkedPosition ?: return null

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level.getCapability(Capabilities.ItemHandler.BLOCK, linkPos, direction)
	}

	fun getFluidHandler(direction: Direction?): IFluidHandler? {
		val level = level ?: return null
		val linkPos = linkedPosition ?: return null

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level.getCapability(Capabilities.FluidHandler.BLOCK, linkPos, direction)
	}

	fun getEnergyHandler(direction: Direction?): IEnergyStorage? {
		val level = level ?: return null
		val linkPos = linkedPosition ?: return null

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level.getCapability(Capabilities.EnergyStorage.BLOCK, linkPos, direction)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val linkPos = linkedPosition
		if (linkPos != null) {
			tag.putLong(LINKED_POSITION_NBT, linkPos.asLong())
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(LINKED_POSITION_NBT)) {
			val linkPosLong = tag.getLong(LINKED_POSITION_NBT)
			linkedPosition = BlockPos.of(linkPosLong)
		}
	}

	companion object {
		const val LINKED_POSITION_NBT = "LinkedPosition"
	}

}