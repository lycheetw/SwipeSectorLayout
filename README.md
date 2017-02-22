# SwipeSectorLayout
The inspiration for this project comes from [Dima Panchenko's post on Dribbble](https://dribbble.com/shots/3272140-Jewelry-E-ommerce-Application).

##Usage
layout
```xml
<tw.lychee.swipecurvelayout.SwipeSectorLayout
  android:layout_width="match_parent"
  android:layout_height="300dp"
  android:id="@+id/container"
  app:item_width="80dp"
  app:sector_inner_degree="100"
  app:sector_outer_degree="60"
  app:sector_height="70dp"
  />
```

code
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
## Screenshots
![alt tag](https://raw.githubusercontent.com/lycheetw/SwipeSectorLayout/master/images/screenshot.gif)
