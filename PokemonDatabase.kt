// Enhanced Pokemon data with comprehensive stats and movesets
import kotlin.random.Random

// Enhanced Pokemon data class with all stats from fetched data
data class Pokemon(
    val name: String,
    val pokedexNumber: Int,
    val primaryType: String,
    val secondaryType: String? = null,
    var level: Int = 1,
    var currentHp: Int,
    val baseHp: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseSpecialAttack: Int,
    val baseSpecialDefense: Int,
    val baseSpeed: Int,
    val moves: MutableList<Move>,
    var isShiny: Boolean = false,
    var statusCondition: StatusCondition? = null,
    var statChanges: StatChanges = StatChanges(),
    var experience: Int = 0
) {
    val maxHp: Int
        get() = calculateStat(baseHp, level, isHpStat = true)
    
    val attack: Int
        get() = applyStat(calculateStat(baseAttack, level), statChanges.attack)
    
    val defense: Int
        get() = applyStat(calculateStat(baseDefense, level), statChanges.defense)
    
    val specialAttack: Int
        get() = applyStat(calculateStat(baseSpecialAttack, level), statChanges.specialAttack)
    
    val specialDefense: Int
        get() = applyStat(calculateStat(baseSpecialDefense, level), statChanges.specialDefense)
    
    val speed: Int
        get() = applyStat(calculateStat(baseSpeed, level), statChanges.speed)
    
    val totalStats: Int
        get() = baseHp + baseAttack + baseDefense + baseSpecialAttack + baseSpecialDefense + baseSpeed
    
    fun getType(): String = if (secondaryType != null) "$primaryType/$secondaryType" else primaryType
    
    fun heal(amount: Int) {
        currentHp = minOf(maxHp, currentHp + amount)
    }
    
    fun takeDamage(damage: Int) {
        currentHp = maxOf(0, currentHp - damage)
    }
    
    fun isFainted(): Boolean = currentHp <= 0
    
    fun fullHeal() {
        currentHp = maxHp
        statusCondition = null
        statChanges = StatChanges()
        moves.forEach { it.restorePp() }
    }
    
    fun learnMove(move: Move): Boolean {
        if (moves.size >= 4) return false
        moves.add(move)
        return true
    }
    
    fun replaceMove(oldMoveIndex: Int, newMove: Move): Boolean {
        if (oldMoveIndex !in 0 until moves.size) return false
        moves[oldMoveIndex] = newMove
        return true
    }
    
    private fun calculateStat(baseStat: Int, level: Int, isHpStat: Boolean = false): Int {
        return if (isHpStat) {
            ((2 * baseStat + 31) * level / 100) + level + 10
        } else {
            ((2 * baseStat + 31) * level / 100) + 5
        }
    }
    
    private fun applyStat(baseStat: Int, statChange: Int): Int {
        val multiplier = when (statChange) {
            -6 -> 0.25
            -5 -> 0.28
            -4 -> 0.33
            -3 -> 0.4
            -2 -> 0.5
            -1 -> 0.66
            0 -> 1.0
            1 -> 1.5
            2 -> 2.0
            3 -> 2.5
            4 -> 3.0
            5 -> 3.5
            6 -> 4.0
            else -> 1.0
        }
        return (baseStat * multiplier).toInt()
    }
}

data class StatChanges(
    var attack: Int = 0,
    var defense: Int = 0,
    var specialAttack: Int = 0,
    var specialDefense: Int = 0,
    var speed: Int = 0,
    var accuracy: Int = 0,
    var evasion: Int = 0
) {
    fun reset() {
        attack = 0
        defense = 0
        specialAttack = 0
        specialDefense = 0
        speed = 0
        accuracy = 0
        evasion = 0
    }
    
    fun changeStat(stat: MoveEffect, amount: Int = 1): Boolean {
        return when (stat) {
            MoveEffect.ATTACK_UP -> { attack = minOf(6, attack + amount); true }
            MoveEffect.ATTACK_DOWN -> { attack = maxOf(-6, attack - amount); true }
            MoveEffect.DEFENSE_UP -> { defense = minOf(6, defense + amount); true }
            MoveEffect.DEFENSE_DOWN -> { defense = maxOf(-6, defense - amount); true }
            MoveEffect.SP_ATTACK_UP -> { specialAttack = minOf(6, specialAttack + amount); true }
            MoveEffect.SP_ATTACK_DOWN -> { specialAttack = maxOf(-6, specialAttack - amount); true }
            MoveEffect.SP_DEFENSE_UP -> { specialDefense = minOf(6, specialDefense + amount); true }
            MoveEffect.SP_DEFENSE_DOWN -> { specialDefense = maxOf(-6, specialDefense - amount); true }
            MoveEffect.SPEED_UP -> { speed = minOf(6, speed + amount); true }
            MoveEffect.SPEED_DOWN -> { speed = maxOf(-6, speed - amount); true }
            MoveEffect.ACCURACY_UP -> { accuracy = minOf(6, accuracy + amount); true }
            MoveEffect.ACCURACY_DOWN -> { accuracy = maxOf(-6, accuracy - amount); true }
            MoveEffect.EVASION_UP -> { evasion = minOf(6, evasion + amount); true }
            MoveEffect.EVASION_DOWN -> { evasion = maxOf(-6, evasion - amount); true }
            else -> false
        }
    }
}

