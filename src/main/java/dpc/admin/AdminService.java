package dpc.admin;

import dpc.std.Service;
import dpc.std.models.StdResponse;
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

    public StdResponse upgradeDb() {
        try {
            String query = readQuery("sql/setup.sql");
            jt.execute(query);

            return new StdResponse(200, true, "Successfully upgraded database");
        } catch (Exception e) {
            e.printStackTrace();
            return new StdResponse(500, false, "Failed to upgrade database");
        }
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
