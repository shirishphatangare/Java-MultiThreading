package com.packt.tfesenko.multithreading.section1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class RecursiveActionDemo {

	public static void main(String[] args) throws InterruptedException {
		AppleTree[] appleTrees = AppleTree.newTreeGarden(12);
		PickFruitAction task = new PickFruitAction(appleTrees, 0, appleTrees.length - 1);

		ForkJoinPool pool = ForkJoinPool.commonPool();

		pool.invoke(task);
		// try this: pool.execute(task); // execute() just arranges task for async execution. It do not wait for completion. join() is necessary with execute().
		// try this: pool.execute(task); task.join(); // invoke task and wait till completion. join() method is mandatory with execute() method. 
		// try this: pool.execute(task); pool.awaitTermination(10, TimeUnit.SECONDS); // invoke task with timeout of 10 seconds

		System.out.println();
		System.out.println("Done!");
	}

	public static class PickFruitAction extends RecursiveAction {

		private final AppleTree[] appleTrees;
		private final int startInclusive;
		private final int endInclusive;

		private final int taskThreadshold = 4;

		public PickFruitAction(AppleTree[] array, int startInclusive, int endInclusive) {
			this.appleTrees = array;
			this.startInclusive = startInclusive;
			this.endInclusive = endInclusive;
		}

		@Override
		protected void compute() {
			if (endInclusive - startInclusive < taskThreadshold) {
				doCompute();
				return;
			}
			int midpoint = startInclusive + (endInclusive - startInclusive) / 2;

			PickFruitAction leftSum = new PickFruitAction(appleTrees, startInclusive, midpoint);
			PickFruitAction rightSum = new PickFruitAction(appleTrees, midpoint + 1, endInclusive);

			rightSum.fork(); // computed asynchronously
			leftSum.compute();// computed synchronously: immediately and in the current thread
			rightSum.join();
		}

		protected void doCompute() {
			IntStream.rangeClosed(startInclusive, endInclusive)//
					.forEach(i -> appleTrees[i].pickApples());

		}
	}
}
