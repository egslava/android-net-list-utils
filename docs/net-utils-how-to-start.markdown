How to Start
=======
Next steps will help you to configure your first project with NetUtils:

1. [Configure Android Annotations](https://github.com/excilys/androidannotations/wiki/Configuration)
1. Copy NetUtils into workspace as an Android library.
1. Point your IDE ([I use Eclipse ADT](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject)) that you project depends on the NetUtils.
1. Create special package for [ItemView](net-utils-samples-list-with-logic)s and add the following string to your AndroidManifest.xml (between Application tag):

```xml
<meta-data
android:name="ViewMapperAdapter.itemViewsPackage"
android:value="com.example.netutilssamples.itemviews" />
```
Where _com.example.netutilssamples.itemviews_ is your ItemViews package name.

That's all. Now you're ready to go throw [the first example](net-utils-samples-simple-mapping)