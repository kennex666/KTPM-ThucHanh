public class ProcessingOrderState implements OrderState {

    @Override
    public void handleOrder(OrderContext context) {
        System.out.println("Đóng gói và vận chuyển...");
        context.setState(new DeliveredOrderState());
    }
}
