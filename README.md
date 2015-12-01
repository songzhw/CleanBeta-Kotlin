This is an Android sample project.

## What is included in this project?

The project's skeleton is Kotlin , RxJava and RxAndroid. Kotlin is a new programming lauguage that runs on the java virtual machine. Kotlin is designed to interoerate with Java code and is reliant on Java code from the existing jar or aar.   RxJava is a library for composing asynchronous and event-based programs using observable sequences for the Java VM.
 * Kotlin
 * RxJava
 * RxAndroid
<p><p>

The third-party libraries used in this project includes:
 * retrofit : for network accessing
 * zxing : and JourneyApps-Zxing for simplizing it.
 * UniversalImageLoader 
<p><p>


I import JUnit and Espresso for test (Unit test, and UI test):
 * Junit
 * Espreeso
<p><p>

I also want to use NDK in this Android project written by Kotlin. So this project also is a example of how kotlin use NDK in Android project.
 * NDK
<p><p>

To design a clean, testable, reusable and expandable project, I also import MVP pattern. MVP is a user interface architectural pattern engineered to facilitate automated unit testing and improve the separation of concerns in presentation logic.
 * MVP
<p><p>

  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

## Decompose this project

This project has many parts , and Each part is designed to show some certain features of kotlin/RxJava/NDK/...

Here is a list :

#### Splash Page / Home Page
* how to apply MVP
* how to use RxAndroid 0.25
* how to use RxJava
* how to use Retrofit in a kotlin project
* How to use ZXing in a kotlin project


#### Available Payments Page
* how to apply MVP to this more complex example

#### Portrait Detail Page
* how to use onActivityResult() in a kotlin project
* how to access file in a RxJava project

#### Unlock Page
* how to use java-style custom view in a kotlin project

#### WebView Page
* how to new another process to a android project  </br>
( songzhw: multi-process may have some problems you have to deal with)

#### TestRx Page
* the typical occasions that have better use RxJava/RxAndroid
    * Download a picture : Memory -> Disk -> Server
    * Need to access more than one api of server
    * Need to access one API first, then access the second API
    * avoid the many click event in a short time

#### JniUtil
* how to apply NDK to a kotlin project

#### Kotlin
* how to expand the existing methods/attributes
* how to use Kotlin expansion for Andorid