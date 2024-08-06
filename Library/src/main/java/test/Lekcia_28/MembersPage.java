package test.Lekcia_28;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MembersPage {
    private final Scanner scanner;

    private InputValidator inputValidator;


    public static List<Member> members = new ArrayList<>();

    public MembersPage(Scanner scanner) {
        this.scanner = scanner;
        loadMembers();
    }

    public MembersPage() {
        this.scanner = new Scanner(System.in);
    }

    public void showMembersMenu() {
        System.out.println("Members ");
        System.out.println("1 - Show All Members");
        System.out.println("2 - Add Member");
        System.out.println("3 - Remove Member");
        System.out.println("4 - Back");
        System.out.print("Choose an option: ");

        InputValidator inputValidator = new InputValidator(scanner);
        int choice = inputValidator.getValidatedChoice(4);

        switch (choice) {
            case 1:
                showAllMembers();
                break;
            case 2:
                userAddMember();
                break;
            case 3:
                userDeleteMember();
                break;
            case 4:
                goBack();
                break;
            default:
                showMembersMenu();
                break;
        }
    }

    public void showAllMembers() {
        listingAllMembers();

        int count = getMembersCount();
        if (count >= 1) {
            System.out.println("Total number of all members: " + count);
            System.out.println(System.lineSeparator() + "Press enter to return to Members menu...");
        } else {
            System.out.println(System.lineSeparator() + "Member list is empty. Press enter to return to Members menu...");
        }
        scanner.nextLine();
        showMembersMenu();
    }

    private void loadMembers() {
        try {
            members = MemberFileManager.loadMembersFromFile();
        } catch (IOException e) {
            System.err.println("Error loading members from file");
            members = new ArrayList<>();
        }
    }

    public void listingAllMembers() {
        int memberCounter = 1;
        for (Member member : members) {
            System.out.println(memberCounter + ". " + member);
            memberCounter++;
        }
    }

    public void addMember(Member member) {
        members.add(member);
        try {
            MemberFileManager.saveMemberToFile(member);
        } catch (IOException e) {
            System.err.println("Error saving the member to file");
        }
    }

    public boolean deleteMember(int memberIndex) {
        if (memberIndex >= 0 && memberIndex < members.size()) {
            members.remove(memberIndex);
            try {
                MemberFileManager.saveAllMembersToFile(members);
                return true;
            } catch (IOException e) {
                System.err.println("Error saving changes to file");
                return false;
            }
        }
        return false;
    }

    public int getMembersCount() {
        return members.size();
    }

    public void userAddMember() {
        System.out.println("Add member page");
        System.out.print("Enter member's first name: ");
        String name = inputValidator.validationCheckString();

        System.out.print("Enter member's last name: ");
        String surname = inputValidator.validationCheckString();

        System.out.print("Enter member's date of birth (dd.MM.yyyy): ");
        String dateOfBirth = inputValidator.validateDateOfBirthInput();

        System.out.print("Enter personal id: ");
        int personalId = inputValidator.validationCheckInt();
        scanner.nextLine();

        Member newMember = new Member(name, surname, dateOfBirth, personalId);
        addMember(newMember);

        System.out.println("Member created successfully.");
        showMembersMenu();
    }

    public void userDeleteMember() {
        System.out.println("Remove member page");
        System.out.println("Choose a member to delete:");
        listingAllMembers();

        int maxNumber = members.size();

        if (maxNumber == 0) {
            System.out.println("Member list is empty.");
            showMembersMenu();
            return;
        }

        System.out.print("Choose an option:");

        int memberNumber = inputValidator.getValidatedChoice(maxNumber) - 1;

        if (deleteMember(memberNumber)) {
            System.out.println("Member deleted successfully.");
        } else {
            System.out.println("Error occurred while deleting the member.");
        }

        showMembersMenu();
    }

    public Member getMember(int memberNumber) {
        if (memberNumber >= 0 && memberNumber < members.size()) {
            return members.get(memberNumber);
        } else {
            return null;
        }
    }


    private void goBack() {
        System.out.println("Going back to main menu...");
        LibraryApp.showMainMenu();
    }
}
