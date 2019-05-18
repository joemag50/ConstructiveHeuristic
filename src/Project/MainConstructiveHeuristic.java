/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author reydb
 */
public class MainConstructiveHeuristic {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int p = 0;
        ArrayList<ArrayList<Double>> distancesClientsSites = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> distancesSitesSites = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> positionsClients = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> positionsSites = new ArrayList<ArrayList<Double>>();

        System.out.println("Name of the instance:");
        String filename = sc.nextLine();

        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(filename))) {
            String currentLine;
            int data = 0;
            int line = 0;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.equals("")) {
                    data++;
                } else {
                    String[] splittedLine = currentLine.split(" ");

                    ArrayList<Double> splittedDouble = new ArrayList<Double>();;
                    for (String s : splittedLine) {
                        String replace = s.replace(",", ".");
                        splittedDouble.add(Double.parseDouble(replace));
                    }

                    switch (data) {
                        case 0: {
                            p = (int) Math.round(splittedDouble.get(0));
                            break;
                        }
                        case 1: {
                            distancesClientsSites.add(splittedDouble);
                            break;
                        }
                        case 2: {
                            distancesSitesSites.add(splittedDouble);
                            break;
                        }
                        case 3: {
                            positionsClients.add(splittedDouble);
                            break;
                        }
                        case 4: {
                            positionsSites.add(splittedDouble);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] matrixDistances = new double[distancesClientsSites.size()][];
        for (int i = 0; i < matrixDistances.length; i++) {
            matrixDistances[i] = new double[distancesClientsSites.get(i).size()];
        }
        for (int i = 0; i < distancesClientsSites.size(); i++) {
            for (int j = 0; j < distancesClientsSites.get(i).size(); j++) {
                matrixDistances[i][j] = distancesClientsSites.get(i).get(j);
            }
        }

        System.out.println("");
        System.out.println("");
        System.out.println(Arrays.deepToString(matrixDistances));
        for (int i = 0; i < matrixDistances.length; i++) {
            for (int j = 0; j < matrixDistances[i].length; j++) {
                System.out.print(matrixDistances[i][j]);
                if ((j + 1) < matrixDistances[i].length) {
                    System.out.print("\t");
                }
            }
            System.out.println("");
        }

        ConstructiveHeuristic constructiveHeuristic = new ConstructiveHeuristic(filename, p, distancesClientsSites);
        constructiveHeuristic.HeuristicProcedure();
    }

}