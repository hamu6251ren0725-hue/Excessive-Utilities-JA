package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterFlagsComponent
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.Level

class ItemFilterItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack?> {
		val usedStack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val menuConstructor = MenuConstructor { containerId, playerInventory, player ->
				ItemFilterMenu(containerId, playerInventory, usedHand)
			}

			val provider = SimpleMenuProvider(menuConstructor, usedStack.hoverName)
			player.openMenu(provider) { data -> data.writeEnum(usedHand) }
		}

		return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val flagComponent = getFlagComponent(stack)
		for (flag in flagComponent.flagList) {
			val component = flag.getMessage(true).withStyle(ChatFormatting.BLUE)
			tooltipComponents.add(component)
		}

		for (y in 0 until 4) {
			val stacks = mutableListOf<ItemStack>()

			for (x in 0 until 4) {
				val slot = y * 4 + x
				val ghostStack = getGhostStack(stack, slot)

				if (ghostStack.isNotEmpty()) {
					stacks.add(ghostStack)
				}
			}

			if (stacks.isEmpty()) continue

			val component = Component.empty()
			for ((i, stack) in stacks.withIndex()) {
				component.append(stack.hoverName)

				if (i < stacks.size - 1) {
					component.append(Component.literal(", "))
				}
			}

			tooltipComponents.add(component)
		}

	}

	companion object {
		const val CONTAINER_SIZE = 16

		fun setFlags(filterStack: ItemStack, flags: List<ItemFilterFlagsComponent.Flag>) {
			if (flags.isEmpty()) {
				filterStack.remove(ModDataComponents.ITEM_FILTER_FLAGS)
				return
			}

			val flagComponent = ItemFilterFlagsComponent(flags)
			filterStack.set(ModDataComponents.ITEM_FILTER_FLAGS, flagComponent)
		}

		fun getFlagComponent(filterStack: ItemStack): ItemFilterFlagsComponent {
			return filterStack.getOrDefault(ModDataComponents.ITEM_FILTER_FLAGS, ItemFilterFlagsComponent())
		}

		fun getGhostStack(filterStack: ItemStack, slot: Int): ItemStack {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return ItemStack.EMPTY
			if (slot !in 0 until CONTAINER_SIZE) return ItemStack.EMPTY

			val filterItems = getFilterItems(filterStack)
			return filterItems[slot]
		}

		fun placeGhostInSlot(filterStack: ItemStack, slot: Int, stackToPlace: ItemStack) {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return
			if (slot !in 0 until CONTAINER_SIZE) return

			val filterItems = getFilterItems(filterStack)
			filterItems[slot] = stackToPlace.copyWithCount(1)

			val isEmpty = filterItems.all(ItemStack::isEmpty)
			if (isEmpty) {
				filterStack.remove(DataComponents.CONTAINER)
			} else {
				val newComponent = ItemContainerContents.fromItems(filterItems)
				filterStack.set(DataComponents.CONTAINER, newComponent)
			}
		}

		fun getFilterItems(filterStack: ItemStack): NonNullList<ItemStack> {
			val list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
			val container = filterStack.get(DataComponents.CONTAINER) ?: return list
			container.copyInto(list)
			return list
		}

		fun filterItems(filterStack: ItemStack, items: List<ItemStack>): List<ItemStack> {
			return items.filter { passesFilter(filterStack, it) }
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: ItemStack): Boolean {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return false

			val flags = getFlagComponent(filterStack)

			val isInverted = flags.isInverted
			val ignoreDamage = flags.ignoreDamage
			val ignoreAllComponents = flags.ignoreAllComponents
			val useTags = flags.useTags

			if (checkedStack.isEmpty) return isInverted

			val container = filterStack.get(DataComponents.CONTAINER) ?: return isInverted

			for (slot in 0 until container.slots) {
				val stackInFilter = container.getStackInSlot(slot)
				if (stackInFilter.isEmpty) continue

				// Allow for nested Item Filters
				// If it passes the nested Filter, return true (unless the outer Filter is inverted)
				if (stackInFilter.isItem(ModItems.ITEM_FILTER)) {
					val passesNestedFilter = passesFilter(stackInFilter, checkedStack)
					return passesNestedFilter != isInverted
				}

				// If using tags it ignores item + components and just checks if
				// the stack in the filter has any of the tags of the checked stack
				if (useTags) {
					val tagsMatch = stackInFilter.tags.anyMatch { checkedStack.isItem(it) }
					if (tagsMatch) return !isInverted
				}

				// At this point it can only pass it's the same item
				if (!stackInFilter.isItem(checkedStack.item)) continue

				// Everything after this is just comparing components
				// If we're ignoring components, none of that matters
				if (ignoreAllComponents) {
					return isInverted
				}

				// If we're ignoring damage, remove the damage component and compare the rest
				if (ignoreDamage) {
					val leftWithoutDamage = stackInFilter.copy()
					val rightWithoutDamage = checkedStack.copy()

					leftWithoutDamage.remove(DataComponents.DAMAGE)
					rightWithoutDamage.remove(DataComponents.DAMAGE)

					val matchesNonDamageComponents = ItemStack.isSameItemSameComponents(leftWithoutDamage, rightWithoutDamage)
					return matchesNonDamageComponents != isInverted
				}

				val matchesAllComponents = ItemStack.isSameItemSameComponents(stackInFilter, checkedStack)
				return matchesAllComponents != isInverted
			}

			// To reach here, checkedStack has to match with none of the stacks in the filter,
			// so return false (or true if inverted)

			return isInverted
		}
	}

}