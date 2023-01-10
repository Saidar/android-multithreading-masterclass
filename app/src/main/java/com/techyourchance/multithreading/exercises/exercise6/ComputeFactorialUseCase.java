package com.techyourchance.multithreading.exercises.exercise6;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.WorkerThread;

import com.techyourchance.multithreading.common.BaseObservable;

import java.math.BigInteger;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(String result);
    }


    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private final Object LOCK = new Object();

    private int mNumberOfThreads; // safe
    private ComputationRange[] mThreadsComputationRanges; // safe
    private volatile BigInteger[] mThreadsComputationResults; // safe
    private int mNumOfFinishedThreads = 0; // safe

    private long mComputationTimeoutTime; // safe

    private volatile boolean mAbortComputation; // safe

    public ComputeFactorialUseCase(){

    }

    @Override
    public void unregisterListener(Listener listener) {
        super.unregisterListener(listener);
        synchronized (LOCK) {
            mAbortComputation = true;
            LOCK.notifyAll();
        }
    }

    public void compute(final int factorialArgument, final int timeout) {
        initComputationParams(factorialArgument, timeout);
        startComputation();
        waitForThreadsResultsOrTimeoutOrAbort();
        processComputationResults();
    }

    private void initComputationParams(int factorialArgument, int timeout) {
        mNumberOfThreads = factorialArgument < 20
                ? 1 : Runtime.getRuntime().availableProcessors();

        synchronized (LOCK) {
            mNumOfFinishedThreads = 0;
        }

        mAbortComputation = false;

        mThreadsComputationResults = new BigInteger[mNumberOfThreads];

        mThreadsComputationRanges = new ComputationRange[mNumberOfThreads];

        initThreadsComputationRanges(factorialArgument);

        mComputationTimeoutTime = System.currentTimeMillis() + timeout;
    }

    private void initThreadsComputationRanges(int factorialArgument) {
        int computationRangeSize = factorialArgument / mNumberOfThreads;

        long nextComputationRangeEnd = factorialArgument;
        for (int i = mNumberOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = mThreadsComputationRanges[i].start - 1;
        }

        // add potentially "remaining" values to first thread's range
        mThreadsComputationRanges[0].start = 1;
    }

    @WorkerThread
    private void startComputation() {
        for (int i = 0; i < mNumberOfThreads; i++) {

            final int threadIndex = i;

            new Thread(() -> {
                long rangeStart = mThreadsComputationRanges[threadIndex].start;
                long rangeEnd = mThreadsComputationRanges[threadIndex].end;
                BigInteger product = new BigInteger("1");
                for (long num = rangeStart; num <= rangeEnd; num++) {
                    if (isTimedOut()) {
                        break;
                    }
                    product = product.multiply(new BigInteger(String.valueOf(num)));
                }
                mThreadsComputationResults[threadIndex] = product;

                synchronized (LOCK) {
                    mNumOfFinishedThreads++;
                    LOCK.notifyAll();
                }

            }).start();

        }
    }

    @WorkerThread
    private void waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while (mNumOfFinishedThreads != mNumberOfThreads && !mAbortComputation && !isTimedOut()) {
                try {
                    LOCK.wait(getRemainingMillisToTimeout());
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private long getRemainingMillisToTimeout() {
        return mComputationTimeoutTime - System.currentTimeMillis();
    }

    @WorkerThread
    private void processComputationResults() {
        String resultString;

        if (mAbortComputation) {
            resultString = "Computation aborted";
        }
        else {
            resultString = computeFinalResult().toString();
        }

        // need to check for timeout after computation of the final result
        if (isTimedOut()) {
            resultString = "Computation timed out";
        }

        final String finalResultString = resultString;

        mUiHandler.post(() -> {
            for(Listener listener : getListeners()) {
                listener.onFactorialComputed(finalResultString);
            }
        });
    }

    @WorkerThread
    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for (int i = 0; i < mNumberOfThreads; i++) {
            if (isTimedOut()) {
                break;
            }
            result = result.multiply(mThreadsComputationResults[i]);
        }
        return result;
    }

    private boolean isTimedOut() {
        return System.currentTimeMillis() >= mComputationTimeoutTime;
    }

    private static class ComputationRange {
        private long start;
        private long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }


    public class Result{

        private String finalResultString;

        public Result(String finalResultString) {
            this.finalResultString = finalResultString;
        }

        public String getFinalResultString() {
            return finalResultString;
        }
    }

}
