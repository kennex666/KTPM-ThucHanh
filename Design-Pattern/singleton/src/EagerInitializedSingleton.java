public class EagerInitializedSingleton {
    private static final EagerInitializedSingleton instance = new EagerInitializedSingleton();

    private EagerInitializedSingleton() {
        System.out.println("EagerInitializedSingleton instance created");
    }

    public static EagerInitializedSingleton getInstance() {
        return instance;
    }

    public void sayHello() {
        System.out.println("EagerInitializedSingleton sayHello");
    }
}
