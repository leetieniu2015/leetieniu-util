package org.leetieniu.util;

import java.io.Serializable;

/**
 * 单存的栈 , 不继承Collection
 * @author leetieniu
 * @param <E>
 */
public interface Stack<E> extends Iterable<E>, Serializable {
	
	public boolean push(E item);
	
	public E pop();
	
	public E peek();
	
	public int size();
	
	public boolean isEmpty();
	
	public void clear();
	
}
