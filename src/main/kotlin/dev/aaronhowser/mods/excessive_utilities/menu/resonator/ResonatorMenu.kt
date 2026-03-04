package dev.aaronhowser.mods.excessive_utilities.menu.resonator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class ResonatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val resonatorContainer: Container,
	val resonatorContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.SINGLE_SLOT.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(3),
				SimpleContainerData(1)
			)


	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return resonatorContainer.stillValid(player)
	}
}