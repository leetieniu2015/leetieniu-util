package org.leetieniu.util.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.leetieniu.util.Stack;

public class ConcurrentStackTest {
	
	private static final Executor executor = Executors.newFixedThreadPool(8);
	
	private static final AtomicInteger val = new AtomicInteger(0);
	
	private static final int size = 10000 * 100;
	
	private static final CountDownLatch cd = new CountDownLatch(size);
	
	@Test
	public void pushTest() {
		
		long beginTime = System.currentTimeMillis();
		
		final Stack<Integer> stack = new ConcurrentStack<Integer>();
		
		for(int i = 0; i < size; i ++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					stack.push(val.incrementAndGet());
					cd.countDown();
				}
			});
			/*stack.push(val.incrementAndGet());
			cd.countDown();*/
		}
		
		try {
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		
		/*while(!stack.isEmpty()) {
			System.out.println(stack.pop());
		}*/
		
		System.out.println("size : " + size + ",cost " + (endTime - beginTime) + " ms");
	}
	
	//@Test
	public void pushTestJdkStack() {
		
		long beginTime = System.currentTimeMillis();
		
		final java.util.Stack<Integer> stack = new java.util.Stack<Integer>();
		
		for(int i = 0; i < size; i ++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					stack.push(val.incrementAndGet());
					cd.countDown();
				}
			});
			/*stack.push(val.incrementAndGet());
			cd.countDown();*/
		}
		
		try {
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		
		/*while(!stack.isEmpty()) {
			System.out.println(stack.pop());
		}*/
		
		System.out.println("size : " + size + ",cost " + (endTime - beginTime) + " ms");
	}
}
