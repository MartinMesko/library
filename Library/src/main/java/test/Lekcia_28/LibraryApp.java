package test.Lekcia_28;

import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TitlesPage titlesPage = new TitlesPage(scanner);
    private static final MembersPage membersPage = new MembersPage(scanner);
    private static final RentalsPage rentalsPage = new RentalsPage(scanner);

    private static final MessagesPage messages = new MessagesPage(scanner);

    private static final InputValidator inputValidator = new InputValidator(scanner);
    private static boolean exitRequested = false;

    public static void main(String[] args) {
        System.out.println(">>>> Welcome to our Library <<<<");
        showMainMenu();
        if (exitRequested) {
            System.out.println("Exiting the application...");
        }
    }

    public static void showMainMenu() {
        System.out.println("1 - Titles");
        System.out.println("2 - Members");
        System.out.println("3 - Rentals");
        System.out.println("4 - Messages");
        System.out.println("5 - Exit");
        System.out.print("Choose an option: ");

        switch (inputValidator.getValidatedChoice(5)) {
            case 1 -> titlesPage.showTitlesMenu();
            case 2 -> membersPage.showMembersMenu();
            case 3 -> rentalsPage.showRentalsMenu();
            case 4 -> messages.showQueueMembers();
            case 5 -> exitRequested = true;
        }
        while(!exitRequested);
    }

}
