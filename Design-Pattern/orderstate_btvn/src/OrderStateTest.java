public class OrderStateTest {
    public static void main(String[] args) {
        System.out.println("--- Testing State Pattern ---");
        OrderContext state = new OrderContext();
        state.processOrder();
        state.processOrder();
        state.processOrder();
        state.processOrder();
    }
}