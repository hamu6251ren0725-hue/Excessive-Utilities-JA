package dev.aaronhowser.mods.excessive_utilities.menu.resonator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block.entity.ResonatorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ResonatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val resonatorContainer: Container,
	val resonatorContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.RESONATOR.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(3),
				SimpleContainerData(1)
			)

	init {
		checkContainerSize(resonatorContainer, ResonatorBlockEntity.CONTAINER_SIZE)

		addSlots()
		addPlayerInventorySlots(84)
	}

	override fun addSlots() {
		val inputSlot = Slot(resonatorContainer, ResonatorBlockEntity.INPUT_SLOT, 60, 34)
		val outputSlot = Slot(resonatorContainer, ResonatorBlockEntity.OUTPUT_SLOT, 120, 34)
		val upgradeSlot = Slot(resonatorContainer, ResonatorBlockEntity.UPGRADE_SLOT, 150, 34)

		this.addSlot(inputSlot)
		this.addSlot(outputSlot)
		this.addSlot(upgradeSlot)
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return resonatorContainer.stillValid(player)
	}
}