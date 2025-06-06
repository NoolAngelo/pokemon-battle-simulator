import kotlin.random.Random

// Game state variables
var playerLevel: Int = 1
var playerExp: Int = 0
var playerCoins: Int = 100
var currentBattleStage: Int = 1

// Player's Pokemon collection
var playerPokemon = mutableListOf<Pokemon>()
var activePokemon: Pokemon? = null

// Enemy data
var enemyPokemon: Pokemon? = null

// Battle system instance
val battleSystem = BattleSystem()

fun main() {
    initializeGame()
    showMainMenu()
}

fun initializeGame() {
    // Initialize starter Pokemon based on character choice
    println("═══════════════════════════════════")
    println("🎮 POKEMON BATTLE SIMULATOR 🎮")
    println("═══════════════════════════════════")
    println("Welcome, trainer! Choose your starter:")
    println("1. 🔥 Ash (Fire Type Specialist)")
    println("2. 🪨 Brock (Rock Type Specialist)") 
    println("3. 💧 Misty (Water Type Specialist)")
    print("Character Select (1-3): ")
    
    val choice = readLine()?.toIntOrNull() ?: 1
    
    when (choice) {
        1 -> initializeAsh()
        2 -> initializeBrock()
        3 -> initializeMisty()
        else -> {
            println("Invalid choice! Defaulting to Ash...")
            initializeAsh()
        }
    }
}

fun initializeAsh() {
    val charizard = PokemonDatabase.createPokemon(6, 25) // Charizard, level 25
    if (charizard != null) {
        charizard.isShiny = false
        playerPokemon.add(charizard)
        activePokemon = charizard
        println("You chose Ash! ${charizard.name} is ready for battle! 🔥")
        println("Type: ${charizard.getType()} | Total Stats: ${charizard.totalStats}")
    }
}

fun initializeBrock() {
    val onix = PokemonDatabase.createPokemon(95, 25) // Using Onix proxy, level 25
    if (onix != null) {
        // Create custom Onix since it's not in our database yet
        val customOnix = Pokemon(
            name = "Onix", pokedexNumber = 95, primaryType = "rock", secondaryType = "ground",
            level = 25, currentHp = 0, baseHp = 35, baseAttack = 45, baseDefense = 160,
            baseSpecialAttack = 30, baseSpecialDefense = 45, baseSpeed = 70,
            moves = mutableListOf(
                MoveDatabase.getMove("rock throw")!!,
                MoveDatabase.getMove("earthquake")!!,
                MoveDatabase.getMove("iron tail")!!,
                MoveDatabase.getMove("rock slide")!!
            )
        )
        customOnix.currentHp = customOnix.maxHp
        customOnix.isShiny = false
        playerPokemon.add(customOnix)
        activePokemon = customOnix
        println("You chose Brock! ${customOnix.name} is ready to rock! 🪨")
        println("Type: ${customOnix.getType()} | Total Stats: ${customOnix.totalStats}")
    }
}

fun initializeMisty() {
    val starmie = PokemonDatabase.createPokemon(121, 25) // Using proxy, level 25
    if (starmie != null) {
        // Create custom Starmie since it's not in our database yet
        val customStarmie = Pokemon(
            name = "Starmie", pokedexNumber = 121, primaryType = "water", secondaryType = "psychic",
            level = 25, currentHp = 0, baseHp = 60, baseAttack = 75, baseDefense = 85,
            baseSpecialAttack = 100, baseSpecialDefense = 85, baseSpeed = 115,
            moves = mutableListOf(
                MoveDatabase.getMove("water gun")!!,
                MoveDatabase.getMove("psychic")!!,
                MoveDatabase.getMove("hydro pump")!!,
                MoveDatabase.getMove("swift")!!
            )
        )
        customStarmie.currentHp = customStarmie.maxHp
        customStarmie.isShiny = false
        playerPokemon.add(customStarmie)
        activePokemon = customStarmie
        println("You chose Misty! ${customStarmie.name} is ready to splash! 💧")
        println("Type: ${customStarmie.getType()} | Total Stats: ${customStarmie.totalStats}")
    }
}

