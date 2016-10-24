import dpc.Application;
import dpc.admin.AdminService;
import dpc.auth.AuthService;
import dpc.auth.models.LoginRequest;
import dpc.auth.models.RegisterRequest;
import dpc.exceptions.IncorrectPasswordException;
import dpc.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.fail;

/**
 * Created by jiaweizhang on 10/12/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class AuthTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AdminService adminService;

    @Before
    public void before() {
        // refresh the database
        adminService.upgradeDb();
    }

    @Test
    public void registerUser() {
        RegisterRequest registerRequest = ModelFactory.registerRequest(
                "abc@gmail.com", "ABC", "password", "Duke", "2017"
        );
        assert (authService.register(registerRequest).success);
    }

    @Test
    public void registerUserWithExistingEmail() {
        RegisterRequest registerRequest = ModelFactory.registerRequest(
                "abc@gmail.com", "ABC", "password", "Duke", "2017"
        );
        assert (authService.register(registerRequest).success);

        try {
            RegisterRequest registerRequest2 = ModelFactory.registerRequest(
                    "abc@gmail.com", "ABC_new", "password", "Duke", "2017"
            );
            authService.register(registerRequest2);
            fail("Cannot reach here");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Cannot reach here");
        }
    }

    @Test
    public void loginUser() {
        registerUser();

        LoginRequest loginRequest = ModelFactory.loginRequest("abc@gmail.com", "password");
        assert (authService.login(loginRequest).success);
    }

    @Test
    public void loginUserWithNonexistentEmail() {
        registerUser();

        try {
            LoginRequest loginRequest = ModelFactory.loginRequest("abcd@gmail.com", "password");
            authService.login(loginRequest);
            fail("Cannot reach here");
        } catch (UserNotFoundException e) {

        } catch (Exception e) {
            fail("Cannot reach here");
        }
    }

    @Test
    public void loginUserWithIncorrectPassword() {
        registerUser();

        try {
            LoginRequest loginRequest = ModelFactory.loginRequest("abc@gmail.com", "incorrect_password");
            authService.login(loginRequest);
            fail("cannot reach here");
        } catch (IncorrectPasswordException e) {

        } catch (Exception e) {
            fail("cannot reach here");
        }
    }
}
