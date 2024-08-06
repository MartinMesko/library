package test.Lekcia_28;

public class RentalEntries {
    private int memberId;
    private String rentedDate;
    private String returnDate;
    private int titleId;
    private int timesProlongued;
    private String titleType;

    public RentalEntries(int memberId, String rentedDate, String returnDate, int titleId, int timesProlongued, String titleType) {
        this.memberId = memberId;
        this.rentedDate = rentedDate;
        this.returnDate = returnDate;
        this.titleId = titleId;
        this.timesProlongued = timesProlongued;
        this.titleType = titleType;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(String rentedDate) {
        this.rentedDate = rentedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getTimesProlongued() {
        return timesProlongued;
    }

    public void setTimesProlongued(int timesProlongued) {
        this.timesProlongued = timesProlongued;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public void increaseTimesProlongued() {
        this.timesProlongued++;
    }

    @Override
    public String toString() {
        return "RentalEntries{" +
                "memberId=" + memberId +
                ", rentedDate='" + rentedDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", titleId=" + titleId +
                ", timesProlongued=" + timesProlongued +
                ", titleType='" + titleType + '\'' +
                '}';
    }
}
