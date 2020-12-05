# ShoeStore

ShoeStore project requirement for [Android App Development in Kotlin course on Udacity](https://www.udacity.com/course/developing-android-apps-with-kotlin--ud9012)

### Dependencies

```
com.github.bumptech.glide:glide:4.9.0

def room_version = "2.2.5"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    testImplementation "androidx.room:room-testing:$room_version"

implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"

androidx.lifecycle:lifecycle-livedata-ktx:2.2.0
androidx.navigation:navigation-fragment-ktx:2.3.1
androidx.datastore:datastore-preferences:1.0.0-alpha04
com.google.android.material:material:1.3.0-alpha04
androidx.lifecycle:lifecycle-extensions:2.2.0
androidx.core:core-ktx:1.3.2
```

### Installation

```
git clone https://github.com/Wakamoly/nd940-android-kotlin-course1-starter.git
```

## Project Instructions

[Project specific instructions](starter/README.md)

## Built With

* [Glide](https://github.com/bumptech/glide) - Glide for image loading, the only reason internet permissions have been made in the manifest
* [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) - Converting *List<String>* to JSON Array format for caching in Room 
* [AndroidX Room](https://developer.android.com/jetpack/androidx/releases/room) - Database persistence for storing and managing Shoe listings
* [AndroidX Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - Navigation across all screens in a single Activity
* [KotlinX Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Coroutines allows us to multitask without blocking the UI thread
* [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Our UserPreferences class for storing our key-value pairs of identity and logged-in state
