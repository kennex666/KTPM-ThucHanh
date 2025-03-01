public class BillPughSingleton {
    private BillPughSingleton() {
        System.out.println("Bill Pugh Singleton");
    }

    private static class SingletonHolder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void printBill() {
        System.out.println("This is Bill Pugh Singleton");
    }
}