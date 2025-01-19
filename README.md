# PO Project - DARWIN WORLD

* [General info](#general-info)
* [Features](#features)
* [How It Works](#how-it-works)

## General info
This project simulates an ecosystem where animals live and interact with the environment on a dynamic map. The animals roam the map, eating grass to survive while being exposed to environmental challenges, such as the rising water levels from a tidal system. The simulation also introduces a genetic mutation mechanism that influences the reproduction of animals, creating random genetic variations. 

The project includes a graphical interface to visualize the map, animals, and changing environmental factors. Additionally, the application uses multithreading, allowing users to run multiple instances of the simulation simultaneously.

## Features
- **Dynamic Map**: The map consists of tiles that can either be land or water. Water levels fluctuate due to a tidal system, which primarily affects areas next to water, posing a risk to the animals.
- **Animal Behavior**: Animals move across the map in search of grass to eat, gaining energy. If they don’t find enough food, they eventually die from starvation. Animals also reproduce, passing on their genes, which may undergo mutations.
- **Genetic Mutation**: During reproduction, a random mutation can occur, altering one of the animal's genes slightly. This creates diversity in the population and affects their survival capabilities.
- **Tidal System**: The water levels rise and fall periodically. The system primarily affects the areas near water, flooding tiles adjacent to water and making them dangerous for the animals. If the water reaches the animals, they drown and die.
- **Grass Growth and Equator System**: Grass grows on the map and serves as the primary food source for animals. The equator, running through the middle of the map, has a higher chance of grass growth. This makes the areas near the equator more fertile, providing a greater food source for the animals.
- **Graphical Interface**: A user interface displays the map in real-time, showing the animals, grass, and water levels. It allows users to observe the ecosystem's evolution and the interactions between animals and their environment.
- **Multithreading**: The application supports running multiple instances of the simulation concurrently, enabling users to observe different scenarios and configurations at the same time.

## How It Works
The simulation is built around a grid-based map, where each tile can either be land or water. The map is subject to periodic tidal changes. When the water level rises, it only affects tiles that are adjacent to water, flooding them. These flooded tiles pose a threat to the animals, as they may get trapped and drown if they fail to find dry ground.

Animals are initially placed randomly on the map, where they must find food (grass) to survive. If they don’t graze enough, they lose energy and eventually die. During reproduction, animals pass on their genes to their offspring, with a chance of mutation that may shift one of the genes slightly, adding a layer of unpredictability to the offspring's traits.

The tidal system causes water levels to periodically rise, flooding the tiles next to the water. This flooding forces the animals to move to higher ground. If they fail to do so in time, the rising water will reach them, and they will drown, resulting in their death.

Additionally, the map features an equator, a horizontal line passing through the middle of the map. This equator has a higher chance of having grass grow on the tiles. As a result, the areas near the equator are more fertile, providing more food for the animals and making these areas more attractive for the animals to roam. 

As the simulation progresses, users can observe the behavior of animals, the growth of grass, and the shifting water levels. The graphical interface provides a real-time view of the ecosystem, allowing users to track changes in animal populations and their interactions with the environment.

This system offers a way to explore survival dynamics, ecological interactions, and the impact of random genetic mutations in a constantly evolving environment.