fun showMainMenu() {
    while (true) {
        println("\n" + "═".repeat(40))
        println("🏠 MAIN MENU")
        println("═".repeat(40))
        println("👤 Trainer Level: $playerLevel | 🪙 Coins: $playerCoins")
        println("📊 EXP: $playerExp/${playerLevel * 100}")
        println()
        println("1. ⚔️  Battle Arena (Stage $currentBattleStage)")
        println("2. 📱 Pokemon Collection")
        println("3. 🎰 Gacha Shop")
        println("4. 📋 Status")
        println("5. 🚪 Exit Game")
        print("Choose option (1-5): ")
        
        val choice = readLine()?.toIntOrNull() ?: 0
        
        when (choice) {
            1 -> battleArena()
            2 -> showPokemonCollection()
            3 -> gachaShop()
            4 -> showStatus()
            5 -> {
                println("Thanks for playing! See you next time! 👋")
                return
            }
            else -> println("❌ Invalid choice! Please select 1-5.")
        }
    }
}

fun battleArena() {
    val enemy = generateRandomEnemy()
    enemyPokemon = enemy
    
    println("\n🏟️ BATTLE ARENA - STAGE $currentBattleStage")
    println("Wild ${enemy.name} appeared!")
    
    if (activePokemon == null) {
        println("❌ No active Pokemon! Please select one from your collection.")
        return
    }
    
    val battleResult = startBattle(activePokemon!!, enemy)        if (battleResult) {
            val expGained = Random.nextInt(20, 50)
            val coinsGained = Random.nextInt(15, 35)
            
            playerExp += expGained
            playerCoins += coinsGained
            
            println("🎉 Victory! Gained $expGained EXP and $coinsGained coins!")
            
            // Level up check
            if (playerExp >= playerLevel * 100) {
                playerLevel++
                playerExp -= (playerLevel - 1) * 100
                println("🎊 LEVEL UP! You are now level $playerLevel!")
                
                // Heal all Pokemon on level up
                playerPokemon.forEach { it.fullHeal() }
                println("💚 All Pokemon restored to full health!")
            }
            
            currentBattleStage++
            
            // Chance for rare Pokemon encounter
            if (Random.nextInt(100) < 12) {
                val rarePokemon = generateRarePokemon()
                println("✨ A rare ${rarePokemon.name} wants to join your team!")
                playerPokemon.add(rarePokemon)
            }
        } else {
            println("💔 Defeat! Your Pokemon needs rest...")
            activePokemon?.let { 
                it.currentHp = it.maxHp / 4 
                it.statusCondition = null // Clear status conditions after defeat
                it.statChanges.reset() // Reset stat changes
            }
        }
}

fun startBattle(playerPoke: Pokemon, enemyPoke: Pokemon): Boolean {
    var playerTurn = true
    var turnCount = 0
    
    println("\n🏟️ BATTLE START!")
    println("${playerPoke.name} vs ${enemyPoke.name}")
    
    while (!playerPoke.isFainted() && !enemyPoke.isFainted()) {
        turnCount++
        println("\n" + "─".repeat(60))
        println("Turn $turnCount")
        println("🔥 ${playerPoke.name} (${playerPoke.currentHp}/${playerPoke.maxHp} HP)")
        if (playerPoke.statusCondition != null) {
            println("   Status: ${playerPoke.statusCondition!!.displayName}")
        }
        println("🏴‍☠️ ${enemyPoke.name} (${enemyPoke.currentHp}/${enemyPoke.maxHp} HP)")
        if (enemyPoke.statusCondition != null) {
            println("   Status: ${enemyPoke.statusCondition!!.displayName}")
        }
        println("─".repeat(60))
        
        // Determine turn order based on speed (simplified)
        val playerSpeed = playerPoke.speed * (playerPoke.statusCondition?.getSpeedMultiplier() ?: 1.0)
        val enemySpeed = enemyPoke.speed * (enemyPoke.statusCondition?.getSpeedMultiplier() ?: 1.0)
        
        val playerGoesFirst = playerSpeed >= enemySpeed
        
        if (playerGoesFirst) {
            if (!playerPoke.isFainted()) playerAttackTurn(playerPoke, enemyPoke)
            if (!enemyPoke.isFainted() && !playerPoke.isFainted()) enemyAttackTurn(enemyPoke, playerPoke)
        } else {
            if (!enemyPoke.isFainted()) enemyAttackTurn(enemyPoke, playerPoke)
            if (!playerPoke.isFainted() && !enemyPoke.isFainted()) playerAttackTurn(playerPoke, enemyPoke)
        }
        
        // Apply end-of-turn effects
        if (!playerPoke.isFainted()) {
            val playerEffects = battleSystem.applyEndOfTurnEffects(playerPoke)
            if (playerEffects.isNotEmpty()) println(playerEffects)
        }
        
        if (!enemyPoke.isFainted()) {
            val enemyEffects = battleSystem.applyEndOfTurnEffects(enemyPoke)
            if (enemyEffects.isNotEmpty()) println(enemyEffects)
        }
        
        // Small delay for better experience
        Thread.sleep(1500)
    }
    
    return !playerPoke.isFainted()
}

