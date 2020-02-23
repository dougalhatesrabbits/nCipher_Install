package com.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class ReadSecWorld {
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            // JSON file to Java object
            SecWorld world = mapper.readValue(new File("SecWorld.json"), SecWorld.class);

            // JSON string to Java object
            //String jsonInString = "{\"name\":\"mkyong\",\"age\":37,\"skills\":[\"java\",\"python\"]}";
            //Staff staff2 = mapper.readValue(jsonInString, Staff.class);

            // compact print
            System.out.println(world);

            // pretty print
            String prettyworld = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(world);

            System.out.println(prettyworld);

            String[] linuxSearch = world.getLinuxSearch();
            Map<String, Integer> versions =world.getVersions();
            List<String> windowsSearch = world.getWindowsSearch();


            //System.out.println(world.getLinuxSearch());
            for (String i : world.getLinuxSearch()) {
                System.out.println(i);
            }
            System.out.println(world.getVersions());
            System.out.println(world.getWindowsSearch());

            System.out.println(linuxSearch[0]);
            System.out.println(versions.get("12.60.3"));
            System.out.println(windowsSearch.get(0));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
