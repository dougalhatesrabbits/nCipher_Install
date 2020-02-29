/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestPBW {

    public static void main(String[] args) throws Exception {

        List cmd = new ArrayList();
        cmd.add("ls");
        cmd.add("-al");
        ProcessBuilderWrapper pbd = new ProcessBuilderWrapper(new File("/tmp"), cmd);
        System.out.println("Command has terminated with status: " + pbd.getStatus());
        System.out.println("Output:\n" + pbd.getInfos());
        System.out.println("Error: " + pbd.getErrors());

        cmd.clear();

        //cmd.add("/bin/bash");
        //cmd.add("-c");
        cmd.add(new String[]{"ls", "-l"});
        //cmd.add("-l");
        pbd = new ProcessBuilderWrapper(cmd);
        //ProcessBuilderWrapper pbd = new ProcessBuilderWrapper(new File("/etc"), cmd);
        System.out.println(pbd.getStatus());
        System.out.println(pbd.getInfos());
        System.out.println(pbd.getErrors());


    }


}
