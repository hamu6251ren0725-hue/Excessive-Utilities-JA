package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class DivisionSigilItem(properties: Properties) : Item(properties) {

	override fun getName(stack: ItemStack): Component {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		return if (remainingUses < 0) {
			ModItemLang.PSEUDO_INVERSION_SIGIL.toComponent()
		} else {
			super.getName(stack)
		}
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		val component = if (remainingUses < 0) {
			Component.literal("Infinite Uses")
		} else {
			Component.literal("$remainingUses Uses Remaining")
		}

		tooltipComponents.add(component)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.fireResistant()
					.component(ModDataComponents.REMAINING_USES, 0)
			}
	}

}