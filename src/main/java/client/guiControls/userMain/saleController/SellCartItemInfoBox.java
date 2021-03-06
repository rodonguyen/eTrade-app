package client.guiControls.userMain.saleController;

import client.IViewUnit;
import common.Exceptions.InvalidArgumentValueException;
import common.dataClasses.CartItem;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A box to display an item information and can be interacted with.
 */
public class SellCartItemInfoBox extends VBox implements IViewUnit {
    private SaleController controller;
    private CartItem cartItem;

    private Label nameLabel;
    private Label quantityTextField;
    private Label priceTextField;
    private Label totalPriceLabel;

    private Button removeButton;

    /**
     * Initialises an info box for the cartItem in the organisational unit's stock.
     * @param cartItem The linked cartItem.
     * @param controller The associated display controller containing this box.
     */
    public SellCartItemInfoBox(CartItem cartItem, SaleController controller){
        this.cartItem = cartItem;
        this.controller = controller;

        initialize();
        load();
    }

    /**
     * Initialise the display elements and their styling.
     */
    @Override
    public void initialize(){
        this.setAlignment(Pos.CENTER_LEFT);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("blackLabel");
        nameLabel.getStyleClass().add("boldText");
        nameLabel.setId("cartItemNameLabel" + cartItem.getId());

        totalPriceLabel = new Label();
        totalPriceLabel.getStyleClass().add("blackLabel");
        totalPriceLabel.getStyleClass().add("smallText");
        totalPriceLabel.getStyleClass().add("boldText");
        totalPriceLabel.setPrefWidth(100);
        totalPriceLabel.setId("cartItemTotalPriceLabel" + cartItem.getId());


        quantityTextField = new Label("Quantity: " + cartItem.getQuantity());
        quantityTextField.setPrefWidth(150);
        quantityTextField.getStyleClass().add("blackLabel");
        quantityTextField.setId("cartItemSellQuantityTextField" + cartItem.getId());

        priceTextField = new Label("Price: " + cartItem.getPrice());
        priceTextField.setPrefWidth(150);
        priceTextField.getStyleClass().add("blackLabel");
        priceTextField.setId("cartItemSellPriceTextField" + cartItem.getId());

        removeButton = new Button("x");
        removeButton.setOnAction(e -> {
            try {
                removeBox();
            } catch (InvalidArgumentValueException invalidArgumentValueException) {
                invalidArgumentValueException.printStackTrace();
            }
        });
        removeButton.setId("cartItemRemoveButton" + cartItem.getId());

        HBox upperInfoRow = new HBox();
        upperInfoRow.setSpacing(20);
        upperInfoRow.setAlignment(Pos.CENTER_LEFT);
        upperInfoRow.setPrefWidth(350);
        upperInfoRow.getChildren().addAll(priceTextField, totalPriceLabel);

        HBox lowerInfoRow = new HBox();
        lowerInfoRow.setSpacing(20);
        lowerInfoRow.setAlignment(Pos.CENTER_LEFT);
        lowerInfoRow.setPrefWidth(350);
        lowerInfoRow.getChildren().addAll(quantityTextField, removeButton);

        this.getChildren().addAll(nameLabel, upperInfoRow, lowerInfoRow);
    }


    /**
     * Sets the item linked with the display and reset the display.
     * @param cartItem The linked item.
     */
    public void setCartItem(CartItem cartItem){
        this.cartItem = cartItem;
        loadView();
    }

    /**
     * Display the GUI components.
     */
    private void loadView(){
        load();
        this.getChildren().clear();
    }

    /**
     * Loads the underlying data to the GUI components.
     */
    @Override
    public void load(){
        loadNameLabel();
        loadTotalPriceLabel();
    }

    /**
     * loads a label displaying the cartItem's name.
     */
    private void loadNameLabel(){
        nameLabel.setText(cartItem.getName());
    }

    /**
     * loads a label displaying the cartItem's name.
     */
    private void loadTotalPriceLabel(){
        totalPriceLabel.setText("Total: " + cartItem.getTotalPrice());
    }

    /**
     * Removes the current box.
     */
    private void removeBox() throws InvalidArgumentValueException {
        controller.removeCartItem(cartItem);
    }
}