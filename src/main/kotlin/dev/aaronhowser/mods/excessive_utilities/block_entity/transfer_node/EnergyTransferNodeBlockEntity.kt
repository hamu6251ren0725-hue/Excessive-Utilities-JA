package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

class EnergyTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.ENERGY_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferEnergyStorage: EnergyStorage = EnergyStorage(1_000_000)

	private fun getParentEnergyStorage(level: ServerLevel): IEnergyStorage? {
		return level.getCapability(Capabilities.EnergyStorage.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun pullerTick(level: ServerLevel) {
		pushIntoParent(level)

		if (bufferEnergyStorage.energyStored > 0) {
			ping.reset()
			return
		}

		pullFromPingPos(level)

		if (bufferEnergyStorage.energyStored <= 0) {
			ping.march(level)
		}
	}

	private fun pullFromPingPos(level: ServerLevel) {
		val neighborStorages = getEnergyStorageAroundPing(level)
		if (neighborStorages.isEmpty()) return

		val amountThatCanFit = bufferEnergyStorage.maxEnergyStored - bufferEnergyStorage.energyStored
		if (amountThatCanFit <= 0) return

		val maxAmountToPull = getAmountPerTick()
		val amountToExtract = maxAmountToPull.coerceAtMost(amountThatCanFit)

		for (storage in neighborStorages) {
			val simulated = storage.extractEnergy(amountToExtract, true)
			if (simulated <= 0) continue

			val actualExtracted = storage.extractEnergy(simulated, false)
			if (actualExtracted <= 0) continue

			bufferEnergyStorage.receiveEnergy(actualExtracted, false)
			didWorkThisTick = true
		}
	}

	private fun pushIntoParent(level: ServerLevel) {
		val parentEnergyStorage = getParentEnergyStorage(level) ?: return

		val energyToPush = bufferEnergyStorage.extractEnergy(bufferEnergyStorage.energyStored, true)
		if (energyToPush <= 0) return

		val actualAmountPushed = parentEnergyStorage.receiveEnergy(energyToPush, false)
		if (!hasCreativeUpgrade()) {
			bufferEnergyStorage.extractEnergy(actualAmountPushed, false)
		}

		didWorkThisTick = true
	}

	private fun getEnergyStorageAroundPing(level: ServerLevel): List<IEnergyStorage> {
		val possibleDirections = ping.getNextDirections(level)
		val pingPos = ping.currentPingPos

		val handlers = mutableListOf<IEnergyStorage>()
		for (dir in possibleDirections) {
			val neighborPos = pingPos.relative(dir)
			val handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, dir.opposite) ?: continue
			handlers.add(handler)
		}

		return handlers
	}

	override fun pusherTick(level: ServerLevel) {
		pullFromParent(level)

		if (bufferEnergyStorage.energyStored <= 0) {
			ping.reset()
			return
		}

		val amountBefore = bufferEnergyStorage.energyStored
		pushIntoPingPos(level)
		val amountAfter = bufferEnergyStorage.energyStored

		if (amountAfter == amountBefore) {
			ping.march(level)
		}
	}

	private fun pushIntoPingPos(level: ServerLevel) {
		val possibleDirections = ping.getNextDirections(level)
		val pingPos = ping.currentPingPos

		var energyToPush = bufferEnergyStorage.extractEnergy(bufferEnergyStorage.energyStored, true)
		if (energyToPush <= 0) return

		for (dir in possibleDirections) {
			val neighborPos = pingPos.relative(dir)
			val handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, dir.opposite) ?: continue

			val accepted = handler.receiveEnergy(energyToPush, false)
			if (accepted <= 0) continue

			if (!hasCreativeUpgrade()) {
				bufferEnergyStorage.extractEnergy(accepted, false)
			}

			energyToPush -= accepted
			didWorkThisTick = true

			if (energyToPush <= 0) break
		}
	}

	private fun pullFromParent(level: ServerLevel) {
		val parentStorage = getParentEnergyStorage(level) ?: return

		val amountThatCanFit = bufferEnergyStorage.maxEnergyStored - bufferEnergyStorage.energyStored
		if (amountThatCanFit <= 0) return

		val maxAmountToPull = getAmountPerTick()
		val amountToExtract = maxAmountToPull.coerceAtMost(amountThatCanFit)
		if (amountToExtract <= 0) return

		val simulated = parentStorage.extractEnergy(amountToExtract, true)
		if (simulated <= 0) return

		val actualExtracted = parentStorage.extractEnergy(simulated, false)
		if (actualExtracted <= 0) return

		bufferEnergyStorage.receiveEnergy(actualExtracted, false)
		didWorkThisTick = true
	}

	private fun getAmountPerTick(): Int {
		return if (hasStackUpgrade()) 192_000 else 3_000
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		TODO("Not yet implemented")
	}
}