package dev.aaronhowser.mods.excessive_utilities.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import io.netty.buffer.ByteBuf
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.*

class UpdatePlayerWingPacket(
	val playerUuid: UUID,
	val wingType: AngelRingItem.Type?
) : AaronPacket() {

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun handleOnClient(context: IPayloadContext) {
		if (wingType == null) {
			AngelRingItem.PLAYER_WINGS.remove(playerUuid)
		} else {
			AngelRingItem.PLAYER_WINGS[playerUuid] = wingType
		}
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdatePlayerWingPacket> =
			CustomPacketPayload.Type(ExcessiveUtilities.modResource("update_grid_power"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdatePlayerWingPacket> =
			StreamCodec.composite(
				UUIDUtil.STREAM_CODEC, UpdatePlayerWingPacket::playerUuid,
				AngelRingItem.Type.STREAM_CODEC, UpdatePlayerWingPacket::wingType,
				::UpdatePlayerWingPacket
			)
	}

}