fun playerAttackTurn(attacker: Pokemon, defender: Pokemon) {
    println("\n${attacker.name}'s turn!")
    println("Choose your move:")
    
    attacker.moves.forEachIndexed { index, move ->
        val ppText = if (move.currentPp > 0) "${move.currentPp}/${move.maxPp} PP" else "No PP!"
        val typeIcon = getTypeIcon(move.type)
        val categoryText = when (move.category) {
            MoveCategory.PHYSICAL -> "⚔️"
            MoveCategory.SPECIAL -> "✨"
            MoveCategory.STATUS -> "🔮"
        }
        println("${index + 1}. $typeIcon $categoryText ${move.name} (${move.power} power, $ppText)")
        println("   ${move.description}")
    }
    println("5. 🧪 Use Potion (Restore 50 HP - 10 coins)")
    println("6. 📖 View Move Details")
    
    print("Select action (1-6): ")
    val choice = readLine()?.toIntOrNull() ?: 1
    
    when (choice) {
        in 1..4 -> {
            val move = attacker.moves.getOrNull(choice - 1)
            if (move != null) {
                if (move.currentPp > 0) {
                    val result = battleSystem.executeMove(attacker, defender, move)
                    println(result.message)
                } else {
                    println("❌ ${move.name} has no PP left!")
                    playerAttackTurn(attacker, defender) // Retry turn
                }
            } else {
                println("❌ Invalid move!")
                playerAttackTurn(attacker, defender) // Retry turn
            }
        }
        5 -> {
            if (playerCoins >= 10) {
                playerCoins -= 10
                attacker.heal(50)
                println("🧪 Used Potion! ${attacker.name} restored 50 HP! (-10 coins)")
                println("${attacker.name} now has ${attacker.currentHp}/${attacker.maxHp} HP")
            } else {
                println("❌ Not enough coins for potion!")
                playerAttackTurn(attacker, defender) // Retry turn
            }
        }
        6 -> {
            showMoveDetails(attacker)
            playerAttackTurn(attacker, defender) // Show details then retry turn
        }
        else -> {
            println("❌ Invalid choice!")
            playerAttackTurn(attacker, defender) // Retry turn
        }
    }
}

fun enemyAttackTurn(attacker: Pokemon, defender: Pokemon) {
    println("\n${attacker.name}'s turn!")
    
    // AI move selection - prefer moves with PP and higher power
    val availableMoves = attacker.moves.filter { it.currentPp > 0 }
    if (availableMoves.isEmpty()) {
        println("${attacker.name} has no moves left!")
        return
    }
    
    // Simple AI: choose highest power move with a bit of randomness
    val move = if (Random.nextInt(100) < 70) {
        availableMoves.maxByOrNull { it.power } ?: availableMoves.random()
    } else {
        availableMoves.random()
    }
    
    val result = battleSystem.executeMove(attacker, defender, move)
    println(result.message)
}

