package com.github.lotashinski.collections;

import org.junit.jupiter.api.Assertions;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;

class CustomListTest {

	@Test
	void testCustomListDefaultConstructor() {
		new CustomList<>();
	}

	@Test
	void testCustomListFromList() {
		new CustomList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
	}

	@Test
	void testZeroSizeInNewList() {
		List<String> list = new CustomList<>();

		Assertions.assertEquals(list.size(), 0);
	}

	@Test
	void testSizeAfterAdd() {
		List<Number> list = new CustomList<>();

		for (int i = 0; i < 20; ++i) {
			list.add(i);
		}

		Assertions.assertEquals(list.size(), 20);

		for (int i = 0; i < 20; ++i) {
			list.add(i);
		}

		Assertions.assertEquals(list.size(), 40);
	}

	@Test
	void testToArrayTWhenTargetArrayIsEmty() {
		Integer[] ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

		List<Integer> list = new CustomList<>();

		for (int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}

		Number[] nums = list.toArray(new Number[0]);
		Assertions.assertArrayEquals((Number[]) ints, nums);
	}

	@Test
	void testToArrayTWhenTargetArrayIsEqual() {
		Integer[] ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		List<Integer> list = new CustomList<>();

		for (int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}

		Number[] nums = list.toArray(new Number[ints.length]);
		Assertions.assertArrayEquals((Number[]) ints, nums);
	}

	@Test
	void testToArrayTWhenTargetArrayIsGreater() {
		Integer[] ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		List<Integer> list = new CustomList<>();

		for (int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}

		Number[] nums = list.toArray(new Number[ints.length * 2]);

		for (int i = 0; i < ints.length; ++i) {
			Assertions.assertEquals(nums[i], ints[i]);
		}

		Assertions.assertNull(nums[ints.length]);
	}

	@Test
	void testAddElement() {
		List<Integer> list = new CustomList<>();

		Assertions.assertTrue(list.add(1));
		Assertions.assertTrue(list.add(2));
		Assertions.assertTrue(list.add(3));
	}

	@Test
	void testAddNulls() {
		List<Integer> list = new CustomList<>();

		Assertions.assertTrue(list.add(null));
		Assertions.assertTrue(list.add(null));
		Assertions.assertTrue(list.add(null));
	}
	
	@Test
	void testRemoveObject() {
		List<Integer> list = new CustomList<>(List.of(3, 1, 1, 3, 7, 0));

		Assertions.assertTrue(list.remove(Integer.valueOf(1)));
		Assertions.assertEquals(List.of(3, 1, 3, 7, 0), list);
	}

	@Test
	void testClear() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		list.clear();

