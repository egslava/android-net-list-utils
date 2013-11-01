Common information
=======
We very often work with lists and network. There're few types of very common situations in development:

# Mapping/unmapping

1. __Unmapping__. Send data from interface to a server. For instance, we have a screen with user information (first name, last name, middle name, and so forth). So we get data from interface and send it to a server. Pseudocode is:
 
 ```java
 User = new User(); 
 user.firstName = ((EditText) findViewById(R.id.editTextFirstName).getText().toString();
  user.lastName = ((EditText) findViewById(R.id.editTextLastName).getText().toString(); 
 user.ages = Integer.valueOf(((EditText) findViewById(R.id.editTextAges).getText().toString()); 
 String json = new Gson.serialize(user); 
 api.updateProfile(user);
 ```
 
1. __Mapping__. Reverse situation: we get information about user from a server and should render it to UI.
 
 ```java
 User = api.getProfile(token); 
 ((EditText) findViewById(R.id.editTextFirstName).setText(user.firstName); 
 ((EditText) findViewById(R.id.editTextLastName).setText(user.lastName); 
 ((EditText) findViewById(R.id.editTextAges).setText(String.valueOf(user.ages));
 ```

So when layout has many fields to map/unmap, project code has many routine…
In case of using view mapper (real code): 

```java
@Bean 
ViewMapper mapper; //initialization of mapper

@ViewById 
ViewGroup root; //root layout

User user = new User("Egorenkov", "Viacheslav", "Igorevich", "Brief biography"); 
mapper.map(root, user); //renders object 'user' to 'root' layout

User user2 = new User(); 
mapper.unmap(root, user2); //renders layout 'root' to object 'user2'
```

# List mapping
```java
 @Bean
 ViewMapperAdapter	adapter;	
...
...
  List<Person> people = Person.generatePeople(1000, this);
  adapter.setObjects(people);
  setListAdapter(adapter);
 }
}
```
Will show ListActivity with layout person.xml as an item.

# Dynamic list items loading
There're many lists that should supports the pagination feature. For instance it might be news feed or very long message history or something else.
With NetUtils library using of that feature is really simple:

```java
@EActivity
public class DynamicListLoadingActivity extends ListActivity implements DataSource {
 @Bean
 CursorScrollListener scroller;
	
 @AfterInject
 void init(){
  scroller.init(ListType.FROM_BOTTOM_TO_TOP, this, getListView());
 }

 @Override
 public List<? extends CursorScrollable> getNextDataPortion(int startId, String direction) {
  return new ArrayList<Person>(Person.generatePeople(10, this) );
 }
}
```

# Flexibility
You are able to show list with logic in its elements (for instance, button click listeners, etc) and correctly process events, set fallback id for images loaded from Internet, set your own naming policies in layouts and enable/disable items in lists.
