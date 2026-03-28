package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory

class EnchanterJeiCategory(
	recipeType: RecipeType<EnchanterRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<EnchanterRecipe>(
	recipeType,
	ModBlocks.ENCHANTER.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.ENCHANTER),
	140,
	50
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: EnchanterRecipe, focuses: IFocusGroup) {
		TODO("Not yet implemented")
	}

}