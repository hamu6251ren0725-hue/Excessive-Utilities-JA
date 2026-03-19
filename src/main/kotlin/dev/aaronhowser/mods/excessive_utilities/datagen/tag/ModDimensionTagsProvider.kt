package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.dimension.LevelStem
import java.util.concurrent.CompletableFuture

class ModDimensionTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : TagsProvider<LevelStem>(output, Registries.LEVEL_STEM, lookupProvider) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(ALLOWS_DIVISION_SIGIL_ACTIVATION)
			.addOptional(LevelStem.OVERWORLD.location())

		tag(ALLOWS_DIVISION_SIGIL_INVERSION)
			.addOptional(LevelStem.END.location())
	}

	companion object {
		val ALLOWS_DIVISION_SIGIL_ACTIVATION = create("allows_division_sigil_activation")
		val ALLOWS_DIVISION_SIGIL_INVERSION = create("allows_division_sigil_inversion")

		private fun create(name: String): TagKey<LevelStem> {
			return TagKey.create(
				Registries.LEVEL_STEM,
				ExcessiveUtilities.modResource(name)
			)
		}
	}

}