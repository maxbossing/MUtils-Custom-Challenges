package de.maxbossing.mxchallenges.main

import de.maxbossing.mxchallenges.addons.internal.AddonManager
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.extensions.console
import de.maxbossing.mxpaper.main.*

/**
 * Entry point for the MChallenge-Addon plugin. We use [KSpigot] to use all KSpigot utilities later
 */
class MXChallenges : MXPaper() {
    companion object {
        lateinit var INSTANCE: MXChallenges
        lateinit var addonName: String
    }

    override fun load() {
        INSTANCE = this

        MXPaperConfiguration.Events.autoRegistration = false

        prefix = cmp("MXChallenges", cAccent) + cmp(" >>", cBase) + cmp(" ")

        @Suppress("DEPRECATION")
        addonName = description.name
    }

    override fun startup() {
        AddonManager.loadMods()

        console.sendMessage(prefix + cmp("MXChallenges Addon loaded!", cBase))
    }

    override fun shutdown() {
        // Save all data on shutdown
        AddonManager.saveMods()
    }
}

val mxchallenges by lazy { MXChallenges.INSTANCE }