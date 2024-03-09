import kotlin.random.Random

var badangHealth: Int = 10000
var playerHealth: Int = 10000

fun main() {
    println("Characters: Ash, Brock, or Misty")
    print("Character Select: ")
    val userInput = readLine()

    if (userInput != null) {
        when (userInput.toLowerCase()) {
            "ash" -> {
                // Implement Ash function
            }
            "brock" -> {
                while (playerHealth > 0 && badangHealth > 0) {
                    Brock()
                }
                if (playerHealth <= 0) {
                    println("Game Over!")
                } else {
                    println("Badang is dead, yehey!")
                }
            }
            "misty" -> {
                // Implement Misty functionality if needed
            }
            else -> {
                println("Invalid character input.")
            }
        }
    }
}

fun Brock() {
    println("Enemy: BADANG")
    println("Health: $badangHealth")
    println("\nYou selected Brock GGs")
    println("Pokemon: Onyx")
    println("\nSkills:")
    println("1. Headbutt: 360")
    println("2. Tailwhip: 600")
    println("3. RockSmash: 1100")

    print("Select Attack: ")
    val choice: Int = readLine()?.toIntOrNull() ?: 0

    when (choice) {
        1 -> {
            attack(360)
        }
        2 -> {
            attack(600)
        }
        3 -> {
            attack(1100)
        }
        else -> {
            println("Invalid attack choice.")
        }
    }

    // Badang attacks after player
    if (badangHealth > 0) {
        badangAttack()
    }
}

fun attack(damage: Int) {
    badangHealth -= damage
    if (badangHealth < 0) {
        badangHealth = 0
    }
    println("Damage dealt: $damage")
    println("Badang remaining health: $badangHealth")
}

fun badangAttack() {
    val attacks = listOf(360, 650, 1900,565,200,20,648,2100)
    val attackDamage = attacks[Random.nextInt(attacks.size)]
    playerHealth -= attackDamage
    if (playerHealth < 0) {
        playerHealth = 0
    }
    println("Badang attacked! Damage dealt: $attackDamage")
    println("Player remaining health: $playerHealth")
}
