package com.packt.tfesenko.multithreading.section1;

import static java.util.Arrays.asList;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolInvokeAll {
	public static void main(String[] args) {
		AppleTree[] appleTrees = AppleTree.newTreeGarden(6);

		Callable<Void> applePicker1 = createApplePicker(appleTrees, 0, 2, "Alex"); // Task 1 with no results 
		Callable<Void> applePicker2 = createApplePicker(appleTrees, 2, 4, "Bob"); // Task 2 with no results
		Callable<Void> applePicker3 = createApplePicker(appleTrees, 4, 6, "Carol"); // Task 3 with no results

		// Pickup All Fruits with invokeAll()
		// Executing tasks in parallel with ForkJoinPool
		ForkJoinPool.commonPool().invokeAll(asList(applePicker1, applePicker2, applePicker3));		
		
		System.out.println();
		System.out.println("All fruits collected!");
	}

	public static Callable<Void> createApplePicker(AppleTree[] appleTrees, int fromIndexInclusive, int toIndexExclusive, String workerName) {
		return () -> {
			for (int i = fromIndexInclusive; i < toIndexExclusive; i++) {
				appleTrees[i].pickApples(workerName);
			}
			return null;
		};
	}
}
