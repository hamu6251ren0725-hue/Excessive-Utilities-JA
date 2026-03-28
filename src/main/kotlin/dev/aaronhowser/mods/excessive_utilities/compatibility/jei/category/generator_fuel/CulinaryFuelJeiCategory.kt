package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.CulinaryGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class CulinaryFuelJeiCategory(
	recipeType: RecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<CulinaryFuelJeiCategory.Recipe>(
	recipeType,
	ModBlocks.CULINARY_GENERATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.CULINARY_GENERATOR),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val itemInputSlot = builder.addInputSlot(9, 9)
			.setStandardSlotBackground()

		for (stack in recipe.stacks) {
			itemInputSlot.addItemStack(stack)
		}

	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val fePerTick = recipe.fePerTick
		val duration = recipe.duration
		val totalFe = recipe.feTotal

		val perTickString = "%,d".format(fePerTick)
		val durationString = "%,d".format(duration)
		val totalFeString = "%,d".format(totalFe)

		builder.addText(
			Component.literal("$perTickString FE/tick"),
			200, 12
		).setPosition(32, 1).setColor(0xFF808080.toInt())

		builder.addText(
			Component.literal("$durationString ticks"),
			200, 12
		).setPosition(32, 16).setColor(0xFF808080.toInt())

		builder.addText(
			Component.literal("$totalFeString FE total"),
			200, 12
		).setPosition(32, 31).setColor(0xFF808080.toInt())
	}

	companion object {
		fun getAllRecipes(ingredientManager: IIngredientManager): List<Recipe> {
			return ingredientManager.allItemStacks
				.asSequence()

				.mapNotNull { itemStack ->
					val values = CulinaryGeneratorBlockEntity.getFeValues(itemStack)

					if (values.first > 0 && values.second > 0) {
						values to itemStack
					} else {
						null
					}
				}
				.groupBy(
					keySelector = { it.first },
					valueTransform = { it.second }
				)
				.map { (values, stacks) ->
					Recipe(
						stacks,
						values.first,
						values.second
					)
				}
				.sortedByDescending(Recipe::feTotal)
				.toList()
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val fePerTick: Int, val duration: Int) {
		val feTotal = fePerTick * duration
	}

}