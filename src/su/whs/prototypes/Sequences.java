package su.whs.prototypes;

import android.util.SparseArray;

public class Sequences {
	public class Symbol {
		public byte origin;
		public int  index;
	}
	
	public class Group {
		public Symbol first;
		public int length;
	}
	
	public class Pair {
		public Symbol next;
		public Symbol prev;
	}
	
	private class Chain {
		private SparseArray<Pair> mIndex = new SparseArray<Pair>();
		private Sequences mSeqs;
		
		public Chain(Sequences p) {
			mSeqs = p;
		}
		
		public Pair get(int index) {
			return mIndex.get(index);
		}
		
		public Symbol next(int i) {
			return mIndex.get(i).next;
		}
		
		public Symbol previous(int i) {
			return mIndex.get(i).prev;
		}
	}
	
	private Chain[] mRoots = new Chain[256];

	public Sequences() {
		
	}
	
	public class Context {
		private Sequences mSeqs;
		private Sequences mShort = new Sequences();
		private int mWidth = 1;
		private int mHeight = 1;
		private Symbol mPivot;
		public Context(Sequences p) { mSeqs = p; }
		
		public void push(byte[] data) {
			mShort.push(data);
		}
		
		public void apply() {
			mSeqs.apply(this);
		}
		
		protected Sequences getShort() {
			return mShort;
		}
		
		protected int getWidth() { return mWidth; }
		protected int getHeight() { return mHeight; }
	}	
	
	protected void apply(Context ctx) {
		// forward data from ctx.getShort() to mRoots
	}
	
	protected Symbol next(Symbol s) {
		return mRoots[s.origin].next(s.index);
	}
	
	protected Symbol previous(Symbol s) {
		return mRoots[s.origin].previous(s.index);
	}
	
	private Symbol mLast;
	
	/* non-context raw push */
	protected void push(byte[] data) {
		
	}
}
