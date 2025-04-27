package com.github.lotashinski.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Intended to generalize the SubList functionality 
 * and generate a ListIterator.
 * 
 * Implemented common behavior
 * 
 * @param <T>
 */
abstract class ModifiedList<T> extends Modified<T> implements List<T> {

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) > -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o))
				return false;
		}

		return true;
	}

	@Override
	public boolean add(T e) {
		add(size(), e);

		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return addAll(size(), c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ret = false;
		for (Object object : c) {
			ret |= remove(object);
		}

		return ret;
	}

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

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public Object[] toArray() {
		Object[] tmp = new Object[size()];

		return toArray(tmp);
	}

	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new CustomListiterator<>(this, index);
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		for (Object e : this)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());

		return hashCode;
	}

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

	private class CustomListiterator<E> extends Modified<E> implements ListIterator<E> {

		private ModifiedList<E> main;

		private int ref;

		private Integer previous = null;

		CustomListiterator(ModifiedList<E> main, int offset) {
			syncModifications(main);

			this.main = main;
			this.ref = offset;
		}

		@Override
		public boolean hasNext() {
			checkModifications(main);

			return ref < main.size();
		}

		@Override
		public E next() {
			checkModifications(main);
			
			if (ref == main.size())
				throw new NoSuchElementException();
			previous = ref;

			return main.get(ref++);
		}

		@Override
		public boolean hasPrevious() {
			checkModifications(main);

			return ref > 0;
		}

		@Override
		public E previous() {
			checkModifications(main);

			if (ref == 0)
				throw new NoSuchElementException();
			previous = --ref;
			
			return main.get(ref);
		}

		@Override
		public int nextIndex() {
			checkModifications(main);

			return ref + 1;
		}

		@Override
		public int previousIndex() {
			checkModifications(main);

			return ref - 1;
		}

		@Override
		public void remove() {
			checkModifications(main);

			checkState();
			main.remove((int) previous);
			ref = previous;
			previous = null;

			syncModifications(main);
		}

		@Override
		public void set(E e) {
			checkModifications(main);

			checkState();
			main.set(previous, e);

			syncModifications(main);
		}

		@Override
		public void add(E e) {
			checkModifications(main);

			main.add(ref, e);

			syncModifications(main);
		}

		private void checkState() {
			if (previous == null)
				throw new IllegalStateException();
		}

	}

}