enum class StatusCondition(val displayName: String) {
    BURN("Burned"),
    FREEZE("Frozen"),
    PARALYZE("Paralyzed"),
    POISON("Poisoned"),
    BADLY_POISON("Badly Poisoned"),
    SLEEP("Asleep"),
    CONFUSION("Confused");
    
    fun canAct(): Boolean = this != FREEZE && this != SLEEP
    
    fun getSpeedMultiplier(): Double = if (this == PARALYZE) 0.25 else 1.0
    
    fun getAttackMultiplier(): Double = if (this == BURN) 0.5 else 1.0
}

// Comprehensive Pokemon database based on fetched data
object PokemonDatabase {
    private val pokemonData = mutableMapOf<Int, PokemonSpecies>()
    
    init {
        initializePokemon()
    }
    
    fun getPokemon(pokedexNumber: Int): PokemonSpecies? = pokemonData[pokedexNumber]
    
    fun getPokemon(name: String): PokemonSpecies? = 
        pokemonData.values.find { it.name.equals(name, ignoreCase = true) }
    
    fun getAllPokemon(): List<PokemonSpecies> = pokemonData.values.toList()
    
    fun createPokemon(pokedexNumber: Int, level: Int = 1): Pokemon? {
        val species = getPokemon(pokedexNumber) ?: return null
        val moves = species.learnableMoves.shuffled().take(4).mapNotNull { 
            MoveDatabase.getMove(it) 
        }.toMutableList()
        
        return Pokemon(
            name = species.name,
            pokedexNumber = species.pokedexNumber,
            primaryType = species.primaryType,
            secondaryType = species.secondaryType,
            level = level,
            currentHp = 0, // Will be set after calculation
            baseHp = species.baseHp,
            baseAttack = species.baseAttack,
            baseDefense = species.baseDefense,
            baseSpecialAttack = species.baseSpecialAttack,
            baseSpecialDefense = species.baseSpecialDefense,
            baseSpeed = species.baseSpeed,
            moves = moves
        ).apply {
            currentHp = maxHp
        }
    }
    
