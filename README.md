# ConfigurableInventories

A library for PaperMC plugins that lets you define GUI inventories in YAML configuration files using [Configurate](https://github.com/SpongePowered/Configurate).

## Installation

Add JitPack to your repositories and the dependency to your build file.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.StarlessDev:ConfigurableInventories:TAG")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.StarlessDev</groupId>
    <artifactId>ConfigurableInventories</artifactId>
    <version>TAG</version>
</dependency>
```

Replace `TAG` with a release tag or commit hash (e.g. `1.25.7`).

## Usage

### Register serializers

Register the type serializers with Configurate when building your YAML loader:

```java
YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
        .path(path)
        .defaultOptions(opts -> opts.serializers(builder -> builder
                .register(ConfigurableItem.class, new ItemSerializer())
                .register(ConfigurableInventory.class, new InventorySerializer())))
        .build();
```

### Define an inventory in YAML

```yaml
my-gui:
  title: "<gold>My Custom GUI"
  structure:
    - "# # # # # # # # #"
    - "# . . . X . . . #"
    - "# # # # # # # # #"
  items:
    "#":
      material: gray_stained_glass_pane
      displayName: " "
    "X":
      material: diamond
      displayName: "<aqua>Click me!"
      lore:
        - "<gray>An example item"
      enchantment-glint: true
```

### Load and use

```java
ConfigurationNode root = loader.load();
ConfigurableInventory gui = root.node("my-gui").get(ConfigurableInventory.class);

gui.getTitle();          // Returns the inventory title
gui.getStructure();      // Returns the structure as a List<String>
gui.getItem('X');        // Returns a ConfigurableItem by its key character
gui.getItems();          // Returns all items as a Map<String, ConfigurableItem>
```

### ConfigurableItem

```java
// Build an item programmatically
ConfigurableItem item = ConfigurableItem.builder()
        .material(Material.DIAMOND_SWORD)
        .name("<red>Fire Sword")
        .addLore("<gray>Burns enemies on hit")
        .addEnchantment(Enchantment.FIRE_ASPECT, 2)
        .addFlag(ItemFlag.HIDE_ENCHANTS)
        .unbreakable(true)
        .enchantmentGlintOverride(true)
        .amount(1)
        .modelData(100)
        .potionMeta(potionMeta)
        .skullMeta(skullMeta)
        .build();

// Convert to an ItemStack
item.asItemStack();

// Convert to an ItemStack with placeholders
item.asItemStack(List.of(new ItemPlaceholder("player", player.getName())));
// %player% in the display name or lore will be replaced with the player's name

// Edit an existing item
ConfigurableItem edited = item.edit().name("<green>New Name").build();

// Copy an item
ConfigurableItem copy = item.copy();

// Create from an existing ItemStack
ConfigurableItem fromStack = ConfigurableItem.fromItemStack(itemStack);
```

## Requirements

- Java 21+
- Paper 1.21.4+
- Configurate 4.2.0+