# Shifu Interaction Roadmap

This document has been edited for clarity using an AI.

## 1. Core “High-Impact” Interactions

Focus on interactions players are guaranteed to encounter. These define Shifu’s personality and leave a lasting
impression.

- [ ] **First Meeting** – Single, memorable line establishing Shifu as wise and calm.
- [ ] **First Victory Against Shifu** – Rewarding line that feels epic but stays in lore.
- [ ] **First Defeat of Player** – Short, calm, encouraging line.
- [ ] **Final Boss Interaction** – Unique message that stands out and feels special.
- [ ] **Add a message when Shifu kills mobs randomly**
- [ ] **More messages when Shifu gets killed by a player OR naturally**
- [ ] **Book on first join** – Player gets a book from someone who tells them they are from far away, lost memory,
  advising to visit Shifu.
- [ ] **Monsters should always target Shifu**

> Note: One line per key event is enough at first. Make these moments count.

---

## 2. Contextual Flavor Lines (Optional for Later)

These interactions enrich gameplay and can be added gradually.

- [x] **Shifu interacts to rain**
- [ ] **Combat Lines** – Rain, night, day, or special biomes.
- [ ] **Low-Health Warnings** – Shifu or player (e.g., "I'm becoming old")
- [ ] **Unique Ambient Sounds** – Short “Hmm” or wise utterances during fights.
- [ ] **Minor Events** – Small reactions for unusual player actions or environmental changes.
- [ ] **Shifu gives a rememberable item on exchanging legendary scrolls**
- [ ] **Achievements for exchanging different scroll tiers** – First time only, maybe 15 times with Shifu
- [ ] **When a player attacks Shifu** – Shifu should respond like "Let's start training, young one!" or "Today lesson"
- [ ] **First-time player meets Shifu** – Should react with a memorable line
- [ ] **More messages for different times** – Morning, afternoon, night; messages like "keep learning", "once you're my
  age", "I'm still learning"
- [ ] **Allow players to fight with Shifu**

> Tip: Start with one per context, test it, then expand.

---

## 3. Rare Easter Egg Interactions

Rare lines that most players may never encounter but reward dedicated or observant players.

- [ ] Multiple player wins in a row
- [ ] Shifu commenting on special achievements (e.g., first legendary loot)
- [ ] Secret phrases triggered by unusual events or hidden mechanics

> These can wait until core gameplay is fun and polished.

---

## 4. Gameplay & Behavior Notes

- [x] **Shifu heals slowly outside of combat**
- [x] **Shifu heals but slowly, over time**
- [x] **Shifu and Player do not kill each other**
- [x] **Shifu reacts differently when the fight ends depending on who won**
- [x] **Shifu pays attention to the player trading with him**
- [ ] **Shifu takes breaks and advises the player to stay healthy**

---

## 5. Efficient Implementation Tips

- [ ] **Data-Driven Messages** – Use JSON or Kotlin lists instead of hardcoding 50+ messages.
- [ ] **First-Time Flags** – Separate first-time interactions to avoid hundreds of checks.
- [ ] **Better structure spawning** – Keep Shifu entity separate from the NBT structure file? Also consider making the
  chests compatible with loot chest mod by not hardcoding the chests directly in the file?

---

## ✅ Recommended Workflow

- [ ] Implement **core events** and a small set of contextual lines. Test visibility.
- [ ] Move to **structures, entities, and mechanics** that are immediately noticeable.
- [ ] Gradually expand **optional/contextual interactions**.
