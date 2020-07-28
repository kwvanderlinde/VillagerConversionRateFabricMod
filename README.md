# Villager Conversion Rate Fabric Mod

A mod for controlling the chance of a villager turning into a zombie villager, regardless of the world's difficulty.

## License

This mod is licensed under the [BSD 3-Clause license](./LICENSE). I use this license because it is short, easy to
understand, and quite permissive. I believe licenses that achieve these goals are best for the growth of the open source
community.

## What is the point of this mod?

There is a neat combination of mechanics in MineCraft regarding villagers and zombies (and zombie villagers):
1. When a zombie kills a villager, the villager might turn into a zombie villager instead of merely dying.
2. When a zombie villager is cured, it turns back into a villager and offers discounts on its trades.

Putting these two mechanics together, and with a little redstone know-how, we can create trading halls where villagers
are converted to zombies and cured again, providing players with cheap trades for fun and profit. 

Unfortunately there is a catch: the conversion from a villager to a zombie villager is only guaranteed when playing on
Hard mode. On Normal mode, the villager converts only half the time, and on Easy and Peaceful modes the villager always
dies without converting. This means trading is actually easier the higher difficulty you play on, which is odd to say
the least. On top of that, not everyone is interested in playing on Hard mode, and asking them to switch just to be able
to use this useful and fun mechanic is asking too much.

That's what this mod is for. With it, we can set the chance of conversion to 100% so that we never again lose a villager
to a zombie. Actually, the mod gives complete control over the conversion: you could decide that you always want
villagers to die when killed by zombies, even when playing on Hard mode.

## Getting Started

1. Get the `.jar` file from the [latest release](https://github.com/kwvanderlinde/VillagerConversionRateFabricMod/releases/latest)
   on GitHub.
2. Drop the `.jar` file into the `mods/` directory of your fabric installation.
3. (Re)Start the game.
4. Edit the configurations.
   - If you installed the mod on your MineCraft client, then open the Mod Menu, select the `Villager Conversion Rate` mod,
     and click the configuration icon to get to the configuration GUI.
     - From here, makes sure to enable the mod, and set the conversion rate to the desired percentage.
     - Click "Save & Quit" to apply the changes.
   - If you installed the mod on a server, open the `configs/villager_conversion_rate.json` file.
     - Set `"enabled"` to `true` and set `"conversion-rate"` to a number between 0.0 and 1.0 (0.0 means never convert
       villagers when they die to a zombie, and 1.0 means always convert villagers).
     - Save the file and restart the server to apply the new configurations.
5. Enjoy your new zombie/villager/zombie villager mechanics.