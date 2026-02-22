package dev.aaronhowser.mods.excessive_utilities.block.base

import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.level.Level

interface ContainerContainer {
	fun getContainer(): Container?

	fun dropContents(level: Level, pos: BlockPos) {
		val contents = getContainer() ?: return
		Containers.dropContents(level, pos, contents)
	}
}
