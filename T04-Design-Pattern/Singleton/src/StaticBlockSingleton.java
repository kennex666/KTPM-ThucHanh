public class StaticBlockSingleton {
    private static StaticBlockSingleton instance;

    private StaticBlockSingleton() {
        System.out.println("Creating a static block singleton");
    }

    static {
        try {
            instance = new StaticBlockSingleton();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static StaticBlockSingleton getInstance() {
        return instance;
    }

    public void print() {
        System.out.println("This is a static block singleton");
    }
}
