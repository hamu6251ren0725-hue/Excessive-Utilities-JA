package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.Tags

data class MagicalSnowGlobeProgressComponent(
	val requirements: HashMap<TagKey<Biome>, Boolean>
) {

	constructor(map: Map<TagKey<Biome>, Boolean>) : this(HashMap(map))

	fun getWithComplete(biome: Holder<Biome>): MagicalSnowGlobeProgressComponent? {
		val newMap = HashMap(requirements)
		var found = false

		for ((biomeTag, alreadyFound) in requirements) {
			if (alreadyFound) continue

			if (biome.isHolder(biomeTag)) {
				newMap[biomeTag] = true
				found = true
			}
		}

		return if (found) MagicalSnowGlobeProgressComponent(newMap) else null
	}

	companion object {
		val CODEC: Codec<MagicalSnowGlobeProgressComponent> =
			Codec.unboundedMap(
				TagKey.codec(Registries.BIOME),
				Codec.BOOL
			)
				.xmap(
					::MagicalSnowGlobeProgressComponent,
					MagicalSnowGlobeProgressComponent::requirements
				)

		val STREAM_CODEC: StreamCodec<ByteBuf?, MagicalSnowGlobeProgressComponent> =
			ByteBufCodecs.map(
				::HashMap,
				AaronExtraStreamCodecs.tagKeyStreamCodec(Registries.BIOME),
				ByteBufCodecs.BOOL
			).map(::MagicalSnowGlobeProgressComponent, MagicalSnowGlobeProgressComponent::requirements)

		val DEFAULT: MagicalSnowGlobeProgressComponent =
			MagicalSnowGlobeProgressComponent(
				mapOf(
					BiomeTags.IS_FOREST to false,
					BiomeTags.IS_HILL to false,
					BiomeTags.IS_JUNGLE to false,
					Tags.Biomes.IS_MAGICAL to false,
					BiomeTags.IS_MOUNTAIN to false,
					BiomeTags.IS_OCEAN to false,
					Tags.Biomes.IS_PLAINS to false,
					Tags.Biomes.IS_SANDY to false,
					Tags.Biomes.IS_SNOWY to false,
					Tags.Biomes.IS_SWAMP to false,
					BiomeTags.IS_END to false,
					BiomeTags.IS_NETHER to false,
				)
			)

		val DEFAULT_COMPLETED: MagicalSnowGlobeProgressComponent =
			DEFAULT.requirements
				.keys
				.associateWith { true }
				.let(::MagicalSnowGlobeProgressComponent)
	}

}