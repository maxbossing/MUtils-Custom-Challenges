package de.maxbossing.mxchallenges.addons

import de.maxbossing.mxchallenges.addons.internal.AbstractAddon
import de.maxbossing.mxchallenges.addons.internal.AddonManager
import de.maxbossing.mxchallenges.addons.internal.Addons
import de.maxbossing.mxchallenges.utils.enumOf
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.event.unregister
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.settings.ChallengeEnumSetting
import de.miraculixx.challenge.api.settings.ChallengeIntSetting
import de.miraculixx.challenge.api.utils.IconNaming
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class MoreDamagers : AbstractAddon() {

    private val listeners = mutableListOf<Listener>()

    private enum class DamagerMode { ACHIEVEMENT, ITEM_CLICK, RIGHT_CLICK, LEFT_CLICK, MOVE }

    override val name: Component = cmp("More Damagers")
    override val description: List<Component> = listOf(cmp("More options to damage yourself "), cmp("constantly!"))
    override val icon: Material = Material.NETHERITE_SWORD
    override val tags: Set<ChallengeTags> = setOf(ChallengeTags.HARD, ChallengeTags.ADDON)
    override val data: ChallengeData = ChallengeData(
        settings = mapOf(
            "event" to ChallengeEnumSetting(Material.KNOWLEDGE_BOOK.name, DamagerMode.ACHIEVEMENT.name, options = DamagerMode.entries.map { it.name }),
            "amount" to ChallengeIntSetting(Material.BEETROOT.name, 1, "hp", min = 1, step = 1)
        ),
        settingNames = mapOf(
            "event" to IconNaming(name = cmp("Damage Event"), lore = listOf()),
            "amount" to IconNaming(name = cmp("Damage"), lore = listOf())
        )
    )

    private var sMode = DamagerMode.ACHIEVEMENT
    private var sDamage = 1

    override fun start(): Boolean {
        val settings = AddonManager.getSettings(Addons.MORE_DAMAGERS).settings
        sDamage = settings["damage"]?.toInt()?.getValue() ?: 1
        sMode = enumOf<DamagerMode>(settings["event"]?.toEnum()?.getValue()) ?: DamagerMode.ACHIEVEMENT

        when (sMode) {
            DamagerMode.ACHIEVEMENT -> listeners.add(achievementListener)
            DamagerMode.ITEM_CLICK -> listeners.add(itemClickListener)
            DamagerMode.RIGHT_CLICK -> listeners.add(rightClickListener)
            DamagerMode.LEFT_CLICK -> listeners.add(leftClickListener)
            DamagerMode.MOVE -> listeners.add(moveListener)
        }
        return true
    }

    override fun register() {
        when (sMode) {
            DamagerMode.ACHIEVEMENT -> achievementListener.register()
            DamagerMode.ITEM_CLICK -> itemClickListener.register()
            DamagerMode.RIGHT_CLICK -> rightClickListener.register()
            DamagerMode.LEFT_CLICK -> leftClickListener.register()
            DamagerMode.MOVE -> moveListener.register()
        }
    }

    override fun unregister() {
        listeners.forEach(Listener::unregister)
    }


    private val achievementListener = listen<PlayerAdvancementDoneEvent> { damage(it.player) }
    // This one got removed from main MUtils, but it's really fun and really painful to play
    private val itemClickListener = listen<InventoryClickEvent> {
        when (it.click) {
            ClickType.DOUBLE_CLICK, ClickType.LEFT, ClickType.SHIFT_LEFT,
            ClickType.RIGHT, ClickType.SHIFT_RIGHT, ClickType.NUMBER_KEY,
            ClickType.SWAP_OFFHAND -> damage(it.whoClicked)
            else -> {}
        }
    }
    private val leftClickListener = listen<PlayerInteractEvent> { if (it.action.isLeftClick) damage(it.player) }
    private val rightClickListener = listen<PlayerInteractEvent> { if (it.action.isRightClick) damage(it.player) }
    private val moveListener = listen<PlayerMoveEvent> { damage(it.player) }

    private fun damage(player: HumanEntity) {
        val health = player.health
        player.health = if ((health - sDamage) <= 0) 0.0 else health - sDamage
        player.damage(0.01)
    }
}