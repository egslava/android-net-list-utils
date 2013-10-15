package ru.poloniumarts.netutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.widget.SectionIndexer;

/**
 * Helper class for easily enabled fast-scrolling in sorted listviews
 * @author egslava
 *
 */
public abstract class AlphabetSectionIndexer implements SectionIndexer{
//	Character[] letters = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','а','б','в','г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я'};
//	SparseIntArray		indexes;

	abstract public char getFirstLetter(int position);
	abstract public int	size();
	
	List<Character>	sections;		// [A, B, C, D, E, ...]
	List<Integer>	positions;		// [0, 10, 20, 30, 40, ...]
	
	public void notifyDataSetChanged(){
		int size = size();
		sections = new ArrayList<Character>();
		positions = new ArrayList<Integer>();
		Character prevCharacter = null;
		Character currentChar;
		for(int i = 0; i < size; i++){
			currentChar = getFirstLetter(i);
			if (currentChar != prevCharacter){
				sections.add(currentChar);
				positions.add(i);
				prevCharacter = currentChar;
				currentChar++;
			}
		}
	}
	
	@Override
	public int getPositionForSection(int section) {
		section = Math.max( 0, Math.min(section, positions.size() - 1) );
		if ( Utils.isEmpty(positions) ){
			return 0;
		}
		return positions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
//		return Math.max(positions.indexOf(position), 0);
		return Math.max( Collections.binarySearch(positions, position), 0);
	}

	@Override
	public Object[] getSections() {
		return sections.toArray();
	}
}
