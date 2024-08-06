package test.Lekcia_28;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RentalsPage {

    private  Scanner scanner;
    private TitlesPage titlesPage;
    private MembersPage membersPage;
    public static List<RentalEntries> rentalEntriesList = new ArrayList<>();
    public static List<QueueItems> queueItemsList = new ArrayList<>();
    public static final String rentalEntriesFilePatch = "src/main/resources/rentalEntries.txt";
    public static String queueItemsFilePatch = "src/main/resources/queueItems.txt";

    public InputValidator inputValidator;

    public RentalsPage(Scanner scanner) {
        this.scanner = scanner;
        this.titlesPage = new TitlesPage();
        this.membersPage = new MembersPage();
        this.inputValidator = new InputValidator(scanner);
        loadRentalEntriesFromFile();
        loadQueueItemsFromFile();
    }

    public RentalsPage() {

    }

    public void showRentalsMenu() {
        System.out.println("Rentals page");
        System.out.println("1 - Rent a title");
        System.out.println("2 - Return a title");
        System.out.println("3 - Prolong the rental");
        System.out.println("4 - Show all rentals");
        System.out.println("5 - Show rentals past due");
        System.out.println("6 - Show Queue");
        System.out.println("7 - Back");
        System.out.println("Choose an option: ");

        switch (inputValidator.getValidatedChoice(7)) {
            case 1 -> rentTitle();
            case 2 -> returnTitle();
            case 3 -> prolongRental();
            case 4 -> showAllRentals();
            case 5 -> showRentalsPastDue();
            case 6 -> showQueue();
            case 7 -> LibraryApp.showMainMenu();

            default -> showRentalsMenu();
        }
    }

    public boolean rentTitle() {
        System.out.println("Choose a member to rent a title:");
        int memberNumber = chooseMember();
        if (memberNumber == -1) {
            return false;
        }

        int titleNumber = chooseTitle();
        if (titleNumber == -1) {
            return false;
        }

        return processRental(memberNumber, titleNumber);
    }

    private int chooseMember() {
        membersPage.listingAllMembers();
        System.out.print("Choose an option: ");
        int memberNumber = inputValidator.getValidatedChoice(membersPage.getMembersCount());

        if (memberNumber <= 0 || memberNumber > membersPage.getMembersCount()) {
            System.out.println("Invalid member number");
            return -1;
        }
        System.out.println("Selected member: " + memberNumber);
        return memberNumber;
    }

    private int chooseTitle() {
        System.out.println("Choose a title to rent to:");
        titlesPage.showAllTitlesWithoutReturn();
        System.out.print("Choose an option: ");
        int titleNumber = inputValidator.getValidatedChoice(TitlesPage.totalTitlesCount);

        if (titleNumber <= 0 || titleNumber > TitlesPage.totalTitlesCount) {
            System.out.println("Invalid title number");
            return -1;
        }
        return titleNumber;
    }

    public boolean processRental(int memberNumber, int titleNumber) {
        if (isPossibleToRentTitle(memberNumber, titleNumber)) {
            System.out.println("Title rented successfully");
            System.out.println("Press enter to return to Rental page...");
            scanner.nextLine();
            showRentalsMenu();
            return true;
        } else {
            System.out.println("Title not rented.");
            System.out.println("Press enter to return to Rental page...");
            scanner.nextLine();
            showRentalsMenu();
            return false;
        }
    }


    private boolean isPossibleToRentTitle(int memberNumber, int titleNumber) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String rentedDate = currentDate.format(formatter);
        int totalTitles = TitlesPage.books.size() + TitlesPage.dvds.size();
        try {
            if (titleNumber <= totalTitles && titleNumber <= TitlesPage.books.size()) {
                Book selectedBook = TitlesPage.books.get(titleNumber - 1);

                if (selectedBook.getAvailableCopies() >= 1) {
                    Member member = MembersPage.members.get(memberNumber - 1);

                    if (member.getRentedTitleBook().size() >= 2) {
                        System.out.println("The maximum number of rentals has been reached.");
                        return false;
                    }

                    if (member.getRentedTitleBook().contains(selectedBook)) {
                        System.out.println("Title " + selectedBook.getTitle() + " already rented");
                        return false;
                    }

                    saveTheRentalEntriesToTheDatabase(new RentalEntries(
                            member.getMemberId(), rentedDate, "NOT RETURNED",
                            selectedBook.getTitleId(), 0, "Book"
                    ));

                    selectedBook.setAvailableCopies(selectedBook.getAvailableCopies() - 1);
                    member.getRentedTitleBook().add(selectedBook);
                    titlesPage.updateBooksFile();
                    return true;
                } else {
                    showQueueMenu(titleNumber, memberNumber, "Book");
                    return false;
                }
            } else {
                int dvdIndex = titleNumber - TitlesPage.books.size() - 1;

                if (dvdIndex >= 0 && dvdIndex < TitlesPage.dvds.size()) {
                    DVD selectedDvd = TitlesPage.dvds.get(dvdIndex);

                    if (selectedDvd.getAvailableCopies() >= 1) {
                        Member member = MembersPage.members.get(memberNumber - 1);

                        if (member.getRentedTitleDvd().size() >= 2) {
                            System.out.println("The maximum number of rentals for DVDs has been reached.");
                            return false;
                        }

                        if (member.getRentedTitleDvd().contains(selectedDvd)) {
                            System.out.println("Title " + selectedDvd.getTitle() + " already rented");
                            return false;
                        }

                        saveTheRentalEntriesToTheDatabase(new RentalEntries(
                                member.getMemberId(), rentedDate, "NOT RETURNED",
                                selectedDvd.getTitleId(), 0, "DVD"
                        ));

                        selectedDvd.setAvailableCopies(selectedDvd.getAvailableCopies() - 1);
                        member.getRentedTitleDvd().add(selectedDvd);
                        titlesPage.updateDVDsFile();
                        return true;
                    } else {
                        showQueueMenu(titleNumber, memberNumber, "DVD");
                        return false;
                    }
                } else {
                    System.out.println("Invalid title number.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public void saveTheRentalEntriesToTheDatabase(RentalEntries entries) {
        try {
            String rentalEntries = entries.getMemberId() + "," + entries.getRentedDate() + "," + entries.getReturnDate() + "," + entries.getTitleId() + "," + entries.getTimesProlongued() + "," + entries.getTitleType();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalEntriesFilePatch, true))) {
                writer.write(rentalEntries + "\n");
            }
            rentalEntriesList.add(entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRentalEntriesFromFile() {
        Path filePath = Paths.get(rentalEntriesFilePatch);

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {

                    for (int i = 0; i < MembersPage.members.size(); i++) {
                        if (MembersPage.members.get(i).getMemberId() == Integer.parseInt(parts[0].trim())) {

                            if (parts[5].trim().equals("Book")) {
                                for (int j = 0; j < TitlesPage.books.size(); j++) {
                                    if (TitlesPage.books.get(j).getTitleId() == Integer.parseInt(parts[3].trim())) {
                                        MembersPage.members.get(i).getRentedTitleBook().add(TitlesPage.books.get(j));
                                        rentalEntriesList.add(new RentalEntries(MembersPage.members.get(i).getMemberId(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim()), parts[5].trim()));
                                    }
                                }
                            } else {
                                for (int j = 0; j < TitlesPage.dvds.size(); j++) {
                                    if (TitlesPage.dvds.get(j).getTitleId() == Integer.parseInt(parts[3].trim())) {
                                        MembersPage.members.get(i).getRentedTitleDvd().add(TitlesPage.dvds.get(j));
                                        rentalEntriesList.add(new RentalEntries(MembersPage.members.get(i).getMemberId(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim()), parts[5].trim()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading rentalEntries.txt", e);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from line: " + e.getMessage());
        }
    }

    public void showQueueMenu(int titleNumber, int memberNumber, String titleType) {
        System.out.println("\nDo you want to add your inquiry to the queue?");
        System.out.println("1 - Yes");
        System.out.println("2 - No");
        System.out.print("Choose an option: ");

        switch (inputValidator.getValidatedChoice(2)) {
            case 1 -> addToQueue(titleNumber, memberNumber, titleType);
            case 2 -> {
                goBack();
                showRentalsMenu();
            }
            default -> showRentalsMenu();
        }
    }

    public void addToQueue(int titleNumber, int memberNumber, String titleType) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (titleType.equals("Book")) {
            saveTheQueueToTheDatabase(new QueueItems(
                    MembersPage.members.get(memberNumber - 1).getMemberId(),
                    currentDateTime,
                    TitlesPage.books.get(titleNumber - 1).getTitleId(),
                    false
            ));
        } else {
            int dvdIndex = titleNumber - TitlesPage.books.size() - 1;
            saveTheQueueToTheDatabase(new QueueItems(
                    MembersPage.members.get(memberNumber - 1).getMemberId(),
                    currentDateTime,
                    TitlesPage.dvds.get(dvdIndex).getTitleId(),
                    false
            ));
        }
        showRentalsMenu();
    }




    public void saveTheQueueToTheDatabase(QueueItems queueItems) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timeAdded = queueItems.getTimeAdded().format(formatter);

        try {
            String queue = queueItems.getMemberId() + "," + timeAdded + "," + queueItems.getTitleId() + "," + queueItems.getIsResovled();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(queueItemsFilePatch, true))) {
                writer.write(queue + "\n");
            }
            queueItemsList.add(queueItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadQueueItemsFromFile() {
        Path filePath = Paths.get(queueItemsFilePatch);

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {

                    for (int i = 0; i < MembersPage.members.size(); i++) {
                        if (MembersPage.members.get(i).getMemberId() == Integer.parseInt(parts[0].trim())) {

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            LocalDateTime timeAdded = LocalDateTime.parse(parts[1], formatter);

                            queueItemsList.add(new QueueItems(MembersPage.members.get(i).getMemberId(), timeAdded, Integer.parseInt(parts[2]), Boolean.parseBoolean(parts[3])));
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading queueItems.txt", e);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from line: " + e.getMessage());
        }
    }

    public void returnTitle() {
        System.out.println("Return Title Page");
        System.out.println("Please choose a member who is returning a title:");

        int memberNumber = chooseMember();
        if (memberNumber == -1) {
            showRentalsMenu();
            return;
        }

        Member selectedMember = MembersPage.members.get(memberNumber - 1);

        if (selectedMember.getRentedTitleBook().isEmpty() && selectedMember.getRentedTitleDvd().isEmpty()) {
            System.out.println("This member has no titles to return.");
            pressEnterToContinue();
            return;
        }

        System.out.println("Select a title to return:");
        int index = 1;
        for (Object titleObject : selectedMember.getRentedTitleBook()) {
            String titleName = createTitleName(selectedMember, titleObject, "Book");
            System.out.println(index + ". " + titleName + " (Book)");
            index++;
        }
        for (Object titleObject : selectedMember.getRentedTitleDvd()) {
            String titleName = createTitleName(selectedMember, titleObject, "DVD");
            System.out.println(index + ". " + titleName + " (DVD)");
            index++;
        }

        System.out.print("Choose an option: ");
        int choice = inputValidator.getValidatedChoice(selectedMember.getRentedTitleBook().size() + selectedMember.getRentedTitleDvd().size());

        if (choice <= selectedMember.getRentedTitleBook().size()) {
            performReturn(selectedMember, selectedMember.getRentedTitleBook().get(choice - 1), "Book");
        } else {
            performReturn(selectedMember, selectedMember.getRentedTitleDvd().get(choice - 1 - selectedMember.getRentedTitleBook().size()), "DVD");
        }
    }


    public String createTitleName(Member member, Object titleObject, String type) {
        int titleId = -1;
        String titleName = "";
        String memberName = "";
        String rentedDate = "";
        String returnDate = "";
        int timesProlongued = 0;

        if (type.equals("Book") && titleObject instanceof Book) {
            Book book = (Book) titleObject;
            titleId = book.getTitleId();
            titleName = book.getTitle() + " - " + book.getAuthorName();
        } else if (type.equals("DVD") && titleObject instanceof DVD) {
            DVD dvd = (DVD) titleObject;
            titleId = dvd.getTitleId();
            titleName = dvd.getTitle() + " - " + dvd.getAuthorName();
        }

        for (RentalEntries entry : rentalEntriesList) {
            if (entry.getMemberId() == member.getMemberId() && entry.getTitleId() == titleId && entry.getReturnDate().equals("NOT RETURNED")) {
                rentedDate = entry.getRentedDate();
                returnDate = entry.getReturnDate();
                timesProlongued = entry.getTimesProlongued();
                break;
            }
        }

        for (Member memberItem : MembersPage.members) {
            if (memberItem.getMemberId() == member.getMemberId()) {
                memberName = memberItem.getName() + " " + memberItem.getSurname();
                break;
            }
        }

        titleName += " - Rented on: " + rentedDate + " - Rented by: " + memberName + " - Returned: " + returnDate + " - Times prolongued: " + timesProlongued;

        return titleName;
    }


    public boolean processTitleReturn(Member member, Object titleObject, String type) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String returnedDate = currentDate.format(formatter);

        int titleId = -1;
        if (type.equals("Book") && titleObject instanceof Book) {
            Book book = (Book) titleObject;
            titleId = book.getTitleId();
        } else if (type.equals("DVD") && titleObject instanceof DVD) {
            DVD dvd = (DVD) titleObject;
            titleId = dvd.getTitleId();
        }

        for (RentalEntries entry : rentalEntriesList) {
            if (entry.getMemberId() == member.getMemberId() && entry.getTitleId() == titleId && entry.getReturnDate().equals("NOT RETURNED")) {
                LocalDate rentedDate = LocalDate.parse(entry.getRentedDate(), formatter);
                long daysLate = ChronoUnit.DAYS.between(rentedDate, currentDate);

                if (daysLate > 0) {
                    double lateFee = daysLate * 0.10;
                    System.out.println("You are late with the returning of this title!");
                    System.out.println("The fee for your late returnal is: " + lateFee + " Euro");
                }

                entry.setReturnDate(returnedDate);
                updateRentalEntriesFile();
                return true;
            }
        }

        return false;
    }



    public void performReturn(Member member, Object titleObject, String type) {
        if (processTitleReturn(member, titleObject, type)) {
            if (type.equals("Book") && titleObject instanceof Book) {
                Book book = (Book) titleObject;
                int titleId = book.getTitleId();
                for (Book bookItem : TitlesPage.books) {
                    if (bookItem.getTitleId() == titleId) {
                        bookItem.setAvailableCopies(bookItem.getAvailableCopies() + 1);
                        titlesPage.updateBooksFile();
                        break;
                    }
                }
                member.getRentedTitleBook().remove(book);
            } else if (type.equals("DVD") && titleObject instanceof DVD) {
                DVD dvd = (DVD) titleObject;
                int titleId = dvd.getTitleId();
                for (DVD dvdItem : TitlesPage.dvds) {
                    if (dvdItem.getTitleId() == titleId) {
                        dvdItem.setAvailableCopies(dvdItem.getAvailableCopies() + 1);
                        titlesPage.updateDVDsFile();
                        break;
                    }
                }

                member.getRentedTitleDvd().remove(dvd);
            }
            markLastQueueItemAsResolved();
            System.out.println("Title returned successfully");
        } else {
            System.out.println("Title return failed");
        }
        pressEnterToContinue();
    }


    public void updateRentalEntriesFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rentalEntriesFilePatch))) {
            for (RentalEntries entry : rentalEntriesList) {
                if (entry.getReturnDate().equals("NOT RETURNED")) {
                    String updatedEntry = entry.getMemberId() + "," + entry.getRentedDate() + "," + entry.getReturnDate() + "," + entry.getTitleId() + "," + entry.getTimesProlongued() + "," + entry.getTitleType();
                    writer.write(updatedEntry + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prolongRental() {
        System.out.println("Prolong the rental");
        System.out.println("Members:");
        int memberChoice = chooseMember();
        Member selectedMember = MembersPage.members.get(memberChoice - 1);
        List<RentalEntries> rentedTitles = getRentedTitlesForMember(selectedMember);

        if (rentedTitles.isEmpty()) {
            System.out.println("No rentals to prolong...");
            pressEnterToContinue();
            return;
        }
        System.out.println("Rented Titles:");
        int titleChoice = chooseRentedTitle(rentedTitles);
        RentalEntries selectedRental = rentedTitles.get(titleChoice - 1);
        if (prolongTitle(selectedRental)) {
            System.out.println("Rental prolonged successfully...");
        } else {
            System.out.println("Rental prolongation failed...");
        }
    }

    public boolean prolongTitle(RentalEntries selectedRental) {
        double lateFee = calculateLateFee(selectedRental);

        if (lateFee > 0) {
            System.out.println("You are late with the returning of this title!");
            System.out.println("The fee for your late returnal is: " + lateFee + " Euro");
        }
        if (checkProlongationLimit(selectedRental)) {
            selectedRental.increaseTimesProlongued();
            updateRentalEntriesFile();
            System.out.println("Rental prolonged successfully...");
            System.out.println("Press enter to return to Rental page...");
            scanner.nextLine();
            showRentalsMenu();
            return true;
        } else {
            System.out.println("Press enter to return to Rental page...");
            scanner.nextLine();
            showRentalsMenu();
            return false;
        }
    }

    public boolean checkProlongationLimit(RentalEntries selectedRental) {

        if (selectedRental.getTimesProlongued() >= 2) {
            System.out.println("Item already prolonged 2 times, which is the maximum possible!");
            return false;
        }
        return true;
    }

        private List<RentalEntries> getRentedTitlesForMember (Member member){
            List<RentalEntries> rentedTitles = new ArrayList<>();

            for (RentalEntries entry : rentalEntriesList) {
                if (entry.getMemberId() == member.getMemberId() && entry.getReturnDate().equals("NOT RETURNED")) {
                    rentedTitles.add(entry);
                }
            }

            return rentedTitles;
        }

        private int chooseRentedTitle (List <RentalEntries> rentedTitles) {
            for (int i = 0; i < rentedTitles.size(); i++) {
                RentalEntries entry = rentedTitles.get(i);
                System.out.println((i + 1) + " - " + getRentedTitleDetails(entry));
            }

            System.out.print("Choose an option: ");
            return inputValidator.getValidatedChoice(rentedTitles.size());
        }

        public String getRentedTitleDetails (RentalEntries entry){
            String titleDetails = "";
            String titleType = entry.getTitleType();
            if (titleType.equals("Book")) {
                Book book = getBookById(entry.getTitleId());
                if (book != null) {
                    titleDetails = book.getTitle() + " - " + book.getAuthorName();
                }
            } else if (titleType.equals("DVD")) {
                DVD dvd = getDVDById(entry.getTitleId());
                if (dvd != null) {
                    titleDetails = dvd.getTitle() + " - " + dvd.getAuthorName();
                }
            }
            titleDetails += " - Rented on: " + entry.getRentedDate();
            titleDetails += " - Rented by: " + getMemberNameById(entry.getMemberId());
            titleDetails += " - Returned: " + entry.getReturnDate();
            titleDetails += " - Times prolongued: " + entry.getTimesProlongued();

            return titleDetails;
        }

        private double calculateLateFee (RentalEntries entry){
            String titleType = entry.getTitleType();
            double lateFee = 0.0;
            if (titleType.equals("Book")) {
                lateFee = calculateLateFeeForBook(entry);
            } else if (titleType.equals("DVD")) {
                lateFee = calculateLateFeeForDVD(entry);
            }

            return lateFee;
        }

        private Book getBookById (int bookId){
            for (Book book : TitlesPage.books) {
                if (book.getTitleId() == bookId) {
                    return book;
                }
            }
            return null;
        }

        private DVD getDVDById (int dvdId){
            for (DVD dvd : TitlesPage.dvds) {
                if (dvd.getTitleId() == dvdId) {
                    return dvd;
                }
            }
            return null;
        }

        private String getMemberNameById ( int memberId){
            for (Member member : MembersPage.members) {
                if (member.getMemberId() == memberId) {
                    return member.getName() + " " + member.getSurname();
                }
            }
            return "Unknown Member";
        }

        public double calculateLateFeeForBook (RentalEntries entry){
            LocalDate rentedDate = LocalDate.parse(entry.getRentedDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDate currentDate = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(rentedDate, currentDate) - 24;
            double lateFee = 0.0;
            if (daysLate > 0) {
                lateFee = daysLate * 0.10;
            }
            return lateFee;
        }


        public double calculateLateFeeForDVD (RentalEntries entry){

            LocalDate rentedDate = LocalDate.parse(entry.getRentedDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDate currentDate = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(rentedDate, currentDate) - 7;
            double lateFee = 0.0;
            if (daysLate > 0) {
                lateFee = daysLate * 1.0;
            }

            return lateFee;
        }

    public void showAllRentals() {
        System.out.println("All Rentals:");
        for (RentalEntries entry : rentalEntriesList) {
            String titleDetails = getRentedTitleDetails(entry);
            System.out.println(titleDetails);
        }
        pressEnterToContinue();
    }

    public void pressEnterToContinue () {
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        showRentalsMenu();
    }
    public void showRentalsPastDue() {
        System.out.println("Show rentals past due:");

        List<RentalEntries> pastDueRentals = getOverdueRentals();

        for (RentalEntries entry : pastDueRentals) {
            System.out.println(getRentedTitleDetails(entry));
        }
        pressEnterToContinue();
    }

    public List<RentalEntries> getOverdueRentals() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        List<RentalEntries> pastDueRentals = new ArrayList<>();

        for (RentalEntries entry : rentalEntriesList) {
            if (entry.getReturnDate().equals("NOT RETURNED")) {
                LocalDate rentedDate = LocalDate.parse(entry.getRentedDate(), formatter);
                long daysLate = ChronoUnit.DAYS.between(rentedDate, currentDate);

                if (daysLate > 0) {
                    pastDueRentals.add(entry);
                }
            }
        }

        return pastDueRentals;
    }

    public static List<String> showQueueFunction() {
        List<String> queueEntries = new ArrayList<>();

        for (QueueItems queueItem : queueItemsList) {
            int memberId = queueItem.getMemberId();
            int titleId = queueItem.getTitleId();
            boolean isResolved = queueItem.getIsResovled();

            String memberName = getMemberName(memberId);
            String titleName = getTitleName(titleId);
            String status = isResolved ? "Resolved" : "Not Resolved";
            String queueEntry = "Member: " + memberName + " Title: " + titleName + " - Status: " + status + "MemberId: " + memberId;

            queueEntries.add(queueEntry);
        }
        return queueEntries;
    }

    public void showQueue() {
        List<String> queueEntries = showQueueFunction();

        for (String entry : queueEntries) {
            System.out.println(entry);
        }
        pressEnterToContinue();
    }

    public static String getMemberName(int memberId) {
        for (Member member : MembersPage.members) {
            if (member.getMemberId() == memberId) {
                return member.getName() + " " + member.getSurname();
            }
        }
        return "Unknown Member";
    }

    public static String getTitleName(int titleId) {
        for (Book book : TitlesPage.books) {
            if (book.getTitleId() == titleId) {
                return book.getTitle();
            }
        }
        for (DVD dvd : TitlesPage.dvds) {
            if (dvd.getTitleId() == titleId) {
                return dvd.getTitle();
            }
        }
        return "Unknown Title";
    }

    private void markLastQueueItemAsResolved() {
        if (!queueItemsList.isEmpty()) {
            QueueItems lastQueueItem = queueItemsList.get(queueItemsList.size() - 1);
            lastQueueItem.setIsResovled(true);
            updateQueueItemsFile();
        }
    }

    public void updateQueueItemsFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(queueItemsFilePatch))) {
            for (QueueItems queueItem : queueItemsList) {
                String timeAddedFormatted = queueItem.getTimeAdded().format(formatter);
                String updatedQueueItem = queueItem.getMemberId() + "," + timeAddedFormatted + "," + queueItem.getTitleId() + "," + queueItem.getIsResovled();
                writer.write(updatedQueueItem + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String goBack () {
            System.out.println("Going back...");
            return "Back";
        }
}


