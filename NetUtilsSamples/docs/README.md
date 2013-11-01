Net Utils
=======
NetUtils - is an Android library created to lighten developing of network applications. For instance, it allows you to render (map) data from a server directly to your layout. It also allows you to unmap ui to some object (to send to a server) and to map even complex structures with lists and sub-structures inside. Also, it has some interesting conceptions like auto-paginated list. You just give an interface that will return data from server and list will automatically inflate all layouts, add them to ListView and map data to that layouts and, when it's needed.

More of common information you can find [on this page](net-utils-brief-description.markdown).

Try it now - see [how to start](net-utils-how-to-start.markdown) guide.

# Samples
The repository contains NetUtilsSamples project. It contains few activities with next samples:

1. [Simple mapping](#net-utils-samples-simple-mapping.markdown).
1. [Simple unmapping](net-utils-samples-simple-unmapping.markdown).
1. [Simple list](net-utils-samples-simple-list.markdown).
1. [List with logic in list items](net-utils-samples-list-with-logic.markdown)
1. [Mapping of complex structures](net-utils-samples-mapping-of-complex-structures.markdown)
1. [Image loading (fallback url)](net-utils-samples-image-loading-fallback-url.markdown)
1. [Dynamic list loading (pagination, etc)](net-utils-samples-dynamic-list-loading.markdown)
1. [Enabled and disabled items](net-utils-samples-enabled-and-disabled-items.markdown)

Simple Mapping
=======

<a name="net-utils-samples-simple-mapping.markdown">I will use</a> __mapping__ in the context of NetUtils library a process of transportation data from object to an Interface. The code of SimpleMapping example is:

```java
@EActivity(R.layout.activity_simple_mapping)
@OptionsMenu(R.menu.main)
public class SimpleMapping extends Activity {
 @Bean
 ViewMapper mapper;
	
 @ViewById 
 ViewGroup root;
	
 Person person = new Person();
	
 @AfterViews
 public void init(){
  person.set("John", "Doe", "Example", new GregorianCalendar(1979, GregorianCalendar.JANUARY, 21).getTime(), "http://neogrotesque.net/wp-content/uploads/avatar-6.jpg");
  mapper.map(root, person);
 }
}
```

We won't talk about AndroidAnnotations and will think that you already know it. ViewMapper is a bean consists of two functions: _map_ and _unmap_.
In this example root - is the layout where are all controls located. And person is just an object.
Class person consists of the next fields:

```java
 public String	avatar;
 public Date birthday;
 public String	firstName;
 public String	lastName;
 public String	middleName;
```

And the layout root has the next code:

```
root
|-$someId
|-relativeLayout1
--|textView2
--|textView3
--|textView4
--|textView5
--|textView6
--|$avatar (ImageView)
--|$middleName
--|$lastName
--|$firstName
--|$birthday
|-textView7
|-textView8
|-textView9
```
You may notice some View ID's starting with "$" sign. It's special id's for ViewMapper. If ViewMapper finds id with $ sign it will try to find compliant field in the object you passed in map function.
Therefore:
 
+ ___avatar___ String field will be mapped to ___$avatar___ ImageView
+ ___middleName___ String will be mapped to ___$middleName___ TextView
+ ___lastName___ String will be mapped to ___$lastName___ TextView
+ ___firstName___ String will be mapped to ___$firstName___ TextView
+ ___birthday___ Date will be mapped to the ___$birthday___ TextView

# How will be a field mapped to a View?
There're few situations depends on a view type.

## Checkable
For instance, it works on a checkboxes. Correct value for this field is some value which after Boolean.parseBoolean(value.toString()) won't generate an error.

ImageView
------
There're two variants:

1. The field is a String. In that case String will be interpret as URL and ViewMapper will try to load an image by this URL.
1. The field is a Number. In that case Number will be interpret as an drawable id.
ImageView can contain android:tag in its xml representation. In that case, tag - is a number, is an id of drawable that should be displayed if image by URL couldn't be loaded.

## TextView
It also involves EditText, Buttons and so forth, because of they're subclasses of TextView. In that case any object will be acceptable. If field is String it will be mapped as-is, but if field is not String it will be converted to String and then will be mapped.
TextView that should display a Date field should also contain android:tag in its xml representation. In that case tag - is a Date format. If ViewMapper can't find tag it uses the standard Date format ("yyyy-MM-dd'T'HH:mm:ssZ" by default). See ViewMapper.setDateFormat and ViewMapper.getDateFormat to see more information.

## AdapterView
(ListView, GridView, TwoWayGridView are also relates to AdapterView).
In that case it will accept some array of object that will be mapped with help of __ViewMapperAdapter__.

# What's next?
Try go throw [Simple unmapping example](net-utils-samples-simple-unmapping.markdown)!