		Assertions.assertEquals(list, List.of());
		Assertions.assertEquals(list.size(), 0);
	}

	@Test
	void testClearAndAdd() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		list.clear();

		Assertions.assertEquals(list, List.of());
		Assertions.assertEquals(list.size(), 0);

		for (int i = 1; i <= 10; ++i) {
			list.add(i);
		}

		Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list);
	}

	@Test
	void testGet() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> list = new CustomList<>(reference);

		for (int i = 0; i < list.size(); ++i) {
			Assertions.assertEquals(list.get(i), reference.get(i));
		}
	}

	@Test
	void testGetWithInvalidIndex() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> list = new CustomList<>(reference);
		
		try {
			list.get(-1);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}

		try {
			list.get(list.size());
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}
	
	@Test
	void testSet() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3));

		Assertions.assertEquals(1, list.set(0, 3));
		Assertions.assertEquals(3, list.get(0));

		try {
			list.set(-1, 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}

		try {
			list.set(list.size(), 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}

	@Test
	void testSetWithInvalidIndex() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3));

		try {
			list.set(-1, 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}

		try {
			list.set(list.size(), 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}
	
	@Test
	void testAddIntT() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		list.add(2, 0);
		list.add(list.size(), 0);

		Assertions.assertEquals(List.of(1, 2, 0, 3, 4, 5, 0), list);
	}

	@Test
	void testAddIntTWithInvalidIndex() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		try {
			list.add(-1, 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}

		try {
			list.add(list.size() + 1, 0);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}

	@Test
	void testRemoveInt() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		list.remove(3);
		list.remove(2);
		list.remove(1);

		Assertions.assertEquals(List.of(1, 5), list);
	}

	@Test
	void testRemoveIntWithInvalidIndexes() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		try {
			list.remove(-1);
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}

		try {
			list.remove(list.size());
			Assertions.fail();
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}
	
	@Test
	void testSubListData() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));
		List<Integer> view = list.subList(1, list.size());
		
		Assertions.assertEquals(List.of(2, 3, 4, 5), view);
	}
	
	@Test
	void testSubListSize() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));
		List<Integer> view = list.subList(1, list.size());
		
		Assertions.assertEquals(4, view.size());
	}

	@Test
	void testSubListOfSublist() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> reference2 = reference.subList(1, reference.size());

		List<Integer> list = new CustomList<>(reference);
		List<Integer> view = list.subList(1, list.size());

		Assertions.assertEquals(reference2, view);

		List<Integer> reference3 = reference2.subList(1, reference2.size());
		List<Integer> view3 = view.subList(1, view.size());

		Assertions.assertEquals(reference3, view3);
	}

	@Test
	void testClearSubList() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));
		List<Integer> reference = List.of(1, 5);

		list.subList(1, 4).clear();
		Assertions.assertEquals(list, reference);
	}

	@Test
	void testIsEmpty() {
		List<Integer> list = new CustomList<>();

		Assertions.assertTrue(list.isEmpty());

		list.add(1);

		Assertions.assertFalse(list.isEmpty());
	}

	@Test
	void testContains() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		Assertions.assertTrue(list.contains(3));
		Assertions.assertFalse(list.contains(6));
	}

	@Test
	void testContainsAll() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));

		Assertions.assertTrue(list.containsAll(List.of(3, 4, 5)));
		Assertions.assertFalse(list.containsAll(List.of(7, 8, 9)));
	}

	@Test
	void testAddAll() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3));
		list.addAll(List.of(4, 5, 6));

		Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6), list);
	}

	@Test
	void testAddAllInt() {
		List<Integer> list = new CustomList<>(List.of(4, 5, 6));
		list.addAll(0, List.of(1, 2, 3));
		list.addAll(list.size(), List.of(7, 8, 9));
		
		Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), list);
	}

	@Test
	void testRemoveAll() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 0, 1, 2, 3));
		list.removeAll(List.of(0, -1, -2, -3));

		Assertions.assertEquals(list, List.of(1, 2, 3));
	}

	@Test
	void testRetainAll() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 1, 2, 3));
		list.retainAll(List.of(1, 2, 3, 4, 5, 6));

		Assertions.assertEquals(list, List.of(1, 2, 3));
	}

	@Test
	void testIndexOf() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 1, 2, 3, 0));

		Assertions.assertEquals(list.indexOf(0), 3);
		Assertions.assertEquals(list.indexOf(-3), 0);
		Assertions.assertEquals(list.indexOf(null), -1);
	}

	@Test
	void testLastIndexOf() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 0));

		Assertions.assertEquals(list.lastIndexOf(1), 1);
		Assertions.assertEquals(list.lastIndexOf(0), 4);
		Assertions.assertEquals(list.indexOf(null), -1);
	}

	@Test
	void testIteratorInitialization() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4));
		Iterator<Integer> iterator = list.iterator();
		
		Assertions.assertTrue(iterator.hasNext());
	}
	
	@Test
	void testIterator() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4));

		Iterator<Integer> iterator = list.iterator();
		
		int currentValue = 0;
		while (iterator.hasNext()) {
			Assertions.assertEquals(currentValue++, iterator.next());
		}
		
		Assertions.assertEquals(5, currentValue);
	}

	@Test
	void testToArray() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4));

		Assertions.assertArrayEquals(new Object[] { 1, 2, 3, 4 }, list.toArray());
	}

	@Test
	void testListIteratorHasNextHasPrevious() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
		ListIterator<Integer> listIterator = list.listIterator();
		
		Assertions.assertTrue(listIterator.hasNext());
		Assertions.assertFalse(listIterator.hasPrevious());
	}
	
	@Test
	void testListIterator() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
		ListIterator<Integer> listIterator = list.listIterator();
		
		int currentValue = 0;
		
		while(listIterator.hasNext()) {
			Assertions.assertEquals(currentValue++, listIterator.next());
		}
		
		Assertions.assertEquals(10, currentValue);
		
		currentValue = 10;
		
		while(listIterator.hasPrevious()) {
			Assertions.assertEquals(--currentValue, listIterator.previous());
		}
		
		Assertions.assertEquals(0, currentValue);
	}

	@Test
	void testListIteratorIntInitialization() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
		ListIterator<Integer> listi = list.listIterator(5);

		Assertions.assertTrue(listi.hasNext());
		Assertions.assertTrue(listi.hasPrevious());
	}

	@Test
	void testListIteratorInt() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
		ListIterator<Integer> listi = list.listIterator(5);

		int currentValue = 5;		
		while (listi.hasNext()) {
			Assertions.assertEquals(currentValue++, listi.next());
		}

		Assertions.assertEquals(10, currentValue);
		
		listi = list.listIterator(5);
		currentValue = 5;
		
		while (listi.hasPrevious()) {
			Assertions.assertEquals(--currentValue, listi.previous());
		}
	}
	
	@Test
	void testListIteratorRemoveException() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		ListIterator<Integer> iterator = list.listIterator();

		try {
			iterator.remove();
			Assertions.fail();
		} catch (IllegalStateException e) {
			// It`s OK
		}
	}

	@Test
	void testListIteratorRemove() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));
		ListIterator<Integer> iterator = list.listIterator();
		
		iterator.next();
		iterator.remove();

		Assertions.assertEquals(2, list.get(0));
		Assertions.assertFalse(iterator.hasPrevious());
	}

	@Test
	void testListIteratorPreviousIndexAndNextIndex() {
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4, 5));

		ListIterator<Integer> iterator = list.listIterator();

		Assertions.assertEquals(-1, iterator.previousIndex());
		Assertions.assertEquals(1, iterator.nextIndex());

		iterator = list.listIterator(4);

		Assertions.assertEquals(3, iterator.previousIndex());
		Assertions.assertEquals(5, iterator.nextIndex());
	}

	@Test
	void testSortAll() {
		CustomList<Integer> list = new CustomList<>(List.of(9, 0, -128, 23, 5));
		list.sort();
		Assertions.assertEquals(List.of(-128, 0, 5, 9, 23), list);
	}

	@Test
	void testSortPartial() {
		List<Integer> target = List.of(9, 0, -128, 23, 5, 99, 12, 123, 3122);
		
		CustomList<Integer> list = new CustomList<>(target);
		list.sort(0, 5);

		Assertions.assertEquals(List.of(-128, 0, 5, 9, 23, 99, 12, 123, 3122), list);
	}

	@Test
	void testSortWithNulls() {
		CustomList<Integer> list = new CustomList<>(List.of(9, 0, -128, 23, 5));
		list.add(null);
		list.add(null);

		list.sort();
		Assertions.assertNull(list.get(0));
	}

}
