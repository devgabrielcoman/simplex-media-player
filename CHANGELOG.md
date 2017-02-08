CHANGELOG
=========

1.0.4
 - Made the Simplex class implement View.OnClickListener; not only one method, "onClick(View v)", handles both the start and the play/pause button.
 - Created two private methods that resume / pause the video
 - Renamed the former "play(String diskPath)" method to "setMediaFile(String diskPath)" because it alone doesn't start playing, the UI does it.

1.0.3
 - Set the video player to not automatically play video content
 - Autoplay can now be set by the user
 - Added a main "start" button that appears in the center of the video player UI if the video player is not set to autoplay; the "start" button will disappear once the video start playing; 
 - Added better state management when pausing and switching orientation or leaving for another activity; now the video player respects the play / pause state and will return in the same state. 

1.0.2
 - Started work on the controller

1.0.1
 - Created the first draft of a functioning Video Player built on top of the standard android VideoView+MediaPlayer and presented as a Fragment

1.0.0
 - First release