    private fun initializePokemon() {
        // GENERATION 1 - KANTO (Pokemon #1-151)
        // Grass Starters
        addPokemon(1, "Bulbasaur", "grass", "poison", 45, 49, 49, 65, 65, 45, 
            listOf("tackle", "vine whip", "razor leaf", "solar beam", "sleep powder", "poison powder"))
        addPokemon(2, "Ivysaur", "grass", "poison", 60, 62, 63, 80, 80, 60,
            listOf("tackle", "vine whip", "razor leaf", "solar beam", "sleep powder", "poison powder"))
        addPokemon(3, "Venusaur", "grass", "poison", 80, 82, 83, 100, 100, 80,
            listOf("tackle", "vine whip", "razor leaf", "solar beam", "sleep powder", "poison powder", "earthquake"))
        
        // Fire Starters
        addPokemon(4, "Charmander", "fire", null, 39, 52, 43, 60, 50, 65,
            listOf("scratch", "ember", "flamethrower", "fire blast", "dragon rage", "dragon claw"))
        addPokemon(5, "Charmeleon", "fire", null, 58, 64, 58, 80, 65, 80,
            listOf("scratch", "ember", "flamethrower", "fire blast", "dragon rage", "dragon claw"))
        addPokemon(6, "Charizard", "fire", "flying", 78, 84, 78, 109, 85, 100,
            listOf("scratch", "ember", "flamethrower", "fire blast", "dragon rage", "dragon claw", "wing attack", "fly"))
        
        // Water Starters
        addPokemon(7, "Squirtle", "water", null, 44, 48, 65, 50, 64, 43,
            listOf("tackle", "water gun", "bubble beam", "surf", "hydro pump", "water pulse"))
        addPokemon(8, "Wartortle", "water", null, 59, 63, 80, 65, 80, 58,
            listOf("tackle", "water gun", "bubble beam", "surf", "hydro pump", "water pulse"))
        addPokemon(9, "Blastoise", "water", null, 79, 83, 100, 85, 105, 78,
            listOf("tackle", "water gun", "bubble beam", "surf", "hydro pump", "water pulse", "earthquake"))
        
        // Early Route Pokemon
        addPokemon(10, "Caterpie", "bug", null, 45, 30, 35, 20, 20, 45,
            listOf("tackle", "string shot"))
        addPokemon(11, "Metapod", "bug", null, 50, 20, 55, 25, 25, 30,
            listOf("tackle", "harden"))
        addPokemon(12, "Butterfree", "bug", "flying", 60, 45, 50, 90, 80, 70,
            listOf("confusion", "gust", "psybeam", "sleep powder"))
        addPokemon(13, "Weedle", "bug", "poison", 40, 35, 30, 20, 20, 50,
            listOf("poison sting", "string shot"))
        addPokemon(14, "Kakuna", "bug", "poison", 45, 25, 50, 25, 25, 35,
            listOf("harden"))
        addPokemon(15, "Beedrill", "bug", "poison", 65, 90, 40, 45, 80, 75,
            listOf("poison sting", "pin missile", "twineedle", "agility"))
        addPokemon(16, "Pidgey", "normal", "flying", 40, 45, 40, 35, 35, 56,
            listOf("tackle", "gust", "wing attack", "quick attack"))
        addPokemon(17, "Pidgeotto", "normal", "flying", 63, 60, 55, 50, 50, 71,
            listOf("tackle", "gust", "wing attack", "quick attack", "agility"))
        addPokemon(18, "Pidgeot", "normal", "flying", 83, 80, 75, 70, 70, 101,
            listOf("tackle", "gust", "wing attack", "quick attack", "agility", "hurricane"))
        addPokemon(19, "Rattata", "normal", null, 30, 56, 35, 25, 35, 72,
            listOf("tackle", "quick attack", "bite", "hyper fang"))
        addPokemon(20, "Raticate", "normal", null, 55, 81, 60, 50, 70, 97,
            listOf("tackle", "quick attack", "bite", "hyper fang", "super fang"))
        
        // Pikachu Line
        addPokemon(25, "Pikachu", "electric", null, 35, 55, 40, 50, 50, 90,
            listOf("thunder shock", "thunderbolt", "thunder", "thunder wave", "quick attack", "agility"))
        addPokemon(26, "Raichu", "electric", null, 60, 90, 55, 90, 80, 110,
            listOf("thunder shock", "thunderbolt", "thunder", "thunder wave", "quick attack", "agility", "thunder punch"))
        
        // Rock/Ground Pokemon
        addPokemon(74, "Geodude", "rock", "ground", 40, 80, 100, 30, 30, 20,
            listOf("tackle", "rock throw", "earthquake", "rock slide"))
        addPokemon(75, "Graveler", "rock", "ground", 55, 95, 115, 45, 45, 35,
            listOf("tackle", "rock throw", "earthquake", "rock slide", "self-destruct"))
        addPokemon(76, "Golem", "rock", "ground", 80, 120, 130, 55, 65, 45,
            listOf("tackle", "rock throw", "earthquake", "rock slide", "explosion"))
        
        // Fighting Pokemon
        addPokemon(56, "Mankey", "fighting", null, 40, 80, 35, 35, 45, 70,
            listOf("scratch", "karate chop", "low kick", "seismic toss"))
        addPokemon(57, "Primeape", "fighting", null, 65, 105, 60, 60, 70, 95,
            listOf("scratch", "karate chop", "low kick", "seismic toss", "thrash"))
        addPokemon(68, "Machamp", "fighting", null, 90, 130, 80, 65, 85, 55,
            listOf("karate chop", "low kick", "seismic toss", "submission"))
        
        // Psychic Pokemon
        addPokemon(63, "Abra", "psychic", null, 25, 20, 15, 105, 55, 90,
            listOf("teleport"))
        addPokemon(64, "Kadabra", "psychic", null, 40, 35, 30, 120, 70, 105,
            listOf("confusion", "psybeam", "psychic", "teleport"))
        addPokemon(65, "Alakazam", "psychic", null, 55, 50, 45, 135, 95, 120,
            listOf("confusion", "psybeam", "psychic", "teleport", "recover"))
        
        // Water Pokemon
        addPokemon(129, "Magikarp", "water", null, 20, 10, 55, 15, 20, 80,
            listOf("splash", "tackle", "flail"))
        addPokemon(130, "Gyarados", "water", "flying", 95, 125, 79, 60, 100, 81,
            listOf("bite", "hydro pump", "dragon rage", "thrash", "hyper beam"))
        addPokemon(131, "Lapras", "water", "ice", 130, 85, 80, 85, 95, 60,
            listOf("water gun", "ice beam", "surf", "psychic"))
        
        // Eeveelutions
        addPokemon(133, "Eevee", "normal", null, 55, 55, 50, 45, 65, 55,
            listOf("tackle", "tail whip", "swift", "take down"))
        addPokemon(134, "Vaporeon", "water", null, 130, 65, 60, 110, 95, 65,
            listOf("water gun", "aurora beam", "hydro pump", "acid armor"))
        addPokemon(135, "Jolteon", "electric", null, 65, 65, 60, 110, 95, 130,
            listOf("thunder shock", "thunderbolt", "pin missile", "agility"))
        addPokemon(136, "Flareon", "fire", null, 65, 130, 60, 95, 110, 65,
            listOf("ember", "flamethrower", "fire blast", "smog"))
        
        // Fossil Pokemon
        addPokemon(138, "Omanyte", "rock", "water", 35, 40, 100, 90, 55, 35,
            listOf("water gun", "withdraw", "horn attack", "hydro pump"))
        addPokemon(139, "Omastar", "rock", "water", 70, 60, 125, 115, 70, 55,
            listOf("water gun", "withdraw", "horn attack", "hydro pump", "spike cannon"))
        addPokemon(140, "Kabuto", "rock", "water", 30, 80, 90, 55, 45, 55,
            listOf("scratch", "harden", "absorb", "slash"))
        addPokemon(141, "Kabutops", "rock", "water", 60, 115, 105, 65, 70, 80,
            listOf("scratch", "harden", "absorb", "slash", "fury cutter"))
        addPokemon(142, "Aerodactyl", "rock", "flying", 80, 105, 65, 60, 75, 130,
            listOf("wing attack", "bite", "supersonic", "hyper beam"))
        
        // Legendary Pokemon
        addPokemon(143, "Snorlax", "normal", null, 160, 110, 65, 65, 110, 30,
            listOf("tackle", "body slam", "rest", "snore", "hyper beam"))
        addPokemon(144, "Articuno", "ice", "flying", 90, 85, 100, 95, 125, 85,
            listOf("peck", "ice beam", "blizzard", "agility"))
        addPokemon(145, "Zapdos", "electric", "flying", 90, 90, 85, 125, 90, 100,
            listOf("peck", "thunderbolt", "thunder", "agility"))
        addPokemon(146, "Moltres", "fire", "flying", 90, 100, 90, 125, 85, 90,
            listOf("peck", "flamethrower", "fire blast", "agility"))
        addPokemon(149, "Dragonite", "dragon", "flying", 91, 134, 95, 100, 100, 80,
            listOf("wing attack", "dragon rage", "hyper beam", "outrage"))
        addPokemon(150, "Mewtwo", "psychic", null, 106, 110, 90, 154, 90, 130,
            listOf("confusion", "psychic", "psybeam", "shadow ball", "thunderbolt", "ice beam"))
        addPokemon(151, "Mew", "psychic", null, 100, 100, 100, 100, 100, 100,
            listOf("confusion", "psychic", "transform", "metronome", "pound"))
        
        // GENERATION 2 - JOHTO (Pokemon #152-251)
        // Grass Starters
        addPokemon(152, "Chikorita", "grass", null, 45, 49, 65, 49, 65, 45,
            listOf("tackle", "razor leaf", "reflect", "solar beam"))
        addPokemon(153, "Bayleef", "grass", null, 60, 62, 80, 63, 80, 60,
            listOf("tackle", "razor leaf", "reflect", "solar beam", "body slam"))
        addPokemon(154, "Meganium", "grass", null, 80, 82, 100, 83, 100, 80,
            listOf("tackle", "razor leaf", "reflect", "solar beam", "body slam", "earthquake"))
        
        // Fire Starters
        addPokemon(155, "Cyndaquil", "fire", null, 39, 52, 43, 60, 50, 65,
            listOf("tackle", "ember", "flame wheel", "swift"))
        addPokemon(156, "Quilava", "fire", null, 58, 64, 58, 80, 65, 80,
            listOf("tackle", "ember", "flame wheel", "swift", "flamethrower"))
        addPokemon(157, "Typhlosion", "fire", null, 78, 84, 78, 109, 85, 100,
            listOf("tackle", "ember", "flame wheel", "swift", "flamethrower", "fire blast"))
        
        // Water Starters
        addPokemon(158, "Totodile", "water", null, 50, 65, 64, 44, 48, 43,
            listOf("scratch", "water gun", "bite", "scary face"))
        addPokemon(159, "Croconaw", "water", null, 65, 80, 80, 59, 63, 58,
            listOf("scratch", "water gun", "bite", "scary face", "slash"))
        addPokemon(160, "Feraligatr", "water", null, 85, 105, 100, 79, 83, 78,
            listOf("scratch", "water gun", "bite", "scary face", "slash", "hydro pump"))
        
        // New Types
        addPokemon(179, "Mareep", "electric", null, 55, 40, 40, 65, 45, 35,
            listOf("tackle", "thunder shock", "thunder wave", "take down"))
        addPokemon(180, "Flaaffy", "electric", null, 70, 55, 55, 80, 60, 45,
            listOf("tackle", "thunder shock", "thunder wave", "take down", "thunderbolt"))
        addPokemon(181, "Ampharos", "electric", null, 90, 75, 85, 115, 90, 55,
            listOf("tackle", "thunder shock", "thunder wave", "take down", "thunderbolt", "thunder"))
        
        // Dark Type Introduction
        addPokemon(197, "Umbreon", "dark", null, 95, 65, 110, 60, 130, 65,
            listOf("tackle", "sand attack", "moonlight", "mean look"))
        addPokemon(198, "Murkrow", "dark", "flying", 60, 85, 42, 85, 42, 91,
            listOf("peck", "pursuit", "night shade", "faint attack"))
        
        // Steel Type Introduction
        addPokemon(205, "Forretress", "bug", "steel", 75, 90, 140, 60, 60, 40,
            listOf("tackle", "protect", "spikes", "explosion"))
        addPokemon(208, "Steelix", "steel", "ground", 75, 85, 200, 55, 65, 30,
            listOf("tackle", "rock throw", "harden", "earthquake"))
        addPokemon(227, "Skarmory", "steel", "flying", 65, 80, 140, 40, 70, 70,
            listOf("peck", "leer", "steel wing", "agility"))
        
        // Legendary Beasts
        addPokemon(243, "Raikou", "electric", null, 90, 85, 75, 115, 100, 115,
            listOf("bite", "leer", "thunderbolt", "roar"))
        addPokemon(244, "Entei", "fire", null, 115, 115, 85, 90, 75, 100,
            listOf("bite", "leer", "ember", "roar"))
        addPokemon(245, "Suicune", "water", null, 100, 75, 115, 90, 115, 85,
            listOf("bite", "leer", "bubble beam", "roar"))
        
        // Legendary Birds
        addPokemon(249, "Lugia", "psychic", "flying", 106, 90, 130, 90, 154, 110,
            listOf("aeroblast", "safeguard", "recover", "hydro pump"))
        addPokemon(250, "Ho-Oh", "fire", "flying", 106, 130, 90, 110, 154, 90,
            listOf("sacred fire", "safeguard", "recover", "fire blast"))
        addPokemon(251, "Celebi", "psychic", "grass", 100, 100, 100, 100, 100, 100,
            listOf("confusion", "leech seed", "recover", "perish song"))
        
        // GENERATION 3 - HOENN (Pokemon #252-386)
        // Grass Starters
        addPokemon(252, "Treecko", "grass", null, 40, 45, 35, 65, 55, 70,
            listOf("pound", "leer", "absorb", "pursuit"))
        addPokemon(253, "Grovyle", "grass", null, 50, 65, 45, 85, 65, 95,
            listOf("pound", "leer", "absorb", "pursuit", "leaf blade"))
        addPokemon(254, "Sceptile", "grass", null, 70, 85, 65, 105, 85, 120,
            listOf("pound", "leer", "absorb", "pursuit", "leaf blade", "dragon claw"))
        
        // Fire Starters
        addPokemon(255, "Torchic", "fire", null, 45, 60, 40, 70, 50, 45,
            listOf("scratch", "growl", "ember", "peck"))
        addPokemon(256, "Combusken", "fire", "fighting", 60, 85, 60, 85, 60, 55,
            listOf("scratch", "growl", "ember", "peck", "double kick"))
        addPokemon(257, "Blaziken", "fire", "fighting", 80, 120, 70, 110, 70, 80,
            listOf("scratch", "growl", "ember", "peck", "double kick", "blaze kick"))
        
        // Water Starters
        addPokemon(258, "Mudkip", "water", null, 50, 70, 50, 50, 50, 40,
            listOf("tackle", "growl", "water gun", "mud slap"))
        addPokemon(259, "Marshtomp", "water", "ground", 70, 85, 70, 60, 70, 50,
            listOf("tackle", "growl", "water gun", "mud slap", "rock slide"))
        addPokemon(260, "Swampert", "water", "ground", 100, 110, 90, 85, 90, 60,
            listOf("tackle", "growl", "water gun", "mud slap", "rock slide", "earthquake"))
        
        // Legendary Weather Trio
        addPokemon(382, "Kyogre", "water", null, 100, 100, 90, 150, 140, 90,
            listOf("water pulse", "scary face", "ice beam", "hydro pump"))
        addPokemon(383, "Groudon", "ground", null, 100, 150, 140, 100, 90, 90,
            listOf("mud shot", "scary face", "earth power", "earthquake"))
        addPokemon(384, "Rayquaza", "dragon", "flying", 105, 150, 90, 150, 90, 95,
            listOf("twister", "scary face", "extreme speed", "outrage"))
        
        // GENERATION 4 - SINNOH (Pokemon #387-493)
        // Grass Starters
        addPokemon(387, "Turtwig", "grass", null, 55, 68, 64, 45, 55, 31,
            listOf("tackle", "withdraw", "absorb", "razor leaf"))
        addPokemon(388, "Grotle", "grass", null, 75, 89, 85, 55, 65, 36,
            listOf("tackle", "withdraw", "absorb", "razor leaf", "bite"))
        addPokemon(389, "Torterra", "grass", "ground", 95, 109, 105, 75, 85, 56,
            listOf("tackle", "withdraw", "absorb", "razor leaf", "bite", "earthquake"))
        
        // Fire Starters
        addPokemon(390, "Chimchar", "fire", null, 44, 58, 44, 58, 44, 61,
            listOf("scratch", "leer", "ember", "taunt"))
        addPokemon(391, "Monferno", "fire", "fighting", 64, 78, 52, 78, 52, 81,
            listOf("scratch", "leer", "ember", "taunt", "mach punch"))
        addPokemon(392, "Infernape", "fire", "fighting", 76, 104, 71, 104, 71, 108,
            listOf("scratch", "leer", "ember", "taunt", "mach punch", "flare blitz"))
        
        // Water Starters
        addPokemon(393, "Piplup", "water", null, 53, 51, 53, 61, 56, 40,
            listOf("pound", "growl", "bubble", "water sport"))
        addPokemon(394, "Prinplup", "water", null, 64, 66, 68, 81, 76, 50,
            listOf("pound", "growl", "bubble", "water sport", "metal claw"))
        addPokemon(395, "Empoleon", "water", "steel", 84, 86, 88, 111, 101, 60,
            listOf("pound", "growl", "bubble", "water sport", "metal claw", "hydro pump"))
        
        // Legendary Creation Trio
        addPokemon(483, "Dialga", "steel", "dragon", 100, 120, 120, 150, 100, 90,
            listOf("dragon breath", "scary face", "metal claw", "roar of time"))
        addPokemon(484, "Palkia", "water", "dragon", 90, 120, 100, 150, 120, 100,
            listOf("dragon breath", "scary face", "water pulse", "spacial rend"))
        addPokemon(487, "Giratina", "ghost", "dragon", 150, 100, 120, 100, 120, 90,
            listOf("dragon breath", "scary face", "shadow force", "shadow ball"))
        addPokemon(493, "Arceus", "normal", null, 120, 120, 120, 120, 120, 120,
            listOf("tackle", "judgment", "recover", "hyper beam"))
        
        // GENERATION 5 - UNOVA (Pokemon #494-649)
        // Grass Starters
        addPokemon(495, "Snivy", "grass", null, 45, 45, 55, 45, 55, 63,
            listOf("tackle", "leer", "vine whip", "wrap"))
        addPokemon(496, "Servine", "grass", null, 60, 60, 75, 60, 75, 83,
            listOf("tackle", "leer", "vine whip", "wrap", "slam"))
        addPokemon(497, "Serperior", "grass", null, 75, 75, 95, 75, 95, 113,
            listOf("tackle", "leer", "vine whip", "wrap", "slam", "leaf storm"))
        
        // Fire Starters
        addPokemon(498, "Tepig", "fire", null, 65, 63, 45, 45, 45, 45,
            listOf("tackle", "tail whip", "ember", "odor sleuth"))
        addPokemon(499, "Pignite", "fire", "fighting", 90, 93, 55, 70, 55, 55,
            listOf("tackle", "tail whip", "ember", "odor sleuth", "arm thrust"))
        addPokemon(500, "Emboar", "fire", "fighting", 110, 123, 65, 100, 65, 65,
            listOf("tackle", "tail whip", "ember", "odor sleuth", "arm thrust", "flare blitz"))
        
        // Water Starters
        addPokemon(501, "Oshawott", "water", null, 55, 55, 45, 63, 45, 45,
            listOf("tackle", "tail whip", "water gun", "water sport"))
        addPokemon(502, "Dewott", "water", null, 75, 75, 60, 83, 60, 60,
            listOf("tackle", "tail whip", "water gun", "water sport", "fury cutter"))
        addPokemon(503, "Samurott", "water", null, 95, 100, 85, 108, 70, 70,
            listOf("tackle", "tail whip", "water gun", "water sport", "fury cutter", "hydro pump"))
        
        // Legendary Dragon
        addPokemon(643, "Reshiram", "dragon", "fire", 100, 120, 100, 150, 120, 90,
            listOf("dragon breath", "noble roar", "flamethrower", "blue flare"))
        addPokemon(644, "Zekrom", "dragon", "electric", 100, 150, 120, 120, 100, 90,
            listOf("dragon breath", "noble roar", "thunderbolt", "bolt strike"))
        addPokemon(646, "Kyurem", "dragon", "ice", 125, 130, 90, 130, 90, 95,
            listOf("dragon breath", "noble roar", "ice beam", "glaciate"))
        
        // GENERATION 6 - KALOS (Pokemon #650-721)
        // Grass Starters
        addPokemon(650, "Chespin", "grass", null, 56, 61, 65, 48, 45, 38,
            listOf("tackle", "growl", "vine whip", "rollout"))
        addPokemon(651, "Quilladin", "grass", null, 61, 78, 95, 56, 58, 57,
            listOf("tackle", "growl", "vine whip", "rollout", "bite"))
        addPokemon(652, "Chesnaught", "grass", "fighting", 88, 107, 122, 74, 75, 64,
            listOf("tackle", "growl", "vine whip", "rollout", "bite", "spiky shield"))
        
        // Fire Starters
        addPokemon(653, "Fennekin", "fire", null, 40, 45, 40, 62, 60, 60,
            listOf("scratch", "tail whip", "ember", "howl"))
        addPokemon(654, "Braixen", "fire", null, 59, 59, 58, 90, 70, 73,
            listOf("scratch", "tail whip", "ember", "howl", "psybeam"))
        addPokemon(655, "Delphox", "fire", "psychic", 75, 69, 72, 114, 100, 104,
            listOf("scratch", "tail whip", "ember", "howl", "psybeam", "mystical fire"))
        
        // Water Starters
        addPokemon(656, "Froakie", "water", null, 41, 56, 40, 62, 44, 71,
            listOf("pound", "growl", "bubble", "quick attack"))
        addPokemon(657, "Frogadier", "water", null, 54, 63, 52, 83, 56, 97,
            listOf("pound", "growl", "bubble", "quick attack", "water pulse"))
        addPokemon(658, "Greninja", "water", "dark", 72, 95, 67, 103, 71, 122,
            listOf("pound", "growl", "bubble", "quick attack", "water pulse", "water shuriken"))
        
        // Legendary Life/Death
        addPokemon(716, "Xerneas", "fairy", null, 126, 131, 95, 131, 98, 99,
            listOf("tackle", "gravity", "geomancy", "moonblast"))
        addPokemon(717, "Yveltal", "dark", "flying", 126, 131, 95, 131, 98, 99,
            listOf("tackle", "gravity", "oblivion wing", "dark pulse"))
        addPokemon(718, "Zygarde", "dragon", "ground", 108, 100, 121, 81, 95, 95,
            listOf("tackle", "glare", "land's wrath", "extreme speed"))
        
        // GENERATION 7 - ALOLA (Pokemon #722-809)
        // Grass Starters
        addPokemon(722, "Rowlet", "grass", "flying", 68, 55, 55, 50, 50, 42,
            listOf("tackle", "leafage", "astonish", "peck"))
        addPokemon(723, "Dartrix", "grass", "flying", 78, 75, 75, 70, 70, 52,
            listOf("tackle", "leafage", "astonish", "peck", "pluck"))
        addPokemon(724, "Decidueye", "grass", "ghost", 78, 107, 75, 100, 100, 70,
            listOf("tackle", "leafage", "astonish", "peck", "pluck", "spirit shackle"))
        
        // Fire Starters
        addPokemon(725, "Litten", "fire", null, 45, 65, 40, 60, 40, 70,
            listOf("scratch", "ember", "lick", "leer"))
        addPokemon(726, "Torracat", "fire", null, 65, 85, 50, 80, 50, 90,
            listOf("scratch", "ember", "lick", "leer", "bite"))
        addPokemon(727, "Incineroar", "fire", "dark", 95, 115, 90, 80, 90, 60,
            listOf("scratch", "ember", "lick", "leer", "bite", "darkest lariat"))
        
        // Water Starters
        addPokemon(728, "Popplio", "water", null, 50, 54, 54, 66, 56, 40,
            listOf("pound", "water gun", "growl", "disarming voice"))
        addPokemon(729, "Brionne", "water", null, 60, 69, 69, 91, 81, 50,
            listOf("pound", "water gun", "growl", "disarming voice", "bubble beam"))
        addPokemon(730, "Primarina", "water", "fairy", 80, 74, 74, 126, 116, 60,
            listOf("pound", "water gun", "growl", "disarming voice", "bubble beam", "sparkling aria"))
        
        // Ultra Necrozma
        addPokemon(800, "Necrozma", "psychic", null, 97, 107, 101, 127, 89, 79,
            listOf("moonlight", "psybeam", "confusion", "prismatic laser"))
        
        // GENERATION 8 - GALAR (Pokemon #810-898)
        // Grass Starters
        addPokemon(810, "Grookey", "grass", null, 50, 65, 50, 40, 40, 65,
            listOf("scratch", "growl", "branch poke", "taunt"))
        addPokemon(811, "Thwackey", "grass", null, 70, 85, 70, 55, 60, 80,
            listOf("scratch", "growl", "branch poke", "taunt", "razor leaf"))
        addPokemon(812, "Rillaboom", "grass", null, 100, 125, 90, 60, 70, 85,
            listOf("scratch", "growl", "branch poke", "taunt", "razor leaf", "drum beating"))
        
        // Fire Starters
        addPokemon(813, "Scorbunny", "fire", null, 50, 71, 40, 40, 40, 69,
            listOf("tackle", "growl", "ember", "quick attack"))
        addPokemon(814, "Raboot", "fire", null, 65, 86, 60, 55, 60, 94,
            listOf("tackle", "growl", "ember", "quick attack", "flame charge"))
        addPokemon(815, "Cinderace", "fire", null, 80, 116, 75, 65, 75, 119,
            listOf("tackle", "growl", "ember", "quick attack", "flame charge", "pyro ball"))
        
        // Water Starters
        addPokemon(816, "Sobble", "water", null, 50, 40, 40, 70, 40, 70,
            listOf("pound", "growl", "water gun", "bind"))
        addPokemon(817, "Drizzile", "water", null, 65, 60, 55, 95, 55, 90,
            listOf("pound", "growl", "water gun", "bind", "water pulse"))
        addPokemon(818, "Inteleon", "water", null, 70, 85, 65, 125, 65, 120,
            listOf("pound", "growl", "water gun", "bind", "water pulse", "snipe shot"))
        
        // Legendary Wolves
        addPokemon(888, "Zacian", "fairy", null, 92, 130, 115, 80, 115, 138,
            listOf("tackle", "quick guard", "bite", "behemoth blade"))
        addPokemon(889, "Zamazenta", "fighting", null, 92, 130, 115, 80, 115, 138,
            listOf("tackle", "quick guard", "bite", "behemoth bash"))
        addPokemon(890, "Eternatus", "poison", "dragon", 140, 85, 95, 145, 95, 130,
            listOf("poison sting", "dragon tail", "agility", "eternabeam"))
        
        // GENERATION 9 - PALDEA (Pokemon #899-1010)
        // Grass Starters
        addPokemon(899, "Sprigatito", "grass", null, 40, 61, 54, 45, 45, 65,
            listOf("scratch", "tail whip", "leafage", "hone claws"))
        addPokemon(900, "Floragato", "grass", null, 61, 80, 63, 60, 63, 83,
            listOf("scratch", "tail whip", "leafage", "hone claws", "bite"))
        addPokemon(901, "Meowscarada", "grass", "dark", 76, 110, 70, 81, 70, 123,
            listOf("scratch", "tail whip", "leafage", "hone claws", "bite", "flower trick"))
        
        // Fire Starters
        addPokemon(902, "Fuecoco", "fire", null, 67, 45, 59, 63, 40, 36,
            listOf("tackle", "leer", "ember", "bite"))
        addPokemon(903, "Crocalor", "fire", null, 81, 55, 78, 90, 58, 49,
            listOf("tackle", "leer", "ember", "bite", "flamethrower"))
        addPokemon(904, "Skeledirge", "fire", "ghost", 104, 75, 100, 110, 75, 66,
            listOf("tackle", "leer", "ember", "bite", "flamethrower", "torch song"))
        
        // Water Starters
        addPokemon(905, "Quaxly", "water", null, 55, 65, 45, 50, 45, 50,
            listOf("pound", "growl", "water gun", "quick attack"))
        addPokemon(906, "Quaxwell", "water", null, 70, 85, 65, 65, 60, 65,
            listOf("pound", "growl", "water gun", "quick attack", "water pulse"))
        addPokemon(907, "Quaquaval", "water", "fighting", 85, 120, 80, 85, 75, 85,
            listOf("pound", "growl", "water gun", "quick attack", "water pulse", "aqua step"))
        
        // Legendary Treasures
        addPokemon(1007, "Koraidon", "fighting", "dragon", 100, 135, 115, 85, 100, 135,
            listOf("tackle", "ancient power", "collision course", "flame charge"))
        addPokemon(1008, "Miraidon", "electric", "dragon", 100, 85, 100, 135, 115, 135,
            listOf("tackle", "charge", "electro drift", "agility"))
    }
    
