Enabled and Disabled Items
===
There're some kind of lists which you don't have ability to click on the list item. For instance, chat messages list. One message - one list item. But you don't want to have an effect of push on the list item:

![Selected item in a list](http://s17.postimg.org/4ld0r6b3z/selected_item.png)

You may set item as enabled or disabled independently by implementing `Enableable` interface or just set 
`ViewMapper`'s `isItemsEnabled` variable.

The `Enableable` interface has only one method to implement:

```java
 public boolean isEnabled();
```
A value of `isEnabled()` method has higher priority than value `isItemsEnabled` priority. So you can regard the `isItemsEnabled` as a default value if you don't want to implement Enableable interface for your class.