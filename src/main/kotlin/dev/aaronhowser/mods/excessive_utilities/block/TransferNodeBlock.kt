package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.world.level.block.Block

class TransferNodeBlock(
	val type: Type,
	val isRetrieval: Boolean
) : Block(
	Properties.of()
		.strength(1.5f, 6f)
		.requiresCorrectToolForDrops()
		.noOcclusion()
) {

	enum class Type {
		ITEM, FLUID, ENERGY
	}

}