package dev.aaronhowser.mods.excessive_utilities.attachment

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class SoulDebt(
	val extraHealth: Double
) {

	constructor() : this(0.0)

	companion object {
		val CODEC: Codec<SoulDebt> =
			Codec.DOUBLE.xmap(::SoulDebt, SoulDebt::extraHealth)

		val STREAM_CODEC: StreamCodec<ByteBuf, SoulDebt> =
			ByteBufCodecs.DOUBLE.map(::SoulDebt, SoulDebt::extraHealth)
	}

}