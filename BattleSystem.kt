// Enhanced Type Effectiveness and Battle System
import kotlin.random.Random

// Comprehensive type effectiveness chart based on Pokemon mechanics
object TypeEffectiveness {
    private val typeChart = mapOf(
        "normal" to mapOf(
            "rock" to 0.5, "ghost" to 0.0, "steel" to 0.5
        ),
        "fire" to mapOf(
            "fire" to 0.5, "water" to 0.5, "grass" to 2.0, "ice" to 2.0,
            "bug" to 2.0, "rock" to 0.5, "dragon" to 0.5, "steel" to 2.0
        ),
        "water" to mapOf(
            "fire" to 2.0, "water" to 0.5, "grass" to 0.5, "ground" to 2.0,
            "rock" to 2.0, "dragon" to 0.5
        ),
        "electric" to mapOf(
            "water" to 2.0, "electric" to 0.5, "grass" to 0.5, "ground" to 0.0,
            "flying" to 2.0, "dragon" to 0.5
        ),
        "grass" to mapOf(
            "fire" to 0.5, "water" to 2.0, "grass" to 0.5, "poison" to 0.5,
            "ground" to 2.0, "flying" to 0.5, "bug" to 0.5, "rock" to 2.0,
            "dragon" to 0.5, "steel" to 0.5
        ),
        "ice" to mapOf(
            "fire" to 0.5, "water" to 0.5, "grass" to 2.0, "ice" to 0.5,
            "ground" to 2.0, "flying" to 2.0, "dragon" to 2.0, "steel" to 0.5
        ),
        "fighting" to mapOf(
            "normal" to 2.0, "ice" to 2.0, "poison" to 0.5, "flying" to 0.5,
            "psychic" to 0.5, "bug" to 0.5, "rock" to 2.0, "ghost" to 0.0,
            "dark" to 2.0, "steel" to 2.0, "fairy" to 0.5
        ),
        "poison" to mapOf(
            "grass" to 2.0, "poison" to 0.5, "ground" to 0.5, "rock" to 0.5,
            "ghost" to 0.5, "steel" to 0.0, "fairy" to 2.0
        ),
        "ground" to mapOf(
            "fire" to 2.0, "electric" to 2.0, "grass" to 0.5, "poison" to 2.0,
            "flying" to 0.0, "bug" to 0.5, "rock" to 2.0, "steel" to 2.0
        ),
        "flying" to mapOf(
            "electric" to 0.5, "grass" to 2.0, "ice" to 0.5, "fighting" to 2.0,
            "bug" to 2.0, "rock" to 0.5, "steel" to 0.5
        ),
        "psychic" to mapOf(
            "fighting" to 2.0, "poison" to 2.0, "psychic" to 0.5, "dark" to 0.0,
            "steel" to 0.5
        ),
        "bug" to mapOf(
            "fire" to 0.5, "grass" to 2.0, "fighting" to 0.5, "poison" to 0.5,
            "flying" to 0.5, "psychic" to 2.0, "ghost" to 0.5, "dark" to 2.0,
            "steel" to 0.5, "fairy" to 0.5
        ),
        "rock" to mapOf(
            "fire" to 2.0, "ice" to 2.0, "fighting" to 0.5, "ground" to 0.5,
            "flying" to 2.0, "bug" to 2.0, "steel" to 0.5
        ),
        "ghost" to mapOf(
            "normal" to 0.0, "psychic" to 2.0, "ghost" to 2.0, "dark" to 0.5
        ),
        "dragon" to mapOf(
            "dragon" to 2.0, "steel" to 0.5, "fairy" to 0.0
        ),
        "dark" to mapOf(
            "fighting" to 0.5, "psychic" to 2.0, "ghost" to 2.0, "dark" to 0.5,
            "fairy" to 0.5
        ),
        "steel" to mapOf(
            "fire" to 0.5, "water" to 0.5, "electric" to 0.5, "ice" to 2.0,
            "rock" to 2.0, "steel" to 0.5, "fairy" to 2.0
        ),
        "fairy" to mapOf(
            "fire" to 0.5, "fighting" to 2.0, "poison" to 0.5, "dragon" to 2.0,
            "dark" to 2.0, "steel" to 0.5
        )
    )
    
