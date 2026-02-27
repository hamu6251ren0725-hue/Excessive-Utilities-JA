package dev.aaronhowser.mods.excessive_utilities.handler.quantum_quarry

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData

class QuantumQuarryHandler : SavedData() {

	private var radius: Int = 0
	private var mostRecentChunk: ChunkPos = ChunkPos.ZERO

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		tag.putInt(RADIUS_TAG, radius)
		tag.putLong(MOST_RECENT_CHUNK_TAG, mostRecentChunk.toLong())
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_quantum_quarry_handler"
		const val RADIUS_TAG = "Radius"
		const val MOST_RECENT_CHUNK_TAG = "MostRecentChunk"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): QuantumQuarryHandler {
			val handler = QuantumQuarryHandler()
			handler.radius = tag.getInt(RADIUS_TAG)
			handler.mostRecentChunk = ChunkPos(tag.getLong(MOST_RECENT_CHUNK_TAG))
			return handler
		}

		fun get(level: ServerLevel): QuantumQuarryHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val factory = Factory(::QuantumQuarryHandler, ::load)
			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}
	}

}