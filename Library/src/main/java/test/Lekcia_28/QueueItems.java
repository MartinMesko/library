package test.Lekcia_28;

import java.time.LocalDateTime;

public class QueueItems {
    private int memberId;
    private LocalDateTime timeAdded;
    private int titleId;
    private Boolean isResovled;

    public QueueItems(int memberId, LocalDateTime timeAdded, int titleId, Boolean isResovled) {
        this.memberId = memberId;
        this.timeAdded = timeAdded;
        this.titleId = titleId;
        this.isResovled = isResovled;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalDateTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public Boolean getIsResovled() {
        return isResovled;
    }

    public void setIsResovled(Boolean isResovled) {
        this.isResovled = isResovled;
    }

    @Override
    public String toString() {
        return "QueueItems{" +
                "memberId=" + memberId +
                ", timeAdded=" + timeAdded +
                ", titleId=" + titleId +
                ", isResovled=" + isResovled +
                '}';
    }
}
