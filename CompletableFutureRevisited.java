package com.packt.tfesenko.multithreading.section2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureRevisited {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// If executor is not provided, CompletableFuture.supplyAsync will use ForkJoinPool by default
		ExecutorService executor = Executors.newCachedThreadPool();

		
		final String tomatoes = "Tomatoes";
		CompletableFuture<String> sliceTomatoes = CompletableFuture.supplyAsync(() -> { // // provided task is asynchronously executed and store result in Future
//			try {
//				TimeUnit.MILLISECONDS.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			System.out.println("   Restaurant> Slicing tomatoes");
			if (tomatoes == null) {
				throw new RuntimeException("No tomatoes");				
			} 
			return "Tomatoes ";
		}, executor).handle((result, e) -> { // handle is always executed irrespective of exception or not
			if (result == null) {
				System.out.println("Problems with tomatoes");
				return "";
			}
			return result;
		});

		CompletableFuture<String> chopOnions = CompletableFuture.supplyAsync(() -> { // provided task is asynchronously executed and store result in Future
			System.out.println("   Restaurant> Chopping onions");
			return "Onions ";
		}, executor);

		
		// Returns a new CompletionStageResult which is executed with the combined result as an argument to the supplied function. Returned CompletionStageResult is executed ONLY AFTER both input stages complete normally 

		CompletableFuture<String> prepIngredients = sliceTomatoes.thenCombine(chopOnions, String::concat); // combines results from two futures sliceTomatoes and chopOnions. 

		
		// Returns a new CompletionStage which is executed with this stage's result as the argument to the supplied function. Returned CompletionStageResult is executed ONLY AFTER this stage completes normally
		
		CompletableFuture<Object> prepPizza = prepIngredients.thenApply(toppings -> {
			System.out.println("   Restaurant> Spreading with tomato sauce and sprinkle with toppings: " + toppings);
			return "Raw pizza with " + toppings;
		});
		
		// Returns a new CompletionStage which is executed with this stage's result as the argument to the supplied function. Returned CompletionStageResult is executed ONLY AFTER this stage completes normally

		CompletableFuture<String> bakePizza = prepPizza.thenApply(rawPizza -> {
			System.out.println("   Restaurant> Baking pizza: " + rawPizza);
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Pizza";
		});
		
		//thenAccept() accepts a Consumer which do not return a result. 

		bakePizza.thenAccept(pizza -> System.out.println("Eating pizza: " + pizza));
		// or, the old way  // System.out.println(bakePizza.get());
	}

}
