package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.ICraftingGridHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.ShapedRecipe

class ShapedRecipeExtension<T : ShapedRecipe> : ICraftingCategoryExtension<T> {

	override fun setRecipe(
		recipeHolder: RecipeHolder<T>,
		builder: IRecipeLayoutBuilder,
		craftingGridHelper: ICraftingGridHelper,
		focuses: IFocusGroup
	) {
		val registry = AaronClientUtil.localLevel?.registryAccess() ?: return

		val recipe = recipeHolder.value
		val result = recipe.getResultItem(registry)

		val width = recipe.width
		val height = recipe.height

		craftingGridHelper.createAndSetOutputs(builder, listOf(result))
		craftingGridHelper.createAndSetIngredients(builder, recipe.ingredients, width, height)
	}

	override fun isHandled(recipeHolder: RecipeHolder<T>): Boolean {
		return recipeHolder.value.isSpecial
	}

}