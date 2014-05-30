package su.whs.search;

import java.util.ArrayList;
import java.util.List;
import su.whs.search.TrigrammsIndex.InvalidIndexStreamException;
import android.util.SparseIntArray;

/**
 * 
 * @author igor.n.boulliev
 *
 */

public class SearchIndex {
	private TrigrammsIndex mTrigramms;
	/**
	 * 
	 * @param index - instance of TrigrammsIndex (see su.whs.search.TrigrammsIndex)
	 * 
	 */
	public SearchIndex(TrigrammsIndex index)  {
		mTrigramms = index;
	}
	/**
	 * construct new search
	 * @return
	 */
			
	public SearchEngine search() {
		return new SearchEngine();
	}
	
	public class SearchEngine {
		private String[] mWords;
		/**
		 * 
		 * @return string array with words from search
		 */
		public String[] getWords() { return mWords; }
		/**
		 * 
		 * @param query - search query
		 * @return list of associated id's (see gentgindex.py --doc for details)
		 * @throws InvalidIndexStreamException
		 */
		public List<Integer> find(String query) throws InvalidIndexStreamException {
			TrigrammsIndex.Builder b = mTrigramms.build();
			String q[] = query.replaceAll("\\b\\w*\\W+\\w*(?=\\b)", "").split(" ");
			mWords = q;
			SparseIntArray candidates = new SparseIntArray();
			for (String w: q) {
				List<String> tgms = new ArrayList<String>();
				int rest = w.length();
				int counter = rest-3;
				for (int i=0; i<counter; i++) {
					String tgm = w.substring(i,i+3);
					rest -= 1;
					tgms.add(tgm);
					b.add(tgm);
				}
				if (rest>0) {
					String tgm = w.substring(w.length()-rest-1,w.length()-1);
					tgms.add(tgm);
					b.add(tgm);
				}
				int words[] = b.getResult();
				for (int i=0; i<words.length; i++) {
					candidates.put(words[i], candidates.get(words[i],0)+1);
				}
			}
			// now in candidates we have all used words
			return mTrigramms.get_links_list_for_words(candidates);
		}
	}
}
