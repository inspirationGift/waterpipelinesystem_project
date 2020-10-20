package com;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please, put the filepath of the water pipeline system: ");
        String systemWaterPipeLineCSV = sc.next();

        System.out.println("Please, put the filepath of the points, seeking routes: ");
        String pointsCSV = sc.next();

        System.out.println("Please, put path name of output folder: ");
        String outputFolder = sc.next();
        sc.close();

        Worker worker = new Worker();

        worker.loadFiles(systemWaterPipeLineCSV, pointsCSV);
        worker.findRoutes();
        worker.uploadResults(outputFolder);

    }
}
