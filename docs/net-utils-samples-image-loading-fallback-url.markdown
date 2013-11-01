ImageLoading fallback
======
If you mapping images from url, you need the ability to show fallback image (in terms of [Android Query](https://code.google.com/p/android-query/)). Fallback image - is an image that should be setter into `ImageView` if an image on your URL isn't available.

To set fallback image, just set ImageView tag to id of drawable resource. For example:

```xml
<ImageView
  android:id="@+id/$invalidURL"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:src="@drawable/ic_launcher"
  android:tag="ic_launcher" />
```

In this example, if image URL won't be loaded in process of mapping, `ViewMapper` will set a R.drawable.ic_launcher.