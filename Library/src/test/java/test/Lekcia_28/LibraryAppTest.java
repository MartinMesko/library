package test.Lekcia_28;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class LibraryAppTest {

    @Test
    public void whenReadingMessageFromQueueThenMessageContainsSentAndAvailabilityInfo() {
        String testQueueItemsFile = "testQueueItems.txt";
        createTestQueueItemsFile(testQueueItemsFile, true);
        int testMemberChoice = 1;
        MessagesPage messages = new MessagesPage(new Scanner(System.in));
        RentalsPage.queueItemsFilePatch = testQueueItemsFile;
        String message = messages.readMemberMessage(testMemberChoice);
        assertNotNull(message);
        assertTrue(message.contains("Sent: "));
        assertTrue(message.contains(" is available now!"));
        deleteTestQueueItemsFile(testQueueItemsFile);
    }

    @Test
    public void whenNoMessageForMemberChoiceThenNullReturned() {
        String testQueueItemsFile = "testQueueItems.txt";
        createTestQueueItemsFile(testQueueItemsFile, false);
        int testMemberChoice = 1;
        MessagesPage messages = new MessagesPage(new Scanner(System.in));
        RentalsPage.queueItemsFilePatch = testQueueItemsFile;
        String message = messages.readMemberMessage(testMemberChoice);
        assertNull(message);
        deleteTestQueueItemsFile(testQueueItemsFile);
    }

    @Test
    public void whenQueueFileLineMalformedThenNullReturned() {
        String testQueueItemsFile = "testQueueItems.txt";
        createMalformedQueueItemsFile(testQueueItemsFile);
        int testMemberChoice = 1;
        MessagesPage messages = new MessagesPage(new Scanner(System.in));
        RentalsPage.queueItemsFilePatch = testQueueItemsFile;
        String message = messages.readMemberMessage(testMemberChoice);
        assertNull(message, "Metóda by mala vrátiť null pre poškodený alebo neúplný riadok v súbore");
        deleteTestQueueItemsFile(testQueueItemsFile);
    }




    private void createTestQueueItemsFile(String filePath, boolean includeTrueValue) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            if (includeTrueValue) {
                writer.println("1002,12-10-2023 12:15:57,1009,true");
            }
            writer.println("1007,12-10-2023 13:19:20,2009,false");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTestQueueItemsFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private void createMalformedQueueItemsFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("1002,12-10-2023,1009");
            writer.println("1007,12-10-2023 13:19:20,2009,false");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
