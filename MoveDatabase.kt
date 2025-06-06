// Enhanced Move System with comprehensive Pokemon moves database
import kotlin.random.Random

// Enhanced Move data class with all properties from the fetched data
data class Move(
    val name: String,
    val type: String,
    val category: MoveCategory,
    val power: Int,
    val accuracy: Int,
    val pp: Int,
    val maxPp: Int = pp,
    val priority: Int = 0,
    val description: String,
    val effect: MoveEffect? = null,
    val effectChance: Int = 0,
    val criticalHitRate: Int = 1,
    val makesContact: Boolean = false,
    val isSound: Boolean = false,
    val isPunch: Boolean = false,
    val isBite: Boolean = false
) {
    var currentPp: Int = pp
        private set
    
    fun usePp(): Boolean {
        if (currentPp > 0) {
            currentPp--
            return true
        }
        return false
    }
    
    fun restorePp(amount: Int = maxPp) {
        currentPp = minOf(maxPp, currentPp + amount)
    }
}

enum class MoveCategory {
    PHYSICAL,
    SPECIAL,
    STATUS
}

enum class MoveEffect {
    // Status conditions
    BURN, FREEZE, PARALYZE, POISON, BADLY_POISON, SLEEP, CONFUSION,
    
    // Stat changes
    ATTACK_UP, DEFENSE_UP, SP_ATTACK_UP, SP_DEFENSE_UP, SPEED_UP, ACCURACY_UP, EVASION_UP,
    ATTACK_DOWN, DEFENSE_DOWN, SP_ATTACK_DOWN, SP_DEFENSE_DOWN, SPEED_DOWN, ACCURACY_DOWN, EVASION_DOWN,
    ATTACK_UP_2, DEFENSE_UP_2, SP_ATTACK_UP_2, SP_DEFENSE_UP_2, SPEED_UP_2,
    ATTACK_DOWN_2, DEFENSE_DOWN_2, SP_ATTACK_DOWN_2, SP_DEFENSE_DOWN_2, SPEED_DOWN_2,
    
    // Battle effects
    FLINCH, HEAL, DRAIN, RECOIL, CHARGE, PROTECT, SUBSTITUTE, LEECH_SEED,
    MULTI_HIT, PRIORITY_PLUS, PRIORITY_MINUS, ALWAYS_HIT, NEVER_MISS,
    HIGH_CRIT, SUPER_CRIT, OHKO, FIXED_DAMAGE, USER_FAINTS,
    
    // Weather and field effects
    SUNNY_DAY, RAIN_DANCE, SANDSTORM, HAIL, GRAVITY, TRICK_ROOM,
    REFLECT, LIGHT_SCREEN, SAFEGUARD, MIST,
    
    // Special mechanics
    COPY_MOVE, RANDOM_MOVE, SWITCH_OUT, FORCE_SWITCH, DISABLE,
    CURSE, NIGHTMARE, TORMENT, TAUNT, ENCORE, SKETCH,
    TRANSFORM, COUNTER, MIRROR_COAT, BIDE, RAGE, METRONOME,
    BELLY_DRUM, PSYCH_UP, FOCUS_ENERGY, ENDURE, DESTINY_BOND,
    SPITE, GRUDGE, SNATCH, FOLLOW_ME, HELPING_HAND,
    
    // Trap and bind effects
    BIND, WRAP, FIRE_SPIN, WHIRLPOOL, SAND_TOMB, MAGMA_STORM,
    
    // Recovery and restoration
    ROOST, RECOVER_HALF, MOONLIGHT, SYNTHESIS, MORNING_SUN,
    MILK_DRINK, SLACK_OFF, SOFT_BOILED, WISH, HEAL_BELL, AROMATHERAPY,
    
    // Type-changing moves
    CONVERSION, CONVERSION_2, CAMOUFLAGE, REFLECT_TYPE,
    
    // Entry hazards
    SPIKES, TOXIC_SPIKES, STEALTH_ROCK, STICKY_WEB,
    
    // Team support
    ALLY_SWITCH, QUASH, AFTER_YOU, WIDE_GUARD, QUICK_GUARD,
    
