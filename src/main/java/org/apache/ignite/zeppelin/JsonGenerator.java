package org.apache.ignite.zeppelin;

import java.io.*;
import java.util.*;

public class JsonGenerator {
    private static final Random RND = new Random();

    private static final int ORG_CNT = 500;

    private static final int PERSON_PER_ORG_CNT = 5_000;

    public static void main(String[] args) throws IOException {
        PrintWriter orgWriter = new PrintWriter(new File("organizations.json"));
        PrintWriter personWriter = new PrintWriter(new File("persons.json"));

        try {
            long personId = 0;

            for (long orgId = 0; orgId < ORG_CNT; orgId++) {
                String orgJson = "{\"id\": " + orgId + ", \"name\": \"Organization" + orgId + "\"}";

                orgWriter.println(orgJson);

                Long managerId = null;

                for (long i = 0; i < PERSON_PER_ORG_CNT; i++) {
                    String personJson = "{\"id\": " + personId + ", \"name\": \"Person" + personId + "\", \"orgId\": " +
                        orgId + ", \"managerId\": " + managerId + ", \"salary\": " + RND.nextInt(1000000) + "}";

                    personWriter.println(personJson);

                    if (managerId == null)
                        managerId = personId;

                    personId++;
                }
            }
        }
        finally {
            orgWriter.flush();
            personWriter.flush();

            orgWriter.close();
            personWriter.close();
        }
    }
}
