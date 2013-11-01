Simple lists
======
There're many situations, when you should render much amount of structured data. For instance, you might want to load from a server list of people. Let's see an example:

```java
@EActivity
public class SimpleListActivity extends ListActivity {
	@Bean
	ViewMapperAdapter	adapter;
	
	@AfterInject
	void init(){
		List<Person> people = Person.generatePeople(1000, this);
		adapter.setObjects(people);
		setListAdapter(adapter);
	}
}
```
That's all! In this example ListActivity will show thousand people. But what layout will be inflated as an list item element? The answer is "person.xml". Let's see on outline of person.xm:

```
-LinearLayout
|-LinearLayout
||-$firstName (TextView)
||-$lastName (TextView)
||-$middleName (TextView)
||-$birthday (TextView)
|-LinearLayout
||-minus
||-count
||-plus
```

and data inside class Person

```java
public class Person{
	public String	avatar;
	public Date		birthday;
	public String	firstName;
	public String	lastName;
	public String	middleName;
	...
}
```

So, as you can see, the naming conventions for class and for xml are the same as for [simple mapping](net-utils-samples-simple-mapping).

You might noticed, that the layout also contains buttons "plus"/"minus" and textview "count", but they don't work. Also you might want to change layout name from person.xml to, for instance, my_person.xml. How to do this you may see in the [next tutorial](net-utils-samples-list-with-logic).
> Written with [StackEdit](https://stackedit.io/).