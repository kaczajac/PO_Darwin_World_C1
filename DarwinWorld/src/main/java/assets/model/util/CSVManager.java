package assets.model.util;

import assets.controllers.ConfigController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class CSVManager {

    public void writeConfigFile(File fileName, ConfigController c) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

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

        writer.close();

    }

    public Map<String, String> readConfigFile(File fileName) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        Map<String, String> configParameters = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            String[] args = line.split(",");
            if (args.length == 2) {
                String parameter = args[0].replaceAll("\\s+", "");;
                String value = args[1].replaceAll("\\s+", "");;
                configParameters.put(parameter, value);
            }
        }

        reader.close();
        return configParameters;
    }

}
