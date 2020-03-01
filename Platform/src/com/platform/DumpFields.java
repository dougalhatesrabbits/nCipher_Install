/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Properties;


public class DumpFields {
    public static void main(String[] args) {
        try {
            inspect(ConsoleColours.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> envVars = System.getenv();
        for (String envName : envVars.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    envVars.get(envName));
        }

        System.out.println("JH: " + envVars.get("JAVA_PATH"));
        System.out.println("JH: " + System.getenv("JAVA_PATH"));
        System.out.println();

        Properties envProps = System.getProperties();
        for (Object propName : envProps.keySet()) {
            System.out.format("%s=%s%n",
                    propName,
                    envProps.get(propName));
        }
    }

    static <T> void inspect(Class<T> klazz) throws NoSuchFieldException {
        Field[] fields = klazz.getDeclaredFields();
        System.out.printf("%d fields:%n", fields.length);
        for (Field field : fields) {
            System.out.printf("%s %s %s%n",
                    Modifier.toString(field.getModifiers()),
                    field.getType().getSimpleName(),
                    field.getName()
            );
        }
    }
}
