public class LazyInitializedSingleton {
    private static LazyInitializedSingleton instance;
    private LazyInitializedSingleton() {
        System.out.println("LazyInitializedSingleton");
    }

    public static LazyInitializedSingleton getInstance() {
        if (instance == null) {
            instance = new LazyInitializedSingleton();
        }
        return instance;
    }

    public void print() {
        System.out.println("This is LazyInitializedSingleton");
    }
}
