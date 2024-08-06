package test.Lekcia_28;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TitlesPage {
    private final Scanner scanner;

    private InputValidator inputValidator;

    public static final List<Book> books = new ArrayList<>();

    public static final List<DVD> dvds = new ArrayList<>();
    private final String lineSeparator = System.lineSeparator();

    public static int totalTitlesCount = 0;
    public static String bookFilePath = "src/main/resources/titlesBook.txt";
    public static String dvdFilePath = "src/main/resources/titlesDVD.txt";

    public TitlesPage(Scanner scanner) {
        this.inputValidator = new InputValidator(scanner);
        this.scanner = scanner;
        loadTitles();
    }

    public TitlesPage() {
        this.scanner = new Scanner(System.in);
    }

    public static int loadTitlesFromFile(String filePath, String type) throws IOException {
        int addedTitlesCount = 0;
        BufferedReader reader = null;
        try {
            File titlesFile = new File(filePath);
            reader = new BufferedReader(new FileReader(titlesFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    if (type.equals("Book") && parts.length >= 6) {
                        books.add(new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), "Book"));
                        addedTitlesCount++;
                    } else if (type.equals("DVD") && parts.length >= 5) {
                        int duration = Integer.parseInt(parts[2]);
                        int numberOfTracks = Integer.parseInt(parts[3]);
                        int availableCopies = Integer.parseInt(parts[4]);
                        dvds.add(new DVD(parts[0], parts[1], duration, numberOfTracks, availableCopies, Integer.parseInt(parts[5]), "DVD"));
                        addedTitlesCount++;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number from line: " + line);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        totalTitlesCount += addedTitlesCount;
        return addedTitlesCount;
    }

    public void loadTitles() {
        try {
            totalTitlesCount += loadTitlesFromFile(bookFilePath, "Book");
            totalTitlesCount += loadTitlesFromFile(dvdFilePath, "DVD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTitlesMenu() {
        System.out.println("Titles ");
        System.out.println("1 - Show All Titles");
        System.out.println("2 - Add Title");
        System.out.println("3 - Remove Title");
        System.out.println("4 - Back");
        System.out.print("Choose an option: ");

        switch (inputValidator.getValidatedChoice(4)) {
            case 1 -> showAllTitles();
            case 2 -> addTitle();
            case 3 -> deleteTitle();
            case 4 -> {
                goBack();
                LibraryApp.showMainMenu();
            }
            default -> showTitlesMenu();
        }
    }

    private int displayTitleWithNumber(Object title, int startingNumber) {
        if (title instanceof Book book) {
            System.out.println(startingNumber + book.toString());
        } else if (title instanceof DVD dvd) {
            System.out.println(startingNumber + dvd.toString());
        }
        return startingNumber + 1;
    }

    public void showAllTitles() {
        System.out.println("All Titles:");
        int titleCounter = 1;

        for (Book book : books) {
            titleCounter = displayTitleWithNumber(book, titleCounter);
        }
        for (DVD dvd : dvds) {
            titleCounter = displayTitleWithNumber(dvd, titleCounter);
        }

        System.out.println("Total number of all titles: " + totalTitlesCount);
        System.out.println(lineSeparator + "Press enter to return to Titles menu...");
        scanner.nextLine();
        showTitlesMenu();
    }

    public void addTitle() {
        System.out.println("Add title");
        System.out.println("1 - Add book");
        System.out.println("2 - Add Dvd");
        System.out.println("3 - Back");
        System.out.print("Choose an option: ");

        switch (inputValidator.getValidatedChoice(3)) {
            case 1 -> addBook();
            case 2 -> addDVD();
            case 3 -> showTitlesMenu();
            default -> addTitle();
        }
    }

    public void addBook() {
        System.out.print("Enter Author's name: ");
        String author = inputValidator.validationCheckString();
        System.out.print("Enter title name: ");
        String name = inputValidator.validationCheckString();
        System.out.print("Enter available copies: ");
        int copies = inputValidator.validationCheckInt();
        System.out.print("Enter ISBN: ");
        String isbn = inputValidator.validationCheckISBN();
        System.out.print("Enter number of Pages: ");
        int pages = inputValidator.validationCheckInt();
        System.out.print("Enter Title ID: ");
        int titleId = inputValidator.validationCheckInt();

        boolean result = saveTitle(new Book(name, author, isbn, pages, copies, titleId, "Book"));
        if (result) {
            System.out.println("Book added succesfully..." + lineSeparator + "Press enter to continue...");
            scanner.nextLine();
            showTitlesMenu();
        } else {
            System.out.println("Failed to add the book.");
        }
    }


    public void addDVD() {
        System.out.print("Enter Author's name: ");
        String author = inputValidator.validationCheckString();
        System.out.print("Enter title name: ");
        String name = inputValidator.validationCheckString();
        System.out.print("Enter available copies: ");
        int copies = inputValidator.validationCheckInt();
        System.out.print("Enter Length (minutes): ");
        int length = inputValidator.validationCheckInt();
        System.out.print("Enter number of chapters: ");
        int chapters = inputValidator.validationCheckInt();
        System.out.print("Enter Title ID: ");
        int titleId = inputValidator.validationCheckInt();

        boolean result = saveTitle(new DVD(name, author, chapters, length, copies, titleId, "DVD"));
        if (result) {
            System.out.println("DVD added succesfully..." + lineSeparator + "Press enter to continue...");
            scanner.nextLine();
            showTitlesMenu();
        } else {
            System.out.println("Failed to add the DVD.");
        }
    }

    public boolean saveTitle(Object title) {
        try {
            String fileName;
            String titleString;

            if (title instanceof Book book) {
                fileName = bookFilePath;
                titleString = book.getAuthorName() + "," + book.getTitle() + "," + book.getIsbn() + "," + book.getPageCount() + "," + book.getAvailableCopies() + "," + book.getTitleId();
                books.add(book);
            } else {
                DVD dvd = (DVD) title;
                fileName = dvdFilePath;
                titleString = dvd.getAuthorName() + "," + dvd.getTitle() + "," + dvd.getNumberOfTracks() + "," + dvd.getDurationInMinutes() + "," + dvd.getAvailableCopies() + "," + dvd.getTitleId();
                dvds.add(dvd);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(titleString + lineSeparator);
            }
            totalTitlesCount++;

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteTitle() {
        if (totalTitlesCount == 0) {
            System.out.println("No titles to remove. Press enter to return to titles...");
            scanner.nextLine();
            showTitlesMenu();
            return;
        }
        System.out.println("Remove Title Page");
        showAllTitlesWithoutReturn();

        System.out.print("Select a title to delete: ");
        int titleNumber = inputValidator.validationChecksTheRemovedNumber(getTotalTitlesCount());

        try {
            File titlesFile = new File(bookFilePath);
            List<String> bookLines = Files.readAllLines(titlesFile.toPath(), StandardCharsets.UTF_8);

            File titlesDVDFile = new File(dvdFilePath);
            List<String> dvdLines = Files.readAllLines(titlesDVDFile.toPath(), StandardCharsets.UTF_8);

            if (deleteBookTitle(titleNumber - 1, bookLines)) {
                books.remove(titleNumber - 1);
                Files.write(titlesFile.toPath(), bookLines, StandardCharsets.UTF_8);
            } else if (deleteDvdTitle(titleNumber - 1, bookLines, dvdLines)) {
                dvds.remove(titleNumber - bookLines.size() - 1);
                Files.write(titlesDVDFile.toPath(), dvdLines, StandardCharsets.UTF_8);
            } else {
                System.out.println("Title not found.");
                return;
            }

            totalTitlesCount--;
            System.out.println("Title removed successfully!" + lineSeparator + "Press enter to continue...");
            scanner.nextLine();
            showTitlesMenu();

        } catch (IOException e) {
            System.out.println("Title not removed.");
            e.printStackTrace();
        }
    }

    public boolean deleteDvdTitle(int titleNumber, List<String> bookLines, List<String> dvdLines) {
        if (titleNumber >= bookLines.size() && titleNumber < (bookLines.size() + dvdLines.size())) {
            dvdLines.remove(titleNumber - bookLines.size());
            return true;
        }
        return false;
    }

    public boolean deleteBookTitle(int titleNumber, List<String> bookLines) {
        if (titleNumber < 0 || titleNumber >= bookLines.size()) {
            return false;
        }

        bookLines.remove(titleNumber);
        return true;
    }


    public void showAllTitlesWithoutReturn() {
        System.out.println("All titles:");
        int titleCounter = 1;

        for (Book book : books) {
            titleCounter = displayTitleWithNumber(book, titleCounter);
        }
        for (DVD dvd : dvds) {
            titleCounter = displayTitleWithNumber(dvd, titleCounter);
        }

        System.out.println("Total number of all titles: " + totalTitlesCount);
    }

    public int getTotalTitlesCount() {
        return totalTitlesCount;
    }

    public void updateBooksFile() {
        Path path = Paths.get(bookFilePath);
        List<String> updatedBooks = new ArrayList<>();

        for (Book book : books) {
            String bookData = book.getAuthorName() + "," +
                    book.getTitle() + "," +
                    book.getIsbn() + "," +
                    book.getPageCount() + "," +
                    book.getAvailableCopies() + "," +
                    book.getTitleId();
            updatedBooks.add(bookData);
        }

        try {
            Files.write(path, updatedBooks);
        } catch (IOException e) {
            System.out.println("Failed to update books data.");
            e.printStackTrace();
        }
    }


    public void updateDVDsFile() {
        Path path = Paths.get(dvdFilePath);
        List<String> updatedDVDs = new ArrayList<>();

        for (DVD dvd : dvds) {
            String dvdData = dvd.getAuthorName() + "," +
                    dvd.getTitle() + "," +
                    dvd.getNumberOfTracks() + "," +
                    dvd.getDurationInMinutes() + "," +
                    dvd.getAvailableCopies() + "," +
                    dvd.getTitleId();
            updatedDVDs.add(dvdData);
        }

        try {
            Files.write(path, updatedDVDs);
        } catch (IOException e) {
            System.out.println("Failed to update DVDs data.");
            e.printStackTrace();
        }
    }

    private void goBack() {
        System.out.println("Going back to main menu...");
    }



}
