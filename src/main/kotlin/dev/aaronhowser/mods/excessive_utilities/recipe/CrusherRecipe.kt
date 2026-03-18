package dev.aaronhowser.mods.excessive_utilities.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class CrusherRecipe(
	val ingredient: Ingredient,
	private val primaryOutput: ItemStack,
	private val secondaryOutput: ItemStack,
	val secondaryChance: Float,
) : Recipe<SingleRecipeInput> {

	fun getPrimaryOutput(): ItemStack = primaryOutput.copy()
	fun getSecondaryOutput(): ItemStack = secondaryOutput.copy()

	override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
	override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = primaryOutput.copy()
	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = primaryOutput.copy()

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.CRUSHER.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.CRUSHER.get()

	companion object {
		fun getRecipe(
			level: Level,
			input: ItemStack
		): RecipeHolder<CrusherRecipe>? {
			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(SingleRecipeInput(input), level)
				}
		}

		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<CrusherRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.CRUSHER.get())
		}
	}

	class Serializer : RecipeSerializer<CrusherRecipe> {
		override fun codec(): MapCodec<CrusherRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<CrusherRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						Ingredient.CODEC
							.fieldOf("ingredient")
							.forGetter(CrusherRecipe::ingredient),
						ItemStack.CODEC
							.fieldOf("primary_output")
							.forGetter(CrusherRecipe::primaryOutput),
						ItemStack.CODEC
							.optionalFieldOf("secondary_output", ItemStack.EMPTY)
							.forGetter(CrusherRecipe::secondaryOutput),
						Codec.FLOAT
							.optionalFieldOf("secondary_chance", 0.0f)
							.forGetter(CrusherRecipe::secondaryChance)
					).apply(instance, ::CrusherRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> =
				StreamCodec.composite(
					Ingredient.CONTENTS_STREAM_CODEC, CrusherRecipe::ingredient,
					ItemStack.STREAM_CODEC, CrusherRecipe::primaryOutput,
					ItemStack.OPTIONAL_STREAM_CODEC, CrusherRecipe::secondaryOutput,
					ByteBufCodecs.FLOAT, CrusherRecipe::secondaryChance,
					::CrusherRecipe
				)
		}
	}

}