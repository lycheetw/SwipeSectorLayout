# SwipeSectorLayout
The inspiration for this project comes from [Dima Panchenko's post on Dribbble](https://dribbble.com/shots/3272140-Jewelry-E-ommerce-Application).

## Screenshots
![alt tag](https://raw.githubusercontent.com/lycheetw/SwipeSectorLayout/master/images/screenshot.gif)

## Gradle
```groovy
compile 'tw.lychee:SwipeSectorLayout:1.1.0'
```

## Usage
#### layout
```xml
<tw.lychee.swipecurvelayout.SwipeSectorLayout
      android:layout_width="match_parent"
      android:layout_height="300dp"
      android:id="@+id/container"
      app:item_width="80dp"
      app:degree="100"
      >
      <!--Custom Semi-circular -->
      <ImageView
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_alignParentBottom="true"
          android:layout_centerHorizontal="true"
          android:src="@drawable/circle"
          android:layout_marginBottom="-680dp"
          android:layout_marginLeft="-200dp"
          android:layout_marginRight="-200dp"
          />
</tw.lychee.swipecurvelayout.SwipeSectorLayout>
```

#### code
```java
SwipeSectorLayout swipeSectorLayout = (SwipeSectorLayout) findViewById(R.id.container);
swipeSectorLayout.setAdapter(new ImageAdapter() {
    @Override
    public void setItemView(int position, ImageView view) {

    }

    @Override
    public void setBackgroundView(int position, ImageView view) {

    }

    @Override
    public int getCount() {
        return 0;
    }
});

swipeSectorLayout.setOnPageChangeListener(new SwipeSectorLayout.OnPageChangeListener() {
    @Override
    public void onPageSelected(int position) {
        
    }
});

```


You can find complete demo code [here](https://github.com/lycheetw/SwipeSectorLayout/tree/master/app/src/main)
