package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.TrashCanBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState

class TrashCanBlock(
	val type: Type
) : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): TrashCanBlockEntity? {
		return when (type) {
			Type.ITEM, Type.CHEST -> TrashCanBlockEntity(pos, state)

			else -> null
		}
	}

	enum class Type(
		val id: String
	) : StringRepresentable {
		ITEM("ITEM"),
		CHEST("CHEST"),
		FLUID("FLUID"),
		ENERGY("ENERGY")
		;

		override fun getSerializedName(): String = id
	}

}