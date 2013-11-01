Dynamic list loading
======
There're many situations, when list you are trying to show is too big to load at one time. For example, you do a newsfeed application. You should show only 20 last news articles, but when user will scroll list down you want to load additional 20 articles and so forth.

The code below does this:

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

getNextDataPortion is called when list should prepare for render data that it doesn't have right now. So you should load/generate new data. In this example for simplicity we generate data. But you should know, that **getNextDataPortion is called from the background thread**, so you are able easily to load data from Network.

The result value should be List of `CursorScrollable`.
```java
public interface CursorScrollable {
  public int getId();
}
```

So what is id it's talking about? ID - is just a unique number of element. In our simple case it's just auto incremented sequence of numbers:

```java
 protected static int autoIncrement;
	
 public int id = autoIncrement++;
 /** For supporting of list dynamic loading */
 @Override
 public int getId() {
  return id;
 }
```

But in more common case ID is just unique number of element.

`ListType.FROM_BOTTOM_TO_TOP` means that oldest (with smaller id) entities are in the top and newest (with higher id) in the bottom.
That's good type for such lists as newsfeed. When the newest articles are in the top and to see oldest articles user should scroll list down to bottom.

`ListType.FROM_TOP_TO_BOTTOM` means that newest (with higher id) entities are in the bottom of the list and older (with smaller id) entities are in the top. That's good option for chat-like screens. Newest message is in the bottom and to see messages history you should scroll list to the top.

## getNextDataPortion
Method has two parameters:

+ startId - the entity id to start loading another entities.
+ direction - can be ru.poloniumarts.netutils.Direction.UP and ru.poloniumarts.netutils.Direction.DOWN

For instance, we have startId = 10 and direction = DOWN. That means server you should return a list of elements with id: 11, 12, 13, 14, 15, 16, 17, 18, 19, 20.

Another example: we are in the chat application. First call gives us: startId = -1 and some direction. We ask server to return latest messages. And it returns: 127, 128, 129, 130, 131, 132, 133, 134, 135, 136. User scrolled list up and the getNextDataPortion is called again. startId is 127 and direction is UP. Server should return 117, 118, 119, 120, 121, 122, 123, 124, 125, 126.

## How to ask CursorScrollListener to load next/previous data portion manually?
The methods loadEarlier() and loadNewier() were created for that. For instance, you have button "show more videos". Just call loadEarlier()/loadNewier() in the click listener and a data will be loaded.