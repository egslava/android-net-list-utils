package com.example.netutilssamples.entities;

import java.util.ArrayList;
import java.util.List;

import ru.poloniumarts.netutils.CursorScrollListener.CursorScrollable;

import android.content.Context;

public class University {
	public String	name;
	public String	location;
	public double	rating;
	public Person	director;
	public Person[]	pupils;
	
	public University(String name, String location, double rating) {
		this.name		= name;
		this.location	= location;
		this.rating		= rating;
	}
	
	public static List<University> generate(int count, int maxUniversityPopulation, Context context){
		List<University> result = new ArrayList<University>(count);
		
		for(int i = 0; i < count; i++){
			University nextUniversity = universities.get(i % universities.size());
			nextUniversity.pupils = new Person[maxUniversityPopulation];
			Person.generatePeople(maxUniversityPopulation, context).toArray(nextUniversity.pupils);
			nextUniversity.director = nextUniversity.pupils[0];
			result.add(nextUniversity);
		}
		
		return result;
	}
	
	public static List<University> universities = new ArrayList<University>(){{
		add( new University ("Harvard University", "United States", 100.0) );
		add( new University ("Massachusetts Institute of Technology", "United States", 87.6) );
		add( new University ("University of Cambridge", "United Kingdom", 81.3) );
		add( new University ("University of Oxford", "United Kingdom", 73.0) );
		add( new University ("University of California, Berkeley", "United States", 72.4) );
		add( new University ("Stanford University", "United States", 70.6) );
		add( new University ("Princeton University", "United States", 36.2) );
		add( new University ("University of California, Los Angeles", "United States", 35.6) );
		add( new University ("The University of Tokyo", "Japan", 32.9) );
		add( new University ("Yale University", "United States", 32.8) );
		add( new University ("California Institute of Technology", "United States", 27.8) );
		add( new University ("University of Michigan", "United States", 22.4) );
		add( new University ("Columbia University", "United States", 21.4) );
		add( new University ("University of Chicago", "United States", 21.3) );
		add( new University ("Imperial College London", "United Kingdom", 21.3) );
		add( new University ("University of Toronto", "Canada", 18.8) );
		add( new University ("Cornell University", "United States", 18.3) );
		add( new University ("University of Pennsylvania", "United States", 17.9) );
		add( new University ("Johns Hopkins University", "United States", 16.9) );
		add( new University ("University College London", "United Kingdom", 15.8) );
		add( new University ("ETH Zürich – Swiss Federal Institute of Technology Zürich", "Switzerland", 15.8) );
		add( new University ("National University of Singapore", "Singapore", 15.5) );
		add( new University ("Kyoto University", "Japan", 15.0) );
		add( new University ("University of Illinois at Urbana Champaign", "United States", 14.3) );
		add( new University ("London School of Economics and Political Science", "United Kingdom", 12.1) );
		add( new University ("Carnegie Mellon University", "United States", 11.3) );
		add( new University ("University of Texas at Austin", "United States", 11.2) );
		add( new University ("University of Washington", "United States", 11.2) );
		add( new University ("New York University", "United States", 10.8) );
		add( new University ("University of Wisconsin-Madison", "United States", 10.7) );
		add( new University ("University of British Columbia", "Canada", 10.2) );
		add( new University ("Duke University", "United States", 10.2) );
		add( new University ("McGill University", "Canada", 10.2) );
		add( new University ("University of California, San Diego", "United States", 9.7) );
		add( new University ("Tsinghua University", "China", 9.6) );
		add( new University ("The University of Hong Kong", "Hong Kong", 9.5) );
		add( new University ("Northwestern University", "United States", 9.4) );
		add( new University ("Georgia Institute of Technology", "United States", 9.2) );
		add( new University ("University of Melbourne", "Australia", 8.9) );
		add( new University ("University of California, San Francisco", "United States", 8.7) );
		add( new University ("Seoul National University", "Republic of Korea", 8.3) );
		add( new University ("Australian National University", "Australia", 8.2) );
		add( new University ("University of Massachusetts", "United States", 8.2) );
		add( new University ("Ludwig-Maximilians-Universität München", "Germany", 8.0) );
		add( new University ("Peking University", "China", 7.8) );
		add( new University ("University of Edinburgh", "United Kingdom", 7.7) );
		add( new University ("University of Manchester", "United Kingdom", 7.3) );
		add( new University ("University of California, Davis", "United States", 6.9) );
		add( new University ("University of Sydney", "Australia", 6.7) );
		add( new University ("Lomonosov Moscow State University", "Russian Federation", 6.5) );
		add( new University ("Purdue University", "United States", 6.5) );
	}};
}
