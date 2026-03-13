package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable

data class FluidFilterFlagsComponent(
	val flagList: List<Flag>
) {

	val isInverted = Flag.INVERTED in flagList
	val useTags = Flag.USE_TAGS in flagList
	val ignoreAllComponents = Flag.IGNORE_ALL_COMPONENTS in flagList

	constructor() : this(emptyList())

	companion object {
		val CODEC: Codec<FluidFilterFlagsComponent> =
			Flag.CODEC
				.listOf()
				.xmap(::FluidFilterFlagsComponent, FluidFilterFlagsComponent::flagList)

		val STREAM_CODEC: StreamCodec<ByteBuf, FluidFilterFlagsComponent> =
			Flag.STREAM_CODEC
				.apply(ByteBufCodecs.list())
				.map(::FluidFilterFlagsComponent, FluidFilterFlagsComponent::flagList)
	}

	enum class Flag(
		private val id: String,
		private val message: String
	) : StringRepresentable {
		INVERTED("inverted", ModMenuLang.FLUID_FILTER_INVERTED),
		USE_TAGS("use_tags", ModMenuLang.FLUID_FILTER_TAGS),
		IGNORE_ALL_COMPONENTS("ignore_all_components", ModMenuLang.FLUID_FILTER_IGNORE_ALL_COMPONENTS),
		;

		val bit: Int = 1 shl ordinal

		override fun getSerializedName(): String = id

		fun getMessage(isOn: Boolean): MutableComponent {
			val component = Component.translatable(message)
			if (!isOn) {
				component.withStyle(ChatFormatting.STRIKETHROUGH)
			}

			return component
		}

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Flag> =
				StringRepresentable.fromEnum { entries.toTypedArray() }

			val STREAM_CODEC: StreamCodec<ByteBuf, Flag> =
				ByteBufCodecs.fromCodec(CODEC)

		}
	}

}