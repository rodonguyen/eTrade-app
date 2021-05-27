package server.Excluded_PUT_ALL_EXCLUSIONS_HERE.Features;

import common.Exceptions.InvalidArgumentValueException;
import common.dataClasses.*;
import server.Excluded_PUT_ALL_EXCLUSIONS_HERE.DataSourceClasses.*;

public class TestDatabase {
    public static void main(String[] args) throws InvalidArgumentValueException {
        OrganisationsDataSource organisationsDataSource = new OrganisationsDataSource();
        StockDataSource stockDataSource = new StockDataSource();
        OrderDataSource orderDataSource = new OrderDataSource();
        UserDataSource users = new UserDataSource();
        AssetsDataSource assets = new AssetsDataSource();

    }
}