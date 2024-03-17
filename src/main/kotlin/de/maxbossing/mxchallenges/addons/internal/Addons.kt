package de.maxbossing.mxchallenges.addons.internal

import de.maxbossing.mxchallenges.addons.BlockBreakSpawner
import de.maxbossing.mxchallenges.addons.ChunkEffects
import de.maxbossing.mxchallenges.addons.DamagePunisher
import de.maxbossing.mxchallenges.addons.MoreDamagers
import de.maxbossing.mxchallenges.main.MXChallenges
import de.miraculixx.challenge.api.modules.challenges.CustomChallengeData
import de.miraculixx.challenge.api.utils.Icon
import de.miraculixx.challenge.api.utils.IconNaming
import java.util.*

enum class Addons(private val addon: AbstractAddon, val uuid: UUID = UUID.randomUUID()) {
    CHUNK_EFFECTS(ChunkEffects()),
    MORE_DAMAGERS(MoreDamagers()),
    BLOCK_BREAK_SPAWNER(BlockBreakSpawner()),
    DAMAGE_PUNISHER(DamagePunisher())
    ;

    fun getModData() = CustomChallengeData(
            uuid,
            addon,
            AddonManager.getSettings(this),
            Icon(this.addon.icon.name, null, IconNaming(addon.name, addon.description)),
            addon.tags,
            MXChallenges.addonName
        )

    fun getDefaultSetting() = addon.data
}