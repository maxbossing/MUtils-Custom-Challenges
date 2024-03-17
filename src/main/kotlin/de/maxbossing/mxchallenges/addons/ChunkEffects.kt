package de.maxbossing.mxchallenges.addons

import de.maxbossing.mxchallenges.addons.internal.AddonManager
import de.maxbossing.mxchallenges.addons.internal.AbstractAddon
import de.maxbossing.mxchallenges.addons.internal.Addons
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.event.unregister
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.onlinePlayers
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.settings.ChallengeBoolSetting
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.utils.IconNaming
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ChunkEffects : AbstractAddon() {

    // Challenge Metadata
    override val name = cmp("Chunk Effects")
    override val description = listOf(cmp("Every Chunk gives you a"), cmp("different effect!"))
    override val icon = Material.LINGERING_POTION
    override val tags = setOf(ChallengeTags.FUN, ChallengeTags.ADDON)
    override val data = ChallengeData(
        settings = mapOf("random" to ChallengeBoolSetting(Material.DROPPER.name, false)),
        settingNames = mapOf("random" to IconNaming(name = cmp("Random Effects"), lore = listOf()))
    )

    private var random: Boolean = false
    private val map = mutableMapOf<Chunk, PotionEffectType>()
    private val randomMap = mutableMapOf<Player, PotionEffectType>()

    override fun start(): Boolean {
        // Reload settings
        random = AddonManager.getSettings(Addons.CHUNK_EFFECTS).settings["damage"]?.toBool()?.getValue() ?: false

        return true
    }

    override fun stop() {
        map.clear()
        randomMap.clear()
    }

    override fun register() {
        chunkChangeListener.register()
    }

    override fun unregister() {
        chunkChangeListener.unregister()
        onlinePlayers.forEach {
            map[it.chunk]?.let { e -> it.removePotionEffect(e) }
            randomMap[it]?.let { e -> it.removePotionEffect(e) }
        }
    }



    private val chunkChangeListener = listen<PlayerMoveEvent>{
        val from = it.from.chunk
        val to = it.to.chunk

        if (from == to)return@listen

        if (random) {
            randomMap[it.player]?.let { e ->
                it.player.removePotionEffect(e)
            }
            randomMap[it.player] = PotionEffectType.values().random()
            println("effect: ${randomMap[it.player]}")
            it.player.addPotionEffect(PotionEffect(randomMap[it.player]!!, PotionEffect.INFINITE_DURATION, 0, true, false, true))

            return@listen
        }
        it.player.removePotionEffect(from.effect())

        it.player.addPotionEffect(PotionEffect(to.effect(), PotionEffect.INFINITE_DURATION, 0, true, false, true))
    }

    private fun Chunk.effect(): PotionEffectType {
        if (!map.containsKey(this))
            map += this to PotionEffectType.values().random()

        return map[this]!!
    }
}