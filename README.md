# Tevia Minecraft Mod

A simple Minecraft Forge mod created as an AI experiment. This mod demonstrates the basic structure and setup of a Minecraft mod.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/ (recommended) or https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`
   - Make sure `JAVA_HOME` environment variable is set

2. **Git** (optional, for cloning the repository)
   - Download from: https://git-scm.com/downloads

3. **Minecraft Java Edition** (version 1.19.2)
   - Purchase and install from: https://www.minecraft.net/

## Development Environment Setup

### Step 1: Clone or Download the Repository

```bash
git clone https://github.com/brianjlacy/minecraft-tevia-mod.git
cd minecraft-tevia-mod
```

Or download and extract the ZIP file from GitHub.

### Step 2: Set Up the Development Workspace

The project uses Gradle with ForgeGradle to manage dependencies and build processes. The Gradle wrapper is included, so you don't need to install Gradle separately.

#### On Windows:

```bash
gradlew.bat setup
```

#### On Linux/Mac:

```bash
./gradlew setup
```

This command will:
- Download all required dependencies
- Set up the Minecraft decompiled source code
- Configure the development environment

**Note:** The first run will take several minutes as it downloads Minecraft, Forge, and all dependencies (approximately 500MB-1GB of data).

### Step 3: Generate IDE Project Files (Optional but Recommended)

#### For IntelliJ IDEA:

```bash
# Windows
gradlew.bat genIntellijRuns

# Linux/Mac
./gradlew genIntellijRuns
```

Then open the project in IntelliJ IDEA by selecting "Open" and choosing the project folder.

#### For Eclipse:

```bash
# Windows
gradlew.bat eclipse

# Linux/Mac
./gradlew eclipse
```

Then import the project into Eclipse.

## Building the Mod

To build the mod JAR file:

#### On Windows:

```bash
gradlew.bat build
```

#### On Linux/Mac:

```bash
./gradlew build
```

The compiled mod JAR file will be located at:
```
build/libs/teviamod-1.0.0.jar
```

## Testing the Mod in Development

You can run Minecraft with the mod directly from the development environment:

### Running the Client

```bash
# Windows
gradlew.bat runClient

# Linux/Mac
./gradlew runClient
```

This will launch Minecraft with your mod loaded.

### Running the Server (for testing multiplayer)

```bash
# Windows
gradlew.bat runServer

# Linux/Mac
./gradlew runServer
```

## Installing the Mod in Minecraft

### Step 1: Install Minecraft Forge

1. Download Forge 1.19.2 (version 43.3.0 or compatible) from: https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html
2. Run the Forge installer and select "Install Client"
3. Launch Minecraft and select the "Forge" profile

### Step 2: Install the Mod

1. Build the mod JAR file (see "Building the Mod" section above)
2. Locate your Minecraft installation folder:
   - **Windows**: `%APPDATA%\.minecraft`
   - **Linux**: `~/.minecraft`
   - **Mac**: `~/Library/Application Support/minecraft`
3. Navigate to the `mods` folder (create it if it doesn't exist)
4. Copy `teviamod-1.0.0.jar` from `build/libs/` into the `mods` folder
5. Launch Minecraft using the Forge profile
6. The mod will be loaded automatically

### Step 3: Verify the Mod is Loaded

1. Launch Minecraft with the Forge profile
2. Click "Mods" from the main menu
3. You should see "Tevia Mod" in the list
4. Check the game logs (press F3 + L to open logs folder) - you should see messages like:
   - "Tevia Mod has been initialized!"
   - "Tevia Mod common setup complete!"

## Project Structure

```
minecraft-tevia-mod/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/brianjlacy/teviamod/
│       │       └── TeviaMod.java          # Main mod class
│       └── resources/
│           └── META-INF/
│               └── mods.toml               # Mod metadata
├── build.gradle                            # Build configuration
├── gradle.properties                       # Gradle properties
├── settings.gradle                         # Project settings
├── gradlew                                 # Gradle wrapper (Linux/Mac)
├── gradlew.bat                             # Gradle wrapper (Windows)
└── README.md                               # This file
```

## What This Mod Does

This is a minimal example mod that:
- Registers itself with Forge
- Logs messages during initialization
- Serves as a template for adding custom features

The mod currently doesn't add any items, blocks, or gameplay features, but provides a solid foundation for expansion.

## Troubleshooting

### Build fails with "JAVA_HOME not set"
- Ensure Java 17 or higher is installed
- Set the JAVA_HOME environment variable to your JDK installation path

### Gradle download fails
- Check your internet connection
- If behind a proxy, configure Gradle proxy settings in `gradle.properties`

### Minecraft crashes on launch
- Ensure you're using Forge 1.19.2 (version 43.3.0 or compatible)
- Check the crash logs in the `logs` folder for specific errors
- Verify the mod JAR file was built successfully

### Mod doesn't appear in the mods list
- Verify the JAR file is in the correct `mods` folder
- Check that you're launching Minecraft with the Forge profile
- Ensure the Forge version matches (1.19.2)

## Further Development

To extend this mod, you can:
- Add custom items in `src/main/java/com/brianjlacy/teviamod/item/`
- Add custom blocks in `src/main/java/com/brianjlacy/teviamod/block/`
- Register event handlers to modify game behavior
- Add textures and models in `src/main/resources/assets/teviamod/`

Refer to the Forge documentation for more details: https://docs.minecraftforge.net/

## License

MIT

## Credits

Created by Brian Lacy as a Minecraft modding experiment.
