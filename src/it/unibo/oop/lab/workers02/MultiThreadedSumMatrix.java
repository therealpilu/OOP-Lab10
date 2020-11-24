package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(final int nthread) {
        super();
        if (nthread < 1) {
            throw new IllegalArgumentException();
        }
        this.nthread = nthread;
    }

    private class Worker extends Thread {

        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private double res;

        Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = Arrays.copyOf(matrix, matrix.length);
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
                for (final double d : this.matrix[i]) {
                    this.res += d;
                }
            }
        }

        public double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Thread worker : workers) {
            worker.start();
        }
        double sum = 0;
        for (final Worker worker : workers) {
            try {
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

}
