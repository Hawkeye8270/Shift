package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        String outputDir = ".";
        String prefix = "";
        boolean append = false;
        boolean fullStats = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                outputDir = args[++i];
            } else if (args[i].equals("-p")) {
                prefix = args[++i];
            } else if (args[i].equals("-a")) {
                append = true;
            } else if (args[i].equals("-f")) {
                fullStats = true;
            }
        }

        List<String> inputFiles = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) {
                inputFiles.add(args[i]);
            }
        }

        List<Long> integers = new ArrayList<>();
        List<Double> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    try {
                        long num = Long.parseLong(line);
                        integers.add(num);
                    } catch (NumberFormatException e) {
                        try {
                            double num = Double.parseDouble(line);
                            if (num % 1 != 0) {
                                floats.add(num);
                            }
                        } catch (NumberFormatException ex) {
                            strings.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + inputFile + " - " + e.getMessage());
            }
        }

        writeData(integers, outputDir, prefix, "integers", append);
        writeData(floats, outputDir, prefix, "floats", append);
        writeData(strings, outputDir, prefix, "strings", append);

        printStatisticsFull(integers, floats, strings, fullStats);
    }

    public static void writeData(List<?> data, String outputDir, String prefix, String suffix, boolean append) {
        if (data.isEmpty()) return;
        String filename = outputDir + "/" + prefix + suffix + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            for (Object item : data) {
                writer.write(item.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename + " - " + e.getMessage());
        }
    }

    public static void printStatisticsFull(List<Long> integers, List<Double> floats, List<String> strings, boolean fullStats) {
        printStatisticsNum(integers, "Integers", fullStats);
        printStatisticsNum(floats, "Floats", fullStats);
        printStatisticsString(strings, "Strings", fullStats);
    }

    public static void printStatisticsNum(List<? extends Number> data, String type, boolean fullStats) {
        if (data.isEmpty()) return;
        System.out.println("\nСтатистика для " + type + ":");
        System.out.println("  Количество: " + data.size());
        if (fullStats) {
            double min = data.stream().mapToDouble(Number::doubleValue).min().orElse(0);
            double max = data.stream().mapToDouble(Number::doubleValue).max().orElse(0);
            double sum = data.stream().mapToDouble(Number::doubleValue).sum();
            double avg = sum / data.size();
            System.out.println("  Мин.: " + min);
            System.out.println("  Макс.: " + max);
            System.out.println("  Сумма: " + sum);
            System.out.println("  Среднее: " + avg);
        }
    }

    public static void printStatisticsString(List<String> data, String type, boolean fullStats) {
        if (data.isEmpty()) return;
        System.out.println("\nСтатистика для " + type + ":");
        System.out.println("  Количество: " + data.size());
        if (fullStats) {
            int minLen = data.stream().mapToInt(String::length).min().orElse(0);
            int maxLen = data.stream().mapToInt(String::length).max().orElse(0);
            System.out.println("  Минимальная длина: " + minLen);
            System.out.println("  Максимальная длина: " + maxLen);
        }
    }
}


