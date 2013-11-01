package com.example.netutilssamples.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import ru.poloniumarts.netutils.CursorScrollListener.CursorScrollable;

import android.app.AlertDialog;
import android.content.Context;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

public class Person implements CursorScrollable{
	public String	avatar;
	public Date		birthday;
	public String	firstName;
	public String	lastName;
	public String	middleName;
	
	public static int autoIncrement;
	
	public int id = autoIncrement++;
	/** For supporting of list dynamic loading */
	@Override
	public int getId() {
		return id;
	}
	
	public void alert(Context context){
		new AlertDialog.Builder(context)
			.setMessage(toString())
			.setNeutralButton(android.R.string.ok, null)
			.show();
	}
	
	public static List<Person> generatePeople(int count, Context context){
		ArrayList<Person> result = new ArrayList<Person>();
		
		if (autoIncrement > 250){
			return result;
		}
		
		Random random = new Random();
		
		for (int i = 0; i < count; i++){
			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];
			String middleName = middleNames[random.nextInt(middleNames.length)];
			int birthYear = 1960 + random.nextInt(40);
			int birthMonth = random.nextInt(monthes.length);
			int birthDay = amountOfDaysInMonth[birthMonth];
			birthMonth = monthes[birthMonth];
			Date birthDate = new GregorianCalendar(birthYear, birthMonth, birthDay).getTime();
			
			result.add(new Person().set(firstName, lastName, middleName, birthDate, null));
		}
		
