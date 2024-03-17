package de.maxbossing.mxchallenges.addons

import de.maxbossing.mxchallenges.addons.internal.AbstractAddon
import de.maxbossing.mxchallenges.addons.internal.AddonManager
import de.maxbossing.mxchallenges.addons.internal.Addons
import de.maxbossing.mxchallenges.utils.enumOf
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.event.unregister
import de.maxbossing.mxpaper.extensions.bukkit.*
import de.maxbossing.mxpaper.extensions.onlinePlayers
import de.maxbossing.mxpaper.runnables.task
import de.maxbossing.mxpaper.utils.hasMark
import de.maxbossing.mxpaper.utils.mark
import de.maxbossing.mxpaper.utils.unmark
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.settings.ChallengeEnumSetting
import de.miraculixx.challenge.api.settings.ChallengeIntSetting
import de.miraculixx.challenge.api.utils.IconNaming
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent

class DamagePunisher: AbstractAddon() {

    private enum class Punishment {
        INVENTORY_CLEAR,
        INVENTORY_RANDOM,
        BOOST_TO_THE_SKY,
    }

    override val name: Component = cmp("Damage Punishment")
    override val description: List<Component> = listOf(cmp("Punish yourself for getting damage!"))
    override val icon: Material = Material.REDSTONE
    override val tags: Set<ChallengeTags> = setOf(ChallengeTags.HARD, ChallengeTags.ADDON)
    override val data: ChallengeData = ChallengeData(
        settings = mapOf(
            "punishment" to ChallengeEnumSetting(Material.IRON_SWORD.name, Punishment.INVENTORY_CLEAR.name, options = Punishment.entries.map { it.name }),
            "boost_height" to ChallengeIntSetting(Material.FEATHER.name, 50, min = 2, step = 1)
        ),
        settingNames = mapOf(
            "punishment" to IconNaming(cmp("Punishment"), listOf()),
            "boost_height" to IconNaming(cmp("Boost Height"), listOf())
        )
    )

    private var sMode = Punishment.INVENTORY_CLEAR
    private var sBoostHeight = 50

    private val listeners = mutableListOf<Listener>()

    override fun start(): Boolean {
        val settings = AddonManager.getSettings(Addons.DAMAGE_PUNISHER).settings
        sMode = enumOf<Punishment>(settings["punishment"]?.toEnum()?.getValue())?: Punishment.INVENTORY_CLEAR
        sBoostHeight = settings["boost_height"]?.toInt()?.getValue() ?: 50

        when (sMode) {
            Punishment.INVENTORY_CLEAR -> listeners.add(inventoryClearListener)
            Punishment.INVENTORY_RANDOM -> listeners.add(inventoryRandomizeListener)
            Punishment.BOOST_TO_THE_SKY -> listeners.add(damageBoostListener)
        }

        return true
    }

    override fun register() = when (sMode) {
        Punishment.INVENTORY_CLEAR -> inventoryClearListener.register()
        Punishment.INVENTORY_RANDOM -> inventoryRandomizeListener.register()
        Punishment.BOOST_TO_THE_SKY -> damageBoostListener.register()
    }

    override fun unregister() {
        listeners.forEach(Listener::unregister)
        onlinePlayers.forEach { it.unmark("currently_boosted") }
    }

    private val damageBoostListener = listen<EntityDamageEvent> { event ->
        if (event.entity !is Player)
            return@listen

        if (event.entity.hasMark("currently_boosted")) {
            event.isCancelled = true
            return@listen
        }

        event.entity.boost(sBoostHeight)
        event.entity.mark("currently_boosted")

        task(period = 1) {
            if (event.entity.isGroundSolid && event.entity.velocity.y < 1) {
                event.entity.unmark("currently_boosted")
                it.cancel()
            }
        }
    }
    private val inventoryClearListener = listen<EntityDamageEvent> {
        if (it.entity !is Player)return@listen
        (it.entity as Player).inventory.clear()
    }
    private val inventoryRandomizeListener = listen<EntityDamageEvent> {
        if (it.entity !is Player)return@listen
        (it.entity as Player).inventory.contents = (it.entity as Player).inventory.contents.toList().shuffled().toTypedArray()
    }
}