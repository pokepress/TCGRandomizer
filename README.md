## TCGRandomizer
A basic randomizer of Pokemon TCG (GBC), written in Java. Work in progress.

It randomizes the following ROM:
* Pokémon Trading Card Game (U) [C][!].gbc md5: 219b2cc64e5a052003015d4bd4c622cd

#### <b>Current features</b>

Currently, it randomizes the following data inherent to Pokemon cards (all options are selectable):
* HP
* Weaknesses and resistances
* Retreat cost
* The moves across Pokemon cards of the same type are shuffled with an algorithm that accounts for energy requirements. Empty moves and Pokemon powers are not shuffled. For the rest, a 1-energy move will become another 1-energy move, a 2-energy move will become another 2-energy move, and a 3- or 4-energy move will become another 3- or 4-energy move (always between cards of the same type).

The minimum and maximum HP, retreat cost, and number of weaknesses and resistances is selectable. The HP and retreat cost ranges are selectable for 6 types of evolution classes or stages.

If randomizing weakness and resistance, there are currently four options:
* Randomize each card individually
* Randomize each original weakness/resistance combination identically (in other words, all Pokemon with a weakness to psychic and no resistance will have the same combination in the randomized game)
* Randomize each gen 1 evolution line uniformly. Note that Hitmonchan and Hitmonlee are considered separate, Eevee is grouped in with all its evolutions, and Surfing and Flying Pikachu will be given the same weakness and resistance as regular Pikachu. 
* Remove weakness and resistance for all cards

It also provides a number of options for tweaking game behavior:
* Default the text speed to the fastest setting and disable attack animations when starting a new file
* Skip the tutorial battle entirely (based on Sanqui's solution-used with permission)
* Play as Mint from the Japan-only sequel (based on NikcDC's patch-used with permission), Imakuni?, and more!
* Choose a different set of graphics for the in-game coin
* Insert the illusion cards (normally only available via Card Pop!) randomly into in-game sets, or treat them as promos (available via the Challenge Cup after the two built-in Challenges)
* Prevent boss characters (Club Masters, Grand Masters, and Ronald) from manipulating their opening hand at the start of a battle
* Adjust some Trainer cards to be more balanced:
  * Adds a flip to Energy Removal (changes to "Crushing Hamr.") and Gust of Wind (changes to "PKMN Catcher")
  * Makes Super Energy Removal (changes to "Equal Energy Removal") a 1-to-1 exchange
  * Draw 5 on Professor Oak instad of 7
  * Gambler (changes to "Birch O") draws 7 (heads) or 4 (tails) cards instead of 8/1
* Randomize requirements for battling Club Masters:
  * Isaac (Lightning) may not require defeating Jennifer, Nicholas, and/or Brandon (50/50 each)
  * Murray (Psychic) may require anywhere between 0 and 7 medals
  * Rick (Science) may not require defeating Joseph (50/50 chance)
  * Ken (Fire) may require you to have anywhere between 0 and 500 cards
* Randomize the number of Club Medals needed to fight the Grand Masters to a number between 6 and 8
* Change "Call for Family" attacks to search for a same-typed basic instead of a specific Pokemon
* Rebalance attack costs (currently changes all "Call for Family" attacks to require a single energy matching the Pokemon's type)

#### <b>Current bugs or flaws</b>

* (Bug) If the shuffle moves option is selected, most Pokemon names in the move descriptions will be wrong as they will still refer to the original card.
* (Flaw) If the shuffle moves option is selected, there is a chance that a card ends up with no damage-dealing move (more common for cards with a single move slot).
* (Flaw) If the shuffle moves option is selected, a Pokemon may wind up with a boosting move that references an attack it doesn't have.

#### <b>Planned features or tweaks (in no particular order)</b>

* Selectable "energy mappings" in the shuffle moves option.
* Option to force a damaing move on every card.
* Further randomization of moves, beyond just shuffling the effects, and randomizing energy requirements and damage. Randomizing the move effects themselves may be a possibility, but further reverse engineering of the Pokemon TCG duel engine will be necessary first.
* Randomization of types. This is related to the randomization of energy requirements and would require adjusting the decks to keep them type-consistent. 
* Randomization of other misc Pokemon card data.
* Being able to load the Pokemon TCG ROM from the file system.
* Increase chance of a Challenge Cup being triggrered on boot in postgame.
* Additional options for player character and in-game coin.
* Randomize requirements for Water, Fighting, and Grass Club Masters (Rock's Club Master has no requirement to begin with, making it more complicated)
* Customize the title screen, credits, and other elements

Contributions aren't expected but would always be welcome.

#### <b>Screenshots</b>

![window](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/window.png)

![1](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/1.bmp)
![2](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/2.bmp)
![3](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/3.bmp)
![4](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/4.bmp)
![5](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/5.bmp)
![6](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/6.bmp)
![7](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/7.bmp)
![8](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/8.bmp)
![9](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/9.bmp)
![10](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/10.bmp)
![11](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/11.png)
![12](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/12.png)
![13](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/13.png)
![14](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/14.png)
![15](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/15.png)
![16](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/16.png)
![17](https://raw.githubusercontent.com/pokepress/TCGRandomizer/master/screenshots/17.png)

#### <b>Download</b>

[TCGRandomizer.jar](TCGRandomizer.jar?raw=true)

At the moment, the Pokemon TCG ROM detailed above must be in the same directory as the .jar file, and must be named <b>tcg.gbc</b>. Make sure you have a recent version of Java installed on your computer. Execute the .jar file and choose your settings. Finally, click on the "Randomize!" button to generate a randomized ROM that will named tcgrandomized_(seed)_(Options)_(Other Config).gbc.
