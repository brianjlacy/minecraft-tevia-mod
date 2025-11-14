# Tevia - LLM Automation Mod for Minecraft

A Minecraft Java Edition Fabric mod that enables Large Language Models (LLMs) to fully automate a player character through API integration with Anthropic Claude or Replicate models.

## 🎮 Features

- **Full Player Automation**: LLM controls movement, combat, inventory management, block interactions, and chat
- **Multiple API Providers**: Support for Anthropic Claude and Replicate models
- **Rich World Perception**: Collects detailed information about player state, inventory, nearby entities, blocks, environment, and events
- **Character Profiles**: Define unique AI personalities, goals, and behaviors through JSON profiles
- **Configurable Behavior**: Extensive configuration options for perception, actions, safety, and performance
- **Visual Perception** (Optional): Screenshot capture for vision-capable models
- **Real-time Decision Making**: Configurable LLM decision frequency (0.5-10 Hz)
- **Safety Features**: Emergency stop, action limits, boundary restrictions

## 📋 Table of Contents

- [Requirements](#requirements)
- [Development Environment Setup](#development-environment-setup)
- [Building the Mod](#building-the-mod)
- [Installation](#installation)
- [Configuration](#configuration)
- [Character Profiles](#character-profiles)
- [Usage](#usage)
- [Commands](#commands)
- [Troubleshooting](#troubleshooting)
- [Development](#development)
- [Architecture](#architecture)

## 🔧 Requirements

### Runtime Requirements
- **Minecraft**: Java Edition 1.20.1
- **Java**: JDK 17 or higher
- **Fabric Loader**: 0.15.0 or higher
- **Fabric API**: 0.92.2+1.20.1
- **API Key**: Anthropic Claude API key or Replicate API key

### Development Requirements
- **Java JDK**: 17 or higher
- **Gradle**: 8.3+ (included via wrapper)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## 🛠️ Development Environment Setup

### 1. Clone the Repository

```bash
git clone https://github.com/brianjlacy/minecraft-tevia-mod.git
cd minecraft-tevia-mod
```

### 2. Install Java 17

#### Windows
Download from https://adoptium.net/ or use package manager:
```bash
winget install EclipseAdoptium.Temurin.17.JDK
```

#### macOS
```bash
brew install openjdk@17
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

Verify installation:
```bash
java -version
```

### 3. Build the Project

Due to network dependencies, you'll need to ensure maven repositories are accessible:

```bash
# Initialize Gradle wrapper (if needed)
./gradlew wrapper --gradle-version=8.3

# Download dependencies and build
./gradlew build --refresh-dependencies
```

If you encounter network issues with Fabric Maven, you may need to:
1. Check your internet connection
2. Configure proxy settings if behind a firewall
3. Retry the build command

### 4. IDE Setup

#### IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA
2. Select "Open" and choose the `minecraft-tevia-mod` directory
3. Wait for Gradle import to complete
4. Generate run configurations:
   ```bash
   ./gradlew genSources
   ```
5. Run configurations will appear: "Minecraft Client"

#### Eclipse
```bash
./gradlew eclipse
```

#### VS Code
Install the Java Extension Pack, then open the project folder.

## 🏗️ Building the Mod

### Standard Build

```bash
./gradlew build
```

The compiled mod JAR will be in: `build/libs/minecraft-tevia-mod-1.0.0.jar`

### Clean Build

```bash
./gradlew clean build
```

### Run Tests

```bash
./gradlew test
```

## 📦 Installation

### 1. Install Fabric Loader

1. Download Fabric installer from https://fabricmc.net/use/installer/
2. Run the installer
3. Select Minecraft 1.20.1
4. Click "Install"

### 2. Install Fabric API

1. Download Fabric API 0.92.2+1.20.1 from https://modrinth.com/mod/fabric-api
2. Place in your mods folder:
   - **Windows**: `%APPDATA%\.minecraft\mods`
   - **macOS**: `~/Library/Application Support/minecraft/mods`
   - **Linux**: `~/.minecraft/mods`

### 3. Install Tevia Mod

Copy `minecraft-tevia-mod-1.0.0.jar` to the same mods folder.

### 4. Verify Installation

1. Launch Minecraft with Fabric profile
2. Click "Mods" button in main menu
3. Look for "Tevia - LLM Automation Mod"

## ⚙️ Configuration

On first launch, Tevia creates: `.minecraft/config/tevia-config.json`

### Essential Configuration

Edit `tevia-config.json`:

```json
{
  "apiProvider": "anthropic",
  "anthropicApiKey": "sk-ant-api03-YOUR_KEY_HERE",
  "anthropicModel": "claude-3-5-sonnet-20241022",

  "decisionFrequencyHz": 0.5,
  "characterProfilePath": "profiles/explorer.json",

  "enableCombat": true,
  "enableInventoryManagement": true,
  "enableBlockInteraction": true,
  "enableChatting": true
}
```

### Getting an API Key

**Anthropic Claude**:
1. Visit https://console.anthropic.com/
2. Sign up or log in
3. Go to "API Keys"
4. Create a new key
5. Copy and paste into config

**Replicate**:
1. Visit https://replicate.com/
2. Sign up and get your API token
3. Use `"apiProvider": "replicate"` in config

## 👤 Character Profiles

Profiles define AI personality and behavior.

### Location
`.minecraft/config/tevia/profiles/`

### Built-in Profiles

- **explorer.json**: Curious adventurer
- **warrior.json**: Combat-focused fighter
- **builder.json**: Creative builder
- **miner.json**: Underground specialist

### Custom Profile Example

Create `my-character.json`:

```json
{
  "name": "Your Character",
  "personality": "Friendly and helpful",
  "background": "A helpful assistant",
  "goals": [
    "Help other players",
    "Build useful structures",
    "Gather resources"
  ],
  "knowledgeLevel": "expert",
  "behavioralTraits": ["social", "helpful", "cautious"],
  "specialInstructions": "Always be polite and helpful to others."
}
```

Update config:
```json
"characterProfilePath": "profiles/my-character.json"
```

## 🎯 Usage

### Start Automation

In Minecraft, open chat:
```
/tevia start
```

### Stop Automation

```
/tevia stop
```

### Check Status

```
/tevia status
```

### Reload Configuration

```
/tevia reload
```

## 📝 Commands

| Command | Description |
|---------|-------------|
| `/tevia start` | Start LLM automation |
| `/tevia stop` | Emergency stop |
| `/tevia status` | Show status |
| `/tevia reload` | Reload config |

## 🐛 Troubleshooting

### Mod Won't Load

- Verify Fabric Loader and API are installed
- Check `.minecraft/logs/latest.log` for errors
- Ensure mod JAR is in correct mods folder

### API Errors

- Verify API key is correct
- Check internet connection
- Confirm API key has credits
- Check firewall settings

### No Actions

- Enable `logLLMResponses` in config
- Check logs for parsing errors
- Verify actions are enabled in config
- Try lower `decisionFrequencyHz`

### High Costs

- Reduce `decisionFrequencyHz` (e.g., 0.25)
- Reduce `maxContextMessages`
- Disable `enableVisualPerception`

## 🔬 Development

### Run Tests

```bash
./gradlew test
```

### Run in Development

```bash
./gradlew runClient
```

### Project Structure

```
src/main/java/com/tevia/
├── TeviaAutomationMod.java       # Main entry
├── api/                          # API clients
├── perception/                   # World sensing
├── action/                       # Action execution
├── llm/                          # LLM integration
├── profile/                      # Character profiles
├── config/                       # Configuration
└── commands/                     # Commands

src/test/java/com/tevia/          # Unit tests
```

## 🏛️ Architecture

### Components

1. **Perception System**: Collects world state
2. **LLM Coordinator**: Decision-making loop
3. **Prompt Builder**: Constructs prompts
4. **Response Parser**: Parses JSON actions
5. **Action Executor**: Executes actions
6. **Profile System**: Manages AI personality

### Flow

```
Perception → Prompt → LLM → Parse → Actions → Execute
```

## 📄 License

MIT License - see LICENSE file

## 🙏 Acknowledgments

- Fabric modding framework
- Anthropic Claude API
- Replicate platform
- Minecraft

## ⚠️ Important Notes

- API calls may incur costs - monitor usage
- Test with low frequency first
- Use emergency stop if needed
- Review logs regularly

---

For detailed implementation information, see [PROJECT-TRACKER.md](PROJECT-TRACKER.md)