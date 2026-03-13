package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModMenuLang {

	const val SOUL_OF_A_FORGOTTEN_DEITY = "tooltip.excessive_utilities.soul_of_a_forgotten_deity"
	const val SOUL_DEBT = "tooltip.excessive_utilities.soul_debt"
	const val SOUL_SURPLUS = "tooltip.excessive_utilities.soul_surplus"
	const val SOUL_HEALTH_MODIFIER = "tooltip.excessive_utilities.soul_health_modifier"
	const val FE = "tooltip.geneticsresequenced.fe"
	const val ITEM_FILTER_INVERTED_TRUE = "tooltip.excessive_utilities.item_filter.inverted.true"
	const val ITEM_FILTER_INVERTED_FALSE = "tooltip.excessive_utilities.item_filter.inverted.false"
	const val ITEM_FILTER_TAGS_TRUE = "tooltip.excessive_utilities.item_filter.tags.true"
	const val ITEM_FILTER_TAGS_FALSE = "tooltip.excessive_utilities.item_filter.tags.false"
	const val ITEM_FILTER_IGNORE_DAMAGE_TRUE = "tooltip.excessive_utilities.item_filter.ignore_damage.true"
	const val ITEM_FILTER_IGNORE_DAMAGE_FALSE = "tooltip.excessive_utilities.item_filter.ignore_damage.false"
	const val ITEM_FILTER_IGNORE_ALL_COMPONENTS_TRUE = "tooltip.excessive_utilities.item_filter.ignore_all_components.true"
	const val ITEM_FILTER_IGNORE_ALL_COMPONENTS_FALSE = "tooltip.excessive_utilities.item_filter.ignore_all_components.false"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SOUL_OF_A_FORGOTTEN_DEITY, "Soul of a Forgotten Deity")
			add(SOUL_DEBT, "Soul Debt: %d")
			add(SOUL_SURPLUS, "Soul Surplus: %d")
			add(SOUL_HEALTH_MODIFIER, "Current Health Modifier: %s")
			add(FE, "%1\$s/%2\$s FE")
			add(ITEM_FILTER_INVERTED_TRUE, "Inverted: True")
			add(ITEM_FILTER_INVERTED_FALSE, "Inverted: False")
			add(ITEM_FILTER_TAGS_TRUE, "Use Tags: True")
			add(ITEM_FILTER_TAGS_FALSE, "Use Tags: False")
			add(ITEM_FILTER_IGNORE_DAMAGE_TRUE, "Ignore Damage: True")
			add(ITEM_FILTER_IGNORE_DAMAGE_FALSE, "Ignore Damage: False")
			add(ITEM_FILTER_IGNORE_ALL_COMPONENTS_TRUE, "Ignore All Components: True")
			add(ITEM_FILTER_IGNORE_ALL_COMPONENTS_FALSE, "Ignore All Components: False")
		}
	}

}