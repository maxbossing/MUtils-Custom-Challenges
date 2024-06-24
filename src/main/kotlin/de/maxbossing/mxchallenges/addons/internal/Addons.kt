package de.maxbossing.mxchallenges.addons.internal

import de.maxbossing.mxchallenges.addons.ChunkEffects
import de.maxbossing.mxchallenges.addons.DamagePunisher
import de.maxbossing.mxchallenges.addons.MoreDamagers
import de.maxbossing.mxchallenges.main.MXChallenges
import de.miraculixx.challenge.api.modules.challenges.CustomChallengeData
import de.miraculixx.challenge.api.utils.Icon
import de.miraculixx.challenge.api.utils.IconNaming
import java.util.*

enum class Addons(private val addon: AbstractAddon, val uuid: UUID) {
    CHUNK_EFFECTS(ChunkEffects(), UUID.fromString("87e4fe12-4b97-4a77-ba85-510d3bca7b18")),
    MORE_DAMAGERS(MoreDamagers(), UUID.fromString("cdfaa4bc-2d9d-49bc-bc96-60a9535e9754")),
    DAMAGE_PUNISHER(DamagePunisher(), UUID.fromString("1fb335aa-8622-42b2-821f-04ce83c1bd81")),
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