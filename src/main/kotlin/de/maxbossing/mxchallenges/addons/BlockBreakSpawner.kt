package de.maxbossing.mxchallenges.addons

import de.maxbossing.mxchallenges.addons.internal.AbstractAddon
import de.maxbossing.mxchallenges.addons.internal.AddonManager
import de.maxbossing.mxchallenges.addons.internal.Addons
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.event.unregister
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.settings.ChallengeBoolSetting
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.utils.IconNaming
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakSpawner: AbstractAddon() {
    override val name: Component = cmp("Block Break Spawner")
    override val description: List<Component> = listOf(cmp("Every block you break spawns"), cmp("a random Mob"))
    override val icon: Material = Material.WOODEN_PICKAXE
    override val tags: Set<ChallengeTags> = setOf(ChallengeTags.ADDON, ChallengeTags.FUN)
    override val data: ChallengeData = ChallengeData(
        settings = mapOf(
            "random" to ChallengeBoolSetting(Material.DROPPER.name, false)
        ),
        settingNames = mapOf(
            "random" to IconNaming(cmp("Random Mobs"), listOf())
        )
    )


    private var sRandom = false
    private var map = mutableMapOf<Material, EntityType>()

    override fun start(): Boolean {
        sRandom = AddonManager.getSettings(Addons.BLOCK_BREAK_SPAWNER).settings["random"]?.toBool()?.getValue() ?: false
        return true
    }
    override fun stop() = map.clear()
    override fun register() = blockBreakListener.register()
    override fun unregister() = blockBreakListener.unregister()

    private val blockBreakListener = listen<BlockBreakEvent> { spawnMob(it.block.location) }
    private fun spawnMob(loc: Location) {
        if (sRandom)
            loc.world.spawnEntity(loc, EntityType.entries.filter { it.isAlive && it.isSpawnable && it.isEnabledByFeature(loc.world) }.random())
        else
            loc.world.spawnEntity(loc, map.getOrPut(loc.block.type) {
                EntityType.entries.filter { it.isAlive && it.isSpawnable && it.isEnabledByFeature(loc.world)}.random()
            })
    }
}