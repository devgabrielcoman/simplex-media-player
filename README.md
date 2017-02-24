Simplex Media Player
====================

[![GitHub tag](https://img.shields.io/github/tag/devgabrielcoman/simplex-media-player.svg)]() [![GitHub contributors](https://img.shields.io/github/contributors/devgabrielcoman/simplex-media-player.svg)]() [![license](https://img.shields.io/github/license/devgabrielcoman/simplex-media-player.svg)]() [![Language](https://img.shields.io/badge/language-java-f48041.svg?style=flat)]() [![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg)]()

The Simplex Media Player is an easy to use custom player built on top of the standard Android Media Player class.
It's aim is to provide a safe and simple way of creating and interacting with a media player, without to much boilerplate code.

Download
--------

To download the media player, use **Gradle**:

First add:

```javascript
repositories {
    maven {
        url  "http://dl.bintray.com/gabrielcoman/maven"
    }
}

```

to your list of repositories, then, in your dependency section, add:

```javascript
compile 'com.gabrielcoman.simplexmediaplayer:simplexmediaplayer:1.0.13'

```

Integration
-----------

To add the media player to your activity directly in your layout:

```xml
<RelativeLayout android:id="@+id/PlayerHolder"
		android:layout_width="match_parent"
		android:layout_height="300dp">

		<fragment android:id="@+id/SimplexPlayer"
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

You can define if you want the video to autostart:

```java
mSimplex.shouldAutoStart();

```

Or you can set the style of the player:

```java
mSimplex.setStyle (SimplexStyle.greenStyle());

```

The **SimplexStyle** class implements multiple factory methods such as:

* redStyle ()
* blueStyle ()
* greenStyle ()
* grayStyle ()

All of these methods change the player playback bar color.

If you want to implement your own custom style:

```java

SimplexStyle myStyle = new SimplexStyle ();

// sets the color of the length bar
myStyle.setLengthColor (0xff232323);
// sets the color of the buffer progress bar
myStyle.setBufferColor (0xff898989);
// sets the color of the playback bar and thumb
myStyle.setPlaybackColor (Color.BLUE);
// sets the color of the current time and total time text views
myStyle.setTextColor (Color.LTGRAY);
// sets the bitmap for the play button
myStyle.setPlayBitmap (Resources.customPlayButtonBitmap ());
// sets the bitmap for the replay button (shown when a video
// has ended playing)
myStyle.setReplayBitmap (Resources.customReplayButtonBitmap ());

// update the player style
mSimplex.setStyle (myStyle);

```

Finally, if you don't want to use the default Simplex controller, you can hide it:

```java
mSimplex.hideController ();

```

Play
----

Finally, once you've added your Simplex Media Player instance to your activity and
set it up as you like it, you can set either a remote media resource:

```java

String remoteMediaUrl = "https://myvideo.com/video.mp4";

// the player will set the media resource as a remote Url and
// will try to stream it
mSimplex.setMedia(remoteMediaUrl);

```

or a local file:

```java

String localMediaName = "video.mp4";

// create a File object.
// The media that you want to play must be in the Android files directory
File file = new File(context.getFilesDir(), localMediaName);

if (file.exists()) {

	// get the full file path
	String filePath = file.toString ();

	// the player will set the media resource as a file path and will try to
	// play it locally
	mSimplex.setMedia (filePath);
}

```

If you've setup the player to autostart using **mSimplex.shouldAutoStart()**, then once everything is ready, the  
media resource will start buffering and playing.
If not, the user may use the media player controller UI to start video playing.

Callbacks
---------

The Simplex Media Player provides an easy to use interface for receiving callback events:

```java

mSimplex.setListener(new Simplex.Listener() {
	@Override public void didStart() {
		// Player started playing current media
	}
	@Override public void didPause() {
		// Player paused current media
	}
	@Override public void didComplete() {
		// Player completed playing media
	}
	@Override public void didError() {
		// Player encountered an error
	}
	@Override public void didClose () {
		// Player closed
	}
	@Override public void didUpdateBuffer(float percent) {
		// Player got buffered data up to "percent"
	}
	@Override public void didUpdatePlayback(int hour, int minute, int second) {
		// Player played to time indicated by hour, minute and second
	}
});

```

Todos
-----
