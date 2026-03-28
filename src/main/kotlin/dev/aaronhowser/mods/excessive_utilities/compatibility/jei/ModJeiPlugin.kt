package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun getPluginUid(): ResourceLocation = ID

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addRecipeCatalyst(ModBlocks.FURNACE, RecipeTypes.SMELTING)
	}

	override fun registerCategories(registration: IRecipeCategoryRegistration) {
		val guiHelper = registration.jeiHelpers.guiHelper

		registration.addRecipeCategories(
			EnchanterJeiCategory(ENCHANTER, guiHelper)
		)
	}

	companion object {
		val ID = ExcessiveUtilities.modResource("jei_plugin")

		val CRUSHER: RecipeType<RecipeHolder<CrusherRecipe>> = makeRecipeType("crusher")
		val ENCHANTER: RecipeType<RecipeHolder<EnchanterRecipe>> = makeRecipeType("enchanter")
		val QED: RecipeType<RecipeHolder<QedRecipe>> = makeRecipeType("qed")
		val RESONATOR: RecipeType<RecipeHolder<ResonatorRecipe>> = makeRecipeType("resonator")

		private fun <T : Recipe<*>> makeRecipeType(id: String): RecipeType<RecipeHolder<T>> {
			return RecipeType.createRecipeHolderType<T>(ExcessiveUtilities.modResource(id))
		}
	}

}