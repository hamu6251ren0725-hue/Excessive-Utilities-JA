package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class UnstableIngotItem(properties: Properties) : Item(properties) {

	override fun getName(stack: ItemStack): Component {
		if (stack.has(ModDataComponents.COUNTDOWN)) return super.getName(stack)
		return ModItemLang.MOBIUS_INGOT.toComponent()
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties().component(ModDataComponents.COUNTDOWN, 20 * 10)
		}
	}

}