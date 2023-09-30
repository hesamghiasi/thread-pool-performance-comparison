import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

class ThreadPoolPerformanceTest {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int start = 10;
    @ParameterizedTest
    @ValueSource(longs = {start, 10*start, 100*start, 1000*start, 10_000*start, 50_000*start, 100_000*start, 1_000_000*start})
    void testCachedThreadPoolPerformance(long numTasks) throws InterruptedException {
        if (numTasks == start)
            System.out.println("cachedThreadPool:");
        ExecutorService executor = Executors.newCachedThreadPool();
        runTasks(executor, numTasks);
    }

    @ParameterizedTest
    @ValueSource(longs = {start, 10*start, 100*start, 1000*start, 10_000*start, 50_000*start, 100_000*start, 1_000_000*start})
    void testForkJoinPoolPerformance(long numTasks) throws InterruptedException {
        if (numTasks == start)
            System.out.println("forkJoinThreadPool:");
        ForkJoinPool executor = new ForkJoinPool(NUM_THREADS);
        runTasks(executor, numTasks);
    }

    @ParameterizedTest
    @ValueSource(longs = {start, 10*start, 100*start, 1000*start, 10_000*start, 50_000*start, 100_000*start, 1_000_000*start})
    void testFixedThreadPoolPerformance(long numTasks) throws InterruptedException {
        if (numTasks == start)
            System.out.println("fixedThreadPool:");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        runTasks(executor, numTasks);
    }

    @ParameterizedTest
    @ValueSource(longs = {start, 10*start, 100*start, 1000*start, 10_000*start, 50_000*start, 100_000*start, 1_000_000*start})
    void testVirtualThreadPoolPerformance(long numTasks) throws InterruptedException {
        if (numTasks == start)
            System.out.println("virtualThreadPool:");
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        runTasks(executor, numTasks);
    }

    private void runTasks(ExecutorService executor, long numTasks) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        for (long i = 0; i < numTasks; i++) {
            executor.execute(new FileOperationTask(i));
        }
        long endOfLoop = System.currentTimeMillis();
        executor.shutdown();
        boolean awaitTermination = executor.awaitTermination(10, MINUTES);
        long endTime = System.currentTimeMillis();
//        System.out.println(" creating threads took " + (endOfLoop - startTime) + " ms");
        String executionTime = awaitTermination ? "Total execution time is " + (endTime - startTime)  + " ms" : "timeout (more than 1 minute)";
        System.out.println(numTasks + " number of tasks. " + executionTime);
    }
}
