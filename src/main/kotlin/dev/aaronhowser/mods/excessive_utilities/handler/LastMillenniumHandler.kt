package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronUtil
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class LastMillenniumHandler : SavedData() {

	private val playerChunks: MutableMap<UUID, ChunkPos> = mutableMapOf()

	fun getChunk(uuid: UUID): ChunkPos {
		val existing = playerChunks[uuid]
		if (existing != null) {
			return existing
		}

		val nextIndex = playerChunks.size
		val (x, y) = AaronUtil.getGridSpiralPos(nextIndex)
		val chunkPos = ChunkPos(x * 16, y * 16)
		playerChunks[uuid] = chunkPos
		return chunkPos
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val listTag = tag.getList(CHUNK_LIST_NBT, Tag.TAG_COMPOUND.toInt())

		for ((uuid, chunkPos) in playerChunks) {
			val chunkTag = CompoundTag()
			chunkTag.putUUID(PLAYER_NBT, uuid)
			chunkTag.putLong(CHUNK_NBT, chunkPos.toLong())
			listTag.add(chunkTag)
		}

		tag.put(CHUNK_LIST_NBT, listTag)
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_last_millennium"

		const val CHUNK_LIST_NBT = "Chunks"
		const val PLAYER_NBT = "Player"
		const val CHUNK_NBT = "Chunk"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): LastMillenniumHandler {
			val data = LastMillenniumHandler()

			val listTag = tag.getList(CHUNK_LIST_NBT, Tag.TAG_COMPOUND.toInt())
			for (i in listTag.indices) {
				val coordinateTag = listTag.getCompound(i)

				val uuid = coordinateTag.getUUID(PLAYER_NBT)
				val chunkLong = coordinateTag.getLong(CHUNK_NBT)
				val chunkPos = ChunkPos(chunkLong)

				data.playerChunks[uuid] = chunkPos
			}

			return data
		}

		fun get(level: ServerLevel): LastMillenniumHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val factory = Factory(::LastMillenniumHandler, ::load)

			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}
	}
}