package common.dataClasses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an Order
 */
public class Order implements Serializable, IData {

    public enum Type{
        BUY,
        SELL
    }

    public enum Status{
        PENDING,
        APPROVED,
        CANCELLED
    }

    protected int orderId;
    protected Type orderType; //buy/sell
    protected int unitId;
    protected int assetId;
    protected int placedQuantity;//cartitem
    protected int resolvedQuantity = 0;
    protected float price;
    protected LocalDateTime finishDate = null; //Cartitem
    protected LocalDateTime orderDate;
    protected Status status;

    public Order(int orderId, Type orderType, int unitId, int assetId, int placedQuantity, int resolvedQuantity, float price, LocalDateTime finishDate, LocalDateTime orderDate, Status status) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.unitId = unitId;
        this.assetId = assetId;
        this.placedQuantity = placedQuantity;
        this.resolvedQuantity = resolvedQuantity;
        this.price = price;
        this.finishDate = finishDate;
        this.orderDate = orderDate;
        this.status = status;
    }

    /**
     * A method to record how many assets in an order have been successfully purchased
     * @param assetnumber number of the assets in the transaction
     */
    public  void ResolvedQuantity(int assetnumber) {
        resolvedQuantity += assetnumber;
    }

    /**
     *
     * @return Order ID
     */
    public int getOrderId(){return orderId;}

    /**
     *
     * @return the Order's type (Buy/Sell)
     */
    public Type getOrderType() {return orderType;}

    /**
     *
     * @return OrganisationalUnit ID
     */
    public int getUnitId(){return unitId;}

    /**
     *
     * @return Asset ID
     */
    public int getAssetId(){return assetId;}


    /**
     *
     * @return Order's Placed Quantity
     */
    public int getPlacedQuantity() {return placedQuantity;}

    /**
     *
     * @return Order's Resolved Quantity
     */
    public int getResolvedQuantity() {return resolvedQuantity;}

    /**
     *
     * @return Order total price
     */
    public float getPrice() {return price;}

    /**
     *
     * @return Transaction finished date
     */
    public LocalDateTime getFinishDate(){return finishDate;}

    /**
     *
     * @return Transaction placed date
     */
    public LocalDateTime getOrderDate() {return orderDate;}

    /**
     *
     * @return return Transaction's status (Finished/Remaining)
     */
    public Status getStatus() {return status;}

    /**
     * set the order id to given Int
     * @param order_id
     */
    public void setOrderID(int order_id) { this.orderId = order_id;
    }

    /**
     * set order type to new type
     * @param order_type
     */
    public void setOrderType(Type order_type) { this.orderType = order_type;
    }

    /**
     * Set new organisational unit ID
     * @param organisation_id
     */
    public void setunitId(int organisation_id) { this.unitId = organisation_id;
    }

    /**
     * Set new asset ID
     * @param asset_id
     */
    public void setAssetID(int asset_id) { this.assetId = asset_id;
    }

    /**
     * Set new quantity
     * @param placed_quantity
     */
    public void setPlacedQuantity(int placed_quantity) { this.placedQuantity = placed_quantity;
    }

    /**
     * Set new quantity
     * @param resolved_quantity
     */
    public void setResolvedQuantity(int resolved_quantity) { this.resolvedQuantity = resolved_quantity;
    }

    /**
     * Set new price
     * @param price
     */
    public void setPrice(float price) { this.price = price;
    }

    /**
     * Set new order date
     * @param order_date
     */
    public void setOrderDate(LocalDateTime order_date) { this.orderDate = order_date;
    }

    /**
     * Set new Finished date
     * @param finished_date
     */
    public void setFinishDate(LocalDateTime finished_date) { this.finishDate = finished_date;
    }

    /**
     * Set new status
     * @param status
     */
    public void setStatus(Status status) { this.status = status;
    }

    /**
     * Indicates if some object is equal to this instance.
     * @param o The object to compare.
     * @return true if the object is equal to the instance, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId && unitId == order.unitId && assetId == order.assetId && placedQuantity == order.placedQuantity && resolvedQuantity == order.resolvedQuantity && Float.compare(order.price, price) == 0 && Objects.equals(orderType, order.orderType) && Objects.equals(finishDate, order.finishDate) && Objects.equals(orderDate, order.orderDate) && Objects.equals(status, order.status);
    }

    /**
     * Returns the hashCode of this instance.
     * @return The hashCode of this instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderType, unitId, assetId, placedQuantity, resolvedQuantity, price, finishDate, orderDate, status);
    }
}