package com.github.lotashinski.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.Assertions;
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
	void testSizeZero() {
		List<String> list = new CustomList<>();
		
		Assertions.assertEquals(list.size(), 0);
	}

	@Test
	void testSizeTwenty() {
		List<Number> list = new CustomList<>();
		
		for(int i = 0; i < 20; ++i) {
			list.add(i);
		}
		
		assertEquals(list.size(), 20);
	}
	
	@Test
	void testToArrayTArrayTargetIsEmty() {
		Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		List<Integer> list = new CustomList<>();
		
		for(int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}
		
		Number[] nums = list.toArray(new Number[] {});
		assertArrayEquals((Number[]) ints, nums);
	}

	@Test
	void testToArrayTArrayTargetIsEqual() {
		Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		List<Integer> list = new CustomList<>();
		
		for(int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}
		
		Number[] nums = list.toArray(new Number[ints.length]);
		assertArrayEquals((Number[]) ints, nums);
	}
	
	@Test
	void testToArrayTArrayTargetIsGreater() {
		Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		List<Integer> list = new CustomList<>();
		
		for(int i = 0; i < ints.length; ++i) {
			list.add(ints[i]);
		}
		
		Number[] nums = list.toArray(new Number[ints.length * 2]);
		
		for(int i = 0; i < ints.length; ++i) {
			assertEquals(nums[i], ints[i]);
		}
		
		assertNull(nums[ints.length]);
	}
	
	@Test
	void testAddElement() {
		List<Integer> list = new CustomList<>();
		
		assertTrue(list.add(1));
		assertTrue(list.add(2));
		assertTrue(list.add(3));
	}

	@Test
	void testRemoveObject() {
		List<Integer> list = new CustomList<>();
		
		assertTrue(list.add(1));
		assertTrue(list.remove(Integer.valueOf(1)));
	}

	@Test
	void testClear() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> list = new CustomList<>(reference);
		
		assertEquals(reference, list);
		assertEquals(reference.size(), list.size());
		
		list.clear();
		
		assertEquals(list, List.of());
		assertEquals(list.size(), 0);
	}

	@Test
	void testGet() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> list = new CustomList<>(reference);
		
		for(int i = 0; i < list.size(); ++i) {
			assertEquals(list.get(i), reference.get(i));; 
		}
		
		try {
			list.get(-1);
		} catch (IndexOutOfBoundsException e) {
			
		} 
		
		try {
			list.get(list.size());
		} catch (IndexOutOfBoundsException e) {
			
		} 
	}

	@Test
	void testSet() {
		List<Integer> list = new CustomList<>();
		
		list.add(1);
		list.add(2);
		list.add(3);
		
		assertEquals(1, list.set(0, 3));
		assertEquals(3, list.get(0));
		
		try {
			list.set(-1, 0);
		} catch (IndexOutOfBoundsException e) {
			
		} 
		
		try {
			list.set(list.size(), 0);
		} catch (IndexOutOfBoundsException e) {
			
		} 
	}

	@Test
	void testAddIntT() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> reference2 = List.of(1, 2, 0, 3, 4, 5);
		
		List<Integer> list = new CustomList<>(reference);
		assertEquals(reference, list);
		
		list.add(2, reference2.get(2));
		assertEquals(reference2, list);
		
		try {
			list.add(-1, 0);
		} catch (IndexOutOfBoundsException e) {
			
		} 
		
		try {
			list.add(list.size(), 0);
		} catch (IndexOutOfBoundsException e) {
			
		} 
	}

	@Test
	void testRemoveInt() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> reference2 = List.of(1, 5);
		
		List<Integer> list = new CustomList<>(reference);
		assertEquals(reference, list);
		
		list.remove(3);
		list.remove(2);
		list.remove(1);
		
		assertEquals(reference2, list);
		
		try {
			list.remove(-1);
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		} 
		
		try {
			list.remove(list.size());
		} catch (IndexOutOfBoundsException e) {
			// it is OK
		}
	}

	@Test
	void testSubList() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5);
		List<Integer> reference2 = List.of(2, 3, 4);
		
		List<Integer> list = new CustomList<>(reference);
		List<Integer> view = list.subList(1, 4);
		
		assertEquals(reference2, view);
		
		List<Integer> view2 = list.subList(1, 1);
		assertTrue(view2.isEmpty());
	}

	@Test
	void testIsEmpty() {
		List<Integer> list = new CustomList<>();
		
		assertTrue(list.isEmpty());
		
		list.add(1);
		
		assertFalse(list.isEmpty());
	}

	@Test
	void testContains() {
		List<Integer> reference = List.of(3);
		List<Integer> list = new CustomList<>(reference);
		
		assertTrue(list.contains(3));
		assertFalse(list.contains(6));
	}

	@Test
	void testContainsAll() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5, 6, 7, 8);
		List<Integer> referenceView = reference.subList(1, 4);
		
		List<Integer> referenceFail = List.of(-2, -1, 0, 1, 2);
		
		List<Integer> list = new CustomList<>(reference);
		
		assertTrue(list.containsAll(referenceView));
		assertFalse(list.containsAll(referenceFail));
	}

	@Test
	void testAddAllCollectionOfQextendsT() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5, 6);
		
		List<Integer> list = new CustomList<>(List.of(1, 2, 3));
		list.addAll(List.of(4, 5, 6));
		
		assertEquals(reference, list);
	}

	@Test
	void testAddAllIntCollectionOfQextendsT() {
		List<Integer> reference = List.of(1, 2, 3, 4, 5, 6);
		
		List<Integer> list = new CustomList<>(List.of(4, 5, 6));
		list.addAll(0, List.of(1, 2, 3));
		
		assertEquals(reference, list);
	}

	@Test
	void testRemoveAll() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 1, 2, 3));
		list.removeAll(List.of(0, -1, -2, -3));
		
		assertEquals(list, List.of(1, 2, 3));
	}

	@Test
	void testRetainAll() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 1, 2, 3));
		list.retainAll(List.of(1, 2, 3, 4, 5, 6));
		
		assertEquals(list, List.of(1, 2, 3));
	}

	@Test
	void testIndexOf() {
		List<Integer> list = new CustomList<>(List.of(-3, -2, -1, 0, 1, 2, 3, 0));
		
		assertEquals(list.indexOf(0), 3);
		assertEquals(list.indexOf(-3), 0);
		
		assertEquals(list.indexOf(null), -1);
	}

	@Test
	void testLastIndexOf() {
		List<Integer> list = new CustomList<>(List.of(0, 1, 2, 3, 0));
		
		assertEquals(list.lastIndexOf(1), 1);
		assertEquals(list.lastIndexOf(0), 4);
		
		assertEquals(list.indexOf(null), -1);
	}

	@Test
	void testIterator() {
		List<Integer> reference = List.of(0, 1, 2, 3, 4);
		List<Integer> list = new CustomList<>(reference);
		
		Iterator<Integer> refi = reference.iterator();
		Iterator<Integer> listi = list.iterator();
		
		while(refi.hasNext() && listi.hasNext()) {
			assertEquals(refi.next(), listi.next());
		}
		
		assertEquals(refi.hasNext(), listi.hasNext());
	}

	@Test
	void testToArray() {
		Object[] reference = {1, 2, 3, 4};
		List<Integer> list = new CustomList<>(List.of(1, 2, 3, 4));
		
		assertArrayEquals(list.toArray(), reference);
	}

	@Test
	void testListIterator() {
		List<Integer> reference = List.of(0, 1, 2, 3, 4);
		List<Integer> list = new CustomList<>(reference);
		
		ListIterator<Integer> refi = reference.listIterator();
		ListIterator<Integer> listi = list.listIterator();
		
		while(refi.hasNext() && listi.hasNext()) {
			assertEquals(refi.next(), listi.next());
		}
		
		assertEquals(refi.hasNext(), listi.hasNext());
		
		while(refi.hasPrevious() && listi.hasPrevious()) {
			assertEquals(refi.previous(), listi.previous());
		}
		
		assertEquals(refi.hasPrevious(), listi.hasPrevious());
		
		while(refi.hasNext() && listi.hasNext()) {
			assertEquals(refi.next(), listi.next());
		}
		
		assertEquals(refi.hasNext(), listi.hasNext());
	}

	@Test
	void testListIteratorInt() {
		List<Integer> reference = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		List<Integer> list = new CustomList<>(reference);
		
		ListIterator<Integer> refi = reference.listIterator(5);
		ListIterator<Integer> listi = list.listIterator(5);
		
		while(refi.hasNext() && listi.hasNext()) {
			assertEquals(refi.next(), listi.next());
		}
		
		assertEquals(refi.hasNext(), listi.hasNext());
		
		while(refi.hasPrevious() && listi.hasPrevious()) {
			assertEquals(refi.previous(), listi.previous());
		}
		
		assertEquals(refi.hasPrevious(), listi.hasPrevious());
		
		while(refi.hasNext() && listi.hasNext()) {
			assertEquals(refi.next(), listi.next());
		}
		
		assertEquals(refi.hasNext(), listi.hasNext());
	}

}
