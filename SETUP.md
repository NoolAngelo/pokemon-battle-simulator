# Pokemon Battle Simulator - Setup Guide

## Installation Requirements

### Install Kotlin

```bash
# Using Homebrew (recommended for macOS)
brew install kotlin

# Or using SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install kotlin
```

### Run the Game

```bash
# Compile and run
kotlinc Main.kt -include-runtime -d Main.jar
java -jar Main.jar

# Or run directly (if kotlin command is available)
kotlin Main.kt
```

## Game Features

### 🎮 Enhanced Gameplay

- **Character Selection**: Choose from Ash (Fire), Brock (Rock), or Misty (Water)
- **Type System**: Pokemon types with effectiveness multipliers
- **Turn-based Combat**: Strategic battle system with accuracy and critical hits
- **Progressive Difficulty**: Enemies scale with your level

### 🏆 RPG Elements

- **Leveling System**: Gain EXP and level up to unlock stronger content
- **Pokemon Collection**: Collect and manage multiple Pokemon
- **Active Pokemon System**: Switch between your collected Pokemon

### 💰 Economy & Rewards

- **Coin System**: Earn coins from battles
- **Potion System**: Use coins to heal during battles
- **Stage Progression**: Advance through increasingly difficult stages

### 🎰 Gacha Collection System

- **Basic Pack** (50 coins): Common Pokemon
- **Premium Pack** (150 coins): 30% chance for rare Pokemon
- **Ultra Pack** (300 coins): 70% chance for rare + 20% shiny chance

### ✨ Special Features

- **Shiny Pokemon**: Rare variants with visual indicators
- **Critical Hits**: Chance-based damage multipliers
- **Type Effectiveness**: Fire > Grass > Water > Fire, Rock interactions
- **Random Encounters**: 10% chance for rare Pokemon after victories
- **Status Tracking**: Comprehensive player progress display

### 🎯 Addictive Elements Inspired by Popular Games

1. **Collection Mechanics** (Pokemon GO style): Gotta catch 'em all!
2. **Gacha System** (Mobile games): Random rewards create excitement
3. **Progressive Difficulty** (RPGs): Always something challenging ahead
4. **Daily Rewards Feel** (Through battles): Consistent coin/EXP gain
5. **Type Strategy** (Pokemon series): Rock-paper-scissors depth
6. **Shiny Hunting** (Pokemon series): Rare collectibles for completionists

## Controls

- **Main Menu**: Navigate with numbers 1-5
- **Battles**: Select moves 1-4, or use potion (5)
- **Collection**: View and switch active Pokemon
- **Shop**: Spend coins on Pokemon packs
