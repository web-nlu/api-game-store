package vn.edu.hcmaf.apigamestore.order.dto;

public interface OrderAdminDataProjection {
    int getTotalOrders();
    int getCompletedOrders();
    int getPendingOrders();
    int getCancelledOrders();
}