    fun getEffectiveness(attackType: String, defenderPrimaryType: String, defenderSecondaryType: String? = null): Double {
        var effectiveness = typeChart[attackType]?.get(defenderPrimaryType) ?: 1.0
        
        defenderSecondaryType?.let { secondType ->
            val secondEffectiveness = typeChart[attackType]?.get(secondType) ?: 1.0
            effectiveness *= secondEffectiveness
        }
        
        return effectiveness
    }
    
    fun getEffectivenessText(effectiveness: Double): String {
        return when {
            effectiveness == 0.0 -> "It has no effect..."
            effectiveness < 1.0 -> "It's not very effective..."
            effectiveness > 1.0 -> "It's super effective!"
            else -> ""
        }
    }
}

// Enhanced Battle System with comprehensive mechanics
class BattleSystem {
    
    fun calculateDamage(attacker: Pokemon, defender: Pokemon, move: Move): Int {
        if (move.category == MoveCategory.STATUS) return 0
        
        // Base damage calculation using Pokemon damage formula
        val level = attacker.level
        val attackStat = if (move.category == MoveCategory.PHYSICAL) attacker.attack else attacker.specialAttack
        val defenseStat = if (move.category == MoveCategory.PHYSICAL) defender.defense else defender.specialDefense
        val power = move.power
        
        // Base damage formula: ((((2 * Level / 5 + 2) * Power * Attack / Defense) / 50) + 2)
        var damage = (((2 * level / 5 + 2) * power * attackStat / defenseStat) / 50) + 2
        
        // Apply type effectiveness
        val effectiveness = TypeEffectiveness.getEffectiveness(move.type, defender.primaryType, defender.secondaryType)
        damage = (damage * effectiveness).toInt()
        
        // STAB (Same Type Attack Bonus)
        if (move.type == attacker.primaryType || move.type == attacker.secondaryType) {
            damage = (damage * 1.5).toInt()
        }
        
        // Critical hit check
        val criticalChance = when (move.criticalHitRate) {
            1 -> 6.25  // 1/16
            2 -> 12.5  // 1/8
            3 -> 25.0  // 1/4
            4 -> 33.3  // 1/3
            else -> 6.25
        }
        
        val isCritical = Random.nextInt(1000) < (criticalChance * 10).toInt()
        if (isCritical) {
            damage = (damage * 1.5).toInt()
        }
        
        // Apply burn condition (reduces physical attack damage)
        if (attacker.statusCondition == StatusCondition.BURN && move.category == MoveCategory.PHYSICAL) {
            damage = (damage * 0.5).toInt()
        }
        
        // Random factor (85-100%)
        val randomFactor = Random.nextInt(85, 101) / 100.0
        damage = (damage * randomFactor).toInt()
        
        // Minimum damage is 1
        return maxOf(1, damage)
    }
    
