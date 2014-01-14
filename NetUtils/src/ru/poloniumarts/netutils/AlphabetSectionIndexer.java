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
public abstract class AlphabetSectionIndexer implements ViewMapperSectionIndexer{

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
		return Math.max( Collections.binarySearch(positions, position), 0);
	}

	@Override
	public Object[] getSections() {
		return sections.toArray();
	}
}
