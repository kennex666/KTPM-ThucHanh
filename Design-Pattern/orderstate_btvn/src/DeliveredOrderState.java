public class DeliveredOrderState implements OrderState{
    @Override
    public void handleOrder(OrderContext context) {
        System.out.println("Đơn hàng đã được giao!");
        context.setState(new CanceledOrderState());
    }
}
