package server.DataSourcesTest;

import common.Exceptions.InvalidArgumentValueException;
import common.dataClasses.Asset;
import org.junit.jupiter.api.*;
import server.DBConnection;
import server.DataSourceClasses.AssetsDataSource;
import server.DataSourceClasses.CasesToResponse;

import static org.junit.jupiter.api.Assertions.*;

class AssetsDataSourceTest {

    private static AssetsDataSource assetsDataSource;
    private static AssetsDataSource emptyAssetsDataSource;

    @BeforeAll
    static void startTestMode(){
        DBConnection.setTestMode(true);
    }

    @AfterAll
    static void stopTestMode(){
        DBConnection.setTestMode(false);
    }

    @BeforeEach
    void setUp() {
        CasesToResponse.cleanDatabase();
        assetsDataSource = new AssetsDataSource();
    }

    @AfterEach
    void tearDown(){
        CasesToResponse.cleanDatabase();
    }

    @Test
    void addAndGetAsset() throws InvalidArgumentValueException {
        assetsDataSource.addAsset(new Asset(0, "Bitcoin", "Crypto currency"));
        assertEquals(0, assetsDataSource.getAsset(0).getId());
        assertEquals("Bitcoin", assetsDataSource.getAsset(0).getName()) ;
        assertEquals("Crypto currency", assetsDataSource.getAsset(0).getDescription());
    }

    @Test
    void deleteAssetAndGetAssetList() throws InvalidArgumentValueException {
        assetsDataSource.addAsset(new Asset(0, "Bitcoin", "Crypto currency"));
        assetsDataSource.deleteAsset(0);
        assertEquals(0, assetsDataSource.getAssetList().size());
    }

    @Test
    void deleteAllAssetAndGetAssetList() throws InvalidArgumentValueException {
        emptyAssetsDataSource = new AssetsDataSource();
        assetsDataSource.addAsset(new Asset(0, "Bitcoin", "Crypto currency"));
        assetsDataSource.addAsset(new Asset(3, "Ethereum", "Crypto currency"));
        assetsDataSource.addAsset(new Asset(1, "GPU 100x", "GPU cal power"));
        assetsDataSource.addAsset(new Asset(2, "GPU 500x", "GPU cal power"));
        assetsDataSource.deleteAllAsset();
        assertEquals(0, assetsDataSource.getAssetList().size()) ;
    }

    @Test
    void editAsset() throws InvalidArgumentValueException {
        assetsDataSource.addAsset(new Asset(0, "Bitcoin", "Crypto currency"));
        assertEquals(assetsDataSource.getAsset(0).getName(), "Bitcoin");
        assetsDataSource.editAsset(new Asset(0, "Ethereum", "Crypto"));
        assertEquals("Ethereum", assetsDataSource.getAsset(0).getName());
        assertEquals( "Crypto", assetsDataSource.getAsset(0).getDescription());
    }
}