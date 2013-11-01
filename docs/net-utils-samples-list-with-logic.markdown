List with logic in its items
=======
In [previous tutorial](net-utils-samples-simple-list) we created a very simple list, **that can not**:

1. Process events of view inside a list item. For example, if you have some buttons in your list view, you can't call "setOnClickListener" on them.
2. Set an arbitrary layout.
3. Show/hide elements when it's needed.

And so forth.

The code of activity in this tutorial is the same as for previous tutorial, but there're some another changes:

1. In the `AndroidManifest.xml` have been added the next lines:
 
 ```xml
 <meta-data
 android:name="ViewMapperAdapter.itemViewsPackage"
 android:value="com.example.netutilssamples.itemviews" />
 ```
1. In the package `com.example.netutilssamples.itemviews` has been added the **PersonItemView.java** (we will discuss its here, but a little bit later).

Let's look at meta-data section. That code should be placed inside an application tag one time for a project. In this code you should change only the _value_. The value - it's your package that will contain all ItemViews. ItemView - it's just [AndroidAnnotaions enhanced view](https://github.com/excilys/androidannotations/wiki/Enhance-custom-views), but is used as list items.

Let's see PersonItemView:

```java
@EViewGroup(R.layout.person)
public class PersonItemView extends RelativeLayout implements ViewMapperItemView{
 public PersonItemView(Context context) { super(context); }
 public PersonItemView(Context context, AttributeSet attrs) {super(context, attrs);}
 public PersonItemView(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle);}

 @Bean
 ViewMapper	mapper;
	
 Person	currentPerson;
	
 @Override
 public ViewGroup setContentView(Object listItemData, Object listData) {
  if (currentPerson == listItemData){
   return this;
  }
  
  currentPerson = (Person)listItemData;
  mapper.map(this, currentPerson);
  
  return this;
 }
	
 int counter = 0;

 @Click
 void plus(){
  counter++;
  update();
 }
	
 @Click
 void minus(){
  counter--;
  update();
 }
	
 @ViewById
 TextView count;
	
 private void update() {
  count.setText( String.valueOf(counter) );
 }
}
```

Ok, PersonItemView it's just a ViewGroup that implements `ViewMapperItemView` interface. `ViewMapperItemView` contains only one method to implement: `setContentView`. This method is used analogically `Adapter`'s `setContentView`. The method have to return some view to add to list. In most cases you just should return `this`.

After class declaration you see three standard constructors for ViewGroup. Eclipse is generated it automatically.

Section

```java
  if (currentPerson == listItemData){
   return this;
  }
  
  currentPerson = (Person)listItemData;
```
isn't necessary - it's just sort of optimisation.

**The most important part is:**

```java
  mapper.map(this, currentPerson);
```
After some processing you should map your object independently. In our case we don't do any processing, so there's just sort of template code.

All the next code does a logic of ItemView and similar to casual AndroidAnnotations code.

Go to the [next tutorial](net-utils-samples-mapping-of-complex-structures)!