package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SoulFragmentItem(properties: Properties) : Item(properties) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val stack = player.getItemInHand(usedHand)

		if (!level.isClientSide && !player.isFakePlayer) {
			SoulDebt.absorbSoulFragment(player, 1)
			stack.shrink(1)
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
	}

}