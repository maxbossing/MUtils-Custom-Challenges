package de.maxbossing.mxchallenges.addons.internal

import de.miraculixx.challenge.api.modules.challenges.Challenge
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.settings.ChallengeSetting
import kotlinx.serialization.Contextual
import net.kyori.adventure.text.Component
import org.bukkit.Material

abstract class AbstractAddon: Challenge {
    abstract val name: Component
    abstract val description: List<Component>
    abstract val icon: Material
    abstract val tags: Set<ChallengeTags>
    abstract val data: ChallengeData
}