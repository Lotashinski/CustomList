package com.github.lotashinski.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation based on an array of the {@code List} interface.
 * 
 * The {@code size}, {@code isEmpty}, {@code get}, {@code set},
 * {@code iterator}, and {@code listIterator} operations run in constant time.
 * The {@code add} operation runs in amortized constant time, that is, adding n
 * elements requires O(n) time. All of the other operations run in linear time.
 * 
 * @param <T> the type of elements in this list
 * @author Alexander Lotashinsky
 * @see Collection
 * @see List
 */
public class CustomList<T> extends AbstractList<T> implements List<T> {

	/**
	 * Default initial capacity.
	 */
	private static final int INIT_CAPACITY = 10;

	/**
	 * The size of the CustomList
	 */
	private int size;

	/**
	 * Array with stored collection elements
	 */
	private Object[] container;

	/**
	 * Counter of structural changes (remove, add, clear and other). Changes when
	 * the collection size changes.
	 * 
	 * Necessary for SubList and ListIterator
	 */
	private int version = 0;

	/**
	 * Creates an empty collection
	 */
	public CustomList() {
		container = new Object[INIT_CAPACITY];
		size = 0;
	}

	/**
	 * Creates a collection based on producer, preserving the order of elements from
	 * {@code producr.toArray()}.
	 * 
	 * @param producer source collection. Serves as a source for initialization
	 */
	public CustomList(Collection<? extends T> producer) {
		container = producer.toArray();
		size = producer.size();
	}

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return the number of elements in this list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element); the runtime type of the returned array
	 * is that of the specified array. If the list fits in the specified array, it
	 * is returned therein. Otherwise, a new array is allocated with the runtime
	 * type of the specified array and the size of this list.
	 *
	 * <p>
	 * If the list fits in the specified array with room to spare (i.e., the array
	 * has more elements than the list), the element in the array immediately
	 * following the end of the list is set to {@code null}. (This is useful in
	 * determining the length of the list <i>only</i> if the caller knows that the
	 * list does not contain any null elements.)
	 *
	 * <p>
	 * Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs. Further, this method allows precise
	 * control over the runtime type of the output array, and may, under certain
	 * circumstances, be used to save allocation costs.
	 *
	 * <p>
	 * Suppose {@code x} is a list known to contain only strings. The following code
	 * can be used to dump the list into a newly allocated array of {@code String}:
	 *
	 * <pre>{@code
	 * String[] y = x.toArray(new String[0]);
	 * }</pre>
	 *
	 * Note that {@code toArray(new Object[0])} is identical in function to
	 * {@code toArray()}.
	 *
	 * @param a the array into which the elements of this list are to be stored, if
	 *          it is big enough; otherwise, a new array of the same runtime type is
	 *          allocated for this purpose.
	 * @return an array containing the elements of this list
	 * @throws ArrayStoreException  if the runtime type of the specified array is
	 *                              not a supertype of the runtime type of every
	 *                              element in this list
	 * @throws NullPointerException if the specified array is null
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size())
			a = (T[]) Array.newInstance(a.getClass().componentType(), size());
		if (a.length > size())
			a[size()] = null;

		System.arraycopy(container, 0, a, 0, size());

		return a;
	}

	/**
	 * Removes the first occurrence of the specified element from this list, if it
	 * is present (optional operation). If this list does not contain the element,
	 * it is unchanged. More formally, removes the element with the lowest index
	 * {@code i} such that {@code Objects.equals(o, get(i))} (if such an element
	 * exists). Returns {@code true} if this list contained the specified element
	 * (or equivalently, if this list changed as a result of the call).
	 *
	 * @param o element to be removed from this list, if present
	 * @return {@code true} if this list contained the specified element
	 */
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index == -1)
			return false;

		shiftLeft(index, 1);

		return true;
	}

	/**
	 * Removes all of the elements from this list (optional operation). The list
	 * will be empty after this call returns.
	 */
	@Override
	public void clear() {
		if (size() == 0)
			return;

		setSize(0);

		/*
		 * For garbage collector: The old container can be freed from memory before the
		 * new one is initialized.
		 */
		container = null;
		container = new Object[INIT_CAPACITY];
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   ({@code index < 0 || index >= size()})
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		checkIndexAndThrowIfNeed(index);

		return (T) container[index];
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation).
	 *
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws ClassCastException        if the class of the specified element
	 *                                   prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   ({@code index < 0 || index >= size()})
	 */
	@Override
	public T set(int index, T element) {
		checkIndexAndThrowIfNeed(index);
		
		T tmp = get(index);
		container[index] = element;

		return tmp;
	}

	/**
	 * Appends all of the elements in the specified collection to the end of this
	 * list, in the order that they are returned by the specified collection's
	 * iterator (optional operation). The behavior of this operation is undefined if
	 * the specified collection is modified while the operation is in progress.
	 * (Note that this will occur if the specified collection is this list, and it's
	 * nonempty.)
	 *
	 * @param c collection containing elements to be added to this list
	 * @return {@code true} if this list changed as a result of the call
	 * @throws ClassCastException        if the class of an element of the specified
	 *                                   collection prevents it from being added to
	 *                                   this list
	 * @throws NullPointerException      if the {@code c} is null
	 * @throws IllegalArgumentException  if some property of an element of the
	 *                                   specified collection prevents it from being
	 *                                   added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   ({@code index < 0 || index > size()})
	 * @see #add(Object)
	 */
	@Override
	public void add(int index, T element) {
		checkRangeAndThrowIfNeed(index);
		shiftRight(index, 1);

		container[index] = element;
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation). Shifts the element currently at that position (if any)
	 * and any subsequent elements to the right (adds one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws UnsupportedOperationException if the {@code add} operation is not
	 *                                       supported by this list
	 * @throws ClassCastException            if the class of the specified element
	 *                                       prevents it from being added to this
	 *                                       list
	 * @throws NullPointerException          if the {@code c} is null
	 * @throws IndexOutOfBoundsException     if the index is out of range
	 *                                       ({@code index < 0 || index > size()})
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if (c.size() == 0)
			return false;

		shiftRight(index, c.size());
		Iterator<? extends T> it = c.iterator();
		while (it.hasNext())
			set(index++, it.next());

		return true;
	}

	/**
	 * Removes the element at the specified position in this list (optional
	 * operation). Shifts any subsequent elements to the left (subtracts one from
	 * their indices). Returns the element that was removed from the list.
	 *
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   ({@code index < 0 || index >= size()})
	 */
	@Override
	public T remove(int index) {
		T tmp = get(index);

		shiftLeft(index, 1);

		return tmp;
	}

	/**
	 * Returns a view of the portion of this list between the specified
	 * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive. (If
	 * {@code fromIndex} and {@code toIndex} are equal, the returned list is empty.)
	 * The returned list is backed by this list, so non-structural changes in the
	 * returned list are reflected in this list, and vice-versa. The returned list
	 * supports all of the optional list operations supported by this list.
	 * <p>
	 *
	 * This method eliminates the need for explicit range operations (of the sort
	 * that commonly exist for arrays). Any operation that expects a list can be
	 * used as a range operation by passing a subList view instead of a whole list.
	 * For example, the following idiom removes a range of elements from a list:
	 * 
	 * <pre>{@code
	 * list.subList(from, to).clear();
	 * }</pre>
	 * 
	 * Similar idioms may be constructed for {@code indexOf} and
	 * {@code lastIndexOf}, and all of the algorithms in the {@code Collections}
	 * class can be applied to a subList.
	 * <p>
	 *
	 * The semantics of the list returned by this method become undefined if the
	 * backing list (i.e., this list) is <i>structurally modified</i> in any way
	 * other than via the returned list. (Structural modifications are those that
	 * change the size of this list, or otherwise perturb it in such a fashion that
	 * iterations in progress may yield incorrect results.)
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex   high endpoint (exclusive) of the subList
	 * @return a view of the specified range within this list
	 * @throws IndexOutOfBoundsException for an illegal endpoint index value
	 *                                   ({@code fromIndex < 0 || toIndex > size ||
	 *         fromIndex > toIndex}   )
	 */
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		int subListSize = toIndex - fromIndex;

		return new SubList<>(this, fromIndex, subListSize);
	}

	/**
	 * Sorts a collection using merge sort. When comparing, the type cast to
	 * Comparable will be used if possible. Otherwise, the type cast to string will
	 * be used.
	 */
	public void sort() {
		sort(0, size());
	}

	/**
	 * Sorts a collection using merge sort.
	 * 
	 * @param comparator will be used for comparison.
	 */
	public void sort(Comparator<? super T> comparator) {
		sort(0, size(), comparator);
	}

	/**
	 * Sorts a part of collection using merge sort. When comparing, the type cast to
	 * Comparable will be used if possible. Otherwise, the type cast to string will
	 * be used.
	 * 
	 * @param from low endpoint for sort
	 * @param size total elements for sort
	 * @throws IndexOutOfBoundsException for an illegal endpoint value
	 *                                   ({@code from < 0 || size < 0  || from + size > size() 
	 *         || fromIndex >= size()})
	 */
	public void sort(int from, int size) {
		sort(from, size, new OrderingComparator<T>());
	}

	/**
	 * Sorts a part of collection using merge sort. Comparator<? super T> will be
	 * used for comparison
	 * 
	 * @param from       low endpoint for sort
	 * @param size       total elements for sort
	 * @param comparator will be used for comparison.
	 * @throws IndexOutOfBoundsException for an illegal endpoint value
	 *                                   ({@code from < 0 || size < 0  || from + size > size() 
	 *         || fromIndex >= size()})
	 */
	public void sort(int from, int size, Comparator<? super T> comparator) {
		checkIndexAndThrowIfNeed(from);
		checkRangeAndThrowIfNeed(from + size);

		List<T> view = subList(from, from + size);
		List<? extends T> sorted = mergeSort(view, comparator);

		for (int i = 0; i < size; ++i) {
			set(from + i, sorted.get(i));
		}
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new CustomListiterator<>(this, index, 0, size());
	}

	/**
	 * The default implementation of the comparator. If the elements being compared
	 * implement comparison behavior, then {@code compareTo(other)} will be called.
	 * Otherwise, casting to strings will be called.
	 * 
	 * @param <T> the type of comparing elements
	 */
	private static class OrderingComparator<T> implements Comparator<T> {

		/**
		 * Compares its two arguments for order. Returns a negative integer, zero, or a
		 * positive integer as the first argument is less than, equal to, or greater
		 * than the second. The null value will always be the smallest (except for the
		 * case with two nulls)
		 * 
		 * @param o1 the first object to be compared.
		 * @param o2 the second object to be compared.
		 * @return a negative integer, zero, or a positive integer as the first argument
		 *         is less than, equal to, or greater than the second.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public int compare(T o1, T o2) {
			if (o1 == o2)
				return 0;
			if (o1 == null)
				return 1;
			if (o2 == null)
				return -1;

			if (o2 instanceof Comparable) {
				@SuppressWarnings({ "rawtypes" })
				Comparable c2 = (Comparable) o2;

				return c2.compareTo(o1);
			}

			return o2.toString().compareTo(o1.toString());
		}

	}

	/**
	 * Implementation of Merge Sort
	 * {@linkplain https://en.wikipedia.org/wiki/Merge_sort MergeSort (wikipedia)}
	 * 
	 * @param <T>        the type of elements in this list
	 * @param list       original collection
	 * @param comparator
	 * @return new sorted collection ({@code CustomList<T>})
	 */
	private static <T> List<T> mergeSort(List<T> list, Comparator<? super T> comparator) {
		if (list.size() < 2)
			return list;

		int leftSize = list.size() / 2;
		int rightSize = list.size() - leftSize;

		List<T> left = mergeSort(list.subList(0, leftSize), comparator);
		List<T> right = mergeSort(list.subList(leftSize, leftSize + rightSize), comparator);

		List<T> tmp = new CustomList<T>();

		int leftRef = 0;
		int rightRef = 0;
		while (leftRef < left.size() || rightRef < right.size()) {
			if (leftRef == left.size()) {
				tmp.add(right.get(rightRef++));
				continue;
			} else if (rightRef == right.size()) {
				tmp.add(left.get(leftRef++));
				continue;
			}

			T le = left.get(leftRef);
			T re = right.get(rightRef);
			int comparableResult = comparator.compare(le, re);

			if (comparableResult > 0) {
				tmp.add(le);
				leftRef++;
			} else {
				tmp.add(re);
				rightRef++;
			}
		}

		return tmp;
	}

	private void setSize(int size) {
		this.size = size;
		incVersion();
	}

	private void incVersion() {
		version++;
	}

	private int getVersion() {
		return version;
	}

	private void shiftRight(int index, int elements) {
		checkRangeAndThrowIfNeed(index);
		checkAndResizeIfNecessary(elements);

		int elementsBeforeShift = size();
		setSize(elementsBeforeShift + elements);

		int end = index + elements;
		for (int i = size() - 1; i >= end; --i) {
			container[i] = container[i - elements];
		}
	}

	private void shiftLeft(int index, int elements) {
		checkIndexAndThrowIfNeed(index);
		checkIndexAndThrowIfNeed(index + elements);

		int elementsBeforeShift = size();
		setSize(elementsBeforeShift - elements);

		for (; index < elementsBeforeShift - elements; ++index) {
			container[index] = container[index + elements];
		}

		for (; index < elementsBeforeShift; ++index) {
			container[index] = null;
		}

	}

	private void checkIndexAndThrowIfNeed(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException(index);
	}

	private void checkRangeAndThrowIfNeed(int index) {
		if (index < 0 || index > size())
			throw new IndexOutOfBoundsException(index);
	}

	private void checkAndResizeIfNecessary(int newItems) {
		int estimatedSize = size() + newItems;

		if (estimatedSize > container.length) {
			int newSize = calculateNewContainerSize(estimatedSize);
			resize(newSize);
		}
	}

	private int calculateNewContainerSize(int estimatedSize) {
		int newSize = Math.max(container.length, INIT_CAPACITY);
		while (newSize < estimatedSize)
			newSize <<= 1;

		return newSize;
	}

	private void resize(int newSize) {
		Object[] target = new Object[newSize];
		System.arraycopy(container, 0, target, 0, size);

		container = target;
	}

	/**
	 * Used to create subList via {@link CustomList#subList(int, int)} or
	 * {@link #subList(int, int)}
	 * 
	 * @param <E> the type of elements in this list
	 */
	private static class SubList<E> extends AbstractList<E> implements List<E> {

		/**
		 * Offset from the main collection
		 */
		private int offset;

		/**
		 * View size
		 */
		private int size;

		/**
		 * Main collection
		 */
		private CustomList<E> main;

		private int mainVersion;

		SubList(CustomList<E> main, int offset, int size) {
			this.offset = offset;
			this.size = size;
			this.main = main;
			mainVersion = main.getVersion();

		}

		SubList(SubList<E> other, int offset, int size) {
			this.offset = offset + other.offset;
			this.size = size;
			main = other.main;
			mainVersion = main.getVersion();

		}

		@Override
		public int size() {
			checkMainVersion();

			return size;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public <T> T[] toArray(T[] a) {
			checkMainVersion();

			if (a.length < size())
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), size());
			if (a.length > size())
				a[size()] = null;

			for (int i = 0; i < size(); ++i)
				a[i] = (T) get(i);

			return a;
		}

		@Override
		public boolean remove(Object o) {
			checkMainVersion();

			int index = indexOf(o);
			if (index == -1)
				return false;

			main.remove(offset + index);
			size--;
			syncVersions();

			return true;
		}

		@Override
		public void clear() {
			checkMainVersion();
			main.shiftLeft(offset, size);
			size = 0;
			syncVersions();
		}

		@Override
		public E get(int index) {
			checkMainVersion();

			return main.get(offset + index);
		}

		@Override
		public E set(int index, E element) {
			try {
				checkMainVersion();

				return main.set(offset + index, element);
			} finally {
				syncVersions();
			}
		}

		@Override
		public void add(int index, E element) {
			checkMainVersion();
			main.add(offset + index, element);
		}

		@Override
		public E remove(int index) {
			try {
				checkMainVersion();
				E tmp = main.remove(index + offset);

				return tmp;
			} finally {
				syncVersions();
			}
		}

		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			checkMainVersion();
			return new SubList<>(this, fromIndex, toIndex - fromIndex);
		}

		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			try {
				checkMainVersion();

				return main.addAll(offset + index, c);
			} finally {
				syncVersions();
			}
		}

		private void checkMainVersion() {
			if (main.getVersion() != mainVersion)
				throw new ConcurrentModificationException();
		}

		private void syncVersions() {
			mainVersion = main.version;
		}

		@Override
		public ListIterator<E> listIterator(int index) {
			return new CustomListiterator<>(main, offset + index, offset, offset + size());
		}

	}

	private static class CustomListiterator<E> implements ListIterator<E> {

		private AbstractList<E> main;

		private int refStart;

		private int refEnd;

		private int ref;

		private Integer previous = null;

		CustomListiterator(CustomList<E> main, int offset, int refStart, int refEnd) {
			this.main = main;
			this.ref = offset;
			this.refStart = refStart;
			this.refEnd = refEnd;
		}

		@Override
		public boolean hasNext() {
			return ref < refEnd;
		}

		@Override
		public E next() {
			if (ref == refEnd)
				throw new NoSuchElementException();
			previous = ref;

			return main.get(ref++);
		}

		@Override
		public boolean hasPrevious() {
			return ref > refStart;
		}

		@Override
		public E previous() {
			if (ref == refStart)
				throw new NoSuchElementException();
			previous = --ref;

			return main.get(ref);
		}

		@Override
		public int nextIndex() {
			return ref + 1;
		}

		@Override
		public int previousIndex() {
			return ref - 1;
		}

		@Override
		public void remove() {
			checkState();
			main.remove((int) previous);
			ref = previous;
			previous = null;
		}

		@Override
		public void set(E e) {
			checkState();
			main.set(previous, e);
		}

		@Override
		public void add(E e) {
			main.add(ref, e);
		}

		private void checkState() {
			if (previous == null)
				throw new IllegalStateException();
		}

	}

}