		return result;
	}
	
	public Person set(String firstName, String lastName, String middleName,
			Date birthday, String avatar) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.birthday = birthday;
		this.avatar = avatar;
		return this;
	}
	
	@Override
	public String toString(){
		return String.format("%s %s %s (%s)", firstName, lastName, middleName, new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(birthday));
	}
	
	public static final String[] middleNames = new String[]{
		"Leif", "Keane", "Keith", "James", "Rhys", "Ross", "Scott", "Reid", "Wayne", "Wade", "Troy", "Trey", "Trent", "Paul", "Brent", "Brogue", "Blake", "Blaze", "Drake", "Blaine", "Dean", "Allen", "Jay", "Jett", "Lars", "Lance", "Pierce", "Lee", "Cole", "Clay", "Clark", "Kirk", "Kent", "Kai", "Chase", "Chance", "Seth", "Finn", "Neil", "Noel", "Grey", "Luke", "Mark", "Gage", "Grant", "Graham", "John", "Miles", "Quinn", "Shane", "Sean", "Todd", "Teague", "Tate", "Max", "Pax", "Zane", "Bryce", "Beau", "Brock", "Charles", "Dane", "Dale", "Heath", "Hugh", "Dell", "Asher", "Adam", "Anton", "Adair", "Aaron", "Alec", "Arthur", "Austin", "Brady", "Brody", "Brendan", "Byron", "Bennett", "Benton", "Eli", "Booker", "Bradley", "Garrett", "Peter", "Patrick", "Ian", "Isaac", "Caleb", "Christian", "Braxton", "Calum", "Caelum", "Carter", "Carson", "Collin", "Milo", "Mitchell", "Marshall", "Mason", "Michael", "Micah", "Malcolm", "Cody", "Colby", "Channing", "Chandler", "Cedric", "Silas", "Cyrus", "Cyril", "Eugene", "Reuben", "Kian", "Kieran", "Kenton", "Kenyon", "Dexter", "David", "Daniel", "Davis", "Dante", "Douglas", "Derek", "Darrin", "Darryl", "Conley", "Connor", "Conrad", "Finley", "Franklin", "Henry", "Harold", "Curtis", "Dallin", "Victor", "Vincent", "Quincy", "Quinton", "Owen", "Ethan", "Oskar", "Joseph", "Jared", "Jacob", "Casper", "Jasper", "Jasher", "Declan", "Dylan", "Dallas", "Forrest", "Duncan", "Ezra", "Asa", "Ashton", "Emmett", "Ephraim", "Evan", "Ewan", "Gerald", "Gerard", "Gavin", "Gannon", "Griffin", "Griffith", "Hudson", "Harmon", "Leo", "Leon", "Lewis", "Lawrence", "Simon", "Jesse", "Jonah", "Jonas", "Judah", "Noah", "Justin", "Kyle", "Landon", "Logan", "Leo", "Leon", "Levi", "Lincoln", "Lindell", "Lucas", "Luca", "Marcus", "Maxwell", "Maddox", "Cooper", "Parker", "Porter", "Preston", "Patrick", "Raymond", "Richard", "Raleigh", "Roman", "Ronan", "Rory", "Russell", "Ryder", "Sawyer", "Steven", "Tanner", "Tucker", "Tyler", "Trevor", "Travis", "Toby", "Rudy", "Wesley", "Weston", "Wallace", "Walker", "Wendell", "Wyatt", "William", "Nicholas", "Nathaniel", "McKinley", "Leander", "Jonathan", "Simeon", "Ezekiel", "Everett", "Dakota", "Zachary", "Frederick", "Damien", "Dominic", "Anderson", "Anthony", "Adrian", "Benjamin", "Emory", "Elliott", "Emerson", "Elias", "Elijah", "Sebastian", "Garrison", "Harrison", "Gregory", "Theodore", "Timothy", "Oliver", "Orion", "Christopher", "Malachi", "Caspian", "Julian", "Julius", "Jameson", "Jefferson", "Jeffery", "Joshua", "Darius", "Jericho", "Samuel", "Solomon", "Killian", "Tobias"
	};
	
	public static final String[] firstNames = new String[]{
		"Sophia", "Emma", "Olivia", "Isabella", "Ava", "Lily", "Zoe", "Chloe", "Mia", "Madison", "Emily", "Ella", "Madelyn", "Abigail", "Aubrey", "Addison", "Avery", "Layla", "Hailey", "Amelia", "Hannah", "Charlotte", "Kaitlyn", "Harper", "Kaylee", "Sophie", "Mackenzie", "Peyton", "Riley", "Grace", "Brooklyn", "Sarah", "Aaliyah", "Anna", "Arianna", "Ellie", "Natalie", "Isabelle", "Lillian", "Evelyn", "Elizabeth", "Lyla", "Lucy", "Claire", "Makayla", "Kylie", "Audrey", "Maya", "Leah", "Gabriella", "Annabelle", "Savannah", "Nora", "Reagan", "Scarlett", "Samantha", "Alyssa", "Allison", "Elena", "Stella", "Alexis", "Victoria", "Aria", "Molly", "Maria", "Bailey", "Sydney", "Bella", "Mila", "Taylor", "Kayla", "Eva", "Jasmine", "Gianna", "Alexandra", "Julia", "Eliana", "Kennedy", "Brianna", "Ruby", "Lauren", "Alice", "Violet", "Kendall", "Morgan", "Caroline", "Piper", "Brooke", "Elise", "Alexa", "Sienna", "Reese", "Clara", "Paige", "Kate", "Nevaeh", "Sadie", "Quinn", "Isla", "Eleanor", "Aiden", "Jackson", "Ethan", "Liam", "Mason", "Noah", "Lucas", "Jacob", "Jayden", "Jack", "Logan", "Ryan", "Caleb", "Benjamin", "William", "Michael", "Alexander", "Elijah", "Matthew", "Dylan", "James", "Owen", "Connor", "Brayden", "Carter", "Landon", "Joshua", "Luke", "Daniel", "Gabriel", "Nicholas", "Nathan", "Oliver", "Henry", "Andrew", "Gavin", "Cameron", "Eli", "Max", "Isaac", "Evan", "Samuel", "Grayson", "Tyler", "Zachary", "Wyatt", "Joseph", "Charlie", "Hunter", "David", "Anthony", "Christian", "Colton", "Thomas", "Dominic", "Austin", "John", "Sebastian", "Cooper", "Levi", "Parker", "Isaiah", "Chase", "Blake", "Aaron", "Alex", "Adam", "Tristan", "Julian", "Jonathan", "Christopher", "Jace", "Nolan", "Miles", "Jordan", "Carson", "Colin", "Ian", "Riley", "Xavier", "Hudson", "Adrian", "Cole", "Brody", "Leo", "Jake", "Bentley", "Sean", "Jeremiah", "Asher", "Nathaniel", "Micah", "Jason", "Ryder", "Declan", "Hayden", "Brandon", "Easton", "Lincoln", "Harrison"
	};
	
	public static final String[] lastNames = new String[]{
		"SMITH", "JOHNSON", "WILLIAMS", "BROWN", "JONES", "MILLER", "DAVIS", "GARCIA", "RODRIGUEZ", "WILSON", "MARTINEZ", "ANDERSON", "TAYLOR", "THOMAS", "HERNANDEZ", "MOORE", "MARTIN", "JACKSON", "THOMPSON", "WHITE", "LOPEZ", "LEE", "GONZALEZ", "HARRIS", "CLARK", "LEWIS", "ROBINSON", "WALKER", "PEREZ", "HALL", "YOUNG", "ALLEN", "SANCHEZ", "WRIGHT", "KING", "SCOTT", "GREEN", "BAKER", "ADAMS", "NELSON", "HILL", "RAMIREZ", "CAMPBELL", "MITCHELL", "ROBERTS", "CARTER", "PHILLIPS", "EVANS", "TURNER", "TORRES", "PARKER", "COLLINS", "EDWARDS", "STEWART", "FLORES", "MORRIS", "NGUYEN", "MURPHY", "RIVERA", "COOK", "ROGERS", "MORGAN", "PETERSON", "COOPER", "REED", "BAILEY", "BELL", "GOMEZ", "KELLY", "HOWARD", "WARD", "COX", "DIAZ", "RICHARDSON", "WOOD", "WATSON", "BROOKS", "BENNETT", "GRAY", "JAMES", "REYES", "CRUZ", "HUGHES", "PRICE", "MYERS", "LONG", "FOSTER", "SANDERS", "ROSS", "MORALES", "POWELL", "SULLIVAN", "RUSSELL", "ORTIZ", "JENKINS", "GUTIERREZ", "PERRY", "BUTLER", "BARNES", "FISHER", "HENDERSON", "COLEMAN", "SIMMONS", "PATTERSON", "JORDAN", "REYNOLDS", "HAMILTON", "GRAHAM", "KIM", "GONZALES", "ALEXANDER", "RAMOS", "WALLACE", "GRIFFIN", "WEST", "COLE", "HAYES", "CHAVEZ", "GIBSON", "BRYANT", "ELLIS", "STEVENS", "MURRAY", "FORD", "MARSHALL", "OWENS", "MCDONALD", "HARRISON", "RUIZ", "KENNEDY", "WELLS", "ALVAREZ", "WOODS", "MENDOZA", "CASTILLO", "OLSON", "WEBB", "WASHINGTON", "TUCKER", "FREEMAN", "BURNS", "HENRY", "VASQUEZ", "SNYDER", "SIMPSON", "CRAWFORD", "JIMENEZ", "PORTER", "MASON", "SHAW", "GORDON", "WAGNER", "HUNTER", "ROMERO", "HICKS", "DIXON", "HUNT", "PALMER", "ROBERTSON", "BLACK", "HOLMES", "STONE", "MEYER", "BOYD", "MILLS", "WARREN", "FOX", "ROSE", "RICE", "MORENO", "SCHMIDT", "PATEL", "FERGUSON", "NICHOLS", "HERRERA", "MEDINA", "RYAN", "FERNANDEZ", "WEAVER", "DANIELS", "STEPHENS", "GARDNER", "PAYNE", "KELLEY", "DUNN", "PIERCE", "ARNOLD", "TRAN", "SPENCER", "PETERS", "HAWKINS", "GRANT", "HANSEN", "CASTRO", "HOFFMAN", "HART", "ELLIOTT", "CUNNINGHAM", "KNIGHT", "BRADLEY", "CARROLL", "HUDSON", "DUNCAN", "ARMSTRONG", "BERRY", "ANDREWS", "JOHNSTON", "RAY", "LANE", "RILEY", "CARPENTER", "PERKINS", "AGUILAR", "SILVA", "RICHARDS", "WILLIS", "MATTHEWS", "CHAPMAN", "LAWRENCE", "GARZA", "VARGAS", "WATKINS", "WHEELER", "LARSON", "CARLSON", "HARPER", "GEORGE", "GREENE", "BURKE", "GUZMAN", "MORRISON", "MUNOZ", "JACOBS", "OBRIEN", "LAWSON", "FRANKLIN", "LYNCH", "BISHOP", "CARR", "SALAZAR", "AUSTIN", "MENDEZ", "GILBERT", "JENSEN", "WILLIAMSON", "MONTGOMERY", "HARVEY", "OLIVER", "HOWELL", "DEAN", "HANSON", "WEBER", "GARRETT", "SIMS", "BURTON", "FULLER", "SOTO", "MCCOY", "WELCH", "CHEN", "SCHULTZ", "WALTERS", "REID", "FIELDS", "WALSH", "LITTLE", "FOWLER", "BOWMAN", "DAVIDSON", "MAY", "DAY", "SCHNEIDER", "NEWMAN", "BREWER", "LUCAS", "HOLLAND", "WONG", "BANKS", "SANTOS", "CURTIS", "PEARSON", "DELGADO", "VALDEZ", "PENA", "RIOS", "DOUGLAS", "SANDOVAL", "BARRETT", "HOPKINS", "KELLER", "GUERRERO", "STANLEY", "BATES", "ALVARADO", "BECK", "ORTEGA", "WADE", "ESTRADA", "CONTRERAS", "BARNETT", "CALDWELL", "SANTIAGO", "LAMBERT", "POWERS", "CHAMBERS", "NUNEZ", "CRAIG", "LEONARD", "LOWE", "RHODES", "BYRD", "GREGORY", "SHELTON", "FRAZIER", "BECKER", "MALDONADO", "FLEMING", "VEGA", "SUTTON", "COHEN", "JENNINGS", "PARKS", "MCDANIEL", "WATTS", "BARKER", "NORRIS", "VAUGHN", "VAZQUEZ", "HOLT", "SCHWARTZ", "STEELE", "BENSON", "NEAL", "DOMINGUEZ", "HORTON", "TERRY", "WOLFE", "HALE", "LYONS", "GRAVES", "HAYNES", "MILES", "PARK", "WARNER", "PADILLA", "BUSH", "THORNTON", "MCCARTHY", "MANN", "ZIMMERMAN", "ERICKSON", "FLETCHER", "MCKINNEY", "PAGE", "DAWSON", "JOSEPH", "MARQUEZ", "REEVES", "KLEIN", "ESPINOZA", "BALDWIN", "MORAN", "LOVE", "ROBBINS", "HIGGINS", "BALL", "CORTEZ", "LE", "GRIFFITH", "BOWEN", "SHARP", "CUMMINGS", "RAMSEY", "HARDY", "SWANSON", "BARBER", "ACOSTA", "LUNA", "CHANDLER", "BLAIR", "DANIEL", "CROSS", "SIMON", "DENNIS", "OCONNOR", "QUINN", "GROSS", "NAVARRO", "MOSS", "FITZGERALD", "DOYLE", "MCLAUGHLIN", "ROJAS", "RODGERS", "STEVENSON", "SINGH", "YANG", "FIGUEROA", "HARMON", "NEWTON", "PAUL", "MANNING", "GARNER", "MCGEE", "REESE", "FRANCIS", "BURGESS", "ADKINS", "GOODMAN", "CURRY", "BRADY", "CHRISTENSEN", "POTTER", "WALTON", "GOODWIN", "MULLINS", "MOLINA", "WEBSTER", "FISCHER", "CAMPOS", "AVILA", "SHERMAN", "TODD", "CHANG", "BLAKE", "MALONE", "WOLF", "HODGES", "JUAREZ", "GILL", "FARMER", "HINES", "GALLAGHER", "DURAN", "HUBBARD", "CANNON", "MIRANDA", "WANG", "SAUNDERS", "TATE", "MACK", "HAMMOND", "CARRILLO", "TOWNSEND", "WISE", "INGRAM", "BARTON", "MEJIA", "AYALA", "SCHROEDER", "HAMPTON", "ROWE", "PARSONS", "FRANK", "WATERS", "STRICKLAND", "OSBORNE", "MAXWELL", "CHAN", "DELEON", "NORMAN", "HARRINGTON", "CASEY", "PATTON", "LOGAN", "BOWERS", "MUELLER", "GLOVER", "FLOYD", "HARTMAN", "BUCHANAN", "COBB", "FRENCH", "KRAMER", "MCCORMICK", "CLARKE", "TYLER", "GIBBS", "MOODY", "CONNER", "SPARKS", "MCGUIRE", "LEON", "BAUER", "NORTON", "POPE", "FLYNN", "HOGAN", "ROBLES", "SALINAS", "YATES", "LINDSEY", "LLOYD", "MARSH", "MCBRIDE", "OWEN", "SOLIS", "PHAM", "LANG", "PRATT", "LARA", "BROCK", "BALLARD", "TRUJILLO", "SHAFFER", "DRAKE", "ROMAN", "AGUIRRE", "MORTON", "STOKES", "LAMB", "PACHECO", "PATRICK", "COCHRAN", "SHEPHERD", "CAIN", "BURNETT", "HESS", "LI", "CERVANTES", "OLSEN", "BRIGGS", "OCHOA", "CABRERA", "VELASQUEZ", "MONTOYA", "ROTH", "MEYERS", "CARDENAS", "FUENTES", "WEISS", "HOOVER", "WILKINS", "NICHOLSON", "UNDERWOOD", "SHORT", "CARSON", "MORROW", "COLON", "HOLLOWAY", "SUMMERS", "BRYAN", "PETERSEN", "MCKENZIE", "SERRANO", "WILCOX", "CAREY", "CLAYTON", "POOLE", "CALDERON", "GALLEGOS", "GREER", "RIVAS", "GUERRA", "DECKER", "COLLIER", "WALL", "WHITAKER", "BASS", "FLOWERS", "DAVENPORT", "CONLEY", "HOUSTON", "HUFF", "COPELAND", "HOOD", "MONROE", "MASSEY", "ROBERSON", "COMBS", "FRANCO", "LARSEN", "PITTMAN", "RANDALL", "SKINNER", "WILKINSON", "KIRBY", "CAMERON", "BRIDGES", "ANTHONY", "RICHARD", "KIRK", "BRUCE", "SINGLETON", "MATHIS", "BRADFORD", "BOONE", "ABBOTT", "CHARLES", "ALLISON", "SWEENEY", "ATKINSON", "HORN", "JEFFERSON", "ROSALES", "YORK", "CHRISTIAN", "PHELPS", "FARRELL", "CASTANEDA", "NASH", "DICKERSON", "BOND", "WYATT", "FOLEY", "CHASE", "GATES", "VINCENT", "MATHEWS", "HODGE", "GARRISON", "TREVINO", "VILLARREAL", "HEATH", "DALTON", "VALENCIA", "CALLAHAN", "HENSLEY", "ATKINS", "HUFFMAN", "ROY", "BOYER", "SHIELDS", "LIN", "HANCOCK", "GRIMES", "GLENN", "CLINE", "DELACRUZ", "CAMACHO", "DILLON", "PARRISH", "ONEILL", "MELTON", "BOOTH", "KANE", "BERG", "HARRELL", "PITTS", "SAVAGE", "WIGGINS", "BRENNAN", "SALAS", "MARKS", "RUSSO", "SAWYER", "BAXTER", "GOLDEN", "HUTCHINSON", "LIU", "WALTER", "MCDOWELL", "WILEY", "RICH", "HUMPHREY", "JOHNS", "KOCH", "SUAREZ", "HOBBS", "BEARD", "GILMORE", "IBARRA", "KEITH", "MACIAS", "KHAN", "ANDRADE", "WARE", "STEPHENSON", "HENSON", "WILKERSON", "DYER", "MCCLURE", "BLACKWELL", "MERCADO", "TANNER", "EATON", "CLAY", "BARRON", "BEASLEY", "ONEAL", "PRESTON", "SMALL", "WU", "ZAMORA", "MACDONALD", "VANCE", "SNOW", "MCCLAIN", "STAFFORD", "OROZCO", "BARRY", "ENGLISH", "SHANNON", "KLINE", "JACOBSON", "WOODARD", "HUANG", "KEMP", "MOSLEY", "PRINCE", "MERRITT", "HURST", "VILLANUEVA", "ROACH", "NOLAN", "LAM", "YODER", "MCCULLOUGH", "LESTER", "SANTANA", "VALENZUELA", "WINTERS", "BARRERA", "LEACH", "ORR", "BERGER", "MCKEE", "STRONG", "CONWAY", "STEIN", "WHITEHEAD", "BULLOCK", "ESCOBAR", "KNOX", "MEADOWS", "SOLOMON", "VELEZ", "ODONNELL", "KERR", "STOUT", "BLANKENSHIP", "BROWNING", "KENT", "LOZANO", "BARTLETT", "PRUITT", "BUCK", "BARR", "GAINES", "DURHAM", "GENTRY", "MCINTYRE", "SLOAN", "MELENDEZ", "ROCHA", "HERMAN", "SEXTON", "MOON", "HENDRICKS", "RANGEL", "STARK", "LOWERY", "HARDIN", "HULL", "SELLERS", "ELLISON", "CALHOUN", "GILLESPIE", "MORA", "KNAPP", "MCCALL", "MORSE", "DORSEY", "WEEKS", "NIELSEN", "LIVINGSTON", "LEBLANC", "MCLEAN", "BRADSHAW", "GLASS", "MIDDLETON", "BUCKLEY", "SCHAEFER", "FROST", "HOWE", "HOUSE", "MCINTOSH", "HO", "PENNINGTON", "REILLY", "HEBERT", "MCFARLAND", "HICKMAN", "NOBLE", "SPEARS", "CONRAD", "ARIAS", "GALVAN", "VELAZQUEZ", "HUYNH", "FREDERICK", "RANDOLPH", "CANTU", "FITZPATRICK", "MAHONEY", "PECK", "VILLA", "MICHAEL", "DONOVAN", "MCCONNELL", "WALLS", "BOYLE", "MAYER", "ZUNIGA", "GILES", "PINEDA", "PACE", "HURLEY", "MAYS", "MCMILLAN", "CROSBY", "AYERS", "CASE", "BENTLEY", "SHEPARD", "EVERETT", "PUGH", "DAVID", "MCMAHON", "DUNLAP", "BENDER", "HAHN", "HARDING", "ACEVEDO", "RAYMOND", "BLACKBURN", "DUFFY", "LANDRY", "DOUGHERTY", "BAUTISTA", "SHAH", "POTTS", "ARROYO", "VALENTINE", "MEZA", "GOULD", "VAUGHAN", "FRY", "RUSH", "AVERY", "HERRING", "DODSON", "CLEMENTS", "SAMPSON", "TAPIA", "BEAN", "LYNN", "CRANE", "FARLEY", "CISNEROS", "BENTON", "ASHLEY", "MCKAY", "FINLEY", "BEST", "BLEVINS", "FRIEDMAN", "MOSES", "SOSA", "BLANCHARD", "HUBER", "FRYE", "KRUEGER", "BERNARD", "ROSARIO", "RUBIO", "MULLEN", "BENJAMIN", "HALEY", "CHUNG", "MOYER", "CHOI", "HORNE", "YU", "WOODWARD", "ALI", "NIXON", "HAYDEN", "RIVERS", "ESTES", "MCCARTY", "RICHMOND", "STUART", "MAYNARD", "BRANDT", "OCONNELL", "HANNA", "SANFORD", "SHEPPARD", "CHURCH", "BURCH", "LEVY", "RASMUSSEN", "COFFEY", "PONCE", "FAULKNER", "DONALDSON", "SCHMITT", "NOVAK", "COSTA", "MONTES", "BOOKER", "CORDOVA", "WALLER", "ARELLANO", "MADDOX", "MATA", "BONILLA", "STANTON", "COMPTON", "KAUFMAN", "DUDLEY", "MCPHERSON", "BELTRAN", "DICKSON", "MCCANN", "VILLEGAS", "PROCTOR", "HESTER", "CANTRELL", "DAUGHERTY", "CHERRY", "BRAY", "DAVILA", "ROWLAND", "LEVINE", "MADDEN", "SPENCE", "GOOD", "IRWIN", "WERNER", "KRAUSE", "PETTY", "WHITNEY", "BAIRD", "HOOPER", "POLLARD", "ZAVALA", "JARVIS", "HOLDEN", "HAAS", "HENDRIX", "MCGRATH", "BIRD", "LUCERO", "TERRELL", "RIGGS", "JOYCE", "MERCER", "ROLLINS", "GALLOWAY", "DUKE", "ODOM", "ANDERSEN", "DOWNS", "HATFIELD", "BENITEZ", "ARCHER", "HUERTA", "TRAVIS", "MCNEIL", "HINTON", "ZHANG", "HAYS", "MAYO", "FRITZ", "BRANCH", "MOONEY", "EWING", "RITTER", "ESPARZA", "FREY", "BRAUN", "GAY", "RIDDLE", "HANEY", "KAISER", "HOLDER", "CHANEY", "MCKNIGHT", "GAMBLE", "VANG", "COOLEY", "CARNEY", "COWAN", "FORBES", "FERRELL", "DAVIES", "BARAJAS", "SHEA", "OSBORN", "BRIGHT", "CUEVAS", "BOLTON", "MURILLO", "LUTZ", "DUARTE", "KIDD", "KEY", "COOKE"
	};
	
	public static final int[] monthes = new int[]{GregorianCalendar.JANUARY, GregorianCalendar.FEBRUARY, GregorianCalendar.MARCH, GregorianCalendar.APRIL, GregorianCalendar.MAY, GregorianCalendar.JUNE, GregorianCalendar.JULY, GregorianCalendar.AUGUST, GregorianCalendar.SEPTEMBER, GregorianCalendar.OCTOBER, GregorianCalendar.NOVEMBER, GregorianCalendar.DECEMBER};
	public static final int[] amountOfDaysInMonth = new int[]{30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
}