package dev.aaronhowser.mods.excessive_utilities.menu.simple_machine

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class SimpleMachineScreen(
	menu: SimpleMachineMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<SimpleMachineMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 12

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 21,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/simple_machine.png"), 176, 180)
	}

}