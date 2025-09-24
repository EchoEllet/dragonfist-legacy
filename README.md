# DragonFist Legacy

## Roadmap

### Shifu

See [roadmap document](docs/roadmap/shifu.md).

### Bandits

- [ ] Achievements on killing specific amount of bandits, or when killing their king, bandits interact when killing

their king?

- [ ] Throwable weapons by bandits? https://youtu.be/jP9xMCEIQZw?si=HXeO2jx9XskUVfYK

### Village warriors?

- [ ] They should speak or talk about Shifu sometimes and how good of a trainer he is.

### Blade of Olympus?

- [ ] The player should be able to kill Shifu with it!

### Final boss?

- [ ] Reacts on the fact when we bring Shifu with us or not? Like "Where is Shifu? Once I kill you, I will down that
  clown!" and "So, how are you Master Shifu?"
- [ ] Shifu and the others react if we defeated the final boss?
- [ ] Players defeat the Bandit king, get an item from him and offer it to Master Shifu to reach the final boss?
- Use this:

    ```json
    {
    "weight": 150.0,
    "canBeInterrupted": false,
    "looping": false,
    "cooldown": 400,
    "behaviors": [
    {
    "conditions": [
    { "predicate": "within_distance", "min": 0.0, "max": 45.0 },
    { "predicate": "epicfight:health", "comparator": "less_ratio", "health": 0.5 }
    ],
    "animation": "epicfight:biped/skill/wrathful_lighting"
    }
    ]
    }
    ```

### Scrolls

| Scroll Tier | Scroll Icon    | Ability Points | Rarity   | Reasoning                                                         |
|-------------|----------------|----------------|----------|-------------------------------------------------------------------|
| Very Common | Boot Scroll    | 0.2 → 1 per 5  | COMMON   | Very early-game scroll, matches “common” color.                   |
| Common      | Feather Scroll | 0.33 → 1 per 3 | COMMON   | Slightly stronger than Very Common, still early-game.             |
| Uncommon    | Shield Scroll  | 0.5 → 1 per 2  | UNCOMMON | Mid-game scroll, matches Minecraft “uncommon” yellow.             |
| Rare        | Sword Scroll   | 1              | UNCOMMON | Slightly higher tier than Shield, still yellow to maintain range. |
| Epic        | Eye Scroll     | 2              | RARE     | Strong mid/late-game scroll, blue glow.                           |
| Legendary   | Jade Scroll    | 3              | EPIC     | Top-tier scroll, purple glow, visually unique.                    |

### Considerations

- Consider these structures:
    - https://www.planetminecraft.com/project/minecraft-tree-beyond-nether-portal-statue-v08-free/
    - https://www.planetminecraft.com/project/minecraft-guardian-dragon-statue-free/
    - Shifu https://www.planetminecraft.com/project/field-of-honour-samurai-duel-dojo/
    - Shifu https://www.planetminecraft.com/project/dojo-102611/
    - Shifu https://www.minecraft-schematics.com/schematic/15670/
- Add music disc? Use https://incompetech.com/music/royalty-free/index.html?isrc=USUAN1100693
  OR https://en.shw.in/sozai/japan.php OR https://youtube.com/watch?v=dPVb_RmjUBg (Miyako Japan 3 - SHW and Miyako Japan 2 - SHW )
- Allow Shifu or Bandits and other entities to heal when killing other mobs?
- https://buildpaste.net/build/japanese-bamboo-style-mbZ4LdLQGAMwpEGyDcKb, https://buildpaste.net/build/templesimonefang-BFMHxQskNMOFLlN5pfmh, https://buildpaste.net/build/beautiful-cherry-tree-6k8NBcTnKNyzBqmjtnXz

## License

- All source code and original assets created by the DragonFist Legacy project authors are licensed under the [MIT License](LICENSE).
- Third-party assets included in this project are **not covered** by the MIT License. See [CREDITS.md](CREDITS.md) for full attribution and licensing information.

## Credits

See [CREDITS.md](CREDITS.md) for full attribution.

## Disclaimer

> [!WARNING]
> **DragonFist Legacy is NOT AN OFFICIAL MINECRAFT PRODUCT.  
It is NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.**

> [!NOTE]
> **This mod is not affiliated with the [Epic Fight Team](https://github.com/Epic-Fight).**
