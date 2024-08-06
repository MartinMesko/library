package test.Lekcia_28;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemberFileManager {

    public static String DEFAULT_FILE_PATH = "src/main/resources/members.txt";

    public static List<Member> loadMembersFromFile() throws IOException {
        return loadMembersFromFile(DEFAULT_FILE_PATH);
    }

    public static List<Member> loadMembersFromFile(String filePath) throws IOException {
        List<Member> members = new ArrayList<>();
        BufferedReader reader = null;
        try {
            File membersFile = new File(filePath);
            reader = new BufferedReader(new FileReader(membersFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (parts.length >= 3) {
                        members.add(new Member(parts[0].trim(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim())));
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
        return members;
    }

    public static void saveMemberToFile(Member member) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(DEFAULT_FILE_PATH, true);
            writer.write(member.getName() + "," + member.getSurname() + "," + member.getDateOfBirth() + "," + member.getMemberId() + "\n");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void saveAllMembersToFile(List<Member> members) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(DEFAULT_FILE_PATH);
            for (Member member : members) {
                writer.write(member.getName() + "," + member.getSurname() + "," + member.getDateOfBirth() + "," + member.getMemberId() + "\n");
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


}
