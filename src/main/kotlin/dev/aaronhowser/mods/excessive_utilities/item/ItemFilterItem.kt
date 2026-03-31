package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterComponent
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
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
import java.util.*

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
		val flagComponent = getFilterComponent(stack)
		for (flag in flagComponent.flags) {
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
		fun setFlags(filterStack: ItemStack, flags: List<ItemFilterComponent.Flag>) {
			val currentComponent = getFilterComponent(filterStack)
			val newComponent = currentComponent.withFlags(flags)
			filterStack.set(ModDataComponents.ITEM_FILTER, newComponent)
		}

		fun getFilterComponent(filterStack: ItemStack): ItemFilterComponent {
			return filterStack.getOrDefault(ModDataComponents.ITEM_FILTER, ItemFilterComponent())
		}

		fun getGhostStack(filterStack: ItemStack, slot: Int): ItemStack {
			val component = getFilterComponent(filterStack)
			return component.getItem(slot)
		}

		fun placeGhostInSlot(filterStack: ItemStack, slot: Int, stackToPlace: ItemStack) {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return
			if (slot !in 0 until ItemFilterComponent.CONTAINER_SIZE) return

			val currentComponent = getFilterComponent(filterStack)
			val filterItems = currentComponent.getItems()
			filterItems[slot] = stackToPlace.copyWithCount(1)

			val newContents = ItemContainerContents.fromItems(filterItems)

			val newComponent = currentComponent.withItems(newContents)
			filterStack.set(ModDataComponents.ITEM_FILTER, newComponent)
		}

		fun getFilterItems(filterStack: ItemStack): NonNullList<ItemStack> {
			val component = getFilterComponent(filterStack)
			return component.getItems()
		}

		fun filterItems(filterStack: ItemStack, items: List<ItemStack>): List<ItemStack> {
			return items.filter { passesFilter(filterStack, it) }
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: ItemStack): Boolean {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return false

			val flags = getFilterComponent(filterStack)

			val isInverted = flags.isInverted
			val ignoreDamage = flags.ignoreDamage
			val ignoreAllComponents = flags.ignoreAllComponents
			val useTags = flags.useTags

			if (checkedStack.isEmpty) return isInverted

			val container = flags.itemContents

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
					val matchesNonDamageComponents = isSameComponentsWithoutDamage(stackInFilter, checkedStack)
					return matchesNonDamageComponents != isInverted
				}

				val matchesAllComponents = ItemStack.isSameItemSameComponents(stackInFilter, checkedStack)
				return matchesAllComponents != isInverted
			}

			// To reach here, checkedStack has to match with none of the stacks in the filter,
			// so return false (or true if inverted)

			return isInverted
		}

		private fun isSameComponentsWithoutDamage(leftStack: ItemStack, rightStack: ItemStack): Boolean {
			if (leftStack.item != rightStack.item) return false

			val leftMap = Reference2ObjectOpenHashMap<DataComponentType<*>, Optional<*>>()
			val rightMap = Reference2ObjectOpenHashMap<DataComponentType<*>, Optional<*>>()

			for ((type, value) in leftStack.componentsPatch.entrySet()) {
				if (type === DataComponents.DAMAGE) continue
				leftMap[type] = value
			}

			for ((type, value) in rightStack.componentsPatch.entrySet()) {
				if (type === DataComponents.DAMAGE) continue
				rightMap[type] = value
			}

			return leftMap == rightMap
		}
	}

}