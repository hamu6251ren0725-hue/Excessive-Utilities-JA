#!/usr/bin/env kotlin

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

val baseDirectoryPath = Path.of("").toAbsolutePath()

val oldTexturesPath = baseDirectoryPath.resolve("textures-old")
val newTexturesPath = baseDirectoryPath.resolve("textures")
val notInNewPath = baseDirectoryPath.resolve("not-in-new")
val notInOldPath = baseDirectoryPath.resolve("not-in-old")

Files.createDirectories(notInNewPath)

Files.walk(oldTexturesPath).use { oldStream ->
	oldStream.forEach { oldFilePath ->
		if (Files.isDirectory(oldFilePath)) return@forEach

		val relative = oldTexturesPath.relativize(oldFilePath)
		val newPath = newTexturesPath.resolve(relative)
		val isInNewFolder = Files.exists(newPath)

		if (!isInNewFolder) {
			val differencePath = notInNewPath.resolve(relative)
			Files.createDirectories(differencePath.parent)

			Files.copy(oldFilePath, differencePath, StandardCopyOption.REPLACE_EXISTING)
		}
	}
}

Files.createDirectories(notInOldPath)

Files.walk(newTexturesPath).use { newStream ->
	newStream.forEach { newFilePath ->
		if (Files.isDirectory(newFilePath)) return@forEach

		val relative = newTexturesPath.relativize(newFilePath)
		val oldPath = oldTexturesPath.resolve(relative)
		val isInOldFolder = Files.exists(oldPath)

		if (!isInOldFolder) {
			val differencePath = notInOldPath.resolve(relative)
			Files.createDirectories(differencePath.parent)

			Files.copy(newFilePath, differencePath, StandardCopyOption.REPLACE_EXISTING)
		}
	}
}