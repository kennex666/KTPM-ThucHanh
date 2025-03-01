public class Main {
    public static void main(String[] args) {
//        //1. EagerInitialized
//        EagerInitializedSingleton obj = EagerInitializedSingleton.getInstance();
//        System.out.println(obj);
//        obj.sayHello();

//        //2. Static Block
//        StaticBlockSingleton staticBlockSingleton = StaticBlockSingleton.getInstance();
//        System.out.println(staticBlockSingleton);
//        staticBlockSingleton.print();

//        //3. Lazy Initialization
//        LazyInitializedSingleton lazyInitializedSingleton = LazyInitializedSingleton.getInstance();
//        System.out.println(lazyInitializedSingleton);
//        lazyInitializedSingleton.print();

        //4. Thread Safe Singleton
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                ThreadSafeSingleton instance = ThreadSafeSingleton.getInstance();
//                System.out.println(instance);
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//        Thread thread2 = new Thread(runnable);
//        thread2.start();
//        Thread thread3 = new Thread(runnable);
//        thread3.start();

//        //5. Bill Pugh Singleton
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                BillPughSingleton singleton = BillPughSingleton.getInstance();
//                System.out.println(singleton);
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//        Thread thread2 = new Thread(runnable);
//        thread2.start();
//        Thread thread3 = new Thread(runnable);
//        thread3.start();

        // Reflection Singleton
        EagerInitializedSingleton instanceOne = EagerInitializedSingleton.getInstance();
    }
}