package com.github.lotashinski.collections;

import java.util.ConcurrentModificationException;

/**
 * Simple implementation of a tool for tracking the
 * fact of state change between objects. Allows you
 * to implement a check for operations between 
 * dependent entities.
 * 
 * Implemented on the principle of a change counter. 
 * Derived entities must track changes and 
 * synchronization themselves.
 * 
 * @param <T>
 */
class Modified<T> {

	private int counter;

	
	public Modified() {
	}

	/**
	 * Synchronize with the state of another entity.
	 * The internal counter is set equal to the 
	 * argument counter
	 * 
	 * @param lst
	 */
	protected final void syncModifications(Modified<?> lst) {
		setModifictions(lst.getModifications());
	}

	/**
	 * Checks if the argument counter matches its own
	 * if not, an exception will be thrown 
	 * 
	 * @param m
	 * @throws ConcurrentModificationException
	 */
	protected final void checkModifications(Modified<?> m) throws ConcurrentModificationException {
		if (m.getModifications() != getModifications())
			throw new ConcurrentModificationException("The number of modifications does not match!");
	}

	/**
	 * Change of counter state. Should be called after
	 * each critical state change that could lead to 
	 * an indefinite behavior.
	 */
	protected final void incModofications() {
		counter++;
	}

	
	private void setModifictions(int c) {
		counter = c;
	}

	private int getModifications() {
		return counter;
	}
	
}