    private fun addPokemon(
        pokedexNumber: Int,
        name: String,
        primaryType: String,
        secondaryType: String?,
        baseHp: Int,
        baseAttack: Int,
        baseDefense: Int,
        baseSpecialAttack: Int,
        baseSpecialDefense: Int,
        baseSpeed: Int,
        learnableMoves: List<String>
    ) {
        pokemonData[pokedexNumber] = PokemonSpecies(
            pokedexNumber = pokedexNumber,
            name = name,
            primaryType = primaryType,
            secondaryType = secondaryType,
            baseHp = baseHp,
            baseAttack = baseAttack,
            baseDefense = baseDefense,
            baseSpecialAttack = baseSpecialAttack,
            baseSpecialDefense = baseSpecialDefense,
            baseSpeed = baseSpeed,
            learnableMoves = learnableMoves
        )
    }
}

data class PokemonSpecies(
    val pokedexNumber: Int,
    val name: String,
    val primaryType: String,
    val secondaryType: String?,
    val baseHp: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseSpecialAttack: Int,
    val baseSpecialDefense: Int,
    val baseSpeed: Int,
    val learnableMoves: List<String>
) {
    val totalStats: Int
        get() = baseHp + baseAttack + baseDefense + baseSpecialAttack + baseSpecialDefense + baseSpeed
    
    fun getType(): String = if (secondaryType != null) "$primaryType/$secondaryType" else primaryType
}
