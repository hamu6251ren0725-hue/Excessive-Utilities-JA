package dev.aaronhowser.mods.excessive_utilities.menu.furnace

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block.entity.EUFurnaceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class EUFurnaceMenu(
	containerId: Int,
	playerInventory: Inventory,
	val furnaceContainer: Container,
	val furnaceContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.QED.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(EUFurnaceBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(EUFurnaceBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(furnaceContainer, EUFurnaceBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(furnaceContainerData, EUFurnaceBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addPlayerInventorySlots(84)
	}

	fun getCurrentEnergy(): Int = furnaceContainerData.get(EUFurnaceBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getProgress(): Int = furnaceContainerData.get(EUFurnaceBlockEntity.PROGRESS_DATA_INDEX)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return furnaceContainer.stillValid(player)
	}
}