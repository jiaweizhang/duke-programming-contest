package dpc.services;

import dpc.models.responses.StdResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiaweizhang on 9/14/2016.
 */

@Transactional
@org.springframework.stereotype.Service
public class AdminService extends Service {

    public StdResponse upgradeDb() throws IOException {
        String query = readQuery("sql/setup.sql");
        jt.execute(query);

        // set up procedures
        for (String procFileName : getFilesInDirectory("sql/procs")) {
            System.out.println(procFileName);
            String proc = readQuery(procFileName);
            jt.execute(proc);
        }
        return new StdResponse(200, true, "Successfully upgraded database");
    }

    private List<String> getFilesInDirectory(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        return Arrays.stream(listOfFiles).filter(File::isFile).map(File::getAbsolutePath).collect(Collectors.toList());
    }

    private String readQuery(String file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = in.readLine()) != null) {
            int index;
            // removes single-line comments
            if ((index = str.indexOf("--")) >= 0) {
                str = str.substring(0, index);
            }
            sb.append(str).append(" ");
        }
        in.close();

        return sb.toString();
    }
}
