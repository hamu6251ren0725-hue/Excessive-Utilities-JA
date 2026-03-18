package dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine_menu

import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack

class EnderPorcupineMenu(
	containerId: Int,
	val containerData: ContainerData
) : AbstractContainerMenu(ModMenuTypes.RESONATOR.get(), containerId) {

	override fun quickMoveStack(player: Player, index: Int): ItemStack = ItemStack.EMPTY
	override fun stillValid(player: Player): Boolean = true

}