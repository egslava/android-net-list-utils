Mapping list of complex structures
======
You want to show to a user a list of universities. Universities has students. So class of university looks like:

```java
public class University {
 public String	name;
 public String	location;
 public double	rating;
 public Person	director;
 public Person[]	pupils;
}
```

and also have some auxiliary functionality (constructor and function to generate some random universities). At first glance it's just a casual class for mapping. But the field `director` is an Object. And field `pupils` is an array of objects.

Class of activity is the same as in another samples so we won't focus on it. In this example we also don't have ItemViews. Let's see an ```university.xml``` outline:

```
-LinearLayout
|-LinearLayout
||-TextView ($someId)
||-TextView (textView1)
||-TextView ($location)
||-TextView (textView2)
|-LinearLayout
||-TextView ($director__firstName)
||-TextView ($director__lastName)
||-TextView ($director__middleName)
|-TextView (textView4)
|ListView ($pupils)
```

So to map an array you should have an ```AdapterView``` in your layout. In this case it's a ListView, but NetUtils also supports GridView, and even [Two-Way-Grid-View](https://github.com/jess-anders/two-way-gridview)

Let's take a look at ```$director__name``` construction. There're **two** underscore symbols. It's equivalent to ```university.director.name``` construction. 

## Restrictions
* Current version of NetUtils doesn't support nested ItemViews.
* There's no operation to get element by index. In other words, you can't have an id like `$pupils[0].name`