/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author reydb
 */
public class ConstructiveHeuristic {

    private final String filename;
    private final double[][] matrixDistances;
    private final int p;
    private ArrayList<ArrayList<Double>> distancesClientsSites = new ArrayList<>();

    ConstructiveHeuristic(String filename, int p, ArrayList<ArrayList<Double>> distancesClientsSites) {

        this.filename = filename;
        this.p = p;
        this.distancesClientsSites = distancesClientsSites;
        double[][] matrixDistances = new double[distancesClientsSites.size()][];
        for (int i = 0; i < matrixDistances.length; i++) {
            matrixDistances[i] = new double[distancesClientsSites.get(i).size()];
        }
        for (int i = 0; i < distancesClientsSites.size(); i++) {
            for (int j = 0; j < distancesClientsSites.get(i).size(); j++) {
                matrixDistances[i][j] = distancesClientsSites.get(i).get(j);
            }
        }
        this.matrixDistances = matrixDistances;
    }

    public void HeuristicProcedure() {

        long startTime = System.nanoTime();
        int n = matrixDistances[0].length;
        ArrayList<Integer> facilities = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            facilities.add(i);
        }
//        System.out.println(facilities);

        //1. V (Vector of facilities to build) <- ∅ 
        ArrayList<Integer> selectedFacilities = new ArrayList<Integer>();

        //2. Choose randomly a facility and add it in V
        Random random = new Random(System.currentTimeMillis());
        int randomFacility = random.nextInt(n);
        selectedFacilities.add(randomFacility);
        facilities.remove(facilities.indexOf(randomFacility));

        //3. While the number of facilities V is lower than P
        while (selectedFacilities.size() < p) {
            boolean firstOne = true;
            int farthestClient = 0;
            double farthestClientDistance = 0.0;
            double temporalFarthestClientDistance = 0.0;

            //3.a. Select the farthest client to the facilities in selectedFacilities
            for (int facility : selectedFacilities) {
                for (int client = 0; client < matrixDistances.length; client++) {
                    if (firstOne == true) {
                        farthestClient = client;
                        farthestClientDistance = matrixDistances[client][facility];
                        firstOne = false;
                    } else {
                        temporalFarthestClientDistance = matrixDistances[client][facility];
                        if (temporalFarthestClientDistance > farthestClientDistance) {
                            farthestClient = client;
                            farthestClientDistance = matrixDistances[client][facility];
                        }
                    }
                }
            }

            //3.b. Select the nearest facility, not belonging in V, to this client and add it to V
            firstOne = true;
            int nearestFacility = 0;
            double nearestFacilityDistance = 0.0;
            double temporalNearestFacilityDistance = 0.0;
            for (int facility : facilities) {
                if (firstOne == true) {
                    nearestFacility = facility;
                    nearestFacilityDistance = matrixDistances[farthestClient][facility];
                    firstOne = false;
                } else {
                    temporalNearestFacilityDistance = matrixDistances[farthestClient][facility];
                    if (temporalNearestFacilityDistance < nearestFacilityDistance) {
                        nearestFacility = facility;
                        nearestFacilityDistance = matrixDistances[farthestClient][facility];
                    }

                }
            }
            selectedFacilities.add(nearestFacility);
            facilities.remove(facilities.indexOf(nearestFacility));
        }

//        System.out.println("");
//        System.out.println("SELECTED FACILITIES");
//        System.out.println(selectedFacilities);
//        System.out.println("");
        //4. Distribute the clients to their nearest facilities and compute the total distance
        int nearestFacility = 0;
        double nearestFacilityDistance = 0.0;
        double temporalNearestFacilityDistance = 0.0;
        ArrayList<ArrayList<Integer>> clientsFacilities = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> temporalArray = new ArrayList<Integer>();
            clientsFacilities.add(temporalArray);
        }

        for (int client = 0; client < matrixDistances.length; client++) {
            boolean firstOne = true;
            for (int facility : selectedFacilities) {
                if (firstOne == true) {
                    nearestFacility = facility;
                    nearestFacilityDistance = matrixDistances[client][facility];
                    firstOne = false;
                } else {
                    temporalNearestFacilityDistance = matrixDistances[client][facility];
                    if (temporalNearestFacilityDistance < nearestFacilityDistance) {
                        nearestFacility = facility;
                        nearestFacilityDistance = matrixDistances[client][facility];
                    }
                }
            }
            clientsFacilities.get(nearestFacility).add(client);
        }
        System.out.println(clientsFacilities);
//        for (int facility : selectedFacilities) {
//            System.out.println("Facility " + facility);
//            System.out.println(clientsFacilities.get(facility));
//            System.out.println("");
//        }

//        double totalDistance = 0.0;
//        for (int facility : selectedFacilities) {
//            for (int client : clientsFacilities.get(facility)) {
//                totalDistance += matrixDistances[client][facility];
//            }
//        }
//        System.out.println("Total distance " + totalDistance);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total execution time " + totalTime + " nanoseconds");

        boolean firstOne = true;
        int farthestFacility = 0;
        int farthestClient = 0;
        double maximumDistance = 0.0;
        double temporalMaximumDistance = 0.0;
        System.out.println(clientsFacilities.size());
        for (int facility : selectedFacilities) {
            for (int client : clientsFacilities.get(facility)) {
                if (firstOne == true) {
                    maximumDistance = matrixDistances[client][facility];
                    farthestFacility = facility;
                    farthestClient = client;
                    firstOne = false;
                } else {
                    temporalMaximumDistance = matrixDistances[client][facility];
                    if (temporalMaximumDistance > maximumDistance) {
                        maximumDistance = temporalMaximumDistance;
                        farthestFacility = facility;
                        farthestClient = client;
                    }
                }
            }
        }
        System.out.println("The maximum distance is " + maximumDistance + " of facility " + farthestFacility + " and client " + farthestClient);

        File file = new File(String.format("%s-ConstructiveHeuristic.dat", this.filename));
        try {
            if (file.createNewFile()) {
                System.out.println(String.format("File is created %s!", file.getName()));
            } else {
                System.out.println("File already exists.");
            }

            FileWriter writer = new FileWriter(file);

            writer.write(String.format("%s\n\n", this.p));

            for (int facility : selectedFacilities) {
                writer.write(String.format("%s", facility));
                writer.write("\n");
            }
            writer.write(String.format("\n"));

            for (int j = 0; j < matrixDistances.length; j++) {
                for (int k = 0; k < matrixDistances[0].length; k++) {
                    writer.write(String.format("%.2f ", matrixDistances[j][k]));
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}