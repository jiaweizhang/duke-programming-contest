package dpc.statics;

import dpc.checks.CheckService;
import dpc.exceptions.ContestNotStartedException;
import dpc.std.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 10/2/2016.
 */
@org.springframework.stereotype.Service
public class StaticService extends Service {

    @Autowired
    private CheckService checkService;

    public ResponseEntity getProblem(String fileName) {
        String contestId = parseFileName(fileName);
        checkContestOpen(contestId);
        return loadFile("problems/" + fileName + ".pdf", "application/pdf");
    }

    public ResponseEntity getInput(String fileName) {
        String contestId = parseFileName(fileName);
        checkContestOpen(contestId);
        return loadFile("inputs/" + fileName + ".txt", "text/plain");
    }

    private String parseFileName(String fileName) {
        // retrieve contestId
        String[] stringArray = fileName.split("_");
        String contestId = stringArray[0];
        int problemNumber = Integer.parseInt(stringArray[1]);

        if (problemNumber < 1 || problemNumber > 8) {
            throw new IllegalArgumentException("Invalid problem number specified");
        }

        // check that contestId is valid
        if (!checkService.contestExists(contestId)) {
            throw new IllegalArgumentException("Invalid contestId specified");
        }

        return contestId;
    }

    private void checkContestOpen(String contestId) {
        // check that contest is open
        long unixTime = System.currentTimeMillis() / 1000L;
        Timestamp current = new Timestamp(unixTime);
        Timestamp startTime = jt.queryForObject(
                "SELECT start_date FROM contests WHERE contest_id = ?",
                Timestamp.class,
                contestId
        );

        if (current.before(startTime)) {
            throw new ContestNotStartedException();
        }
    }

    private ResponseEntity loadFile(String path, String mediaType) {
        // check that problem exists
        ClassPathResource file = new ClassPathResource(path);

        try {
            return ResponseEntity
                    .ok()
                    .contentLength(file.contentLength())
                    .contentType(
                            MediaType.parseMediaType(mediaType))
                    .body(new InputStreamResource(file.getInputStream()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file name specified");
        }
    }
}