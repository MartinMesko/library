package test.Lekcia_28;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;


import static test.Lekcia_28.RentalsPage.queueItemsFilePatch;

public class MessagesPage {
    private final Scanner scanner;
    private InputValidator inputValidator;

    public MessagesPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
    }

    public void showQueueMembers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(queueItemsFilePatch))) {
            System.out.println("Messages page");

            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int memberId = Integer.parseInt(parts[0].trim());
                    String memberName = RentalsPage.getMemberName(memberId);
                    System.out.println(lineNumber+1 + " - " + memberName);
                    lineNumber++;
                }
            }

            System.out.print("Choose an option: ");
            int choice = inputValidator.getValidatedChoice(lineNumber);

            if (choice >= 1 && choice <= lineNumber) {

                checkMemberMessages(choice);
            } else {
                System.out.println("Invalid option. Please enter a valid number.");
            }

            System.out.println("Press enter to continue...");
            scanner.nextLine();
            LibraryApp.showMainMenu();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkMemberMessages(int memberChoice) {
        String message = readMemberMessage(memberChoice);

        if (message != null) {
            System.out.println(message);
        } else {
            System.out.println("No messages to display...");
        }
    }

    public String readMemberMessage(int memberChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader(queueItemsFilePatch))) {
            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int memberId = Integer.parseInt(parts[0].trim());
                    String memberName = RentalsPage.getMemberName(memberId);
                    if (lineNumber == memberChoice) {
                        String lastPart = parts[parts.length - 1].trim();
                        if (lastPart.equalsIgnoreCase("true")) {
                            int titleId = Integer.parseInt(parts[2].trim());
                            String titleName = RentalsPage.getTitleName(titleId);

                            return "Sent: " + LocalDate.now() + " - Subject: " + memberName + " - " +
                                    titleName + " is available now!\n" +
                                    "Dear Mr./Mrs. " + RentalsPage.getMemberName(memberId) + ",\n" +
                                    "the title " + titleName + " is available for rent!\n" +
                                    "Best regards, The Library Team <3";
                        }
                    }
                    lineNumber++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
