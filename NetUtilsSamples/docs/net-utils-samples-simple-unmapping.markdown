# Simple unmmaping
Beside [mapping](net-utils-samples-simple-mapping.markdown) you may also be interested in __unmapping__. For example, you want to update user's profile. So you should read data from interface to some object user and after that call something like ```api.updateProfile(user); ```.

Using unmapping operation you can do this in that way:

```java
@Bean
ViewMapper mapper;

@Click
public void showUnmappedUser(){
 try{
  mapper.unmap(root, person);
  person.alert(this);
 }catch(Throwable t){
  new AlertDialog.Builder(this)
   .setTitle(t.getMessage())
   .show();
  t.printStackTrace();
 }
}
```

Below you can see the person.alert method:

```java
 public void alert(Context context){
  new AlertDialog.Builder(context)
   .setMessage(toString())
   .setNeutralButton(android.R.string.ok, null)
   .show();
 }
```
Also, you should know, that __you can't unmap ImageViews__!
So, it's quite simple. The view's id should be named as described in [mapping tutorial](net-utils-samples-simple-mapping.markdown).

The next theme is how to map [list of values](net-utils-samples-simple-list.markdown).