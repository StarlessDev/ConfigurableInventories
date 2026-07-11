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

Replace `TAG` with a release tag or commit hash (e.g. `1.3.0`).

## Usage

### Register serializers

Register the type serializers with Configurate when building your YAML loader:

```java
YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
        .path(path)
        .defaultOptions(opts -> opts.serializers(builder -> builder
                .registerAll(InventoriesConfigurateSerializer.INSTANCE.getSerializers())))
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
      name: " "
    "X":
      material: diamond
      name: "<aqua>Click me!"
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

#### Build an item programmatically

```java
ConfigurableItem item = ConfigurableItem.builder()
        .material(Material.DIAMOND_SWORD)
        .name(Component.text("Fire Sword").color(NamedTextColor.RED))
        .addLore(Component.text("Burns enemies on hit"))
        .addEnchantment(Enchantment.FIRE_ASPECT, 2)
        .addFlag(ItemFlag.HIDE_ENCHANTS)
        .unbreakable(true)
        .enchantmentGlintOverride(true)
        .amount(1)
        .build();
```

#### Convert to an ItemStack

```java
ItemStack itemStack = item.asItemStack();
```

#### Convert to an ItemStack with placeholders

```java
ItemStack itemStack = item.asItemStack(List.of(new ItemPlaceholder("player", player.getName())));
// %player% in the display name or lore will be replaced with the player's name
```

#### Convert to a localized ItemStack

```java
// Using player's locale
ItemStack itemStack = item.asLocalizedItemStack(List.of(new ItemPlaceholder("player", player.getName())), player);

// Using specific locale
ItemStack itemStack = item.asLocalizedItemStack(List.of(new ItemPlaceholder("player", player.getName())), Locale.ENGLISH);
```

#### Edit an existing item

```java
ConfigurableItem edited = item.edit().name("<green>New Name").build();
```

#### Copy an item

```java
ConfigurableItem copy = item.copy();
```

#### Create from an existing ItemStack

```java
ConfigurableItem fromStack = ConfigurableItem.fromItemStack(itemStack);
```

#### Custom Model Data

The library supports various types for custom model data:

```java
ConfigurableItem item = ConfigurableItem.builder(Material.STONE_SWORD)
    .addIntCustomModelData(123)           // Integer
    .addDoubleCustomModelData(456.789)    // Double
    .addFloatCustomModelData(1.23f)       // Float
    .addBooleanCustomModelData(true)      // Boolean
    .addStringCustomModelData("hello")    // String
    .addColorCustomModelData(Color.RED)   // Color (from org.bukkit, converted to ARGB)
    .build();

// Or set entire lists
ConfigurableItem item2 = ConfigurableItem.builder(Material.STONE_SWORD)
    .setFloatCustomModelData(Arrays.asList(1.0f, 2.0f, 3.0f))
    .setBooleanCustomModelData(Arrays.asList(true, false))
    .setStringCustomModelData(Arrays.asList("a", "b", "c"))
    .setColorCustomModelData(Arrays.asList(Color.RED, Color.BLUE))
    .build();
```

#### Additional Components

##### Potion Component
```java
ConfigurablePotionComponent potion = ConfigurablePotionComponent.builder()
    .type(PotionType.STRONG_SWIFTNESS)
    .name(Component.text("Really weird potion"))
    .color(Color.fromRGB(0xffffff))
    .addEffect(new PotionEffect(MobEffect.DAMAGE_BOOST, 200, 1), true)
    .build();

ConfigurableItem item = ConfigurableItem.builder(Material.POTION)
    .potionComponent(potion)
    .build();
```

##### Profile Component (Skull)
```java
ConfigurableProfileComponent profile = ConfigurableProfileComponent.builder()
    .uuid(UUID.randomUUID())
    .username("username")
    .addProperty(new ProfileProperty("textures", "value", "signature"))
    .build();

ConfigurableItem item = ConfigurableItem.builder(Material.PLAYER_HEAD)
    .profileComponent(profile)
    .build();
```

##### Item Modifier
```java
ConfigurableItem item = ConfigurableItem.builder(Material.DIAMOND_SWORD)
    .modifier(itemStack -> {
        // Modify the item stack after it's built
        itemStack.enchant(Enchantment.SHARPNESS, 5);
        itemStack.editMeta(meta -> {
            // ...
        });
    })
    .build();
```

## Requirements

- Java 21+
- Paper 1.21.4+
- Configurate 4.2.0+

## Building from Source

To build the project yourself:

```bash
git clone https://github.com/StarlessDev/ConfigurableInventories.git
cd ConfigurableInventories
./gradlew publishToMavenLocal
```

The JAR file will be located in `build/libs/`.