package client.data;

import common.dataClasses.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest {

    private static Object[][] users = new Object[][] {
            {"admin", "root", "admin", 0},
            {"dan", "123", "user", 0},
            {"duy", "abcd", "user", 1},
            {"lyn", "password", "user", 1},
            {"rodo", "rodo", "user", 2}
    };

    private static Object[][] organisations = new Object[][]{
            {0, "The Justice League", 9999.0f},
            {1, "The supervillains", 5555.0f},
            {2, "The random civilians", 500.0f}
    };

    private static Object[][][] organisationStocks = new Object[][][]{
            {
                    {"CPU Hours", 99},
                    {"10 GB Database Server", 99},
                    {"A Generic Video Game", 99},
                    {"Coffin Dance Video", 99}
            },
            {
                    {"CPU Hours", 55},
                    {"10 GB Database Server", 55},
                    {"A Generic Video Game", 55},
                    {"Coffin Dance Video", 55}
            },
            {
                    {"CPU Hours", 10},
                    {"10 GB Database Server", 10},
                    {"A Generic Video Game", 10},
                    {"Coffin Dance Video", 10}
            },
    };

    private MainController mainController;

    @BeforeEach
    void setUp() {
        this.mainController = new MainController();
        mainController.setServerConnection(new MockServerConnection());
    }

    @Test
    void startValidSession() {
        for (int i = 0; i < users.length; i++) {
            Object[] userData = users[i];
            User user = new User((String) userData[0], (String) userData[1], (String) userData[2], (int) userData[3]);
            mainController.startSession(user);

            Object[] actualCurrentOrganisationData = new Object[]{
                    mainController.getOrganisation().getId(),
                    mainController.getOrganisation().getName(),
                    mainController.getOrganisation().getBalance()
            };

            Object[] expectedCurrentOrganisationData = organisations[user.getOrganisationId()];

            assertArrayEquals(expectedCurrentOrganisationData, actualCurrentOrganisationData);

            Object[][] actualCurrentStockData = new Object[][]{
                    {mainController.getStock().get(0).getName(), mainController.getStock().get(0).getQuantity()},
                    {mainController.getStock().get(1).getName(), mainController.getStock().get(1).getQuantity()},
                    {mainController.getStock().get(2).getName(), mainController.getStock().get(2).getQuantity()},
                    {mainController.getStock().get(3).getName(), mainController.getStock().get(3).getQuantity()}
            };

            Object[][] expectedCurrentStockData = organisationStocks[user.getOrganisationId()];
            assertArrayEquals(expectedCurrentStockData, actualCurrentStockData);
        }
    }

    @Test
    void testValidRequestLogin() {
        for (Object[] userData : users){
            String username = userData[0].toString();
            String password = userData[1].toString();
            boolean actualResult = mainController.requestLogin(username, password);
            assertTrue(actualResult);
        }
    }

    @Test
    void testInvalidRequestLogin() {
        String username = "asdbcasdfsa";
        String password = "12adasb23r4";
        boolean actualResult = mainController.requestLogin(username, password);
        assertFalse(actualResult);
    }
}