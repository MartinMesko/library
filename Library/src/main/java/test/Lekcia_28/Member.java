package test.Lekcia_28;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String name;
    private final String surname;
    private final String dateOfBirth;
    private final int memberId;
    public List<Book> rentedTitleBook = new ArrayList<>();
    public List<DVD> rentedTitleDvd = new ArrayList<>();

    public Member(String name, String surname, String dateOfBirth, int memberId) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.memberId = memberId;
    }

    public Member() {
        this.name = "";
        this.surname = "";
        this.dateOfBirth = "";
        this.memberId = 0;
        this.rentedTitleBook = new ArrayList<>();
        this.rentedTitleDvd = new ArrayList<>();
    }

    public List<Book> getRentedTitleBook() {
        return rentedTitleBook;
    }

    public void setRentedTitleBook(List<Book> rentedTitleBook) {
        this.rentedTitleBook = rentedTitleBook;
    }

    public List<DVD> getRentedTitleDvd() {
        return rentedTitleDvd;
    }

    public void setRentedTitleDvd(List<DVD> rentedTitleDvd) {
        this.rentedTitleDvd = rentedTitleDvd;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return name + " " +
                surname + " | Date of Birth: " +
                dateOfBirth + " - Personal Id: " +
                memberId;
    }
}
