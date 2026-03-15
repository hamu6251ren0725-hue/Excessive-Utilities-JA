package dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator

import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe

enum class SingleInputDataDrivenGeneratorType(
	val baseGeneratorType: GeneratorType
) {
	ENDER(GeneratorType.ENDER),
	EXPLOSIVE(GeneratorType.EXPLOSIVE),
	PINK(GeneratorType.PINK),
	NETHER_STAR(GeneratorType.NETHER_STAR),
	FROSTY(GeneratorType.FROSTY),
	HALITOSIS(GeneratorType.HALITOSIS),
	DEATH(GeneratorType.DEATH)

	;

	val fuelRecipeType: SingleItemFuelRecipe.GeneratorType by lazy {
		when (this) {
			ENDER -> SingleItemFuelRecipe.GeneratorType.ENDER
			EXPLOSIVE -> SingleItemFuelRecipe.GeneratorType.EXPLOSIVE
			PINK -> SingleItemFuelRecipe.GeneratorType.PINK
			NETHER_STAR -> SingleItemFuelRecipe.GeneratorType.NETHER_STAR
			FROSTY -> SingleItemFuelRecipe.GeneratorType.FROSTY
			HALITOSIS -> SingleItemFuelRecipe.GeneratorType.HALITOSIS
			DEATH -> SingleItemFuelRecipe.GeneratorType.DEATH
		}
	}

}