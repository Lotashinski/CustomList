package com.github.lotashinski.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Intended to generalize the SubList and CustomList functionality
 * 
 * Implemented common behavior
 * 
 * @param <T> the type of elements in this list
 * @author Alexander Lotashinsky
 * @see CustomList
 * @see Collection
 * @see List
 */
abstract class AbstractList<T> implements List<T> {

	/**
	 * Returns {@code true} if this list contains no elements.
	 *
	 * @return {@code true} if this list contains no elements
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns {@code true} if this list contains the specified element. More
	 * formally, returns {@code true} if and only if this list contains at least one
	 * element {@code e} such that {@code Objects.equals(o, e)}.
	 *
	 * @param o element whose presence in this list is to be tested
	 * @return {@code true} if this list contains the specified element
	 * @throws ClassCastException   if the type of the specified element is
	 *                              incompatible with this list (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified element is null and this list
	 *                              does not permit null elements (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 */
	@Override
	public boolean contains(Object o) {
		return indexOf(o) > -1;
	}

	/**
	 * Returns {@code true} if this list contains all of the elements of the
	 * specified collection.
	 *
	 * @param c collection to be checked for containment in this list
	 * @return {@code true} if this list contains all of the elements of the
	 *         specified collection
	 * @throws ClassCastException   if the types of one or more elements in the
	 *                              specified collection are incompatible with this
	 *                              list (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified collection contains one or more
	 *                              null elements and this list does not permit null
	 *                              elements (<a href=
	 *                              "Collection.html#optional-restrictions">optional</a>),
	 *                              or if the specified collection is null
	 * @see #contains(Object)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o))
				return false;
		}

		return true;
	}

	/**
	 * Appends the specified element to the end of this list (optional operation).
	 *
	 * <p>
	 * Lists that support this operation may place limitations on what elements may
	 * be added to this list. In particular, some lists will refuse to add null
	 * elements, and others will impose restrictions on the type of elements that
	 * may be added. List classes should clearly specify in their documentation any
	 * restrictions on what elements may be added.
	 *
	 * @param e element to be appended to this list
	 * @return {@code true} (as specified by {@link Collection#add})
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to this list
	 * @throws IllegalArgumentException if some property of this element prevents it
	 *                                  from being added to this list
	 */
	@Override
	public boolean add(T e) {
		add(size(), e);

		return true;
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
	 * @throws ClassCastException       if the class of an element of the specified
	 *                                  collection prevents it from being added to
	 *                                  this list
	 * @throws IllegalArgumentException if some property of an element of the
	 *                                  specified collection prevents it from being
	 *                                  added to this list
	 * @see #add(Object)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return addAll(size(), c);
	}

	/**
	 * Removes from this list all of its elements that are contained in the
	 * specified collection (optional operation).
	 *
	 * @param c collection containing elements to be removed from this list
	 * @return {@code true} if this list changed as a result of the call
	 * @throws ClassCastException if the class of an element of this list is
	 *                            incompatible with the specified collection
	 *                            (<a href=
	 *                            "Collection.html#optional-restrictions">optional</a>)
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ret = false;
		for (Object object : c) {
			while (remove(object))
				ret = true;
		}

		return ret;
	}

	/**
	 * Retains only the elements in this list that are contained in the specified
	 * collection (optional operation). In other words, removes from this list all
	 * of its elements that are not contained in the specified collection.
	 *
	 * @param c collection containing elements to be retained in this list
	 * @return {@code true} if this list changed as a result of the call
	 * @throws ClassCastException if the class of an element of this list is
	 *                            incompatible with the specified collection
	 *                            (<a href=
	 *                            "Collection.html#optional-restrictions">optional</a>)
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		CustomList<T> garbage = new CustomList<>();

		for (T o : this) {
			if (!c.contains(o))
				garbage.add(o);
		}

		removeAll(garbage);

		return !garbage.isEmpty();
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element. More formally, returns
	 * the lowest index {@code i} such that {@code Objects.equals(o, get(i))}, or -1
	 * if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the first occurrence of the specified element in this
	 *         list, or -1 if this list does not contain the element
	 * @throws ClassCastException if the type of the specified element is
	 *                            incompatible with this list (<a href=
	 *                            "Collection.html#optional-restrictions">optional</a>)
	 */
	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < size(); ++i) {
			Object tmp = get(i);

			if (o == tmp)
				return i;
			if (o != null && o.equals(tmp))
				return i;
		}

		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element. More formally, returns
	 * the highest index {@code i} such that {@code Objects.equals(o, get(i))}, or
	 * -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the last occurrence of the specified element in this
	 *         list, or -1 if this list does not contain the element
	 * @throws ClassCastException if the type of the specified element is
	 *                            incompatible with this list (<a href=
	 *                            "Collection.html#optional-restrictions">optional</a>)
	 */
	@Override
	public int lastIndexOf(Object o) {
		for (int i = size() - 1; i >= 0; --i) {
			Object tmp = get(i);

			if (o == tmp)
				return i;
			if (o != null && o.equals(tmp))
				return i;
		}

		return -1;
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 *
	 * @return an iterator over the elements in this list in proper sequence
	 */
	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element).
	 *
	 * <p>
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this list. (In other words, this method must allocate a new array even if
	 * this list is backed by an array). The caller is thus free to modify the
	 * returned array.
	 *
	 * <p>
	 * This method acts as bridge between array-based and collection-based APIs.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *         sequence
	 * @see Arrays#asList(Object[])
	 */
	@Override
	public Object[] toArray() {
		Object[] tmp = new Object[size()];

		return toArray(tmp);
	}

	/**
	 * Returns a list iterator over the elements in this list (in proper sequence).
	 *
	 * @return a list iterator over the elements in this list (in proper sequence)
	 */
	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	/**
	 * Returns the hash code value for this list. The hash code of a list is defined
	 * to be the result of the following calculation:
	 * 
	 * <pre>{@code
	 * int hashCode = 1;
	 * for (E e : list)
	 * 	   hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
	 * }</pre>
	 * 
	 * This ensures that {@code list1.equals(list2)} implies that
	 * {@code list1.hashCode()==list2.hashCode()} for any two lists, {@code list1}
	 * and {@code list2}, as required by the general contract of
	 * {@link Object#hashCode}.
	 *
	 * @return the hash code value for this list
	 * @see Object#equals(Object)
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		int hashCode = 1;
		for (Object e : this)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());

		return hashCode;
	}

	/**
	 * Compares the specified object with this list for equality. Returns
	 * {@code true} if and only if the specified object is also a list, both lists
	 * have the same size, and all corresponding pairs of elements in the two lists
	 * are <i>equal</i>. (Two elements {@code e1} and {@code e2} are <i>equal</i> if
	 * {@code Objects.equals(e1, e2)}.) In other words, two lists are defined to be
	 * equal if they contain the same elements in the same order. This definition
	 * ensures that the equals method works properly across different
	 * implementations of the {@code List} interface.
	 *
	 * @param o the object to be compared for equality with this list
	 * @return {@code true} if the specified object is equal to this list
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof List<?>))
			return false;

		List<?> other = (List<?>) obj;
		if (size() != other.size())
			return false;

		Iterator<?> currentIterator = iterator();
		Iterator<?> otherIterator = other.iterator();

		while (currentIterator.hasNext() && otherIterator.hasNext()) {
			Object ce = currentIterator.next();
			Object oe = otherIterator.next();

			if (ce == null && oe == null)
				continue;
			if (ce != null && !ce.equals(oe))
				return false;
		}

		return true;
	}

}
