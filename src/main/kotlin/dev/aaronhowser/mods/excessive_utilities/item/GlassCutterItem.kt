package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Tiers

class GlassCutterItem(properties: Properties) : DiggerItem(Tiers.IRON, ModBlockTagsProvider.GLASS_CUTTER_MINEABLE, properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.attributes(createAttributes(Tiers.IRON, 1.5f, -3f))
	}

}