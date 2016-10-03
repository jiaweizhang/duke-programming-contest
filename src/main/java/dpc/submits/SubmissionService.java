package dpc.submits;

import dpc.checks.CheckService;
import dpc.exceptions.AlreadyHaveCorrectSubmissionException;
import dpc.exceptions.FileReaderException;
import dpc.exceptions.NotGroupMemberException;
import dpc.std.Service;
import dpc.std.StdRequest;
import dpc.std.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiaweizhang on 10/2/2016.
 */
@org.springframework.stereotype.Service
public class SubmissionService extends Service {

    @Autowired
    private CheckService checkService;

    public StdResponse submit(StdRequest req, String contestId, int problemNumber, String body) {
        List<String> lines = Arrays.asList(body.split("\n"));

        // make sure problem exists
        if (problemNumber < 1 || problemNumber > 8) {
            throw new IllegalArgumentException("Problem number does not exist");
        }

        // make sure contest exists
        if (!checkService.contestExists(contestId)) {
            throw new IllegalArgumentException("Contest does not exist");
        }

        // make sure contest is open
        checkService.checkContestOpen(contestId);

        // make sure that user belongs in a group
        if (!checkService.inGroupForContest(req.userId, contestId)) {
            throw new NotGroupMemberException();
        }

        long groupId = checkService.getGroupId(req.userId, contestId);

        // if already have a correct submission, do not allow another submission
        if (checkService.hasCorrectSubmission(contestId, groupId, problemNumber)) {
            throw new AlreadyHaveCorrectSubmissionException();
        }

        // determine if the result is correct
        List<String> answers = retrieveAnswers(contestId, problemNumber);
        int isCorrect = isCorrect(lines, answers) ? 1 : 0;

        // log submission
        jt.update(
                "INSERT INTO submissions (contest_id, group_id, problem_number, is_correct) " +
                        "VALUES (?, ?, ?, ?)",
                contestId, groupId, problemNumber, isCorrect
        );

        // return submission result
        return new SubmissionResponse(200, true, "Submission acknowledged", isCorrect);
    }

    public StdResponse getSubmissions(StdRequest stdRequest, String contestId) {
        if (!checkService.inGroupForContest(stdRequest.userId, contestId)) {
            throw new NotGroupMemberException();
        }

        long groupId = checkService.getGroupId(stdRequest.userId, contestId);

        List<Submission> submissions = jt.query(
                "SELECT is_correct, submit_time, problem_number FROM submissions WHERE contest_id = ? AND group_id = ?",
                new SubmissionMapper(), contestId, groupId);

        return new SubmissionsResponse(200, true, "Successfully retrieved submissions", submissions);
    }

    private boolean isCorrect(List<String> submission, List<String> key) {
        List<String> filteredSubmission = submission.stream()
                .map(String::trim)
                .filter(l -> l.length() > 0)
                .collect(Collectors.toList());

        List<String> filteredKey = submission.stream()
                .map(String::trim)
                .filter(l -> l.length() > 0)
                .collect(Collectors.toList());

        if (filteredSubmission.size() != filteredKey.size()) {
            return false;
        }
        for (int i = 0; i < filteredKey.size(); i++) {
            if (!filteredKey.get(i).equals(filteredSubmission.get(i))) {
                return false;
            }
        }
        return true;
    }

    private List<String> retrieveAnswers(String contestId, int problemNumber) {
        String path = "solutions/" + contestId + "_" + problemNumber + "_solution.txt";
        ClassPathResource file = new ClassPathResource(path);

        try (InputStream inputStream = file.getInputStream()) {
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
                return buffer.lines().collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new FileReaderException();
        }
    }

    private static final class SubmissionMapper implements RowMapper<Submission> {
        public Submission mapRow(ResultSet rs, int rowNum) throws SQLException {
            Submission submission = new Submission();
            submission.isCorrect = rs.getInt("is_correct");
            submission.submitTime = rs.getTimestamp("submit_time");
            submission.problemNumber = rs.getInt("problem_number");
            return submission;
        }
    }
}