    // Misc effects
    MINIMIZE, STOCKPILE, SPIT_UP, SWALLOW, RECYCLE, MAGIC_ROOM,
    WONDER_ROOM, TELEKINESIS, EMBARGO, HEAL_BLOCK, POWDER,
    ELECTRIFY, ION_DELUGE, POWDER_MOVE, GRASSY_TERRAIN,
    MISTY_TERRAIN, ELECTRIC_TERRAIN, PSYCHIC_TERRAIN
}

// Comprehensive moves database based on fetched Pokemon data
object MoveDatabase {
    private val movesMap = mutableMapOf<String, Move>()
    
    init {
        initializeMoves()
    }
    
    fun getMove(name: String): Move? = movesMap[name.lowercase()]
    
    fun getAllMoves(): List<Move> = movesMap.values.toList()
    
    fun getMovesByType(type: String): List<Move> = 
        movesMap.values.filter { it.type.equals(type, ignoreCase = true) }
    
    private fun initializeMoves() {
        // Normal Type Moves
        addMove("Tackle", "normal", MoveCategory.PHYSICAL, 40, 100, 35, "A physical attack in which the user charges and slams into the target.")
        addMove("Scratch", "normal", MoveCategory.PHYSICAL, 40, 100, 35, "Hard, pointed, sharp claws rake the target to inflict damage.")
        addMove("Quick Attack", "normal", MoveCategory.PHYSICAL, 40, 100, 30, "The user lunges at the target at a speed that makes it almost invisible.", priority = 1)
        addMove("Hyper Beam", "normal", MoveCategory.SPECIAL, 150, 90, 5, "The target is attacked with a powerful beam. The user can't move on the next turn.")
        addMove("Body Slam", "normal", MoveCategory.PHYSICAL, 85, 100, 15, "The user drops onto the target with its full body weight.", MoveEffect.PARALYZE, 30)
        addMove("Double-Edge", "normal", MoveCategory.PHYSICAL, 120, 100, 15, "A reckless, life-risking tackle that slightly hurts the user.", MoveEffect.RECOIL)
        addMove("Swift", "normal", MoveCategory.SPECIAL, 60, 999, 20, "Star-shaped rays are shot at the opposing Pokémon. This attack never misses.")
        
        // Fire Type Moves
        addMove("Ember", "fire", MoveCategory.SPECIAL, 40, 100, 25, "The target is attacked with small flames.", MoveEffect.BURN, 10)
        addMove("Flamethrower", "fire", MoveCategory.SPECIAL, 90, 100, 15, "The target is scorched with an intense blast of fire.", MoveEffect.BURN, 10)
        addMove("Fire Blast", "fire", MoveCategory.SPECIAL, 110, 85, 5, "The target is attacked with an intense blast of all-consuming fire.", MoveEffect.BURN, 10)
        addMove("Fire Punch", "fire", MoveCategory.PHYSICAL, 75, 100, 15, "The target is punched with a fiery fist.", MoveEffect.BURN, 10, isPunch = true)
        addMove("Flame Wheel", "fire", MoveCategory.PHYSICAL, 60, 100, 25, "The user cloaks itself in fire and charges at the target.", MoveEffect.BURN, 10)
        addMove("Heat Wave", "fire", MoveCategory.SPECIAL, 95, 90, 10, "The user attacks by exhaling hot breath on opposing Pokémon.", MoveEffect.BURN, 10)
        
        // Water Type Moves
        addMove("Water Gun", "water", MoveCategory.SPECIAL, 40, 100, 25, "The target is blasted with a forceful shot of water.")
        addMove("Bubble Beam", "water", MoveCategory.SPECIAL, 65, 100, 20, "A spray of bubbles is forcefully ejected at the target.", MoveEffect.SPEED_DOWN, 10)
        addMove("Surf", "water", MoveCategory.SPECIAL, 90, 100, 15, "The user attacks everything around it by swamping its surroundings with a giant wave.")
        addMove("Hydro Pump", "water", MoveCategory.SPECIAL, 110, 80, 5, "The target is blasted by a huge volume of water launched under great pressure.")
        addMove("Water Pulse", "water", MoveCategory.SPECIAL, 60, 100, 20, "The user attacks the target with a pulsing blast of water.", MoveEffect.CONFUSION, 20)
        addMove("Aqua Tail", "water", MoveCategory.PHYSICAL, 90, 90, 10, "The user attacks by swinging its tail as if it were a vicious wave in a raging storm.")
        
        // Electric Type Moves
        addMove("Thunder Shock", "electric", MoveCategory.SPECIAL, 40, 100, 30, "A jolt of electricity crashes down on the target.", MoveEffect.PARALYZE, 10)
        addMove("Thunderbolt", "electric", MoveCategory.SPECIAL, 90, 100, 15, "A strong electric blast crashes down on the target.", MoveEffect.PARALYZE, 10)
        addMove("Thunder", "electric", MoveCategory.SPECIAL, 110, 70, 10, "A wicked thunderbolt is dropped on the target.", MoveEffect.PARALYZE, 30)
        addMove("Thunder Punch", "electric", MoveCategory.PHYSICAL, 75, 100, 15, "The target is punched with an electrified fist.", MoveEffect.PARALYZE, 10, isPunch = true)
        addMove("Thunder Wave", "electric", MoveCategory.STATUS, 0, 90, 20, "The user launches a weak jolt of electricity that paralyzes the target.", MoveEffect.PARALYZE, 100)
        
        // Grass Type Moves
        addMove("Vine Whip", "grass", MoveCategory.PHYSICAL, 45, 100, 25, "The target is struck with slender, whiplike vines.")
        addMove("Razor Leaf", "grass", MoveCategory.PHYSICAL, 55, 95, 25, "Sharp-edged leaves are launched to slash at opposing Pokémon.", criticalHitRate = 2)
        addMove("Solar Beam", "grass", MoveCategory.SPECIAL, 120, 100, 10, "The user gathers light, then blasts a bundled beam on the next turn.", MoveEffect.CHARGE)
        addMove("Petal Dance", "grass", MoveCategory.SPECIAL, 120, 100, 10, "The user attacks the target by scattering petals for two to three turns.", MoveEffect.CONFUSION)
        addMove("Sleep Powder", "grass", MoveCategory.STATUS, 0, 75, 15, "The user scatters a big cloud of sleep-inducing dust around the target.", MoveEffect.SLEEP, 100)
        addMove("Poison Powder", "grass", MoveCategory.STATUS, 0, 75, 35, "The user scatters a cloud of poisonous dust that poisons the target.", MoveEffect.POISON, 100)
        
        // Ice Type Moves
        addMove("Ice Beam", "ice", MoveCategory.SPECIAL, 90, 100, 10, "The target is struck with an icy-cold beam of energy.", MoveEffect.FREEZE, 10)
        addMove("Blizzard", "ice", MoveCategory.SPECIAL, 110, 70, 5, "A howling blizzard is summoned to strike opposing Pokémon.", MoveEffect.FREEZE, 10)
        addMove("Ice Punch", "ice", MoveCategory.PHYSICAL, 75, 100, 15, "The target is punched with an icy fist.", MoveEffect.FREEZE, 10, isPunch = true)
        addMove("Powder Snow", "ice", MoveCategory.SPECIAL, 40, 100, 25, "The user attacks with a chilling gust of powdery snow.", MoveEffect.FREEZE, 10)
        
        // Fighting Type Moves
        addMove("Karate Chop", "fighting", MoveCategory.PHYSICAL, 50, 100, 25, "The target is attacked with a sharp chop.", criticalHitRate = 2)
        addMove("Low Kick", "fighting", MoveCategory.PHYSICAL, 60, 100, 20, "A powerful low kick that makes the target fall over.")
        addMove("Submission", "fighting", MoveCategory.PHYSICAL, 80, 80, 20, "The user grabs the target and recklessly dives for the ground.", MoveEffect.RECOIL)
        addMove("Seismic Toss", "fighting", MoveCategory.PHYSICAL, 80, 100, 20, "The target is thrown using the power of gravity.")
        addMove("High Jump Kick", "fighting", MoveCategory.PHYSICAL, 130, 90, 10, "The target is attacked with a knee kick from a jump.", MoveEffect.RECOIL)
        
        // Poison Type Moves
        addMove("Poison Sting", "poison", MoveCategory.PHYSICAL, 15, 100, 35, "The user stabs the target with a poisonous stinger.", MoveEffect.POISON, 30)
        addMove("Sludge", "poison", MoveCategory.SPECIAL, 65, 100, 20, "Unsanitary sludge is hurled at the target.", MoveEffect.POISON, 30)
        addMove("Sludge Bomb", "poison", MoveCategory.SPECIAL, 90, 100, 10, "Unsanitary sludge is hurled at the target.", MoveEffect.POISON, 30)
        addMove("Toxic", "poison", MoveCategory.STATUS, 0, 90, 10, "A move that leaves the target badly poisoned.", MoveEffect.BADLY_POISON, 100)
        
        // Ground Type Moves
        addMove("Earthquake", "ground", MoveCategory.PHYSICAL, 100, 100, 10, "The user sets off an earthquake that strikes every Pokémon around it.")
        addMove("Dig", "ground", MoveCategory.PHYSICAL, 80, 100, 10, "The user burrows, then attacks on the next turn.")
        addMove("Sand Attack", "ground", MoveCategory.STATUS, 0, 100, 15, "Sand is hurled in the target's face.", MoveEffect.ACCURACY_DOWN, 100)
        
        // Flying Type Moves
        addMove("Gust", "flying", MoveCategory.SPECIAL, 40, 100, 35, "A gust of wind is whipped up by wings and launched at the target.")
        addMove("Wing Attack", "flying", MoveCategory.PHYSICAL, 60, 100, 35, "The target is struck with large, imposing wings spread wide.")
        addMove("Fly", "flying", MoveCategory.PHYSICAL, 90, 95, 15, "The user soars and then strikes its target on the next turn.")
        addMove("Drill Peck", "flying", MoveCategory.PHYSICAL, 80, 100, 20, "A corkscrewing attack that strikes the target with a sharp beak or horn.")
        
        // Psychic Type Moves
        addMove("Confusion", "psychic", MoveCategory.SPECIAL, 50, 100, 25, "The target is hit by a weak telekinetic force.", MoveEffect.CONFUSION, 10)
        addMove("Psychic", "psychic", MoveCategory.SPECIAL, 90, 100, 10, "The target is hit by a strong telekinetic force.", MoveEffect.SP_DEFENSE_DOWN, 10)
        addMove("Psybeam", "psychic", MoveCategory.SPECIAL, 65, 100, 20, "The target is attacked with a peculiar ray.", MoveEffect.CONFUSION, 10)
        addMove("Hypnosis", "psychic", MoveCategory.STATUS, 0, 60, 20, "The user employs hypnotic suggestion to make the target fall asleep.", MoveEffect.SLEEP, 100)
        
        // Bug Type Moves
        addMove("String Shot", "bug", MoveCategory.STATUS, 0, 95, 40, "The opposing Pokémon are bound with silk blown from the user's mouth.", MoveEffect.SPEED_DOWN, 100)
        addMove("Pin Missile", "bug", MoveCategory.PHYSICAL, 25, 95, 20, "Sharp spikes are shot at the target in rapid succession.")
        addMove("Leech Life", "bug", MoveCategory.PHYSICAL, 80, 100, 10, "The user drains the target's blood.", MoveEffect.DRAIN)
        
        // Rock Type Moves
        addMove("Rock Throw", "rock", MoveCategory.PHYSICAL, 50, 90, 15, "The user picks up and throws a small rock at the target.")
        addMove("Rock Slide", "rock", MoveCategory.PHYSICAL, 75, 90, 10, "Large boulders are hurled at opposing Pokémon.", MoveEffect.FLINCH, 30)
        addMove("Stone Edge", "rock", MoveCategory.PHYSICAL, 100, 80, 5, "The user stabs the target from below with sharpened stones.", criticalHitRate = 2)
        
        // Ghost Type Moves
        addMove("Lick", "ghost", MoveCategory.PHYSICAL, 30, 100, 30, "The target is licked with a long tongue.", MoveEffect.PARALYZE, 30)
        addMove("Night Shade", "ghost", MoveCategory.SPECIAL, 60, 100, 15, "The user makes the target see a frightening mirage.")
        addMove("Shadow Ball", "ghost", MoveCategory.SPECIAL, 80, 100, 15, "The user hurls a shadowy blob at the target.", MoveEffect.SP_DEFENSE_DOWN, 20)
        
        // Dragon Type Moves
        addMove("Dragon Rage", "dragon", MoveCategory.SPECIAL, 80, 100, 10, "This attack hits the target with a shock wave of pure rage.")
        addMove("Dragon Claw", "dragon", MoveCategory.PHYSICAL, 80, 100, 15, "The user slashes the target with huge, sharp claws.")
        addMove("Dragon Breath", "dragon", MoveCategory.SPECIAL, 60, 100, 20, "The user exhales a mighty gust that inflicts damage.", MoveEffect.PARALYZE, 30)
        
        // Steel Type Moves
        addMove("Metal Claw", "steel", MoveCategory.PHYSICAL, 50, 95, 35, "The target is raked with steel claws.", MoveEffect.ATTACK_UP, 10)
        addMove("Iron Tail", "steel", MoveCategory.PHYSICAL, 100, 75, 15, "The target is slammed with a steel-hard tail.", MoveEffect.DEFENSE_DOWN, 30)
        
        // Dark Type Moves
        addMove("Bite", "dark", MoveCategory.PHYSICAL, 60, 100, 25, "The target is bitten with viciously sharp fangs.", MoveEffect.FLINCH, 30, isBite = true)
        addMove("Crunch", "dark", MoveCategory.PHYSICAL, 80, 100, 15, "The user crunches up the target with sharp fangs.", MoveEffect.DEFENSE_DOWN, 20, isBite = true)
        
        // Fairy Type Moves
        addMove("Fairy Wind", "fairy", MoveCategory.SPECIAL, 40, 100, 30, "The user stirs up a fairy wind and strikes the target with it.")
        addMove("Moonblast", "fairy", MoveCategory.SPECIAL, 95, 100, 15, "Borrowing the power of the moon, the user attacks the target.", MoveEffect.SP_ATTACK_DOWN, 30)
        
        // Status Moves
        addMove("Growl", "normal", MoveCategory.STATUS, 0, 100, 40, "The user growls in an endearing way.", MoveEffect.ATTACK_DOWN, 100)
        addMove("Tail Whip", "normal", MoveCategory.STATUS, 0, 100, 30, "The user wags its tail cutely.", MoveEffect.DEFENSE_DOWN, 100)
        addMove("Harden", "normal", MoveCategory.STATUS, 0, 100, 30, "The user stiffens all the muscles in its body.", MoveEffect.DEFENSE_UP, 100)
        addMove("Swords Dance", "normal", MoveCategory.STATUS, 0, 100, 20, "A frenetic dance to uplift the fighting spirit.", MoveEffect.ATTACK_UP, 100)
        addMove("Agility", "psychic", MoveCategory.STATUS, 0, 100, 30, "The user relaxes and lightens its body.", MoveEffect.SPEED_UP, 100)
        addMove("Recover", "normal", MoveCategory.STATUS, 0, 100, 5, "Restoring its own cells, the user restores its own HP.", MoveEffect.HEAL, 100)
    }
    
    private fun addMove(
        name: String, 
        type: String, 
        category: MoveCategory, 
        power: Int, 
        accuracy: Int, 
        pp: Int, 
        description: String,
        effect: MoveEffect? = null,
        effectChance: Int = 0,
        priority: Int = 0,
        criticalHitRate: Int = 1,
        makesContact: Boolean = category == MoveCategory.PHYSICAL,
        isSound: Boolean = false,
        isPunch: Boolean = false,
        isBite: Boolean = false
    ) {
        val move = Move(
            name = name,
            type = type,
            category = category,
            power = power,
            accuracy = accuracy,
            pp = pp,
            description = description,
            effect = effect,
            effectChance = effectChance,
            priority = priority,
            criticalHitRate = criticalHitRate,
            makesContact = makesContact,
            isSound = isSound,
            isPunch = isPunch,
            isBite = isBite
        )
        movesMap[name.lowercase()] = move
    }
}
