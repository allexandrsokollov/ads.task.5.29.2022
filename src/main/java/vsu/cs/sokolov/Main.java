package vsu.cs.sokolov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new MainForm();

    }

    public static String getNextLineFromConsole(String welcomeMessage) {
        System.out.println(welcomeMessage);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}

