package dev.aaronhowser.mods.excessive_utilities.menu.components

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class ToggleStackButton(
	x: Int,
	y: Int,
	width: Int,
	height: Int,
	val font: Font,
	val itemStackGetter: () -> ItemStack,
	val messageGetter: () -> Component,
	val isOnGetter: () -> Boolean,
	onPress: OnPress
) : Button(x, y, width, height, messageGetter(), onPress, DEFAULT_NARRATION) {

	override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		baseRenderWidget(guiGraphics, mouseX, mouseY, partialTick)
		renderItemStack(guiGraphics)
		if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
			renderToolTip(guiGraphics, mouseX, mouseY)
		}
	}

	private fun renderToolTip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		val message = messageGetter()

		guiGraphics.renderComponentTooltip(
			font,
			listOf(this.message),
			mouseX,
			mouseY
		)
	}

	private fun renderItemStack(guiGraphics: GuiGraphics) {
		val stack = itemStackGetter()
		if (stack.isEmpty) return

		guiGraphics.renderItem(
			stack,
			x + (width - 16) / 2,
			y + (height - 16) / 2
		)
	}

	private fun baseRenderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		guiGraphics.setColor(1.0f, 1.0f, 1.0f, this.alpha)
		RenderSystem.enableBlend()
		RenderSystem.enableDepthTest()
		guiGraphics.blitSprite(
			SPRITES.get(isOnGetter(), this.isHovered),
			this.x,
			this.y,
			this.getWidth(),
			this.getHeight()
		)
		guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
	}


}