    fun executeMove(attacker: Pokemon, defender: Pokemon, move: Move): BattleResult {
        val result = BattleResult()
        
        // Check if move has PP
        if (!move.usePp()) {
            result.message = "${attacker.name} tried to use ${move.name}, but it failed! No PP left!"
            result.success = false
            return result
        }
        
        // Check if attacker can act (status conditions)
        if (!canPokemonAct(attacker)) {
            result.message = getStatusMessage(attacker)
            result.success = false
            return result
        }
        
        // Accuracy check (including stat modifiers)
        val accuracy = calculateAccuracy(move.accuracy, attacker.statChanges.accuracy, defender.statChanges.evasion)
        if (Random.nextInt(100) >= accuracy) {
            result.message = "${attacker.name}'s ${move.name} missed!"
            result.success = false
            return result
        }
        
        result.message = "${attacker.name} used ${move.name}!"
        result.success = true
        
        when (move.category) {
            MoveCategory.PHYSICAL, MoveCategory.SPECIAL -> {
                val damage = calculateDamage(attacker, defender, move)
                defender.takeDamage(damage)
                
                val effectiveness = TypeEffectiveness.getEffectiveness(move.type, defender.primaryType, defender.secondaryType)
                val effectivenessText = TypeEffectiveness.getEffectivenessText(effectiveness)
                
                result.damage = damage
                result.message += "\nIt dealt $damage damage!"
                if (effectivenessText.isNotEmpty()) {
                    result.message += "\n$effectivenessText"
                }
                
                // Apply move effects
                move.effect?.let { effect ->
                    if (Random.nextInt(100) < move.effectChance) {
                        applyMoveEffect(attacker, defender, effect, result)
                    }
                }
                
                if (defender.isFainted()) {
                    result.message += "\n${defender.name} fainted!"
                    result.targetFainted = true
                }
            }
            
            MoveCategory.STATUS -> {
                move.effect?.let { effect ->
                    applyMoveEffect(attacker, defender, effect, result)
                }
            }
        }
        
        return result
    }
    
    private fun canPokemonAct(pokemon: Pokemon): Boolean {
        return when (pokemon.statusCondition) {
            StatusCondition.FREEZE -> Random.nextInt(100) < 20 // 20% chance to thaw
            StatusCondition.SLEEP -> {
                // Simplified sleep mechanics - wake up after 1-3 turns
                Random.nextInt(100) < 33
            }
            StatusCondition.PARALYZE -> Random.nextInt(100) < 75 // 25% chance to be fully paralyzed
            StatusCondition.CONFUSION -> {
                if (Random.nextInt(100) < 50) { // 50% chance to hit self in confusion
                    // Self-inflicted damage
                    val damage = pokemon.attack / 4
                    pokemon.takeDamage(damage)
                    false
                } else true
            }
            else -> true
        }
    }
    
    private fun getStatusMessage(pokemon: Pokemon): String {
        return when (pokemon.statusCondition) {
            StatusCondition.FREEZE -> "${pokemon.name} is frozen solid and can't move!"
            StatusCondition.SLEEP -> "${pokemon.name} is fast asleep!"
            StatusCondition.PARALYZE -> "${pokemon.name} is paralyzed and can't move!"
            StatusCondition.CONFUSION -> "${pokemon.name} hurt itself in its confusion!"
            else -> "${pokemon.name} is unable to move!"
        }
    }
    
    private fun calculateAccuracy(baseAccuracy: Int, attackerAccuracy: Int, defenderEvasion: Int): Int {
        val accuracyMultiplier = when (attackerAccuracy) {
            -6 -> 0.33; -5 -> 0.375; -4 -> 0.43; -3 -> 0.5; -2 -> 0.6; -1 -> 0.75
            0 -> 1.0; 1 -> 1.33; 2 -> 1.66; 3 -> 2.0; 4 -> 2.33; 5 -> 2.66; 6 -> 3.0
            else -> 1.0
        }
        
        val evasionMultiplier = when (defenderEvasion) {
            -6 -> 3.0; -5 -> 2.66; -4 -> 2.33; -3 -> 2.0; -2 -> 1.66; -1 -> 1.33
            0 -> 1.0; 1 -> 0.75; 2 -> 0.6; 3 -> 0.5; 4 -> 0.43; 5 -> 0.375; 6 -> 0.33
            else -> 1.0
        }
        
        return (baseAccuracy * accuracyMultiplier / evasionMultiplier).toInt()
    }
    
