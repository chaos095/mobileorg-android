Setting up the build environment
================================

Prerequisites
-------------
* Java SDK
* Android SDK tools (SDK and build tools)
* (Optional) Android Studio

Make sure you have the most recent versions.

Android Studio Setup
--------------------
Select "Check out from Version Control" on the welcome page and insert
the url to the git repository. Use default settings and a project
should be checked out and built. Remember to configure the path to the
Android SDK.

Command Line Setup
------------------
If you do not want to use Android Studio (this is an Emacs project
anyways) or studio builds fail (The project is using an unsupported
version of the Android Gradle plug-in (0.7.3). The recommended version
is 1.2.2.), use the command line:
$ ./gradlew assembleRelease
Note that the keystore as configured in MobileOrg/build.gradle needs
to be located in MobileOrg/other.keystore.
The apk is created as MobileOrg/build/apk/MobileOrg-release.apk.

Notes regarding Dropbox
-----------------------
You will need an API key for the dropbox platform if you want to use
dropbox with your own build. Adding the app key to the Manifest.xml
file and inserting the correct keys into values/dropbox.xml will make
dropbox authentication work.


Notes regarding pull requests
-----------------------------
Make sure that any changes or additions that you make will also build from the
command line.  This is a requirement before changes will be accepted into the
mainline branch.


Notes regarding debian(-like) amd64 build hosts
-----------------------------------------------

You might need to install 32bits version of some libraries.
Depending of your system version, you could install the 'ia32-libs'
package, on a 'multiarch' you will need: 'lib32z1', 'libc6-i386' and
'lib32stdc++6'.
