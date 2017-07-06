package org.leetieniu.util.concurrent;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.leetieniu.util.Stack;

/**
 * 并发栈
 * @author leetieniu
 * @param <E>
 */
public final class ConcurrentStack<E> implements Stack<E> {
	
	private static final long serialVersionUID = 1L;
	
	private AtomicReference<StackNode> top = new AtomicReference<StackNode>(null);
	
	private AtomicInteger size = new AtomicInteger(0);

	@Override
	public boolean push(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		for(;;) {
			StackNode oldNode = top.get();
			StackNode newNode = new StackNode(item, oldNode);
			if(top.compareAndSet(oldNode, newNode)) {
				size.incrementAndGet();
				return true;
			}
		}
		
	}
	
	@Override
	public E peek() {
		for(;;) {
			StackNode oldNode = top.get();
			if(oldNode == null) {
				return null;
			}
			if(top.compareAndSet(oldNode, oldNode)) {
				return oldNode.e;
			}
		}
	}
	
	@Override
	public E pop() {
		for(;;) {
			StackNode oldNode = top.get();
			if(oldNode == null) {
				return null;
			}
			StackNode next = oldNode.next;
			if(top.compareAndSet(oldNode, next)) {
				size.decrementAndGet();
				return oldNode.e;
			}
		}
	}
	
	@Override
	public int size() {
		return size.get();
	}
	
	@Override
	public boolean isEmpty() {
		return top.compareAndSet(null, null);
	}

	@Override
	public void clear() {
		for(;;) {
			StackNode oldNode = top.get();
			if(top.compareAndSet(oldNode, null)) {
				size.getAndSet(0);
				return;
			}
		}
	}
	
	@Override
	public Iterator<E> iterator() {
		return new StackNodeIterator();
	}
	
	class StackNode implements Cloneable, Serializable {
		
		private static final long serialVersionUID = 1L;
		
		final E e;
		final StackNode next;
		
		StackNode(E e, StackNode next) {
			this.e = e;
			this.next = next;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}
	
	private class StackNodeIterator implements Iterator<E> {
		
		private StackNode now;
		
		@SuppressWarnings("unchecked")
		private StackNodeIterator() {
			StackNode node = top.get();
			if(node != null) {
				try {
					node = (StackNode) node.clone();
				} catch (CloneNotSupportedException ex) {
					throw new RuntimeException("Clone NotSupported", ex);
				}
			}
			now = node;
		}
		
		@Override
		public boolean hasNext() {
			return now != null;
		}

		@Override
		public E next() {
			StackNode node = now;
			now = node.next;
			return node.e;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}
}
