// Enhanced Move Tutor System - Based on all Pokemon games
import kotlin.random.Random

// Move Tutor Database with comprehensive moves from all generations
object MoveTutorDatabase {
    private val tutorMoves = mutableMapOf<String, TutorMove>()
    
    init {
        initializeTutorMoves()
    }
    
    fun getTutorMove(name: String): TutorMove? = tutorMoves[name.lowercase()]
    
    fun getAllTutorMoves(): List<TutorMove> = tutorMoves.values.toList()
    
    fun getTutorMovesByType(type: String): List<TutorMove> = 
        tutorMoves.values.filter { it.type.equals(type, ignoreCase = true) }
    
    fun getTutorMovesByCategory(category: MoveCategory): List<TutorMove> = 
        tutorMoves.values.filter { it.category == category }
    
    fun getAffordableMoves(coins: Int): List<TutorMove> = 
        tutorMoves.values.filter { it.cost <= coins }
    
    private fun initializeTutorMoves() {
        // PHYSICAL ATTACK MOVES
        addTutorMove("body slam", "normal", MoveCategory.PHYSICAL, 85, 100, 15, 300, 
            "A full-body charge attack that may cause paralysis.", MoveEffect.PARALYZE, 30)
        addTutorMove("double-edge", "normal", MoveCategory.PHYSICAL, 120, 100, 15, 400,
            "A reckless tackle that also hurts the user.", MoveEffect.DRAIN, 0, recoil = 33)
        addTutorMove("mega kick", "normal", MoveCategory.PHYSICAL, 120, 75, 5, 350,
            "An extremely powerful kick with intense force.")
        addTutorMove("mega punch", "normal", MoveCategory.PHYSICAL, 80, 85, 20, 250,
            "A punch thrown with incredible power.")
        addTutorMove("seismic toss", "fighting", MoveCategory.PHYSICAL, 1, 100, 20, 300,
            "Inflicts damage equal to the user's level.")
        
        // ELEMENTAL PUNCHES
        addTutorMove("fire punch", "fire", MoveCategory.PHYSICAL, 75, 100, 15, 400,
            "A fiery punch that may burn the foe.", MoveEffect.BURN, 10)
        addTutorMove("ice punch", "ice", MoveCategory.PHYSICAL, 75, 100, 15, 400,
            "An icy punch that may freeze the foe.", MoveEffect.FREEZE, 10)
        addTutorMove("thunder punch", "electric", MoveCategory.PHYSICAL, 75, 100, 15, 400,
            "An electrified punch that may paralyze the foe.", MoveEffect.PARALYZE, 10)
        
        // SPECIAL ATTACK MOVES
        addTutorMove("flamethrower", "fire", MoveCategory.SPECIAL, 90, 100, 15, 500,
            "A powerful fire attack that may burn the foe.", MoveEffect.BURN, 10)
        addTutorMove("ice beam", "ice", MoveCategory.SPECIAL, 90, 100, 10, 500,
            "An ice attack that may freeze the foe.", MoveEffect.FREEZE, 10)
        addTutorMove("thunderbolt", "electric", MoveCategory.SPECIAL, 90, 100, 15, 500,
            "A strong electric attack that may paralyze the foe.", MoveEffect.PARALYZE, 10)
        addTutorMove("psychic", "psychic", MoveCategory.SPECIAL, 90, 100, 10, 600,
            "A powerful psychic attack that may lower Sp. Def.", MoveEffect.SP_DEFENSE_DOWN, 10)
        
        // SIGNATURE MOVES
        addTutorMove("blast burn", "fire", MoveCategory.SPECIAL, 150, 90, 5, 1000,
            "The ultimate Fire-type attack. User must rest next turn.", starter = true)
        addTutorMove("hydro cannon", "water", MoveCategory.SPECIAL, 150, 90, 5, 1000,
            "The ultimate Water-type attack. User must rest next turn.", starter = true)
        addTutorMove("frenzy plant", "grass", MoveCategory.SPECIAL, 150, 90, 5, 1000,
            "The ultimate Grass-type attack. User must rest next turn.", starter = true)
        addTutorMove("draco meteor", "dragon", MoveCategory.SPECIAL, 130, 90, 5, 800,
            "The ultimate Dragon attack that lowers user's Sp. Atk.", MoveEffect.SP_ATTACK_DOWN, 100, dragonOnly = true)
        
        // PLEDGE MOVES
        addTutorMove("fire pledge", "fire", MoveCategory.SPECIAL, 80, 100, 10, 600,
            "A fire attack that can be combined with other pledges.", starter = true)
        addTutorMove("water pledge", "water", MoveCategory.SPECIAL, 80, 100, 10, 600,
            "A water attack that can be combined with other pledges.", starter = true)
        addTutorMove("grass pledge", "grass", MoveCategory.SPECIAL, 80, 100, 10, 600,
            "A grass attack that can be combined with other pledges.", starter = true)
        
        // PHYSICAL SWEEPING MOVES
        addTutorMove("earthquake", "ground", MoveCategory.PHYSICAL, 100, 100, 10, 700,
            "A powerful ground attack that hits all nearby Pokemon.")
        addTutorMove("rock slide", "rock", MoveCategory.PHYSICAL, 75, 90, 10, 350,
            "Large boulders that may make the foe flinch.", MoveEffect.FLINCH, 30)
        addTutorMove("iron head", "steel", MoveCategory.PHYSICAL, 80, 100, 15, 400,
            "A steel attack that may make the foe flinch.", MoveEffect.FLINCH, 30)
        addTutorMove("zen headbutt", "psychic", MoveCategory.PHYSICAL, 80, 90, 15, 400,
            "A psychic headbutt that may make the foe flinch.", MoveEffect.FLINCH, 20)
        
        // SPECIAL SWEEPING MOVES
        addTutorMove("heat wave", "fire", MoveCategory.SPECIAL, 95, 90, 10, 600,
            "Hot air that may burn the foe.", MoveEffect.BURN, 10)
        addTutorMove("earth power", "ground", MoveCategory.SPECIAL, 90, 100, 10, 550,
            "Makes the ground erupt with power. May lower Sp. Def.", MoveEffect.SP_DEFENSE_DOWN, 10)
        addTutorMove("dragon pulse", "dragon", MoveCategory.SPECIAL, 85, 100, 10, 500,
            "Generates a shock wave to damage the foe.")
        addTutorMove("dark pulse", "dark", MoveCategory.SPECIAL, 80, 100, 15, 450,
            "A horrible aura that may make the foe flinch.", MoveEffect.FLINCH, 20)
        addTutorMove("signal beam", "bug", MoveCategory.SPECIAL, 75, 100, 15, 400,
            "A strange beam that may confuse the foe.", MoveEffect.CONFUSION, 10)
        
        // PHYSICAL UTILITY MOVES
        addTutorMove("superpower", "fighting", MoveCategory.PHYSICAL, 120, 100, 5, 800,
            "Boosts Attack but lowers user's Attack and Defense.", MoveEffect.ATTACK_DOWN, 100)
        addTutorMove("outrage", "dragon", MoveCategory.PHYSICAL, 120, 100, 10, 700,
            "A rampage that lasts 2-3 turns but confuses the user.")
        addTutorMove("gunk shot", "poison", MoveCategory.PHYSICAL, 120, 80, 5, 600,
            "Filthy garbage that may poison the foe.", MoveEffect.POISON, 30)
        addTutorMove("seed bomb", "grass", MoveCategory.PHYSICAL, 80, 100, 15, 400,
            "A barrage of hard seeds.")
        addTutorMove("aqua tail", "water", MoveCategory.PHYSICAL, 90, 90, 10, 450,
            "Attacks with a tail like a wave.")
        addTutorMove("iron tail", "steel", MoveCategory.PHYSICAL, 100, 75, 15, 500,
            "Attacks with a steel-hard tail. May lower Defense.", MoveEffect.DEFENSE_DOWN, 30)
        
        // SPECIAL UTILITY MOVES
        addTutorMove("hyper voice", "normal", MoveCategory.SPECIAL, 90, 100, 10, 500,
            "A loud attack using sound waves.")
        addTutorMove("icy wind", "ice", MoveCategory.SPECIAL, 55, 95, 15, 300,
            "A chilling attack that lowers the foe's Speed.", MoveEffect.SPEED_DOWN, 100)
        addTutorMove("shock wave", "electric", MoveCategory.SPECIAL, 60, 999, 20, 300,
            "An electric attack that never misses.")
        addTutorMove("water pulse", "water", MoveCategory.SPECIAL, 60, 100, 20, 300,
            "An attack with ultrasonic waves that may confuse.", MoveEffect.CONFUSION, 20)
        
        // STATUS MOVES
        addTutorMove("thunder wave", "electric", MoveCategory.STATUS, 0, 90, 20, 200,
            "Paralyzes the foe with electricity.", MoveEffect.PARALYZE, 100)
        addTutorMove("toxic", "poison", MoveCategory.STATUS, 0, 90, 10, 250,
            "Badly poisons the foe. Damage increases each turn.", MoveEffect.BADLY_POISON, 100)
        addTutorMove("will-o-wisp", "fire", MoveCategory.STATUS, 0, 85, 15, 200,
            "Burns the foe with ghostly flames.", MoveEffect.BURN, 100)
        addTutorMove("sleep powder", "grass", MoveCategory.STATUS, 0, 75, 15, 200,
            "Scatters powder that may make the foe sleep.", MoveEffect.SLEEP, 100)
        
        // STAT BOOST MOVES
        addTutorMove("swords dance", "normal", MoveCategory.STATUS, 0, 999, 20, 300,
            "Raises user's Attack sharply.", MoveEffect.ATTACK_UP, 100)
        addTutorMove("calm mind", "psychic", MoveCategory.STATUS, 0, 999, 20, 300,
            "Raises user's Sp. Atk and Sp. Def.", MoveEffect.SP_ATTACK_UP, 100)
        addTutorMove("dragon dance", "dragon", MoveCategory.STATUS, 0, 999, 20, 400,
            "Raises user's Attack and Speed.", MoveEffect.ATTACK_UP, 100)
        addTutorMove("agility", "psychic", MoveCategory.STATUS, 0, 999, 30, 250,
            "Sharply raises user's Speed.", MoveEffect.SPEED_UP, 100)
        addTutorMove("iron defense", "steel", MoveCategory.STATUS, 0, 999, 15, 200,
            "Hardens the body to sharply raise Defense.", MoveEffect.DEFENSE_UP, 100)
        
        // UTILITY STATUS MOVES
        addTutorMove("substitute", "normal", MoveCategory.STATUS, 0, 999, 10, 300,
            "Creates a decoy using 1/4 of max HP.")
        addTutorMove("protect", "normal", MoveCategory.STATUS, 0, 999, 10, 250,
            "Completely protects user from attacks this turn.")
        addTutorMove("recover", "normal", MoveCategory.STATUS, 0, 999, 10, 400,
            "Restores up to half of max HP.", MoveEffect.HEAL, 100)
        addTutorMove("roost", "flying", MoveCategory.STATUS, 0, 999, 10, 350,
            "Restores HP and temporarily loses Flying type.", MoveEffect.HEAL, 100)
        addTutorMove("synthesis", "grass", MoveCategory.STATUS, 0, 999, 5, 300,
            "Restores HP. Amount varies with weather.", MoveEffect.HEAL, 100)
        
        // ADVANCED MOVES
        addTutorMove("stealth rock", "rock", MoveCategory.STATUS, 0, 999, 20, 500,
            "Lays a trap that damages Pokemon switching in.")
        addTutorMove("spikes", "ground", MoveCategory.STATUS, 0, 999, 20, 400,
            "Lays spikes that damage grounded Pokemon switching in.")
        addTutorMove("knock off", "dark", MoveCategory.PHYSICAL, 65, 100, 20, 350,
            "Knocks off the foe's held item.")
        addTutorMove("trick", "psychic", MoveCategory.STATUS, 0, 100, 10, 300,
            "Switches held items with the target.")
        
        // SPECIALTY MOVES
        addTutorMove("focus punch", "fighting", MoveCategory.PHYSICAL, 150, 100, 20, 800,
            "Requires focus. Fails if user takes damage first.")
        addTutorMove("sky attack", "flying", MoveCategory.PHYSICAL, 140, 90, 5, 700,
            "A two-turn attack with high critical hit ratio.", criticalRate = 2)
        addTutorMove("bounce", "flying", MoveCategory.PHYSICAL, 85, 85, 5, 450,
            "Bounces up high, then attacks on the second turn.", MoveEffect.PARALYZE, 30)
        addTutorMove("dive", "water", MoveCategory.PHYSICAL, 80, 100, 10, 400,
            "Dives underwater, then attacks on the second turn.")
        
        // MULTI-HIT MOVES
        addTutorMove("fury cutter", "bug", MoveCategory.PHYSICAL, 40, 95, 20, 250,
            "Power doubles with each consecutive hit.")
        addTutorMove("dual chop", "dragon", MoveCategory.PHYSICAL, 40, 90, 15, 300,
            "Hits twice with dragon claws.")
        addTutorMove("bullet seed", "grass", MoveCategory.PHYSICAL, 25, 100, 30, 200,
            "Fires 2-5 seeds in rapid succession.")
        
        // LOW POWER/HIGH UTILITY
        addTutorMove("swift", "normal", MoveCategory.SPECIAL, 60, 999, 20, 200,
            "Star-shaped rays that never miss.")
        addTutorMove("snore", "normal", MoveCategory.SPECIAL, 50, 100, 15, 150,
            "Can only be used while asleep. May make foe flinch.", MoveEffect.FLINCH, 30)
        addTutorMove("mud-slap", "ground", MoveCategory.SPECIAL, 20, 100, 10, 100,
            "Hurls mud to reduce foe's accuracy.", MoveEffect.ACCURACY_DOWN, 100)
        
        // SPECIAL LEGENDARY MOVES
        addTutorMove("secret sword", "fighting", MoveCategory.SPECIAL, 85, 100, 10, 1200,
            "Cuts with a long horn using physical Defense for damage.", legendary = "keldeo")
        addTutorMove("relic song", "normal", MoveCategory.SPECIAL, 75, 100, 10, 1000,
            "An ancient song that may put foes to sleep.", MoveEffect.SLEEP, 10, legendary = "meloetta")
        addTutorMove("volt tackle", "electric", MoveCategory.PHYSICAL, 120, 100, 15, 800,
            "A life-risking tackle that may paralyze the foe.", MoveEffect.PARALYZE, 10, recoil = 33, legendary = "pikachu")
        addTutorMove("dragon ascent", "flying", MoveCategory.PHYSICAL, 120, 100, 5, 1500,
            "Rayquaza's signature move that lowers defenses.", MoveEffect.DEFENSE_DOWN, 100, legendary = "rayquaza")
        
        // PARTNER PIKACHU/EEVEE MOVES (Let's Go)
        addTutorMove("zippy zap", "electric", MoveCategory.PHYSICAL, 80, 100, 10, 600,
            "Always goes first and always critical hit.", priority = 2, criticalRate = 999, legendary = "pikachu")
        addTutorMove("splishy splash", "water", MoveCategory.SPECIAL, 90, 100, 15, 600,
            "May paralyze the target.", MoveEffect.PARALYZE, 30, legendary = "pikachu")
        addTutorMove("floaty fall", "flying", MoveCategory.PHYSICAL, 90, 95, 15, 600,
            "May make the target flinch.", MoveEffect.FLINCH, 30, legendary = "pikachu")
        
        addTutorMove("bouncy bubble", "water", MoveCategory.SPECIAL, 60, 100, 20, 500,
            "Heals user for half damage dealt.", MoveEffect.DRAIN, 0, legendary = "eevee")
        addTutorMove("buzzy buzz", "electric", MoveCategory.SPECIAL, 60, 100, 20, 500,
            "Always paralyzes the target.", MoveEffect.PARALYZE, 100, legendary = "eevee")
        addTutorMove("sizzly slide", "fire", MoveCategory.PHYSICAL, 60, 100, 20, 500,
            "Always burns the target.", MoveEffect.BURN, 100, legendary = "eevee")
        addTutorMove("glitzy glow", "psychic", MoveCategory.SPECIAL, 80, 95, 15, 600,
            "Sets up Light Screen after attacking.", legendary = "eevee")
        addTutorMove("baddy bad", "dark", MoveCategory.SPECIAL, 80, 95, 15, 600,
            "Sets up Reflect after attacking.", legendary = "eevee")
        addTutorMove("sappy seed", "grass", MoveCategory.PHYSICAL, 100, 90, 10, 700,
            "Traps the target and drains HP each turn.", legendary = "eevee")
        addTutorMove("freezy frost", "ice", MoveCategory.SPECIAL, 100, 90, 10, 700,
            "Eliminates all stat changes on the field.", legendary = "eevee")
        addTutorMove("sparkly swirl", "fairy", MoveCategory.SPECIAL, 120, 85, 5, 800,
            "Heals user's team of status conditions.", legendary = "eevee")
        
        // NEWER GENERATION MOVES
        addTutorMove("liquidation", "water", MoveCategory.PHYSICAL, 85, 100, 10, 500,
            "May lower the target's Defense.", MoveEffect.DEFENSE_DOWN, 20)
        addTutorMove("throat chop", "dark", MoveCategory.PHYSICAL, 80, 100, 15, 450,
            "Prevents the target from using sound moves for 2 turns.")
        addTutorMove("stomping tantrum", "ground", MoveCategory.PHYSICAL, 75, 100, 10, 400,
            "Power doubles if user's last move failed.")
        addTutorMove("laser focus", "normal", MoveCategory.STATUS, 0, 999, 30, 300,
            "User's next attack will be a critical hit.")
        
        // COMBO/SUPPORT MOVES
        addTutorMove("helping hand", "normal", MoveCategory.STATUS, 0, 999, 20, 200,
            "Boosts ally's attack power for this turn.")
        addTutorMove("after you", "normal", MoveCategory.STATUS, 0, 999, 15, 250,
            "Target moves immediately after user.")
        addTutorMove("ally switch", "psychic", MoveCategory.STATUS, 0, 999, 15, 300,
            "User switches places with ally.", priority = 2)
        addTutorMove("magic coat", "psychic", MoveCategory.STATUS, 0, 999, 15, 300,
            "Reflects status moves back to the attacker.", priority = 4)
        addTutorMove("snatch", "dark", MoveCategory.STATUS, 0, 999, 10, 350,
            "Steals the beneficial effects of moves.", priority = 4)
        
        // FIELD EFFECT MOVES
        addTutorMove("gravity", "psychic", MoveCategory.STATUS, 0, 999, 5, 400,
            "Grounds all Pokemon and increases accuracy.")
        addTutorMove("magic room", "psychic", MoveCategory.STATUS, 0, 999, 10, 350,
            "Suppresses held item effects for 5 turns.")
        addTutorMove("wonder room", "psychic", MoveCategory.STATUS, 0, 999, 10, 350,
            "Swaps Defense and Sp. Defense for 5 turns.")
        addTutorMove("tailwind", "flying", MoveCategory.STATUS, 0, 999, 15, 300,
            "Boosts team's Speed for 4 turns.")
        addTutorMove("defog", "flying", MoveCategory.STATUS, 0, 999, 15, 250,
            "Clears hazards and lowers foe's evasion.", MoveEffect.EVASION_DOWN, 100)
        
        // ADDITIONAL AUTHENTIC MOVE TUTOR MOVES FROM POKEMON GAMES
        
        // CRYSTAL GAME CORNER TUTORS (4000 coins each)
        // Already have flamethrower, ice beam, thunderbolt
        
        // FIRERED/LEAFGREEN TUTORS
        addTutorMove("counter", "fighting", MoveCategory.PHYSICAL, 1, 100, 20, 400,
            "Returns double the physical damage received.")
        addTutorMove("dream eater", "psychic", MoveCategory.SPECIAL, 100, 100, 15, 350,
            "Eats the dreams of sleeping foes and recovers HP.", MoveEffect.DRAIN, 0)
        addTutorMove("explosion", "normal", MoveCategory.PHYSICAL, 250, 100, 5, 1000,
            "Inflicts massive damage but causes user to faint.")
        addTutorMove("metronome", "normal", MoveCategory.STATUS, 0, 999, 10, 300,
            "Randomly uses any move in the game.")
        addTutorMove("mimic", "normal", MoveCategory.STATUS, 0, 999, 10, 250,
            "Copies the target's last used move.")
        addTutorMove("soft-boiled", "normal", MoveCategory.STATUS, 0, 999, 10, 500,
            "Restores user's HP by up to half of maximum.", MoveEffect.HEAL, 100)
        
        // EMERALD BATTLE FRONTIER TUTORS
        addTutorMove("defense curl", "normal", MoveCategory.STATUS, 0, 999, 40, 100,
            "Curls up to raise Defense. Boosts Rollout's power.", MoveEffect.DEFENSE_UP, 100)
        addTutorMove("endure", "normal", MoveCategory.STATUS, 0, 999, 10, 300,
            "Endures any attack with at least 1 HP remaining.", priority = 4)
        addTutorMove("psych up", "normal", MoveCategory.STATUS, 0, 999, 10, 300,
            "Copies the target's stat changes.")
        addTutorMove("rollout", "rock", MoveCategory.PHYSICAL, 30, 90, 20, 300,
            "Attacks for 5 turns, growing stronger each turn.")
        addTutorMove("dynamic punch", "fighting", MoveCategory.PHYSICAL, 100, 50, 5, 600,
            "Always confuses the target if it hits.", MoveEffect.CONFUSION, 100)
        addTutorMove("sleep talk", "normal", MoveCategory.STATUS, 0, 999, 10, 200,
            "Randomly uses a move while asleep.")
        addTutorMove("swagger", "normal", MoveCategory.STATUS, 0, 85, 15, 250,
            "Confuses target but sharply raises its Attack.", MoveEffect.CONFUSION, 100)
        
        // PLATINUM SHARD TUTORS
        addTutorMove("air cutter", "flying", MoveCategory.SPECIAL, 60, 95, 25, 300,
            "Attacks with blades of air. High critical hit ratio.", criticalRate = 2)
        addTutorMove("ominous wind", "ghost", MoveCategory.SPECIAL, 60, 100, 5, 350,
            "May raise all user's stats.", MoveEffect.ATTACK_UP, 10)
        addTutorMove("sucker punch", "dark", MoveCategory.PHYSICAL, 70, 100, 5, 400,
            "Strikes first if target is preparing an attack.", priority = 1)
        addTutorMove("vacuum wave", "fighting", MoveCategory.SPECIAL, 40, 100, 30, 250,
            "Quick jab that always strikes first.", priority = 1)
        addTutorMove("last resort", "normal", MoveCategory.PHYSICAL, 140, 100, 5, 800,
            "Can only be used after all other moves are used.")
        addTutorMove("magnet rise", "electric", MoveCategory.STATUS, 0, 999, 10, 300,
            "Levitates using magnetism for 5 turns.")
        addTutorMove("spite", "ghost", MoveCategory.STATUS, 0, 100, 10, 200,
            "Reduces PP of target's last used move.")
        addTutorMove("uproar", "normal", MoveCategory.SPECIAL, 90, 100, 10, 350,
            "Causes uproar for 3 turns. No one can sleep.")
        addTutorMove("ancient power", "rock", MoveCategory.SPECIAL, 60, 100, 5, 300,
            "May raise all user's stats.", MoveEffect.ATTACK_UP, 10)
        addTutorMove("endeavor", "normal", MoveCategory.PHYSICAL, 1, 100, 5, 600,
            "Reduces target's HP to equal user's HP.")
        addTutorMove("gastro acid", "poison", MoveCategory.STATUS, 0, 100, 10, 250,
            "Suppresses the target's Ability.")
        addTutorMove("twister", "dragon", MoveCategory.SPECIAL, 40, 100, 20, 200,
            "Whips up a tornado. May make target flinch.", MoveEffect.FLINCH, 20)
        
        // HEARTGOLD/SOULSILVER BATTLE FRONTIER TUTORS
        addTutorMove("bug bite", "bug", MoveCategory.PHYSICAL, 60, 100, 20, 250,
            "Bites the target and eats their Berry.")
        addTutorMove("low kick", "fighting", MoveCategory.PHYSICAL, 1, 100, 20, 300,
            "Power increases with target's weight.")
        addTutorMove("pain split", "normal", MoveCategory.STATUS, 0, 999, 20, 350,
            "Adds user and target's HP, then splits evenly.")
        addTutorMove("super fang", "normal", MoveCategory.PHYSICAL, 1, 90, 10, 400,
            "Cuts target's HP in half.")
        addTutorMove("block", "normal", MoveCategory.STATUS, 0, 999, 5, 200,
            "Prevents the target from escaping.")
        addTutorMove("heal bell", "normal", MoveCategory.STATUS, 0, 999, 5, 400,
            "Heals all status conditions of the user's team.")
        addTutorMove("role play", "psychic", MoveCategory.STATUS, 0, 999, 10, 300,
            "Copies the target's Ability.")
        addTutorMove("string shot", "bug", MoveCategory.STATUS, 0, 95, 40, 100,
            "Lowers target's Speed sharply.", MoveEffect.SPEED_DOWN, 100)
        addTutorMove("worry seed", "grass", MoveCategory.STATUS, 0, 100, 10, 200,
            "Changes target's Ability to Insomnia.")
        addTutorMove("headbutt", "normal", MoveCategory.PHYSICAL, 70, 100, 15, 200,
            "May make the target flinch.", MoveEffect.FLINCH, 30)
        
        // BLACK 2 & WHITE 2 SHARD TUTORS
        addTutorMove("covet", "normal", MoveCategory.PHYSICAL, 60, 100, 25, 200,
            "Attacks and steals the target's held item.")
        addTutorMove("drill run", "ground", MoveCategory.PHYSICAL, 80, 95, 10, 400,
            "Spins like a drill. High critical hit ratio.", criticalRate = 2)
        addTutorMove("bind", "normal", MoveCategory.PHYSICAL, 15, 85, 20, 150,
            "Binds and squeezes for 4-5 turns.")
        addTutorMove("drain punch", "fighting", MoveCategory.PHYSICAL, 75, 100, 10, 450,
            "Punches to drain HP. User recovers half damage dealt.", MoveEffect.DRAIN, 0)
        addTutorMove("giga drain", "grass", MoveCategory.SPECIAL, 75, 100, 10, 450,
            "Drains nutrients. User recovers half damage dealt.", MoveEffect.DRAIN, 0)
        addTutorMove("electroweb", "electric", MoveCategory.SPECIAL, 55, 95, 15, 300,
            "Captures foe in electric net. Lowers Speed.", MoveEffect.SPEED_DOWN, 100)
        addTutorMove("foul play", "dark", MoveCategory.PHYSICAL, 95, 100, 15, 500,
            "Uses target's Attack stat instead of user's.")
        addTutorMove("iron defense", "steel", MoveCategory.STATUS, 0, 999, 15, 200,
            "Hardens body to sharply raise Defense.", MoveEffect.DEFENSE_UP, 100)
        addTutorMove("after you", "normal", MoveCategory.STATUS, 0, 999, 15, 250,
            "Makes target act immediately after user.")
        addTutorMove("magic room", "psychic", MoveCategory.STATUS, 0, 999, 10, 350,
            "Suppresses held item effects for 5 turns.")
        addTutorMove("recycle", "normal", MoveCategory.STATUS, 0, 999, 10, 200,
            "Recycles a used item for one more use.")
        addTutorMove("skill swap", "psychic", MoveCategory.STATUS, 0, 999, 10, 400,
            "Swaps Abilities with the target.")
        addTutorMove("stealth rock", "rock", MoveCategory.STATUS, 0, 999, 20, 500,
            "Lays a trap of levitating stones around foe.")
        addTutorMove("wonder room", "psychic", MoveCategory.STATUS, 0, 999, 10, 350,
            "Swaps Defense and Sp. Defense stats for 5 turns.")
        
        // OMEGA RUBY & ALPHA SAPPHIRE TUTORS
        addTutorMove("focus punch", "fighting", MoveCategory.PHYSICAL, 150, 100, 20, 800,
            "Requires concentration. Fails if user takes damage.")
        
        // ULTRA SUN & ULTRA MOON TUTORS
        addTutorMove("ally switch", "psychic", MoveCategory.STATUS, 0, 999, 15, 300,
            "User switches places with ally.", priority = 2)
        addTutorMove("telekinesis", "psychic", MoveCategory.STATUS, 0, 999, 15, 300,
            "Makes target float. All attacks hit for 3 turns.")
        
        // ADDITIONAL STATUS AND UTILITY MOVES
        addTutorMove("confuse ray", "ghost", MoveCategory.STATUS, 0, 100, 10, 150,
            "Confuses the target with a sinister ray.", MoveEffect.CONFUSION, 100)
        addTutorMove("double team", "normal", MoveCategory.STATUS, 0, 999, 15, 200,
            "Creates copies to raise evasiveness.", MoveEffect.EVASION_UP, 100)
        addTutorMove("minimize", "normal", MoveCategory.STATUS, 0, 999, 10, 200,
            "Shrinks body to sharply raise evasiveness.", MoveEffect.EVASION_UP, 100)
        addTutorMove("smokescreen", "normal", MoveCategory.STATUS, 0, 100, 20, 100,
            "Lowers target's accuracy with smoke.", MoveEffect.ACCURACY_DOWN, 100)
        addTutorMove("haze", "ice", MoveCategory.STATUS, 0, 999, 30, 200,
            "Eliminates all stat changes on the field.")
        
        // ADVANCED PHYSICAL MOVES
        addTutorMove("cross chop", "fighting", MoveCategory.PHYSICAL, 100, 80, 5, 600,
            "Double chop with high critical hit ratio.", criticalRate = 2)
        addTutorMove("crabhammer", "water", MoveCategory.PHYSICAL, 100, 90, 10, 550,
            "Hammers with a claw. High critical hit ratio.", criticalRate = 2)
        addTutorMove("submission", "fighting", MoveCategory.PHYSICAL, 80, 80, 20, 300,
            "Reckless tackle that also hurts user.", recoil = 25)
        addTutorMove("take down", "normal", MoveCategory.PHYSICAL, 90, 85, 20, 300,
            "Reckless charge that also hurts user.", recoil = 25)
        
        // ADVANCED SPECIAL MOVES
        addTutorMove("aurora beam", "ice", MoveCategory.SPECIAL, 65, 100, 20, 300,
            "Rainbow beam that may lower Attack.", MoveEffect.ATTACK_DOWN, 10)
        addTutorMove("bubble beam", "water", MoveCategory.SPECIAL, 65, 100, 20, 300,
            "Bubble attack that may lower Speed.", MoveEffect.SPEED_DOWN, 10)
        addTutorMove("psybeam", "psychic", MoveCategory.SPECIAL, 65, 100, 20, 300,
            "Psychic beam that may confuse target.", MoveEffect.CONFUSION, 10)
        addTutorMove("tri attack", "normal", MoveCategory.SPECIAL, 80, 100, 10, 400,
            "Fires three beams that may burn, freeze, or paralyze.")
        
        // WEATHER AND TERRAIN MOVES
        addTutorMove("sunny day", "fire", MoveCategory.STATUS, 0, 999, 5, 300,
            "Intensifies the sun for 5 turns.")
        addTutorMove("rain dance", "water", MoveCategory.STATUS, 0, 999, 5, 300,
            "Summons rain for 5 turns.")
        addTutorMove("sandstorm", "rock", MoveCategory.STATUS, 0, 999, 10, 300,
            "Summons a sandstorm for 5 turns.")
        addTutorMove("hail", "ice", MoveCategory.STATUS, 0, 999, 10, 300,
            "Summons a hailstorm for 5 turns.")

        // ADDITIONAL BATTLE FRONTIER EXCLUSIVE MOVES (High BP Cost)
        addTutorMove("body slam", "normal", MoveCategory.PHYSICAL, 85, 100, 15, 600,
            "Full-body charge that may paralyze.", MoveEffect.PARALYZE, 30)
        addTutorMove("double-edge", "normal", MoveCategory.PHYSICAL, 120, 100, 15, 800,
            "Reckless life-risking tackle.", recoil = 33)
        addTutorMove("rock slide", "rock", MoveCategory.PHYSICAL, 75, 90, 10, 500,
            "Large boulders may cause flinching.", MoveEffect.FLINCH, 30)
        addTutorMove("self-destruct", "normal", MoveCategory.PHYSICAL, 200, 100, 5, 1200,
            "Inflicts severe damage but causes user to faint.")
        
        // SPECIAL TRAINER EXCLUSIVE MOVES
        addTutorMove("sacred fire", "fire", MoveCategory.PHYSICAL, 100, 95, 5, 1500,
            "Mystical fire that may burn. High critical ratio.", MoveEffect.BURN, 50, criticalRate = 2, legendary = "ho-oh")
        addTutorMove("aeroblast", "flying", MoveCategory.SPECIAL, 100, 95, 5, 1500,
            "Wind blast with high critical hit ratio.", criticalRate = 2, legendary = "lugia")
        addTutorMove("psystrike", "psychic", MoveCategory.SPECIAL, 100, 100, 10, 1200,
            "Psychic attack that damages using Defense stat.", legendary = "mewtwo")
        
        // CONTEST AND APPEAL MOVES (Lower Cost for Show)
        addTutorMove("petal dance", "grass", MoveCategory.SPECIAL, 120, 100, 10, 400,
            "Dancing petals for 2-3 turns, then user becomes confused.")
        addTutorMove("lovely kiss", "normal", MoveCategory.STATUS, 0, 75, 10, 250,
            "Demands a kiss with a cute look that puts foe to sleep.", MoveEffect.SLEEP, 100)
        addTutorMove("sweet kiss", "fairy", MoveCategory.STATUS, 0, 75, 10, 200,
            "Demands a kiss with cute look that causes confusion.", MoveEffect.CONFUSION, 100)
        
        // SIGNATURE COMBO MOVES
        addTutorMove("skull bash", "normal", MoveCategory.PHYSICAL, 130, 100, 10, 700,
            "Two-turn attack that raises Defense, then strikes.")
        addTutorMove("razor wind", "normal", MoveCategory.SPECIAL, 80, 100, 10, 500,
            "Two-turn attack with high critical hit ratio.", criticalRate = 2)
        addTutorMove("wing attack", "flying", MoveCategory.PHYSICAL, 60, 100, 35, 200,
            "Strikes with wings spread wide.")
        
        // SPECIAL EFFECT MOVES (Very High Cost)
        addTutorMove("transform", "normal", MoveCategory.STATUS, 0, 999, 10, 2000,
            "Transforms into the target Pokemon.", legendary = "ditto")
        addTutorMove("sketch", "normal", MoveCategory.STATUS, 0, 999, 1, 3000,
            "Permanently copies target's last move.", legendary = "smeargle")
        
        // ADDITIONAL ELEMENTAL MOVES
        addTutorMove("flame wheel", "fire", MoveCategory.PHYSICAL, 60, 100, 25, 300,
            "Rolling fire attack that may burn.", MoveEffect.BURN, 10)
        addTutorMove("powder snow", "ice", MoveCategory.SPECIAL, 40, 100, 25, 200,
            "Blows powdery snow that may freeze.", MoveEffect.FREEZE, 10)
        addTutorMove("spark", "electric", MoveCategory.PHYSICAL, 65, 100, 20, 250,
            "Electric tackle that may paralyze.", MoveEffect.PARALYZE, 30)
        addTutorMove("absorb", "grass", MoveCategory.SPECIAL, 20, 100, 25, 150,
            "Drains half the damage as HP.", MoveEffect.DRAIN, 0)
        
        // COMPETITIVE TOURNAMENT MOVES (Championship Level)
        addTutorMove("baton pass", "normal", MoveCategory.STATUS, 0, 999, 40, 800,
            "Switches out and passes stat changes to replacement.")
        addTutorMove("perish song", "normal", MoveCategory.STATUS, 0, 999, 5, 1000,
            "Any Pokemon hearing this song faints in 3 turns.")
        addTutorMove("mean look", "normal", MoveCategory.STATUS, 0, 999, 5, 400,
            "Prevents the target from escaping with scary look.")
        addTutorMove("spider web", "bug", MoveCategory.STATUS, 0, 999, 10, 300,
            "Ensnares the target to prevent escape.")

        // ...existing code...
    }
    