    private fun applyMoveEffect(attacker: Pokemon, defender: Pokemon, effect: MoveEffect, result: BattleResult) {
        when (effect) {
            MoveEffect.BURN -> {
                if (defender.statusCondition == null && defender.primaryType != "fire") {
                    defender.statusCondition = StatusCondition.BURN
                    result.message += "\n${defender.name} was burned!"
                }
            }
            MoveEffect.FREEZE -> {
                if (defender.statusCondition == null && defender.primaryType != "ice") {
                    defender.statusCondition = StatusCondition.FREEZE
                    result.message += "\n${defender.name} was frozen!"
                }
            }
            MoveEffect.PARALYZE -> {
                if (defender.statusCondition == null && defender.primaryType != "electric") {
                    defender.statusCondition = StatusCondition.PARALYZE
                    result.message += "\n${defender.name} was paralyzed!"
                }
            }
            MoveEffect.POISON -> {
                if (defender.statusCondition == null && defender.primaryType != "poison" && defender.primaryType != "steel") {
                    defender.statusCondition = StatusCondition.POISON
                    result.message += "\n${defender.name} was poisoned!"
                }
            }
            MoveEffect.SLEEP -> {
                if (defender.statusCondition == null) {
                    defender.statusCondition = StatusCondition.SLEEP
                    result.message += "\n${defender.name} fell asleep!"
                }
            }
            MoveEffect.CONFUSION -> {
                if (defender.statusCondition != StatusCondition.CONFUSION) {
                    defender.statusCondition = StatusCondition.CONFUSION
                    result.message += "\n${defender.name} became confused!"
                }
            }
            MoveEffect.HEAL -> {
                val healAmount = attacker.maxHp / 2
                attacker.heal(healAmount)
                result.message += "\n${attacker.name} restored ${healAmount} HP!"
            }
            MoveEffect.DRAIN -> {
                val drainAmount = result.damage / 2
                attacker.heal(drainAmount)
                result.message += "\n${attacker.name} drained ${drainAmount} HP!"
            }
            MoveEffect.FLINCH -> {
                // Flinch would be handled in turn order, simplified here
                result.message += "\n${defender.name} flinched!"
            }
            else -> {
                // Handle stat changes
                val statChanged = when (effect) {
                    MoveEffect.ATTACK_UP, MoveEffect.ATTACK_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Attack"
                    }
                    MoveEffect.DEFENSE_UP, MoveEffect.DEFENSE_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Defense"
                    }
                    MoveEffect.SP_ATTACK_UP, MoveEffect.SP_ATTACK_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Special Attack"
                    }
                    MoveEffect.SP_DEFENSE_UP, MoveEffect.SP_DEFENSE_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Special Defense"
                    }
                    MoveEffect.SPEED_UP, MoveEffect.SPEED_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Speed"
                    }
                    MoveEffect.ACCURACY_UP, MoveEffect.ACCURACY_DOWN -> {
                        val target = if (effect.name.contains("UP")) attacker else defender
                        target.statChanges.changeStat(effect)
                        "Accuracy"
                    }
                    else -> null
                }
                
                statChanged?.let { stat ->
                    val direction = if (effect.name.contains("UP")) "rose" else "fell"
                    val target = if (effect.name.contains("UP")) attacker else defender
                    result.message += "\n${target.name}'s $stat $direction!"
                }
            }
        }
    }
    
    fun applyEndOfTurnEffects(pokemon: Pokemon): String {
        val messages = mutableListOf<String>()
        
        when (pokemon.statusCondition) {
            StatusCondition.BURN -> {
                val damage = pokemon.maxHp / 16
                pokemon.takeDamage(damage)
                messages.add("${pokemon.name} is hurt by its burn! (${damage} damage)")
            }
            StatusCondition.POISON -> {
                val damage = pokemon.maxHp / 8
                pokemon.takeDamage(damage)
                messages.add("${pokemon.name} is hurt by poison! (${damage} damage)")
            }
            StatusCondition.BADLY_POISON -> {
                // Badly poisoned damage increases each turn (simplified)
                val damage = pokemon.maxHp / 6
                pokemon.takeDamage(damage)
                messages.add("${pokemon.name} is hurt by poison! (${damage} damage)")
            }
            else -> {}
        }
        
        return messages.joinToString("\n")
    }
}

data class BattleResult(
    var success: Boolean = false,
    var message: String = "",
    var damage: Int = 0,
    var targetFainted: Boolean = false,
    var criticalHit: Boolean = false,
    var statusInflicted: StatusCondition? = null
)
