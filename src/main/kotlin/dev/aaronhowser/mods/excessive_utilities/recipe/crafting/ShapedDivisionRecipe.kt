package dev.aaronhowser.mods.excessive_utilities.recipe.crafting

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.DivisionSigilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class ShapedDivisionRecipe(
	pattern: ShapedRecipePattern,
	val output: ItemStack,
) : ShapedRecipe("", CraftingBookCategory.MISC, pattern, output, false) {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		for (inputStack in input.items()) {
			if (!inputStack.isItem(ModItems.DIVISION_SIGIL)) continue

			if (!DivisionSigilItem.isInverted(inputStack)) {
				val remainingUses = inputStack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
				if (remainingUses <= 0) return false
			}
		}

		return super.matches(input, level)
	}

	override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> {
		val list = NonNullList.withSize(input.size(), ItemStack.EMPTY)

		for (i in list.indices) {
			val stack = input.getItem(i)

			if (stack.hasCraftingRemainingItem()) {
				list[i] = stack.craftingRemainingItem
			}

			if (stack.isItem(ModItems.DIVISION_SIGIL)) {
				if (DivisionSigilItem.isInverted(stack)) {
					list[i] = stack.copy()
					break
				}

				val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
				if (remainingUses > 0) {
					val newStack = stack.copy()
					newStack.set(ModDataComponents.REMAINING_USES, remainingUses - 1)
					list[i] = newStack
					break
				}
			}
		}

		return list
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.SHAPED_DIVISION.get()

	class Serializer : RecipeSerializer<ShapedDivisionRecipe> {
		override fun codec(): MapCodec<ShapedDivisionRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ShapedDivisionRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<ShapedDivisionRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(ShapedDivisionRecipe::pattern),
						ItemStack.STRICT_CODEC
							.fieldOf("result")
							.forGetter(ShapedDivisionRecipe::output)
					).apply(instance, ::ShapedDivisionRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ShapedDivisionRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, ShapedDivisionRecipe::pattern,
					ItemStack.STREAM_CODEC, ShapedDivisionRecipe::output,
					::ShapedDivisionRecipe
				)
		}
	}

}