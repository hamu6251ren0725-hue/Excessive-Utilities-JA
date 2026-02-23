package dev.aaronhowser.mods.excessive_utilities.attachment

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class SoulDebt(
	val netSoulFragments: Int
) {

	constructor() : this(0)

	companion object {
		val CODEC: Codec<SoulDebt> =
			Codec.INT.xmap(::SoulDebt, SoulDebt::netSoulFragments)

		val STREAM_CODEC: StreamCodec<ByteBuf, SoulDebt> =
			ByteBufCodecs.VAR_INT.map(::SoulDebt, SoulDebt::netSoulFragments)
	}

}