package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class SunCrystalItem(properties: Properties) : Item(properties) {

	override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
		val level = entity.level()
		if (level.isClientSide) return false

		val charge = stack.getOrDefault(ModDataComponents.CHARGE, 0)
		if (charge >= MAX_CHARGE) return false

		if (!level.isDay) return false
		if (!level.canSeeSky(entity.blockPosition())) return false

		val newCharge = charge + 1
		stack.set(ModDataComponents.CHARGE, newCharge)

		return false
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val charge = stack.getOrDefault(ModDataComponents.CHARGE, 0)
		tooltipComponents.add(Component.literal("Charge: $charge / $MAX_CHARGE"))
	}

	companion object {
		const val MAX_CHARGE = 20 * 30

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.CHARGE, 0)
			}
	}

}