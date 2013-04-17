package su.whs.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public class CircularBuffer<E> extends AbstractList<E> implements RandomAccess {
	
	private final int mCapacity; 
	private final List<E> mItems; 
	private int mFirst = 0;
	private int mSize = 0;
	
	public CircularBuffer(int capacity) {
		mCapacity = capacity;
		mItems = new ArrayList<E>(Collections.nCopies(mCapacity, (E) null));
	}
	
	private int wrapIndex(int index) {
		int m = index % mCapacity;
		if (m<0) m+=mCapacity;
		return m;
	}

	@Override
	public E get(int index) {
		if (index<0||index>=mCapacity-1) throw new IndexOutOfBoundsException();
		if (index>mSize) throw new IndexOutOfBoundsException();
		return mItems.get(wrapIndex(mFirst+index));		
	}
	
	@Override
	public E set(int index, E item) {
		if (index<0||index>=mCapacity-1) throw new IndexOutOfBoundsException();
		if (index==mSize) throw new IndexOutOfBoundsException();
		return mItems.set(wrapIndex(mFirst-mSize+index), item);
	}
	
	public void insert(E item) {
		int s = mSize;
		mItems.set(wrapIndex(mFirst), item);
		mFirst = wrapIndex(++mFirst);
		mItems.set(mFirst, null);
		if (s == mSize-1)
			return;
		mSize++;
	}
	

	@Override
	public void clear() {
		int count = wrapIndex(mFirst-mSize);
		for (;count!=mFirst;count=wrapIndex(++count))
			mItems.set(count, null);
		mSize = 0;
	}
	
	public E removeOldest() {
		int index = wrapIndex(mFirst+1);
		for (;;index=wrapIndex(++index)) {
			if (mItems.get(index)!=null) break;
			if (index==mFirst)
				throw new IllegalStateException("CircularBuffer is empty");
		}
		mSize--;
		return mItems.set(index, null);			
	}
	
	public E getOldest() {
		int index = wrapIndex(mFirst+1);
		for (;;index=wrapIndex(++index)) {
			if (mItems.get(index)!=null) break;
			if (index==mFirst)
				throw new IllegalStateException("CircularBuffer is empty");			
		}
		return mItems.get(index);
	}
	
	public E getNewest() {
		int index = wrapIndex(mFirst-1);
		if(mItems.get(index)==null)
			throw new IllegalStateException("CircularBuffer is empty");
		return mItems.get(index);
	}
	
	@Override
	public int size() {
		return mSize;
	}

	public int capacity() {
		return mCapacity - 1;
	}
}
