package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.*

class WirelessFeTransmitterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(), pos, blockState) {

	var ownerUuid: UUID? = null
	private val destinationCache: MutableSet<IEnergyStorage> = mutableSetOf()

	fun getEnergyStorage(): IEnergyStorage? {
		val level = level as? ServerLevel ?: return null
		val uuid = ownerUuid ?: return null
		return WirelessFeNetworkHandler.get(level).getNetwork(uuid).energyStorage
	}

	private fun serverTick(level: ServerLevel) {
		if (level.gameTime % 20 == 0L) {
			recalculateDestinationCache(level)
		}
	}

	fun recalculateDestinationCache(level: ServerLevel) {
		destinationCache.clear()

		val radius = ServerConfig.CONFIG.wirelessFeTransmitterRange.get()

		val blocks = BlockPos.betweenClosed(
			blockPos.offset(-radius, -radius, -radius),
			blockPos.offset(radius, radius, radius)
		)

		val directions = listOf(null, *Direction.entries.toTypedArray())

		for (pos in blocks) {
			dirLoop@
			for (dir in directions) {
				@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
				val energyHandler = level.getCapability(
					Capabilities.EnergyStorage.BLOCK,
					pos,
					dir
				)

				if (energyHandler != null && energyHandler.canReceive()) {
					destinationCache.add(energyHandler)
					continue@dirLoop
				}
			}
		}

	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"

		fun getEnergyCapability(transmitter: WirelessFeTransmitterBlockEntity, direction: Direction?): IEnergyStorage? {
			return transmitter.getEnergyStorage()
		}
	}

}