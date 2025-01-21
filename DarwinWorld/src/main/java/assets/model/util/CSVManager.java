package assets.model.util;

import assets.controllers.ConfigController;
import assets.controllers.SimulationController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class CSVManager {

    public static void writeConfigFile(File filePath, ConfigController c) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write("Parameter, Value");

        writer.write(String.format("\nMap Height, %s", c.mapHeightField.getText()));
        writer.write(String.format("\nMap Width, %s", c.mapWidthField.getText()));
        writer.write(String.format("\nMap Type, %s", c.waterMapButton.isSelected() ? "WATER" : "DEFAULT"));
        writer.write(String.format("\nMap Water Level, %s", c.mapWaterLevelField.getText()));
        writer.write(String.format("\nMap Flows Duration, %s", c.mapFlowsDurationField.getText()));
        writer.write(String.format("\nAnimal Start Number, %s", c.animalStartNumberField.getText()));
        writer.write(String.format("\nAnimal Start Energy, %s", c.animalStartEnergyField.getText()));
        writer.write(String.format("\nAnimal Genome Length, %s", c.animalGenomeLengthField.getText()));
        writer.write(String.format("\nAnimal Min Fed Energy, %s", c.animalMinFedEnergyField.getText()));
        writer.write(String.format("\nAnimal Birth Cost, %s", c.animalBirthCostField.getText()));
        writer.write(String.format("\nGrass Daily, %s", c.grassDailyField.getText()));
        writer.write(String.format("\nGrass Energy, %s", c.grassEnergyField.getText()));
        writer.write(String.format("\nMin Animal Mutations, %s", c.minAnimalMutationField.getText()));
        writer.write(String.format("\nMax Animal Mutations, %s", c.maxAnimalMutationField.getText()));
        writer.write(String.format("\nAnimal Mutation Chance, %s", c.animalMutationChanceField.getText()));

        writer.close();

    }

    public static Map<String, String> readConfigFile(File filePath) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        Map<String, String> configParameters = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            String[] args = line.split(",");
            if (args.length == 2) {
                String parameter = args[0].replaceAll("\\s+", "");
                String value = args[1].replaceAll("\\s+", "");
                configParameters.put(parameter, value);
            }
        }

        reader.close();
        return configParameters;
    }

    public static void logChangesToCsvFile(File filePath, SimulationController c) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));

        writer.append(String.format("\nSimulation Day, %s", c.dayCounter.getText()));
        writer.append(String.format("\nNumber Of Animals, %s", c.numOfAnimals.getText()));
        writer.append(String.format("\nNumber Of Grasses, %s", c.numOfGrasses.getText()));
        writer.append(String.format("\nNumber Of Empty Positions, %s", c.numOfEmptyPositions.getText()));
        writer.append(String.format("\nAverage Animal Energy, %s", c.averageAnimalEnergy.getText()));
        writer.append(String.format("\nAverage Number Of Children, %s", c.averageChildren.getText()));
        writer.append(String.format("\nMost Popular Genome Among Animals, %s", c.mostPopularGenome.getText()));
        writer.append(String.format("\nSelected Animal's Genome, %s", c.animalGenome.getText()));
        writer.append(String.format("\nSelected Animal's Gene, %s", c.animalGene.getText()));
        writer.append(String.format("\nSelected Animal's Energy, %s", c.animalEnergy.getText()));
        writer.append(String.format("\nSelected Animal's Grass Eaten, %s", c.animalGrass.getText()));
        writer.append(String.format("\nSelected Animal's Children, %s", c.animalChildren.getText()));
        writer.append(String.format("\nSelected Animal's Descendants, %s", c.animalDescendants.getText()));
        writer.append(String.format("\nSelected Animal's Age, %s", c.animalAge.getText()));
        writer.append(String.format("\nSelected Animal's Death Day, %s", c.animalDeathDay.getText()));
        writer.append(String.format("\nSelected Animal's Birthday, %s", c.animalBirthDay.getText()));

        writer.append("\n\n\n");

        writer.close();

    }

}
