# MXChallenges - New Challenges for [MUtils](https://github.com/mutils-mc/mutils)

MXChallenges is an ansemble of Challenges that are not in MUtils, but should be! It simultaneously presents a new Way
to create and Add new MUtils Addons without needing to do multiple steps in multiple files, simply create a Challenge and add it to the enum!

# Current Challenges

## ChunkEffects
Every Chunk gives you a different effect

## Block Breaking Spawner
Every Block you break spawns a different mob. 

## More Damagers
More options to damage yourself! Damage on Achievement, Inventory Interaction, Left Click, Right Click, and even on move (not sure how you would even do this)!

## Damage Punishers
If you take damage, You get a punishment! Inventory Clear, Shuffled Inventory, Boost to the Sky!


# New Addom creation System
Creating MUtils addons is extremely easy, but it still is a bit clunky and can get quite confusing fast, as
ChallengeData and style is saved in the Adddons Enum for all Challenges. MXChallenges presents a new way: 

Just extend the `AbstractAddon()` class and input the Challenge Info alongside the logic in one File! Just add your 
Challenge to the Addons Enum Afterward and your good to go without digging in huge lists of maps and Settings
