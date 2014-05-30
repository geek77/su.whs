package su.whs.search;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * 
 * @author igor.n.boulliev
 * 
 * abstract class for deal with trigramm's index (see gentgindex.py tool)
 * - documentation for gentgindex.py available by running this tool with --doc option
 * 
 * for usage must extend TrigrammsIndex and implement 'initializeIndex' for map <String,Integer>
 * usually this part are generated
 * for example
 * 
 *  protected Map<String,Integer> initializeIndex() {
 *  	Map<String,Integer> result = new HashMap<String,Integer>();
 *      result.put('abc',33);
 *      result.put('cde',43);
 *      result.put('zzz',9999);
 *      ...
 *      result.put('a',199);
 *      return result;
 *  }
 *
 */

public abstract class TrigrammsIndex {
	
	private class WordMap {
		public int wordIndex;
		public int trigrammPos;

		public WordMap(int i, int p) {
			wordIndex = i;
			trigrammPos = p;
		}
	}
	private Map<String,Integer> mTrigrammsIndex;
	private ByteBuffer mTrigrammsBuffer;
	private ByteBuffer mWordsBuffer;
	private int mTrigrammsCounter = 0;
	private int mTrigrammsOffset = 0;
	private int mWordsCounter = 0;
	private int mWordsOffset = 0;
	
	public class InvalidIndexStreamException extends IOException {
		private static final long serialVersionUID = 6224848737805771459L;
		
	}
	
	public TrigrammsIndex(Context context, InputStream tgs, InputStream ws) throws InvalidIndexStreamException {
	    try {
			byte[] b = new byte[tgs.available()];
			tgs.read(b);
			mTrigrammsBuffer = ByteBuffer.wrap(b);
			mTrigrammsCounter = mTrigrammsBuffer.getInt();
			mTrigrammsOffset = mTrigrammsBuffer.getInt();
			b = new byte[ws.available()];
			ws.read(b);
			mWordsBuffer = ByteBuffer.wrap(b);
			mWordsCounter = mWordsBuffer.getInt();
			mWordsOffset = mWordsBuffer.getInt();
		} catch (IOException e) {
			throw new InvalidIndexStreamException();
		}

		mTrigrammsIndex = initializeIndex();
	}
	
	protected abstract Map<String,Integer> initializeIndex();
	
	private List<WordMap> load_trigramms_from_file(int index,int pos) throws IOException {
		List<WordMap> result = new ArrayList<WordMap>();
		// header = ii
		// seek record
		int offset = 8 + 8*index;
		mTrigrammsBuffer.position(offset);
		int counter = mTrigrammsBuffer.getInt();
		int roffset = mTrigrammsBuffer.getInt();
		// read wordmaps
		mTrigrammsBuffer.position(roffset);
		for(int i=0; i<counter; i++) {
			int wid = mTrigrammsBuffer.getInt();
			int tp = mTrigrammsBuffer.getInt();
			if (pos>tp-1 && pos<tp+1)
				result.add(new WordMap(wid, pos));
		}
		
		return result;
	}

	private List<WordMap> load_trigramm(String tg,int pos) throws InvalidIndexStreamException {
		if (mTrigrammsIndex.containsKey(tg))
			try {
				return load_trigramms_from_file(mTrigrammsIndex.get(tg),pos);
			} catch (IOException e) {
				throw new InvalidIndexStreamException();
			}
		return new ArrayList<WordMap>();
	}
	
	/**
	 * 
	 * @author igor.n.boulliev
	 * base query builder and executor
	 *
	 */
	
	public class Builder {
		private List<WordMap> mResult = null;
		private SparseArray<WordMap> mCandidates = new SparseArray<WordMap>();
		private int pos = 0;
		private int mMax = 0;
		/**
		 * 
		 * @param tg - trigramm (1-3 character)
		 * @return length of result
		 * @throws InvalidIndexStreamException 
		 */
				
		public int add(String tg) throws InvalidIndexStreamException {
			if (mResult==null) {
				mResult = load_trigramm(tg,pos);
				update(mResult);
				pos += 1;
				return mResult.size();
			}
			List<WordMap> result = load_trigramm(tg,pos);
			update(result);
			return mResult.size();
		}
		
		private void update(List<WordMap> map) {
			for (WordMap m: map) {
				WordMap g = mCandidates.get(m.wordIndex,null);
				if (g==null)
					g = new WordMap(m.wordIndex, 0);
				g.trigrammPos+=1;
				if (g.trigrammPos>mMax)
					mMax = g.trigrammPos;
				mCandidates.put(g.wordIndex,g); 
			}
		}
		/**
		 * return word's indices, that matches given trigramms sequence
		 * @return
		 */
		public int[] getResult() {
			List<Integer> r = new ArrayList<Integer>();
			for (int i=0; i<mCandidates.size(); i++) {
				WordMap g = mCandidates.get(mCandidates.keyAt(i));
				if (g.trigrammPos>=(mMax-1)) {
					r.add(g.wordIndex);
				}
			}
			int result[] = new int[r.size()];
			for (int i=0; i<r.size(); i++)
				result[i] = r.get(i);
			return result;
		}
	}
	
	/**
	 * get associated ids for words
	 * @param candidates - sparse array of words ids
	 * @return list of associated ids
	 */
	
	public List<Integer> get_links_list_for_words(
			SparseIntArray candidates) {
		List<Integer> result = new ArrayList<Integer>();
		
		for (int i=0; i<candidates.size(); i++) {
			int k = candidates.keyAt(i);
			int offset = 8+k*8;
			mWordsBuffer.position(offset);
			int counter = mWordsBuffer.getInt();
			offset = mWordsBuffer.getInt();
			mWordsBuffer.position(offset);
			for (int j=0; j<counter; j++) {
				int link = mWordsBuffer.getInt();
				if (link>-1)
					result.add(link);
			}
		}
	
		return result;
	}
	/**
	 * start new query for single word
	 * @return Builder instance
	 */
	
	public Builder build() {
		return new Builder();
	}
	
	protected int getTrigrammsCount() { return mTrigrammsCounter; }
	protected int getTrigrammsOffset() { return mTrigrammsOffset; }
	protected int getWordsCount() { return mWordsCounter; }
	protected int getWordsOffset() { return mWordsOffset; }
}