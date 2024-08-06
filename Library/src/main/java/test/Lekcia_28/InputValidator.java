package test.Lekcia_28;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public String validationCheckString() {
        String input = scanner.nextLine();
        try {
            Integer.parseInt(input);
            System.out.println("Please enter a valid value.");
            return validationCheckString();
        } catch (NumberFormatException e) {
            return input;
        }
    }
    public int validationCheckInt() {
        int number;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number:");
                scanner.next();
            }
            number = scanner.nextInt();
            if (number < 0) {
                System.out.println("The number cannot be negative. Please enter a valid non-negative number:");
            }
        } while (number < 0);
        return number;
    }

    public String validationCheckISBN() {
        String input = scanner.nextLine();
        try {
            Long.parseLong(input);
            return input;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid value.");
            return validationCheckISBN();
        }
    }

    public int getValidatedChoice(int numberOfCase) {
        String input = scanner.nextLine();
        try {
            int inputNumber = Integer.parseInt(input);
            if (inputNumber < 1 || inputNumber > numberOfCase){
                System.out.println("Please enter a number in the range from 1 to " + numberOfCase);
                return getValidatedChoice(numberOfCase);
            }
            return inputNumber;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid value.");
            return getValidatedChoice(numberOfCase);
        }
    }

    public String validateDateOfBirthInput() {
        String userInput = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate.parse(userInput, formatter);
            return userInput;
        } catch (DateTimeParseException e) {
            System.out.println("Please enter a valid value.");
            return validateDateOfBirthInput();
        }
    }

    public int validationChecksTheRemovedNumber(int maxNumber) {
        while (true) {
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= maxNumber) {
                    return choice;
                } else {
                    System.out.println("Please enter a number in the range from 1 to " + maxNumber);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid value.");
            }
        }
    }


}
