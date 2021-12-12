# SmoothGraph
A custom smooth graph with animation.  
## Intall
    in progress...
## Sample of using
*in activity_main.xml*

      <app.vazovsky.smoothgraph.SmoothGraphView  
	      android:id="@+id/smoothGraphView" 
          android:layout_width="match_parent"  
	      android:layout_height="wrap_content"  
	      app:graph_countVisiblePoints="5"  
	      app:graph_lineColor="@color/teal_200"  
	      app:graph_lineWidth="4dp"  
	      app:graph_pointColor="@color/black"  
	      app:graph_pointRadius="4dp"  
	      app:graph_pointShow="true"  
	      app:graph_titleColor="@color/black"  
	      app:graph_titleShow="true"  
	      app:graph_titleSize="14sp"  
	      app:graph_valueColor="@color/purple_200"  
	      app:graph_valueShow="true"  
	      app:graph_valueSize="14sp" />
*in MainActivty.kt*  
     
    smoothGraphView.apply {  
      setData(list)  
      startAnim()  
    }
[![Demo SmoothGraph](https://psv4.userapi.com/c235031/u450021022/docs/d17/80717bd63969/video_2021-12-11_18-07-43.gif?extra=Rqqn_uw3w2CGTwXZrEYad3QX7VSxGtDExM1BMii6rAdVZ7q7lAdUQEbP0NWh90NRboQXk6NlVNc146rhztyH7o8UedpHfHIJzZFS9pHSYKPn40ZlKzG9l9fg0DsjsyGbE3-IfhVz7wPPKFAUjjTaai13yg)]
## Functions
|function|args|desc|
|--|--|--|
|setData|``data:List<Point>``|Setup list of points in SmoothGraph|
|startAnim||Start animation for SmoothGraph|

## Elements
|element|code|xml|
|--|--|--|
|point|pointColor|graph_pointColor|
||pointShow|graph_pointShow|
||pointSize|graph_pointRadius|
||countVisiblePoints|graph_countVisiblePoints|
|line|lineColor|graph_lineColor|
||lineWidth|graph_lineWidth|
|title|titleColor|graph_titleColor|
||titleShow|graph_titleShow|
||titleSize|graph_titleSize|
|value|valueColor|graph_valueColor|
||valueShow|graph_valueShow|
||valueSize|graph_valueSize|