fun showMoveDetails(pokemon: Pokemon) {
    println("\n📖 MOVE DETAILS for ${pokemon.name}")
    println("═".repeat(50))
    
    pokemon.moves.forEachIndexed { index, move ->
        val typeIcon = getTypeIcon(move.type)
        val categoryText = when (move.category) {
            MoveCategory.PHYSICAL -> "Physical ⚔️"
            MoveCategory.SPECIAL -> "Special ✨"
            MoveCategory.STATUS -> "Status 🔮"
        }
        
        println("${index + 1}. $typeIcon ${move.name}")
        println("   Type: ${move.type.uppercase()} | Category: $categoryText")
        println("   Power: ${move.power} | Accuracy: ${move.accuracy}%")
        println("   PP: ${move.currentPp}/${move.maxPp}")
        if (move.effect != null) {
            println("   Effect: ${move.effect} (${move.effectChance}% chance)")
        }
        println("   ${move.description}")
        println()
    }
    
    println("Press Enter to continue...")
    readLine()
}

fun getTypeIcon(type: String): String {
    return when (type.lowercase()) {
        "fire" -> "🔥"
        "water" -> "💧"
        "electric" -> "⚡"
        "grass" -> "🌿"
        "ice" -> "🧊"
        "fighting" -> "👊"
        "poison" -> "☠️"
        "ground" -> "🌍"
        "flying" -> "🕊️"
        "psychic" -> "🧠"
        "bug" -> "🐛"
        "rock" -> "🪨"
        "ghost" -> "👻"
        "dragon" -> "🐉"
        "dark" -> "🌙"
        "steel" -> "⚙️"
        "fairy" -> "🧚"
        else -> "⭐"
    }
}

fun generateRandomEnemy(): Pokemon {
    val commonPokemon = listOf(19, 16, 74, 129) // Rattata, Pidgey, Geodude, Magikarp
    val uncommonPokemon = listOf(25, 7, 4, 1) // Pikachu, Squirtle, Charmander, Bulbasaur
    val rarePokemon = listOf(130, 143, 6, 9, 3) // Gyarados, Snorlax, Charizard, Blastoise, Venusaur
    
    val pokemonPool = when {
        currentBattleStage <= 5 -> commonPokemon
        currentBattleStage <= 15 -> commonPokemon + uncommonPokemon
        else -> commonPokemon + uncommonPokemon + rarePokemon
    }
    
    val chosenId = pokemonPool.random()
    val enemyLevel = maxOf(1, playerLevel + Random.nextInt(-2, 3) + (currentBattleStage / 5))
    
    val enemy = PokemonDatabase.createPokemon(chosenId, enemyLevel)
    
    if (enemy != null) {
        // Scale stats based on stage difficulty
        val stageMultiplier = 1.0 + (currentBattleStage * 0.05)
        // Note: Pokemon stats are now calculated automatically based on level
        
        // Small chance for shiny enemy
        if (Random.nextInt(100) < 5) {
            enemy.isShiny = true
        }
        
        return enemy
    } else {
        // Fallback to a basic Pokemon if database lookup fails
        return createFallbackPokemon(enemyLevel)
    }
}

fun createFallbackPokemon(level: Int): Pokemon {
    return Pokemon(
        name = "Wild Pokemon",
        pokedexNumber = 0,
        primaryType = "normal",
        secondaryType = null,
        level = level,
        currentHp = 50 + level * 2,
        baseHp = 50,
        baseAttack = 50,
        baseDefense = 50,
        baseSpecialAttack = 50,
        baseSpecialDefense = 50,
        baseSpeed = 50,
        moves = mutableListOf(
            MoveDatabase.getMove("tackle") ?: Move("Tackle", "normal", MoveCategory.PHYSICAL, 40, 100, 35, "A basic attack")
        )
    ).apply {
        currentHp = maxHp
    }
}

