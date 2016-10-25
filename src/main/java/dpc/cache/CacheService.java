package dpc.cache;

import dpc.cache.models.EmailRequest;
import dpc.cache.models.ExistsResponse;
import dpc.cache.models.GroupNameRequest;
import dpc.checks.CheckService;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiaweizhang on 10/24/2016.
 */

@Transactional
@org.springframework.stereotype.Service
public class CacheService extends Service {

    @Autowired
    private CheckService checkService;

    public StdResponse checkEmail(EmailRequest emailRequest) {
        if (checkService.emailExists(emailRequest.email)) {
            // exists
            return new ExistsResponse(200, true, "Email exists", true);
        } else {
            // does not exist
            return new ExistsResponse(200, true, "Email does not exist", false);
        }
    }

    public StdResponse checkGroupName(GroupNameRequest groupNameRequest) {
        if (checkService.groupNameExists(groupNameRequest.groupName)) {
            // exists
            return new ExistsResponse(200, true, "GroupName exists", true);
        } else {
            // does not exist
            return new ExistsResponse(200, true, "GroupName does not exist", false);
        }
    }
}
