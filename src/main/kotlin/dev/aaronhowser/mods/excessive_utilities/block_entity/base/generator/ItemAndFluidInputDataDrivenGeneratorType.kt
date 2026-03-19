package dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe

enum class ItemAndFluidInputDataDrivenGeneratorType(
	val baseGeneratorType: GeneratorType
) {
	SLIMY(GeneratorType.SLIMY),
	HEATED_REDSTONE(GeneratorType.HEATED_REDSTONE),

	;

	val fuelRecipeType: ItemAndFluidFuelRecipe.GeneratorType by lazy {
		when (this) {
			SLIMY -> ItemAndFluidFuelRecipe.GeneratorType.SLIMY
			HEATED_REDSTONE -> ItemAndFluidFuelRecipe.GeneratorType.HEATED_REDSTONE
		}
	}

}