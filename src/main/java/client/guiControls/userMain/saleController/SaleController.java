package client.guiControls.userMain.saleController;

import client.guiControls.DisplayController;
import client.guiControls.userMain.CurrentOrdersInfoBox;
import client.guiControls.userMain.UserLocalDatabase;
import common.Exceptions.InvalidArgumentValueException;
import common.Request;
import common.dataClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import server.DataSourceClasses.OrderDataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A controller to control the SELL Page (which allows the user to sell items from their organisation's stock).
 */
public class SaleController extends DisplayController {
    Cart sellCart = new Cart(Order.Type.SELL);
    Stock tempStock;
    DataCollection<Order> marketBuyOrders;
    DataCollection<Order> myOrders;

    @FXML Pagination marketBuyOrdersDisplay;
    @FXML VBox stockDisplayBox;
    @FXML VBox sellCartDisplayBox;
    @FXML Button sellAllButton;
    @FXML Label saleTotalLabel;
    @FXML
    Pane buyChartContainer;
    @FXML
    VBox myCurrentBuys;

    /**
     * Displays a new box containing an item's information in the stock.
     * @param item The linked item.
     */
    private void addItemInfoBox(Item item){
        LinkedHashMap<LocalDate, Float> priceHistory = ((UserLocalDatabase) controller.getDatabase()).getPriceHistory(item, Order.Type.BUY);
        SellItemInfoBox sellItemInfoBox = new SellItemInfoBox(item,priceHistory, this);
        stockDisplayBox.getChildren().add(sellItemInfoBox);
    }

    /**
     * Displays a new box containing a cartItem's information in the cart.
     * @param cartItem The linked cartItem.
     */
    private void addCartItemInfoBox(CartItem cartItem){
        SellCartItemInfoBox sellCartItemInfoBox = new SellCartItemInfoBox(cartItem, this);
        sellCartDisplayBox.getChildren().add(sellCartItemInfoBox);
    }

    /**
     * Sell an associated item and remove it from stock.
     * This overload is called by the user placing the order manually.
     * @param item The item to sell
     * @param quantity The quantity to place
     * @param price the price to place
     */
    public void sellItem(Item item, int quantity, float price) throws InvalidArgumentValueException {
        int linkedItemIndex = tempStock.indexOf(item);
        CartItem cartItem = item.moveToCart(quantity, price);
        sellCart.add(cartItem);
        addCartItemInfoBox(cartItem);
        tempStock.set(linkedItemIndex, item);
        refresh();
    }

    /**
     * Attempts to place an order using an asset (that may not be in stock)
     * This overload is called using the quick sell button on the current market orders.
     * @param asset The item to sell
     * @param quantity The quantity to place
     * @param price the price to place
     */
    public void sellItem(Asset asset, int quantity, float price) throws InvalidArgumentValueException {
        for (Item i : tempStock){
            if (i.getId().equals(asset.getId())){
                int sellQuantity = Math.min(quantity, i.getQuantity());
                sellItem(i, sellQuantity, price);
                break;
            }
        }
    }

    /**
     * Copy information from a market's order and allows the user to customise it.
     * @param asset The asset linked with the order
     * @param quantity The quantity of the order
     * @param price The price of the order
     */
    public void customiseItem(Asset asset, int quantity, float price){
        ((SellItemInfoBox) stockDisplayBox.lookup("#sellItemInfoBox" + asset.getId()))
                .setQuantity(quantity)
                .setPrice(price);
    }

    /**
     * Removes an item from a cart and adds it back to the stock.
     * @param cartItem The returning cart item.
     */
    public void removeCartItem(CartItem cartItem) throws InvalidArgumentValueException {
        sellCart.remove(cartItem);
        for (int i = 0; i < tempStock.size(); i++){
            if (tempStock.get(i).getId().equals(cartItem.getId())){
                Item returnItem = tempStock.get(i);
                returnItem.setQuantity(returnItem.getQuantity() + cartItem.getQuantity());
                tempStock.set(i, returnItem);
                break;
            }
        }
        refresh();
    }

    /**
     * Updates the total price of the cart.
     */
    private void updateTotal(){
        saleTotalLabel.setText("Total: " + sellCart.getTotalPrice());
    }

    /**
     * Refreshes the view.
     */
    public void refresh(){
        stockDisplayBox.getChildren().clear();
        for (Item item : tempStock){
            addItemInfoBox(item);
        }
        sellCartDisplayBox.getChildren().clear();
        for (CartItem cartItem : sellCart){
            addCartItemInfoBox(cartItem);
        }
        updateTotal();
    }

    /**
     * Sells all items in the cart.
     */
    public void checkOut() throws InvalidArgumentValueException {
        OrderDataSource orderDataSource = new OrderDataSource();
        int unitId = ((UserLocalDatabase)controller.getDatabase()).getOrganisationalUnit().getId();
        for (CartItem cartItem : sellCart){
            int id = orderDataSource.getOrderList().size();
            Order newOrder = new Order(id, Order.Type.SELL, unitId, cartItem.getId(), cartItem.getQuantity(), 0, cartItem.getPrice(),
                    null, LocalDateTime.now(), Order.Status.PENDING);
            controller.sendRequest(Request.ActionType.CREATE, newOrder, Request.ObjectType.ORDER);
        }
        sellCart.clear();
        update();
    }

    /**
     * Updates with database and displays all items in stock.
     */
    @Override
    public void update(){
        UserLocalDatabase localDatabase = (UserLocalDatabase) controller.getDatabase();
        tempStock = localDatabase.getStock();
        marketBuyOrders = localDatabase.getMarketOrders(Order.Type.BUY);
        if(marketBuyOrdersDisplay != null){
            // Resets the page and page factory http://www.java2s.com/Tutorials/Java/JavaFX/0610__JavaFX_Pagination.htm
            marketBuyOrdersDisplay.setPageFactory(pageIndex -> createPage(pageIndex));
            marketBuyOrdersDisplay.setPageCount((int) Math.ceil((float) marketBuyOrders.size() / ordersPerPage));
        }
        // Show the orders from the current organisational unit
        myOrders = localDatabase.getOwnOrders(Order.Type.BUY);
        for (Order order : myOrders){
            CurrentOrdersInfoBox mySellOrderInfoBox = new CurrentOrdersInfoBox(order, this);
            myCurrentBuys.getChildren().add(mySellOrderInfoBox);
        }
        refresh();
        refresh();
    }

    // TODO: Refactor
    /**
     * The number of orders to be displayed per page.
     */
    private final int ordersPerPage = 7;

    /**
     * Creates a page at a certain index.
     */
    public VBox createPage(int pageIndex){
        VBox ordersContainerBox = new VBox();
        ordersContainerBox.setSpacing(10);

        int startingOrderIndex = pageIndex * ordersPerPage;
        for (int i = startingOrderIndex; i < startingOrderIndex + ordersPerPage && i < marketBuyOrders.size(); i++){
            MarketBuyOrderInfoBox MarketBuyOrderInfoBox = new MarketBuyOrderInfoBox(marketBuyOrders.get(i), this);
            ordersContainerBox.getChildren().add(MarketBuyOrderInfoBox);
        }
        return ordersContainerBox;
    }

    /**
     * Shows an asset's price history in the chart pane.
     * @param priceHistory The price history to show.
     */
    public void showHistory(LinkedHashMap<LocalDate, Float> priceHistory){
        ObservableList<XYChart.Series<String, Float>> series = FXCollections.observableArrayList();

        ObservableList<XYChart.Data<String, Float>> data = FXCollections.observableArrayList();
        Iterator iter = priceHistory.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry pair = (Map.Entry) iter.next();
            LocalDate currentDate = (LocalDate) pair.getKey();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
            String date = ((LocalDate) pair.getKey()).format(formatter);
            data.add(new XYChart.Data<String, Float>(date, (Float) pair.getValue()));
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");
        series.add(new XYChart.Series<String, Float>("Buy Prices", data));
        LineChart priceHistoryChart = new LineChart(xAxis, yAxis, series);
        priceHistoryChart.setCreateSymbols(false);
        priceHistoryChart.setPrefHeight(300);
        priceHistoryChart.setLegendVisible(false);
        priceHistoryChart.setAnimated(false);
        buyChartContainer.getChildren().clear();
        buyChartContainer.getChildren().add(priceHistoryChart);
    }
}
