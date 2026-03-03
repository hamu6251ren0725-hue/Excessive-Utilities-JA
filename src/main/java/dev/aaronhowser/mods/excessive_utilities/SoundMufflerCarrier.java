package dev.aaronhowser.mods.excessive_utilities;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public interface SoundMufflerCarrier {

	default LongOpenHashSet eu$getSoundMufflerBlockPositions() {
		throw new IllegalStateException();
	}

}
