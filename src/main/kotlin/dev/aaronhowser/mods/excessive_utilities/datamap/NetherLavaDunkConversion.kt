package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.datamaps.DataMapType

class NetherLavaDunkConversion(
	val output: ItemStack
) {

	companion object {
		val CODEC: Codec<NetherLavaDunkConversion> =
			ItemStack.CODEC.xmap(::NetherLavaDunkConversion, NetherLavaDunkConversion::output)

		val DATA_MAP: DataMapType<Item, NetherLavaDunkConversion> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("nether_lava_dunk_conversion"),
					Registries.ITEM,
					CODEC
				)
				.synced(CODEC, true)
				.build()
	}

}