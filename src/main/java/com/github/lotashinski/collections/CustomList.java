package com.github.lotashinski.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CustomList<T> extends ModifiedList<T> implements List<T> {

	private static final int INIT_CAPACITY = 10;
	
	private int size;
	
	private Object[] container;
	
	
	public CustomList() {
		container = new Object[INIT_CAPACITY];
		size = 0;
	}
	
	public CustomList(Collection<? extends T> producer) {
		container = producer.toArray();
		size = producer.size();
	}


	@Override
	public int size() {
		return size;
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size()) 
			a = (T[]) Array.newInstance(a.getClass().componentType(), size());
		if (a.length > size()) a[size()] = null;
		
		System.arraycopy(container, 0, a, 0, size());
		
		return a;
	}

	@Override
	public boolean remove(Object o) {
		incModofications();
		
		int index = indexOf(o);
		if (index == -1) return false;
		
		shiftLeft(index, 1);
		
		return true;
	}

	@Override
	public void clear() {
		incModofications();
		size = 0;
		
		/*
		 * For garbage collector: 
		 * The old container can 
		 * be freed from memory 
		 * before the new one 
		 * is initialized.
		 */
		container = null; 
		container = new Object[INIT_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		checkIndexAndThrowExceptionIfNeed(index);
		
		return (T) container[index];
	}

	@Override
	public T set(int index, T element) {
		incModofications();
		
		T tmp = get(index);
		container[index] = element;
		
		return tmp;
	}

	@Override
	public void add(int index, T element) {		
		incModofications();
		
		shiftRight(index, 1);
		container[index] = element;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		incModofications();
		shiftRight(index, c.size());
		Iterator<? extends T> it = c.iterator();
		while(it.hasNext())
			set(index++, it.next());
		
		return true;
	}

	@Override
	public T remove(int index) {
		incModofications();
		
		T tmp = get(index);
		shiftLeft(index, 1);
		
		return tmp;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		int size = toIndex - fromIndex;
		
		return new SubList<>(this, fromIndex, size);
	}
	
	private void shiftRight(int index, int elements) {
		checkIndexAndThrowExceptionIfNeed(index);
		checkAndResizeIfNecessary(elements);
		
		size += elements;
		int end = index + elements;
		for(int i = size - 1; i >= end; --i) {
			container[i] = container[i - elements]; 
		}
	}
	
	private void shiftLeft(int index, int elements) {
		checkIndexAndThrowExceptionIfNeed(index);
		checkIndexAndThrowExceptionIfNeed(index + elements);
		
		size -= elements;
		for(; index < size; ++index) {
			container[index] = container[index + elements];
		}
	}
	
	private void checkIndexAndThrowExceptionIfNeed(int index) {
		if (size < index) throw new IndexOutOfBoundsException(index);
	}
	
	private void checkAndResizeIfNecessary(int newItems) {
		int estimatedSize = size + newItems;
		
		if (estimatedSize > container.length) {
			int newSize = calculateNewContainerSize(estimatedSize);
			resize(newSize);
		}
	} 
	
	private int calculateNewContainerSize(int estimatedSize) {
		int newSize = Math.max(container.length, INIT_CAPACITY);
		while (newSize < estimatedSize) newSize <<= 1;
		
		return newSize;
	}
	
	private void resize(int newSize) {
		Object[] target = new Object[newSize];
		System.arraycopy(container, 0, target, 0, size);
		
		container = target;
	}
	
	private static class SubList<E> extends ModifiedList<E> implements List<E> {

		private int offset;
		
		private int size;
		
		private ModifiedList<E> main;
		
		
		SubList(ModifiedList<E> main, int offset, int size) {
			this.offset = offset;
			this.size = size;
			this.main = main;
			
			syncModifications(main);
		}


		@Override
		public int size() {
			checkModifications(main);
			
			return size;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public <T> T[] toArray(T[] a) {
			checkModifications(main);
			
			if (a.length < size()) 
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), size()); 
			if (a.length > size()) a[size()] = null;
			
			// TODO add throws from super implementation
			
			for(int i = 0; i < size(); ++i)
				a[i] = (T) get(i);
			
			return a;
		}


		@Override
		public boolean remove(Object o) {
			checkModifications(main);
			
			int index = indexOf(o);
			if (index == -1) return false;
			
			main.remove(offset + index);
			size--;
			
			syncModifications(main);
			
			return true;
		}

		@Override
		public void clear() {
			checkModifications(main);
			
			syncModifications(main);
		}


		@Override
		public E get(int index) {
			checkModifications(main);
			
			return main.get(offset + index);
		}


		@Override
		public E set(int index, E element) {
			checkModifications(main);
			
			return main.get(offset + index);
		}


		@Override
		public void add(int index, E element) {
			checkModifications(main);
			
			main.add(offset + index, element);
			
			syncModifications(main);
		}


		@Override
		public E remove(int index) {
			checkModifications(main);
			
			E tmp = main.remove(index + offset);
			
			syncModifications(main);
			
			return tmp;
		}


		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			checkModifications(main);
			
			return new SubList<>(this, fromIndex, toIndex);
		}


		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			return main.addAll(offset + index, c);
		}
		
	}
	
	
	
}