fun generateRarePokemon(): Pokemon {
    val legendaryIds = listOf(150, 151, 245, 384) // Mewtwo, Mew, Suicune, Rayquaza
    val rareIds = listOf(130, 143, 6, 9, 3, 26) // Gyarados, Snorlax, Charizard, Blastoise, Venusaur, Raichu
    
    val allRare = legendaryIds + rareIds
    val chosenId = allRare.random()
    val rareLevel = playerLevel + Random.nextInt(0, 5)
    
    val rare = PokemonDatabase.createPokemon(chosenId, rareLevel)
    
    if (rare != null) {
        // High chance for shiny on rare Pokemon
        if (Random.nextInt(100) < 25) {
            rare.isShiny = true
        }
        return rare
    } else {
        return createFallbackPokemon(rareLevel)
    }
}

fun showPokemonCollection() {
    println("\n📱 POKEMON COLLECTION")
    println("═".repeat(50))
    
    if (playerPokemon.isEmpty()) {
        println("❌ No Pokemon in collection!")
        return
    }
    
    playerPokemon.forEachIndexed { index, pokemon ->
        val activeMarker = if (pokemon == activePokemon) "👑" else "  "
        val shinyMarker = if (pokemon.isShiny) "✨" else ""
        val statusMarker = pokemon.statusCondition?.let { " [${it.displayName}]" } ?: ""
        
        println("${index + 1}$activeMarker $shinyMarker${pokemon.name} (Lv.${pokemon.level})$statusMarker")
        println("    #${pokemon.pokedexNumber.toString().padStart(3, '0')} | Type: ${pokemon.getType()}")
        println("    HP: ${pokemon.currentHp}/${pokemon.maxHp} | Total Stats: ${pokemon.totalStats}")
        println("    ATK: ${pokemon.baseAttack} | DEF: ${pokemon.baseDefense} | SPA: ${pokemon.baseSpecialAttack}")
        println("    SPD: ${pokemon.baseSpecialDefense} | SPE: ${pokemon.baseSpeed}")
        
        // Show learned moves
        println("    Moves: ${pokemon.moves.joinToString(", ") { "${it.name} (${it.currentPp}/${it.maxPp})" }}")
        println()
    }
    
    println("Options:")
    println("1-${playerPokemon.size}. Select active Pokemon")
    println("${playerPokemon.size + 1}. Heal all Pokemon (100 coins)")
    println("${playerPokemon.size + 2}. View Pokemon details")
    println("0. Back to main menu")
    
    print("Choose option: ")
    val choice = readLine()?.toIntOrNull() ?: 0
    
    when {
        choice in 1..playerPokemon.size -> {
            activePokemon = playerPokemon[choice - 1]
            println("✅ ${activePokemon?.name} is now your active Pokemon!")
        }
        choice == playerPokemon.size + 1 -> {
            if (playerCoins >= 100) {
                playerCoins -= 100
                playerPokemon.forEach { it.fullHeal() }
                println("💚 All Pokemon fully healed! (-100 coins)")
            } else {
                println("❌ Not enough coins! Need 100 coins.")
            }
        }
        choice == playerPokemon.size + 2 -> {
            viewPokemonDetails()
        }
        choice == 0 -> return
        else -> println("❌ Invalid choice!")
    }
}

fun viewPokemonDetails() {
    println("\nSelect Pokemon to view details:")
    playerPokemon.forEachIndexed { index, pokemon ->
        println("${index + 1}. ${pokemon.name}")
    }
    
    print("Choose Pokemon (0 to go back): ")
    val choice = readLine()?.toIntOrNull() ?: 0
    
    if (choice in 1..playerPokemon.size) {
        val pokemon = playerPokemon[choice - 1]
        showDetailedPokemonInfo(pokemon)
    }
}

