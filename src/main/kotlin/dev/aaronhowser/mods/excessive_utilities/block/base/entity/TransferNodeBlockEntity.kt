package dev.aaronhowser.mods.excessive_utilities.block.base.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.Container
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class TransferNodeBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer {

	protected val parentPos: BlockPos = this.blockPos.relative(this.blockState.getValue(TransferNodeBlock.PLACED_ON))

	protected val upgradeContainer = ImprovedSimpleContainer(this, UPGRADE_CONTAINER_SIZE)

	override fun getContainers(): List<Container> {
		return listOf(upgradeContainer)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val upgradeTag = CompoundTag()
		upgradeTag.saveItems(upgradeContainer, registries)
		tag.put(UPGRADES_NBT, upgradeTag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val upgradeTag = tag.getCompound(UPGRADES_NBT)
		upgradeTag.loadItems(upgradeContainer, registries)
	}

	companion object {
		const val UPGRADE_CONTAINER_SIZE = 5

		const val UPGRADES_NBT = "Upgrades"
	}

}