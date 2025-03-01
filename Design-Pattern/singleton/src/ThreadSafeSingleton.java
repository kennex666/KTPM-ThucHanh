public class ThreadSafeSingleton {
//    private static ThreadSafeSingleton instance;
//    private ThreadSafeSingleton() {
//        System.out.println("ThreadSafeSingleton is created");
//    }
//
//    public static synchronized ThreadSafeSingleton getInstance() {
//        if (instance == null) {
//            instance = new ThreadSafeSingleton();
//        }
//        return instance;
//    }
//
//    public void print() {
//        System.out.println("ThreadSafeSingleton print");
//    }


    private static volatile ThreadSafeSingleton instance;
    private ThreadSafeSingleton() {}
    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}
