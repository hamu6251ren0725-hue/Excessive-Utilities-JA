package dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class QuantumQuarryScreen(
	menu: QuantumQuarryMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<QuantumQuarryMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 30

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 149,
			y = topPos + 21,
			maxGetter = { 1_000_000 },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		super.renderLabels(guiGraphics, mouseX, mouseY)

		val x = "%,d".format(menu.getTargetX())
		val y = "%,d".format(menu.getTargetY())
		val z = "%,d".format(menu.getTargetZ())

		guiGraphics.drawString(
			font,
			Component.literal("X: $x"),
			35,
			20,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			Component.literal("Y: $y"),
			35,
			30,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			Component.literal("Z: $z"),
			35,
			40,
			4210752,
			false
		)

		val progress = menu.getProgress()

		val amountMined = "%,d".format(menu.getAmountBlocksBroken())

		guiGraphics.drawString(
			font,
			Component.literal("$amountMined mined"),
			35,
			50,
			4210752,
			false
		)

		val biomeRegistry = AaronClientUtil.localLevel
			?.registryAccess()
			?.registryOrThrow(Registries.BIOME)

		if (biomeRegistry != null) {
			val biomeId = menu.getBiomeId()
			val biome = biomeRegistry.byId(biomeId)
			if (biome != null) {
				val biomeKey = biomeRegistry.getKey(biome)
				if (biomeKey != null) {
					guiGraphics.drawString(
						font,
						Component.literal(biomeKey.toString()),
						35,
						60,
						4210752,
						false
					)
				}
			}
		}

	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/quantum_quarry.png"), 176, 198)
	}

}