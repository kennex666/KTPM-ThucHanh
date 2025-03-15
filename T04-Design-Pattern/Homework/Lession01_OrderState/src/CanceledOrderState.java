public class CanceledOrderState implements OrderState {
    @Override
    public void handleOrder(OrderContext context) {
        System.out.println("Đơn hàng đã bị hủy và hoàn tiền!");
    }
}
