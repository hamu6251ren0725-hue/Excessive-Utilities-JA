package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBiomeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.biome.Biome

class BiomeMarkerItem(properties: Properties) : Item(properties) {

	companion object {

		fun getAllCrystals(registries: HolderLookup.Provider): List<ItemStack> {
			return registries
				.lookupOrThrow(Registries.BIOME)
				.listElements()
				.toList()
				.filter { !it.isHolder(ModBiomeTagsProvider.BIOME_MARKER_BLACKLIST) }
				.map(::getMarker)
		}

		fun getMarker(biomeHolder: Holder<Biome>): ItemStack {
			val stack = ModItems.BIOME_MARKER.toStack()
			stack.set(ModDataComponents.BIOME, biomeHolder)
			return stack
		}

	}

}