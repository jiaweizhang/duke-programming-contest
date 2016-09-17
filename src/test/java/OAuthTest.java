import dpc.Application;
import dpc.auth.OAuthService;
import dpc.std.StdResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jiaweizhang on 9/17/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class OAuthTest {

    @Autowired
    private OAuthService oAuthService;

    @Test
    public void TestAdminOAuth() {
        long userId = oAuthService.createUserIfNotExists(TestUtilities.generateUUID());
        assert(userId > 1);
    }
}