fun showDetailedPokemonInfo(pokemon: Pokemon) {
    println("\n" + "═".repeat(60))
    println("📊 DETAILED POKEMON INFO")
    println("═".repeat(60))
    
    val shinyText = if (pokemon.isShiny) " ✨(SHINY)" else ""
    println("Name: ${pokemon.name}$shinyText")
    println("Pokedex #: ${pokemon.pokedexNumber.toString().padStart(3, '0')}")
    println("Type: ${pokemon.getType()}")
    println("Level: ${pokemon.level}")
    
    pokemon.statusCondition?.let {
        println("Status: ${it.displayName}")
    }
    
    println("\n💪 STATS:")
    println("HP: ${pokemon.currentHp}/${pokemon.maxHp}")
    println("Attack: ${pokemon.baseAttack} → ${pokemon.attack}")
    println("Defense: ${pokemon.baseDefense} → ${pokemon.defense}")
    println("Sp. Attack: ${pokemon.baseSpecialAttack} → ${pokemon.specialAttack}")
    println("Sp. Defense: ${pokemon.baseSpecialDefense} → ${pokemon.specialDefense}")
    println("Speed: ${pokemon.baseSpeed} → ${pokemon.speed}")
    println("Total Base Stats: ${pokemon.totalStats}")
    
    if (pokemon.statChanges.attack != 0 || pokemon.statChanges.defense != 0 || 
        pokemon.statChanges.specialAttack != 0 || pokemon.statChanges.specialDefense != 0 || 
        pokemon.statChanges.speed != 0) {
        println("\n📈 STAT CHANGES:")
        if (pokemon.statChanges.attack != 0) println("Attack: ${if (pokemon.statChanges.attack > 0) "+" else ""}${pokemon.statChanges.attack}")
        if (pokemon.statChanges.defense != 0) println("Defense: ${if (pokemon.statChanges.defense > 0) "+" else ""}${pokemon.statChanges.defense}")
        if (pokemon.statChanges.specialAttack != 0) println("Sp. Attack: ${if (pokemon.statChanges.specialAttack > 0) "+" else ""}${pokemon.statChanges.specialAttack}")
        if (pokemon.statChanges.specialDefense != 0) println("Sp. Defense: ${if (pokemon.statChanges.specialDefense > 0) "+" else ""}${pokemon.statChanges.specialDefense}")
        if (pokemon.statChanges.speed != 0) println("Speed: ${if (pokemon.statChanges.speed > 0) "+" else ""}${pokemon.statChanges.speed}")
    }
    
    println("\n🎯 MOVES:")
    pokemon.moves.forEachIndexed { index, move ->
        val typeIcon = getTypeIcon(move.type)
        val categoryIcon = when (move.category) {
            MoveCategory.PHYSICAL -> "⚔️"
            MoveCategory.SPECIAL -> "✨"
            MoveCategory.STATUS -> "🔮"
        }
        
        println("${index + 1}. $typeIcon $categoryIcon ${move.name}")
        println("   Power: ${move.power} | Accuracy: ${move.accuracy}% | PP: ${move.currentPp}/${move.maxPp}")
        if (move.effect != null) {
            println("   Effect: ${move.effect} (${move.effectChance}% chance)")
        }
        println("   ${move.description}")
    }
    
    println("\nPress Enter to continue...")
    readLine()
}

fun gachaShop() {
    println("\n🎰 GACHA SHOP")
    println("═".repeat(40))
    println("🪙 Your Coins: $playerCoins")
    println()
    println("1. 📦 Basic Pack (50 coins) - Common Pokemon")
    println("2. 💎 Premium Pack (150 coins) - Rare Pokemon chance!")
    println("3. 🌟 Ultra Pack (300 coins) - Legendary chance!")
    println("4. 🏪 Move Tutor (200 coins) - Teach new moves!")
    println("5. 🔙 Back to menu")
    
    print("Choose option (1-5): ")
    val choice = readLine()?.toIntOrNull() ?: 5
    
    when (choice) {
        1 -> {
            if (playerCoins >= 50) {
                playerCoins -= 50
                openBasicPack()
            } else {
                println("❌ Not enough coins!")
            }
        }
        2 -> {
            if (playerCoins >= 150) {
                playerCoins -= 150
                openPremiumPack()
            } else {
                println("❌ Not enough coins!")
            }
        }
        3 -> {
            if (playerCoins >= 300) {
                playerCoins -= 300
                openUltraPack()
            } else {
                println("❌ Not enough coins!")
            }
        }
        4 -> {
            if (playerCoins >= 200) {
                moveTutor()
            } else {
                println("❌ Not enough coins! Move tutor costs 200 coins.")
            }
        }
        5 -> return
        else -> println("❌ Invalid choice!")
    }
}

