# Contributing

## Avoid custom constructor properties in classes that extend Minecraft entities (e.g., `Entity`, `PathfinderMob`)

**Long story short:** Do **not** add custom constructor properties to base classes that extend Minecraft’s entity
classes.

Keep classes minimal, like this:

```kotlin
class BanditEntity(
    type: EntityType<BanditEntity>,
    world: Level,
) : PathfinderMob(type, world)
```

### What to avoid

Do **not** design one class that takes an enum (or other custom parameter) in its constructor and then
passing different values to the constructor for different entity types, like this:

```kotlin
enum class BanditRank {
    Regular,
    Enforcer,
    Champion,
    Elite,
    Leader,
    Ruler
}

class BanditEntity(
    type: EntityType<BanditEntity>,
    world: Level,
    val rank: BanditRank, // ❌ BAD
) : PathfinderMob(type, world)

val BANDIT_REGULAR: Supplier<EntityType<BanditEntity>> = registerEntity(
    "bandit_regular",
    EntityType.Builder.of({ type, world ->
        BanditEntity(
            type,
            world,
            // ❌ BAD: passing enum directly
            rank = BanditRank.Regular,
        )
    })
)
```

Minecraft’s codebase was not designed to handle enums or custom constructor parameters gracefully.

This approach will fail at runtime:

* `registerGoals()` is called in the constructor of [net.minecraft.world.entity.Mob].
* At that point, subclass properties (like `rank`) are **not initialized yet**.
* This causes `NullPointerException` when those properties are accessed.

There are other limitations: for example, when registering renderers, attributes,
or any API that expects a unique class type per entity type. Enums do not work well in those cases either.

---

### The recommended workaround

Use one class per entity type, extending a common abstract base.

```
enum class BanditRank {
    Regular,
    Enforcer,
    Champion,
    Elite,
    Leader,
    Ruler,
}

abstract class BanditEntity(
    type: EntityType<out BanditEntity>,
    world: Level,
) : PathfinderMob(type, world) {
    /**
     * Returns the rank of this BanditEntity.
     *
     * IMPORTANT:
     * - MUST be implemented as a function or a getter.
     * - MUST NOT be a class property with a backing field.
     * - This function is accessed during [registerGoals] of [net.minecraft.world.entity.Mob],
     *   which is called in the constructor of [net.minecraft.world.entity.Mob].
     *   If implemented as a normal property with a backing field, the property
     *   will be null at that point, causing a runtime [NullPointerException] (this is the issue
     *   that was reproduced).
     */
    abstract fun getRank(): BanditRank
    
    override fun registerGoals() {
        super.registerGoals()

        // Safe access to rank — no NullPointerException
        getRank().name
    }
}

class BanditRegularEntity(
    type: EntityType<BanditRegularEntity>,
    world: Level,
) : BanditEntity(type, world) {

    override fun getRank(): BanditRank = BanditRank.Regular
}
```

### Rule of thumb

Create a **dedicated class for every distinct entity type**, even if they share the same base class.
Do not rely on enums or constructor parameters to differentiate them.

### Safe entity type registration

Always register entities using a direct constructor reference:

```kotlin
val BANDIT_REGULAR: Supplier<EntityType<BanditRegularEntity>> = registerEntity(
    "bandit_regular",
    // ✔️ GOOD: Use constructor reference instead of manually passing the arguments to the constructor
    EntityType.Builder.of(::BanditRegularEntity, MobCategory.MONSTER)
)
```

❌ Avoid lambda registrations that manually pass parameters::

```kotlin
val BANDIT_REGULAR: Supplier<EntityType<BanditRegularEntity>> = registerEntity(
    "bandit_regular",
    // ❌ BAD: Passing the arguments manually to the constructor
    EntityType.Builder.of({ type, world -> BanditRegularEntity(type, world) }, MobCategory.MONSTER)
)
```

This makes the entity system safe, type-consistent, and future-proof, while avoiding the runtime initialization
pitfalls of Minecraft’s entity constructors.

More info can be found in [NeoForge documentation](https://docs.neoforged.net/docs/entities/#the-entity-class).
