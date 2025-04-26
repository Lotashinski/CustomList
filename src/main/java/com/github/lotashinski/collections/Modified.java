package com.github.lotashinski.collections;

import java.util.ConcurrentModificationException;

class Modified<T> {

	private int counter;

	
	public Modified() {
		super();
	}

	
	protected final void syncModifications(Modified<?> lst) {
		setModifictions(lst.getModifications());
	}

	protected final void checkModifications(Modified<?> m) {
		if (m.getModifications() != getModifications())
			throw new ConcurrentModificationException("The number of modifications does not match!");
	}

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