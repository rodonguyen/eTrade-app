package server;

import common.Exceptions.InvalidArgumentValueException;
import common.Request;
import common.Response;
import common.dataClasses.*;
import common.dataClasses.Order;
import common.dataClasses.User;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test server's response based on request
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ServerTest {
    IServer server;
    User admin;
    Request request;

    DataCollection<User> expectedUsers;
    DataCollection<Asset> expectedAssets;
    DataCollection<OrganisationalUnit> expectedOrganisationalUnits;
    DataCollection<Stock> expectedStocks;
    DataCollection<Order> expectedOrders;

    @BeforeAll
    void setUp() {
        server = new MockServer();
        admin = new User("admin", "root");
    }

    @BeforeEach
    void resetExpectedData() throws InvalidArgumentValueException {
        expectedUsers = new DataCollection<>();
        expectedAssets = new DataCollection<>();
        expectedOrganisationalUnits = new DataCollection<>();
        expectedStocks = new DataCollection<>();
        expectedOrders = new DataCollection<>();

        expectedUsers.add(new User(0, "Admin", "admin", "root", "admin", 0));
        expectedUsers.add(new User(1, "Dan Tran", "dan", "123", "user", 0));
        expectedUsers.add(new User(2, "Daniel Pham", "duy", "abcd", "user", 1));
        expectedUsers.add(new User(3, "Linh Hoang", "lyn", "password", "user", 2));
        expectedUsers.add(new User(4, "Rodo Nguyen", "rodo", "rodo", "user", 3));

        expectedAssets.add(new Asset(0, "CPU Hours", "CPU for rent"));
        expectedAssets.add(new Asset(1, "10 GB Database Server", "Remove SQL Server"));
        expectedAssets.add(new Asset(2, "A Generic Video Game", "Nothing is more generic than this."));
        expectedAssets.add(new Asset(3, "Coffin Dance Video", "You know what this is"));

        expectedOrganisationalUnits.add(new OrganisationalUnit(0, "The Justice League", 9999.0f));
        expectedOrganisationalUnits.add(new OrganisationalUnit(1, "The supervillains", 5555.0f));
        expectedOrganisationalUnits.add(new OrganisationalUnit(2, "The random civilians", 500.0f));
        expectedOrganisationalUnits.add(new OrganisationalUnit(3, "The brokers", 200.0f));

        Stock stock0 = new Stock(0);
        Stock stock1 = new Stock(1);
        Stock stock2 = new Stock(2);
        Stock stock3 = new Stock(3);
        stock0.add(new Item(expectedAssets.get(0), 99));
        stock0.add(new Item(expectedAssets.get(1), 99));
        stock0.add(new Item(expectedAssets.get(2), 99));
        stock0.add(new Item(expectedAssets.get(3), 99));

        stock1.add(new Item(expectedAssets.get(0), 55));
        stock1.add(new Item(expectedAssets.get(1), 55));
        stock1.add(new Item(expectedAssets.get(2), 55));
        stock1.add(new Item(expectedAssets.get(3), 55));

        stock2.add(new Item(expectedAssets.get(0), 10));
        stock2.add(new Item(expectedAssets.get(1), 10));
        stock2.add(new Item(expectedAssets.get(2), 10));
        stock2.add(new Item(expectedAssets.get(3), 10));
        expectedStocks.addAll(new Stock[]{stock0, stock1, stock2, stock3});

        expectedOrders.add(new Order(0, Order.Type.BUY, 0, 0, 23, 0, 22f, null, LocalDateTime.of(2021, 5, 15, 16, 52), Order.Status.PENDING));
        expectedOrders.add(new Order(1, Order.Type.BUY, 0, 1, 32, 0, 3f, null, LocalDateTime.of(2021, 5, 13, 13, 42), Order.Status.PENDING));
        expectedOrders.add(new Order(2, Order.Type.BUY, 0, 2, 45, 0, 4f, null, LocalDateTime.of(2021, 5, 14, 7, 45), Order.Status.PENDING));
        expectedOrders.add(new Order(3, Order.Type.BUY, 0, 3, 36, 0, 5f, null, LocalDateTime.of(2021, 5, 12, 22, 00), Order.Status.PENDING));
        expectedOrders.add(new Order(4, Order.Type.BUY, 1, 0, 74, 0, 6f, null, LocalDateTime.of(2021, 5, 10, 21, 52), Order.Status.PENDING));
        expectedOrders.add(new Order(5, Order.Type.BUY, 1, 1, 32, 0, 7f, null, LocalDateTime.of(2021, 5, 9, 15, 26), Order.Status.PENDING));
        expectedOrders.add(new Order(6, Order.Type.BUY, 1, 2, 45, 0, 8f, null, LocalDateTime.of(2021, 5, 12, 18, 28), Order.Status.PENDING));
        expectedOrders.add(new Order(7, Order.Type.BUY, 1, 3, 64, 0, 9f, null, LocalDateTime.of(2021, 5, 9, 13, 36), Order.Status.PENDING));
        expectedOrders.add(new Order(8, Order.Type.SELL, 2, 0, 76, 0, 10f, null, LocalDateTime.of(2021, 5, 13, 14, 45), Order.Status.PENDING));
        expectedOrders.add(new Order(9, Order.Type.SELL, 2, 1, 86, 0, 10.5f, null, LocalDateTime.of(2021, 5, 5, 11, 14), Order.Status.PENDING));
        expectedOrders.add(new Order(10, Order.Type.SELL, 2, 2, 42, 0, 11.5f, null, LocalDateTime.of(2021, 5, 2, 7, 15), Order.Status.PENDING));
        expectedOrders.add(new Order(11, Order.Type.SELL, 2, 3, 43, 0, 12.5f, null, LocalDateTime.of(2021, 5, 6, 4, 20), Order.Status.PENDING));
        expectedOrders.add(new Order(12, Order.Type.SELL, 3, 0, 56, 0, 13.5f, null, LocalDateTime.of(2021, 5, 8, 6, 21), Order.Status.PENDING));
        expectedOrders.add(new Order(13, Order.Type.SELL, 3, 1, 54, 0, 12.5f, null, LocalDateTime.of(2021, 5, 4, 8, 30), Order.Status.PENDING));
        expectedOrders.add(new Order(14, Order.Type.SELL, 3, 2, 37, 0, 14.5f, null, LocalDateTime.of(2021, 5, 11, 0, 11), Order.Status.PENDING));
        expectedOrders.add(new Order(15, Order.Type.SELL, 3, 3, 82, 0, 15.5f, null, LocalDateTime.of(2021, 5, 13, 3, 42), Order.Status.PENDING));
    }

    @Nested
    class readTest{
        Response actualResponse;
        Response<IData> expectedResponse;

        @AfterEach
        void executeAssertion() throws InvalidArgumentValueException {
            actualResponse = server.createResponse(request);
            assertEquals(expectedResponse, actualResponse);
        }

        @RepeatedTest(5)
        void validLoginTest(RepetitionInfo repetitionInfo) {
            String username = expectedUsers.get(repetitionInfo.getCurrentRepetition() - 1).getUsername();
            String password = expectedUsers.get(repetitionInfo.getCurrentRepetition() - 1).getPassword();
            User tempUser = new User(username, password);
            request = new Request<>(tempUser, "login");
            expectedResponse = new Response<>(true, expectedUsers.get(repetitionInfo.getCurrentRepetition() - 1));
        }

        @Test
        void invalidLoginTest() {
            User tempUser = new User("dan", "12345");
            request = new Request<>(tempUser, "login");
            expectedResponse = new Response<>(false, null);
        }

        @Test
        void queryUsersTest() throws InvalidArgumentValueException {
            request = new Request<>(admin, "query users");
            expectedResponse = new Response<>(true, expectedUsers);
        }

        @Test
        void queryAssetsTest() throws InvalidArgumentValueException {
            request = new Request<User>(admin, "query assets");
            expectedResponse = new Response<>(true, expectedAssets);
        }

        @Test
        void queryOrganisationalUnitsTest() throws InvalidArgumentValueException {
            request = new Request<>(admin, "query organisationalUnits");
            expectedResponse = new Response<>(true, expectedOrganisationalUnits);
        }

        @Test
        void queryStocksTest() throws InvalidArgumentValueException {
            request = new Request<>(admin, "query stocks");
            expectedResponse = new Response<>(true, expectedStocks);
        }

        @Test
        void queryOrdersTest() throws InvalidArgumentValueException {
            request = new Request<>(expectedUsers.get(0), "query orders");
            expectedResponse = new Response<>(true, expectedOrders);
        }

        @RepeatedTest(4)
        void queryStockTest(RepetitionInfo repetitionInfo) throws InvalidArgumentValueException {
            request = new Request<>(expectedUsers.get(repetitionInfo.getCurrentRepetition()), "query stock");
            expectedResponse = new Response<>(true, expectedStocks.get(repetitionInfo.getCurrentRepetition() - 1));
        }

        @RepeatedTest(4)
        void queryOrganisationalUnitTest(RepetitionInfo repetitionInfo) throws InvalidArgumentValueException {
            request = new Request<User>(expectedUsers.get(repetitionInfo.getCurrentRepetition()), "query stock");
            expectedResponse = new Response<>(true, expectedStocks.get(repetitionInfo.getCurrentRepetition() - 1));
        }
    }

    /**
     * Tests actions that modifies the database
     */
    @Nested
    class modificationTest{
        DataCollection<? extends IData> expectedCollection;
        DataCollection<IData> actualCollection;
        Request<? extends IData> queryRequest;

        /**
         * Expected behaviours:
         * - Takes a request attached with an object implementing IData with any value for ID
         * - Changes the object's ID to an appropriate value
         * - Appends the object to the database
         */
        @Nested
        class creationTest {

            @Test
            void userCreationTest() {
                User newUser = new User(-1, "New User", "newuser", "012345", "user", 0);

                request = new Request<>(admin, "add", newUser);
                request.setAttachmentType(User.class);

                newUser.setUserId(5);
                expectedUsers.add(newUser);
                expectedCollection = expectedUsers;

                queryRequest = new Request<>(admin, "query users");
            }

            @Test
            void assetCreationTest() throws InvalidArgumentValueException {
                Asset newAsset = new Asset(-1, "New Asset","N/A");

                request = new Request<>(admin, "add", newAsset);
                request.setAttachmentType(Asset.class);

                newAsset.setId(4);
                expectedAssets.add(newAsset);
                expectedCollection = expectedAssets;

                queryRequest = new Request<>(admin, "query assets");
            }

            @Test
            void organisationalUnitCreationTest(){
                OrganisationalUnit newOrganisationalUnit = new OrganisationalUnit(-1, "New OrganisationalUnit",150f);

                request = new Request<>(admin, "add", newOrganisationalUnit);
                request.setAttachmentType(OrganisationalUnit.class);

                newOrganisationalUnit.setId(4);
                expectedOrganisationalUnits.add(newOrganisationalUnit);
                expectedCollection = expectedOrganisationalUnits;

                queryRequest = new Request<>(admin, "query organisationalUnits");
            }

            @Test
            void orderCreationTest(){
                Order newOrder = new Order(-1, Order.Type.BUY, 0, 0, 23, 0,
                        22f, null, LocalDateTime.of(2021, 5, 13, 16, 52), Order.Status.PENDING);

                request = new Request<>(admin, "add", newOrder);
                request.setAttachmentType(Order.class);

                newOrder.setOrderId(16);
                expectedOrders.add(newOrder);
                expectedCollection = expectedOrders;

                queryRequest = new Request<>(admin, "query orders");
            }
        }

        /**
         * Expected behaviours:
         * - Takes a request attached with an object implementing IData with a set (unchanged) ID
         * - Overrides the row in a database's table, identified by the above ID
         */
        @Nested
        class updatingTest {

            @RepeatedTest(5)
             void userUpdatingTest(RepetitionInfo repetitionInfo){
                User overrideUser = new User(repetitionInfo.getCurrentRepetition() - 1,
                        "User " + (repetitionInfo.getCurrentRepetition() - 1),
                        "user" + (repetitionInfo.getCurrentRepetition() - 1),
                        "p@ssw" + (repetitionInfo.getCurrentRepetition() - 1) + "rd",
                        "user",
                        (repetitionInfo.getCurrentRepetition() - 1));

                request = new Request<>(admin, "edit", overrideUser);
                request.setAttachmentType(User.class);

                expectedUsers.set(repetitionInfo.getCurrentRepetition() - 1, overrideUser);
                expectedCollection = expectedUsers;

                queryRequest = new Request<>(admin, "query users");
            }

            @RepeatedTest(4)
             void assetUpdatingTest(RepetitionInfo repetitionInfo) throws InvalidArgumentValueException {
                Asset overrideAsset = new Asset(repetitionInfo.getCurrentRepetition() - 1,
                        "Asset " + (repetitionInfo.getCurrentRepetition() - 1),
                        "This is asset " + (repetitionInfo.getCurrentRepetition() - 1));

                request = new Request<>(admin, "edit", overrideAsset);
                request.setAttachmentType(Asset.class);

                expectedAssets.set(repetitionInfo.getCurrentRepetition() - 1, overrideAsset);
                expectedCollection = expectedAssets;

                queryRequest = new Request<>(admin, "query assets");
            }

            @RepeatedTest(4)
             void organisationalUnitUpdatingTest(RepetitionInfo repetitionInfo){
                OrganisationalUnit overrideOrganisationalUnit = new OrganisationalUnit(repetitionInfo.getCurrentRepetition() - 1,
                        "OrganisationalUnit " + (repetitionInfo.getCurrentRepetition() - 1),
                        1000f * (repetitionInfo.getCurrentRepetition() - 1));

                request = new Request<>(admin, "edit", overrideOrganisationalUnit);
                request.setAttachmentType(OrganisationalUnit.class);

                expectedOrganisationalUnits.set(repetitionInfo.getCurrentRepetition() - 1, overrideOrganisationalUnit);
                expectedCollection = expectedOrganisationalUnits;

                queryRequest = new Request<>(admin, "query organisationalUnits");
            }

            @RepeatedTest(16)
             void orderUpdatingTest(RepetitionInfo repetitionInfo){
                Order overrideOrder = expectedOrders.get(repetitionInfo.getCurrentRepetition() - 1);
                overrideOrder.setStatus(Order.Status.CANCELLED);

                request = new Request<>(admin, "edit", overrideOrder);
                request.setAttachmentType(Order.class);

                expectedOrders.set(repetitionInfo.getCurrentRepetition() - 1, overrideOrder);
                expectedCollection = expectedOrders;

                queryRequest = new Request<>(admin, "query orders");
            }

            @RepeatedTest(4)
            void stockUpdatingTest(RepetitionInfo repetitionInfo) throws InvalidArgumentValueException {
                Stock overrideStock = new Stock(repetitionInfo.getCurrentRepetition());
                overrideStock.add(new Item(expectedAssets.get(repetitionInfo.getTotalRepetitions() - 1), repetitionInfo.getCurrentRepetition() * 5));

                request = new Request<>(admin, "edit", overrideStock);
                request.setAttachmentType(Stock.class);

                expectedStocks.set(repetitionInfo.getCurrentRepetition() - 1, overrideStock);
                expectedCollection = expectedStocks;

                queryRequest = new Request<>(admin, "query stocks");
            }
        }

        /**
         * Expected behaviours:
         * - Takes a request attached with an object implementing IData with a set (unchanged) ID
         * - Delete the row identified by the above ID
         */
        @Nested
        class deletionTest {

            @RepeatedTest(5)
            void userUpdatingTest(RepetitionInfo repetitionInfo){
                request = new Request<>(admin, "delete", expectedUsers.get(repetitionInfo.getCurrentRepetition() - 1));
                request.setAttachmentType(User.class);

                expectedUsers.remove(repetitionInfo.getCurrentRepetition() - 1);
                expectedCollection = expectedUsers;

                queryRequest = new Request<>(admin, "query users");
            }

            @RepeatedTest(4)
            void assetUpdatingTest(RepetitionInfo repetitionInfo) throws InvalidArgumentValueException {
                request = new Request<>(admin, "delete", expectedAssets.get(repetitionInfo.getCurrentRepetition() - 1));
                request.setAttachmentType(Asset.class);

                expectedAssets.remove(repetitionInfo.getCurrentRepetition() - 1);
                expectedCollection = expectedAssets;

                queryRequest = new Request<>(admin, "query assets");
            }

            @RepeatedTest(4)
            void organisationalUnitUpdatingTest(RepetitionInfo repetitionInfo){
                request = new Request<>(admin, "delete", expectedOrganisationalUnits.get(repetitionInfo.getCurrentRepetition() - 1));
                request.setAttachmentType(OrganisationalUnit.class);

                expectedOrganisationalUnits.remove(repetitionInfo.getCurrentRepetition() - 1);
                expectedCollection = expectedOrganisationalUnits;

                queryRequest = new Request<>(admin, "query organisationalUnits");
            }

            @RepeatedTest(16)
            void orderUpdatingTest(RepetitionInfo repetitionInfo){
                request = new Request<>(admin, "delete", expectedOrders.get(repetitionInfo.getCurrentRepetition() - 1));
                request.setAttachmentType(Order.class);

                expectedOrders.remove(repetitionInfo.getCurrentRepetition() - 1);
                expectedCollection = expectedOrders;

                queryRequest = new Request<>(admin, "query orders");
            }
        }
    }
}