fun moveTutor() {
    if (playerPokemon.isEmpty()) {
        println("❌ No Pokemon to teach moves to!")
        return
    }
    
    println("\n🏫 MOVE TUTOR")
    println("═".repeat(40))
    println("Select a Pokemon to teach a new move:")
    
    playerPokemon.forEachIndexed { index, pokemon ->
        println("${index + 1}. ${pokemon.name} (${pokemon.moves.size}/4 moves)")
    }
    
    print("Choose Pokemon (0 to cancel): ")
    val pokemonChoice = readLine()?.toIntOrNull() ?: 0
    
    if (pokemonChoice !in 1..playerPokemon.size) {
        println("Cancelled.")
        return
    }
    
    val pokemon = playerPokemon[pokemonChoice - 1]
    
    // Show available moves based on Pokemon type
    val availableMoves = MoveDatabase.getAllMoves().filter { move ->
        move.type == pokemon.primaryType || 
        move.type == pokemon.secondaryType ||
        move.type == "normal" ||
        move.name.lowercase() in listOf("rest", "protect", "substitute", "toxic")
    }.shuffled().take(8)
    
    println("\nAvailable moves for ${pokemon.name}:")
    availableMoves.forEachIndexed { index, move ->
        val typeIcon = getTypeIcon(move.type)
        val categoryIcon = when (move.category) {
            MoveCategory.PHYSICAL -> "⚔️"
            MoveCategory.SPECIAL -> "✨"
            MoveCategory.STATUS -> "🔮"
        }
        println("${index + 1}. $typeIcon $categoryIcon ${move.name} (${move.type}, ${move.power} power)")
    }
    
    print("Choose move to learn (0 to cancel): ")
    val moveChoice = readLine()?.toIntOrNull() ?: 0
    
    if (moveChoice !in 1..availableMoves.size) {
        println("Cancelled.")
        return
    }
    
    val newMove = availableMoves[moveChoice - 1]
    
    if (pokemon.moves.size < 4) {
        pokemon.learnMove(newMove)
        playerCoins -= 200
        println("✅ ${pokemon.name} learned ${newMove.name}! (-200 coins)")
    } else {
        println("${pokemon.name} already knows 4 moves! Choose a move to replace:")
        pokemon.moves.forEachIndexed { index, move ->
            println("${index + 1}. ${move.name}")
        }
        
        print("Replace which move? (0 to cancel): ")
        val replaceChoice = readLine()?.toIntOrNull() ?: 0
        
        if (replaceChoice in 1..pokemon.moves.size) {
            val oldMove = pokemon.moves[replaceChoice - 1]
            pokemon.replaceMove(replaceChoice - 1, newMove)
            playerCoins -= 200
            println("✅ ${pokemon.name} forgot ${oldMove.name} and learned ${newMove.name}! (-200 coins)")
        } else {
            println("Cancelled.")
        }
    }
}

fun openBasicPack() {
    println("\n📦 Opening Basic Pack...")
    Thread.sleep(1000)
    
    val commonIds = listOf(19, 16, 74, 129, 104) // Rattata, Pidgey, Geodude, Magikarp, Cubone
    val pokemonId = commonIds.random()
    val level = Random.nextInt(1, playerLevel + 1)
    
    val pokemon = PokemonDatabase.createPokemon(pokemonId, level) ?: createFallbackPokemon(level)
    
    playerPokemon.add(pokemon)
    println("🎉 You got ${pokemon.name}${if (pokemon.isShiny) " ✨(SHINY!)" else ""}!")
    println("Level: ${pokemon.level} | Type: ${pokemon.getType()}")
}

