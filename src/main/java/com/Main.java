package com;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Path to the file that describes the water pipeline system: ");
        String systemWaterPipeLineCSV = sc.next();
        // "/Users/mark/Documents/microservice/movie-project/dbBestTestTask/src/main/resources/waterSystem.csv"

        System.out.println("Path to the file with a set of points: ");
        String pointsCSV = sc.next();
        // "/Users/mark/Documents/microservice/movie-project/dbBestTestTask/src/main/resources/Routes.csv"

        System.out.println("Output folder: ");
        String outputFolder = sc.next();
        // "/Users/mark/Documents/microservice/movie-project/dbBestTestTask/src/main/resources/"
        sc.close();

        Worker worker = new Worker(systemWaterPipeLineCSV, pointsCSV, outputFolder);
        worker.checkRoutes();
    }


}
