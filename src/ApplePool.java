import java.util.*;

public class ApplePool {
    private List<Apple> pool;
    private int maxSize;
    private int screenWidth;
    private int screenHeight;

    public ApplePool(int maxSize, int screenWidth, int screenHeight) {
        this.maxSize = maxSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        pool = new ArrayList<>();
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < maxSize; i++) {
            pool.add(new Apple(screenWidth, screenHeight));
        }
    }

    public Apple borrowApple() {
        if (!pool.isEmpty()) {
            return pool.remove(0);
        } else {
            // Create a new object if the pool is empty (optional)
            return new Apple(screenWidth, screenHeight);
        }
    }

    public void returnApple(Apple apple) {
        if (pool.size() < maxSize) {
            pool.add(apple);
        }
    }
}