fun openPremiumPack() {
    println("\n💎 Opening Premium Pack...")
    Thread.sleep(1500)
    
    val pokemon = if (Random.nextInt(100) < 40) {
        // 40% chance for rare Pokemon
        generateRarePokemon()
    } else {
        // 60% chance for uncommon Pokemon
        val uncommonIds = listOf(25, 7, 4, 1, 105, 26) // Pikachu, Squirtle, etc.
        val pokemonId = uncommonIds.random()
        val level = Random.nextInt(playerLevel, playerLevel + 3)
        PokemonDatabase.createPokemon(pokemonId, level) ?: createFallbackPokemon(level)
    }
    
    // 15% chance for shiny
    if (Random.nextInt(100) < 15) {
        pokemon.isShiny = true
    }
    
    playerPokemon.add(pokemon)
    println("🎉 You got ${pokemon.name}${if (pokemon.isShiny) " ✨(SHINY!)" else ""}!")
    println("Level: ${pokemon.level} | Type: ${pokemon.getType()}")
}

fun openUltraPack() {
    println("\n🌟 Opening Ultra Pack...")
    Thread.sleep(2000)
    
    val pokemon = if (Random.nextInt(100) < 80) {
        // 80% chance for rare/legendary Pokemon
        generateRarePokemon()
    } else {
        // 20% chance for premium uncommon
        val premiumIds = listOf(6, 9, 3, 26, 130) // Evolved starters, Raichu, Gyarados
        val pokemonId = premiumIds.random()
        val level = Random.nextInt(playerLevel + 1, playerLevel + 5)
        PokemonDatabase.createPokemon(pokemonId, level) ?: createFallbackPokemon(level)
    }
    
    // 30% chance for shiny
    if (Random.nextInt(100) < 30) {
        pokemon.isShiny = true
    }
    
    playerPokemon.add(pokemon)
    println("🎉 You got ${pokemon.name}${if (pokemon.isShiny) " ✨(SHINY!)" else ""}!")
    println("Level: ${pokemon.level} | Type: ${pokemon.getType()}")
    println("Total Stats: ${pokemon.totalStats}")
}

fun showStatus() {
    println("\n📋 TRAINER STATUS")
    println("═".repeat(50))
    println("👤 Trainer Level: $playerLevel")
    println("📊 Experience: $playerExp/${playerLevel * 100}")
    println("🪙 Coins: $playerCoins")
    println("🏟️ Current Stage: $currentBattleStage")
    println("📱 Pokemon Collection: ${playerPokemon.size}")
    
    activePokemon?.let { pokemon ->
        println("\n👑 Active Pokemon: ${pokemon.name}${if (pokemon.isShiny) " ✨" else ""}")
        println("   #${pokemon.pokedexNumber.toString().padStart(3, '0')} | Level: ${pokemon.level} | Type: ${pokemon.getType()}")
        println("   HP: ${pokemon.currentHp}/${pokemon.maxHp}")
        println("   Stats: ATK:${pokemon.attack} DEF:${pokemon.defense} SPA:${pokemon.specialAttack} SPD:${pokemon.specialDefense} SPE:${pokemon.speed}")
        
        pokemon.statusCondition?.let {
            println("   Status: ${it.displayName}")
        }
        
        println("   Moves:")
        pokemon.moves.forEach { move ->
            val typeIcon = getTypeIcon(move.type)
            println("     $typeIcon ${move.name} (${move.currentPp}/${move.maxPp} PP)")
        }
    }
    
    if (playerPokemon.isNotEmpty()) {
        println("\n📊 POKEMON SUMMARY:")
        val typeCount = playerPokemon.groupBy { it.primaryType }.mapValues { it.value.size }
        typeCount.forEach { (type, count) ->
            println("   ${getTypeIcon(type)} ${type.uppercase()}: $count")
        }
        
        val averageLevel = playerPokemon.map { it.level }.average()
        println("   Average Level: ${"%.1f".format(averageLevel)}")
        
        val shinyCount = playerPokemon.count { it.isShiny }
        if (shinyCount > 0) {
            println("   ✨ Shiny Pokemon: $shinyCount")
        }
    }
    
    println("\nPress Enter to continue...")
    readLine()
}
