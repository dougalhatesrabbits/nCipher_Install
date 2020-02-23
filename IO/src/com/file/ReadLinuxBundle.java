package com.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class ReadLinuxBundle {
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            // JSON file to Java object
            //LinuxBundle bundle1260 = mapper.readValue(new File("12-60-Bundle.json"), LinuxBundle.class);
            LinuxBundle bundle1250 = mapper.readValue(new File("12-50-Bundle.json"), LinuxBundle.class);

            // JSON string to Java object
            //String jsonInString = "{\"name\":\"mkyong\",\"age\":37,\"skills\":[\"java\",\"python\"]}";
            //Staff staff2 = mapper.readValue(jsonInString, Staff.class);

            // compact print
            System.out.println(bundle1250);

            // pretty print
            String prettybundle = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bundle1250);

            System.out.println(prettybundle);

            List<String> core = bundle1250.getCore();
            List<String> java = bundle1250.getJava();

            System.out.println(core.get(1));
            System.out.println(java.get(0));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