    private fun addTutorMove(
        name: String,
        type: String,
        category: MoveCategory,
        power: Int,
        accuracy: Int,
        pp: Int,
        cost: Int,
        description: String,
        effect: MoveEffect? = null,
        effectChance: Int = 0,
        priority: Int = 0,
        criticalRate: Int = 1,
        recoil: Int = 0,
        starter: Boolean = false,
        dragonOnly: Boolean = false,
        legendary: String? = null
    ) {
        tutorMoves[name.lowercase()] = TutorMove(
            name = name.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
            type = type,
            category = category,
            power = power,
            accuracy = accuracy,
            pp = pp,
            cost = cost,
            description = description,
            effect = effect,
            effectChance = effectChance,
            priority = priority,
            criticalHitRate = criticalRate,
            recoil = recoil,
            starterOnly = starter,
            dragonOnly = dragonOnly,
            legendaryOnly = legendary
        )
    }
}

data class TutorMove(
    val name: String,
    val type: String,
    val category: MoveCategory,
    val power: Int,
    val accuracy: Int,
    val pp: Int,
    val cost: Int, // Cost in coins
    val description: String,
    val effect: MoveEffect? = null,
    val effectChance: Int = 0,
    val priority: Int = 0,
    val criticalHitRate: Int = 1,
    val recoil: Int = 0, // Percentage of damage dealt as recoil
    val starterOnly: Boolean = false,
    val dragonOnly: Boolean = false,
    val legendaryOnly: String? = null, // Specific Pokemon name if legendary-exclusive
    val contact: Boolean = true
) {
    fun toMove(): Move {
        return Move(
            name = name,
            type = type,
            category = category,
            power = power,
            accuracy = accuracy,
            maxPp = pp,
            currentPp = pp,
            effect = effect,
            effectChance = effectChance,
            priority = priority,
            criticalHitRate = criticalHitRate,
            contact = contact,
            description = description
        )
    }
    
    fun canLearn(pokemon: Pokemon): Boolean {
        return when {
            starterOnly -> isStarter(pokemon)
            dragonOnly -> pokemon.primaryType == "dragon" || pokemon.secondaryType == "dragon"
            legendaryOnly != null -> pokemon.name.lowercase() == legendaryOnly.lowercase()
            else -> true
        }
    }
    
    private fun isStarter(pokemon: Pokemon): Boolean {
        val starterNames = setOf(
            // Gen 1
            "bulbasaur", "ivysaur", "venusaur", "charmander", "charmeleon", "charizard",
            "squirtle", "wartortle", "blastoise",
            // Gen 2
            "chikorita", "bayleef", "meganium", "cyndaquil", "quilava", "typhlosion",
            "totodile", "croconaw", "feraligatr",
            // Gen 3
            "treecko", "grovyle", "sceptile", "torchic", "combusken", "blaziken",
            "mudkip", "marshtomp", "swampert",
            // Gen 4
            "turtwig", "grotle", "torterra", "chimchar", "monferno", "infernape",
            "piplup", "prinplup", "empoleon",
            // Gen 5
            "snivy", "servine", "serperior", "tepig", "pignite", "emboar",
            "oshawott", "dewott", "samurott",
            // Gen 6
            "chespin", "quilladin", "chesnaught", "fennekin", "braixen", "delphox",
            "froakie", "frogadier", "greninja",
            // Gen 7
            "rowlet", "dartrix", "decidueye", "litten", "torracat", "incineroar",
            "popplio", "brionne", "primarina",
            // Gen 8
            "grookey", "thwackey", "rillaboom", "scorbunny", "raboot", "cinderace",
            "sobble", "drizzile", "inteleon",
            // Gen 9
            "sprigatito", "floragato", "meowscarada", "fuecoco", "crocalor", "skeledirge",
            "quaxly", "quaxwell", "quaquaval"
        )
        return pokemon.name.lowercase() in starterNames
    }
}
