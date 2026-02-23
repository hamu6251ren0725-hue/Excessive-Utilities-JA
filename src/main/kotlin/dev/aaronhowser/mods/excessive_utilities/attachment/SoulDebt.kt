package dev.aaronhowser.mods.excessive_utilities.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player

data class SoulDebt(
	val netSoulFragments: Int
) {

	constructor() : this(0)

	companion object {
		val CODEC: Codec<SoulDebt> =
			Codec.INT.xmap(::SoulDebt, SoulDebt::netSoulFragments)

		val STREAM_CODEC: StreamCodec<ByteBuf, SoulDebt> =
			ByteBufCodecs.VAR_INT.map(::SoulDebt, SoulDebt::netSoulFragments)

		val ATTRIBUTE_MODIFIER_NAME = ExcessiveUtilities.modResource("soul_debt")

		fun absorbSoulFragment(player: Player, amount: Int) {
			changeSoulFragmentAmount(player, amount)
		}

		fun removeSoulFragment(player: Player, amount: Int) {
			changeSoulFragmentAmount(player, -amount)
		}

		fun changeSoulFragmentAmount(player: Player, amount: Int) {
			val current = player.getData(ModAttachmentTypes.SOUL_DEBT).netSoulFragments
			player.setData(ModAttachmentTypes.SOUL_DEBT, SoulDebt(current + amount))

			updateSoulDebt(player)
		}

		fun updateSoulDebt(player: Player) {
			val maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH) ?: return
			val netSoulFragments = player.getData(ModAttachmentTypes.SOUL_DEBT).netSoulFragments

			if (maxHealthAttribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
				maxHealthAttribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
			}

			if (netSoulFragments == 0) return

			val modifier = AttributeModifier(ATTRIBUTE_MODIFIER_NAME, netSoulFragments.toDouble(), AttributeModifier.Operation.ADD_VALUE)
			maxHealthAttribute.addPermanentModifier(modifier)
		}
	}

}