package tests.admin;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import web.base.BaseTest;
import web.pages.admin.UserManagementPage;
import web.pages.login.LoginPage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserManagementTest extends BaseTest {
    private UserManagementPage userManagementPage;
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String TEST_EMPLOYEE_NAME = "Qwerty Qwerty LName";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "Test@123";

    @BeforeMethod
    public void setUp() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        userManagementPage = new UserManagementPage(page);
    }

    @Test
    public void testAddUserWithExistingUsernameCaseInsensitive() {
        // First, create a user with lowercase username
        userManagementPage.clickAdd();
        userManagementPage.addUser(
            TEST_EMPLOYEE_NAME,
            TEST_USERNAME,
            "ESS",
            "Enabled",
            TEST_PASSWORD,
            TEST_PASSWORD
        );

        // Verify the user was created
        assertTrue(userManagementPage.isUserCreated(TEST_USERNAME), 
            "Initial user should be created successfully");

        // Try to create another user with the same username in uppercase
        userManagementPage.clickAdd();
        userManagementPage.addUser(
            TEST_EMPLOYEE_NAME,
            TEST_USERNAME.toUpperCase(),
            "ESS",
            "Enabled",
            TEST_PASSWORD,
            TEST_PASSWORD
        );

        // Verify the error message for duplicate username
        String errorMessage = userManagementPage.getDuplicateUsernameErrorMessage();
        assertTrue(errorMessage.contains("Already exists"), 
            "Error message should indicate username already exists");

        // Verify the username exists case-insensitive
        assertTrue(userManagementPage.isUsernameExistsCaseInsensitive(TEST_USERNAME.toUpperCase()),
            "Username should exist case-insensitive");

        // Clean up - delete the test user
        userManagementPage.deleteUserByUsername(TEST_USERNAME);
    }

    @Test
    public void testUserDetailsLoadCorrectlyBeforeEditing() {
        // Create a test user
        userManagementPage.clickAdd();
        userManagementPage.addUser(
            TEST_EMPLOYEE_NAME,
            TEST_USERNAME,
            "ESS",
            "Enabled",
            TEST_PASSWORD,
            TEST_PASSWORD
        );

        // Verify the user was created
        assertTrue(userManagementPage.isUserCreated(TEST_USERNAME), 
            "Test user should be created successfully");

        // Click edit for the test user
        userManagementPage.clickEditForUsername(TEST_USERNAME);

        // Get the user details from the edit form
        UserManagementPage.UserDetails loadedDetails = userManagementPage.getUserDetails();

        // Verify all details are loaded correctly
        assertEquals(TEST_USERNAME, loadedDetails.getUsername(), 
            "Username should match the created user");
        assertEquals(TEST_EMPLOYEE_NAME, loadedDetails.getEmployeeName(), 
            "Employee name should match the created user");
        assertEquals("ESS", loadedDetails.getUserRole(), 
            "User role should match the created user");
        assertEquals("Enabled", loadedDetails.getStatus(), 
            "Status should match the created user");

        // Clean up - delete the test user
        userManagementPage.deleteUserByUsername(TEST_USERNAME);
    }
} 