package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import io.netty.buffer.ByteBuf
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

data class ItemFilterComponent(
	val itemContents: ItemContainerContents,
	val flags: List<Flag>
) {

	val isInverted = Flag.INVERTED in flags
	val useTags = Flag.USE_TAGS in flags
	val ignoreDamage = Flag.IGNORE_DAMAGE in flags
	val ignoreAllComponents = Flag.IGNORE_ALL_COMPONENTS in flags

	constructor() : this(ItemContainerContents.EMPTY, emptyList())

	fun withFlags(newFlags: List<Flag>): ItemFilterComponent = copy(flags = newFlags)
	fun withItems(newItems: ItemContainerContents): ItemFilterComponent = copy(itemContents = newItems)

	fun getItems(): NonNullList<ItemStack> {
		val list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
		itemContents.copyInto(list)
		return list
	}

	fun getItem(index: Int): ItemStack {
		if (index !in 0 until CONTAINER_SIZE) return ItemStack.EMPTY
		return getItems()[index]
	}

	companion object {
		const val CONTAINER_SIZE = 16

		val CODEC: Codec<ItemFilterComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ItemContainerContents.CODEC
						.optionalFieldOf("items", ItemContainerContents.EMPTY)
						.forGetter(ItemFilterComponent::itemContents),
					Flag.CODEC.listOf()
						.optionalFieldOf("flags", emptyList())
						.forGetter(ItemFilterComponent::flags)
				).apply(instance, ::ItemFilterComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemFilterComponent> =
			StreamCodec.composite(
				ItemContainerContents.STREAM_CODEC, ItemFilterComponent::itemContents,
				Flag.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemFilterComponent::flags,
				::ItemFilterComponent
			)
	}

	enum class Flag(
		private val id: String,
		private val messageOn: String,
		private val messageOf: String
	) : StringRepresentable {
		INVERTED("inverted", ModMenuLang.INVERTED_ON, ModMenuLang.INVERTED_OFF),
		USE_TAGS("use_tags", ModMenuLang.USE_TAGS_ON, ModMenuLang.USE_TAGS_OFF),
		IGNORE_DAMAGE("ignore_damage", ModMenuLang.IGNORE_DAMAGE_ON, ModMenuLang.IGNORE_DAMAGE_OFF),
		IGNORE_ALL_COMPONENTS("ignore_all_components", ModMenuLang.IGNORE_ALL_COMPONENTS_ON, ModMenuLang.IGNORE_ALL_COMPONENTS_OFF)
		;

		override fun getSerializedName(): String = id

		fun getMessage(isOn: Boolean): MutableComponent {
			val message = if (isOn) messageOn else messageOf
			return Component.translatable(message)
		}

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Flag> =
				StringRepresentable.fromEnum { entries.toTypedArray() }

			val STREAM_CODEC: StreamCodec<ByteBuf, Flag> =
				ByteBufCodecs.fromCodec(CODEC)

		}
	}

}