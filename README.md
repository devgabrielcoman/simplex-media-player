Simplex Media Player
====================

[![GitHub tag](https://img.shields.io/github/tag/devgabrielcoman/simplex-media-player.svg)]() [![GitHub contributors](https://img.shields.io/github/contributors/devgabrielcoman/simplex-media-player.svg)]() [![license](https://img.shields.io/github/license/devgabrielcoman/simplex-media-player.svg)]() [![Language](https://img.shields.io/badge/language-java-f48041.svg?style=flat)]() [![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg)]()

The Simplex Media Player is an easy to use custom player built on top of the standard Android Media Player class.
It's aim is to provide a safe and simple way of creating and interacting with a media player, without to much boilerplate code.

Download
--------

Integration
-----------

To add the media player to your activity directly in your layout:

```xml
<RelativeLayout
		android:id="@+id/PlayerHolder"
		android:layout_width="match_parent"
		android:layout_height="300dp">

		<fragment
				android:id="@+id/SimplexPlayer"
				android:name="com.gabrielcoman.simplexmediaplayer.Simplex"
				android:layout_width="match_parent"
				android:layout_height="match_parent"/>

</RelativeLayout>
```

Then in your activity source code:

```java

private Simplex mSimplex;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	// get the activity's fragment manager
	FragmentManager manager = getFragmentManager ();

	// and find the Simplex fragment defined in your XML layout
	mSimplex = (Simplex) manager.findFragmentById(R.id.SimplexPlayer);
}
```

As an alternative, if you don't want to add it in your layout XML file, you can create
a new Simplex instance like this:

```java
private static final String kSIMPLEX_PLAYER = "SIMPLEX_PLAYER";
private Simplex mSimplex;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	// get the activity's fragment manager
	FragmentManager manager = getFragmentManager ();

	// if the fragment doesn't yet exist (as in when the activity first gets
	// created) then start creating it
	if (manager.findFragmentByTag(kSIMPLEX_PLAYER) == null) {

			// create a new instance
			mSimplex = new Simplex();

			// and begin a Fragment transaction to add the mSimplex object to the
			// RelativeLayout defined by R.id.PlayerHolder
			manager.beginTransaction()
							.add(R.id.PlayerHolder, mSimplex, kSIMPLEX_PLAYER)
							.commitAllowingStateLoss();
	}
	// if the fragment does exist (after an activity orientation change, for
	// example) then just find it and assign it to the local mSimplex variable
	else {
			myPlayer = (Simplex) manager.findFragmentByTag(kSIMPLEX_PLAYER);
	}
}
```

Customize
---------

Play
----

Callbacks
---------

Todos
-----
