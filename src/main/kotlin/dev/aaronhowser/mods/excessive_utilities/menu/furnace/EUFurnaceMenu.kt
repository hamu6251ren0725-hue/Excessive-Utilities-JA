package dev.aaronhowser.mods.excessive_utilities.menu.furnace

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.EUFurnaceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

//TODO: Progress arrow
class EUFurnaceMenu(
	containerId: Int,
	playerInventory: Inventory,
	val furnaceContainer: Container,
	val furnaceContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.FURNACE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(SimpleMachineBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(SimpleMachineBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(furnaceContainer, SimpleMachineBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(furnaceContainerData, SimpleMachineBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(furnaceContainerData)
		addPlayerInventorySlots(98)
	}

	override fun addSlots() {
		val inputSlot = Slot(furnaceContainer, SimpleMachineBlockEntity.INPUT_SLOT, 62, 41)
		val outputSlot = OutputSlot(furnaceContainer, SimpleMachineBlockEntity.OUTPUT_SLOT, 116, 41)
		val upgradeSlot = FilteredSlot(furnaceContainer, SimpleMachineBlockEntity.UPGRADE_SLOT, 153, 5) {
			it.isItem(ModItemTagsProvider.SPEED_UPGRADES)
		}

		addSlot(outputSlot)
		addSlot(inputSlot)
		addSlot(upgradeSlot)
	}

	fun getCurrentEnergy(): Int = furnaceContainerData.get(SimpleMachineBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getProgress(): Int = furnaceContainerData.get(SimpleMachineBlockEntity.PROGRESS_DATA_INDEX)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return furnaceContainer.stillValid(player)
	}
}