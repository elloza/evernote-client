# Evernote-Client
A simple Evernote Client

<p align="center">
  <img src="http://g.recordit.co/7qpIRazdaP.gif" width="250">
</p>

## This project uses:
- [RxJava2](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Retrofit](http://square.github.io/retrofit/) / [OkHttp](http://square.github.io/okhttp/)
- [Gson](https://github.com/google/gson)
- [Dagger 2](http://google.github.io/dagger/)
- [Butterknife](https://github.com/JakeWharton/butterknife)
- [Google Play Services](https://developers.google.com/android/guides/overview)
- [Timber](https://github.com/JakeWharton/timber)
- [Glide 3](https://github.com/bumptech/glide)
- [Stetho](http://facebook.github.io/stetho/)
- [Espresso](https://google.github.io/android-testing-support-library/) for UI tests
- [Robolectric](http://robolectric.org/) for framework specific unit tests
- [Mockito](http://mockito.org/)
- [Checkstyle](http://checkstyle.sourceforge.net/), [PMD](https://pmd.github.io/) and [Findbugs](http://findbugs.sourceforge.net/) for code analysis


## Create new project using yeoman [generator-android-mvp-starter](https://github.com/androidstarters/generator-android-mvp-starter)
```bash
npm install -g yo
npm install -g generator-android-mvp-starter
mkdir NewApp && cd $_
yo android-mvp-starter
```

## Building

To build, install and run a debug version, run this from the root of the project:
```sh
./gradlew app:assembleDebug
```
    
## Testing

To run **unit** tests on your machine:

```sh
./gradlew test
```

To run **instrumentation** tests on connected devices:

```sh
./gradlew connectedAndroidTest
```

## Code Analysis tools

The following code analysis tools are set up on this project:

* [PMD](https://pmd.github.io/)

```sh
./gradlew pmd
```

* [Findbugs](http://findbugs.sourceforge.net/)

```sh
./gradlew findbugs
```

* [Checkstyle](http://checkstyle.sourceforge.net/)

```sh
./gradlew checkstyle
```

## The check task

To ensure that your code is valid and stable use check:

```sh
./gradlew check
```

## Jacoco Reports

#### Generate Jacoco coverage reports for the Debug build. Only unit tests.

```sh
app:testDebugUnitTestCoverage
```

#### Generate Jacoco coverage reports for the Release build. Only unit tests.

```sh
app:testReleaseUnitTestCoverage
```

#### Generate Jacoco coverage reports for the Debug build. Both unit and espresso tests.

```sh
app:unitAndEspressoDebugTestCoverage
```

#### Generate Jacoco coverage reports for the Release build. Both unit and espresso tests.

```sh
app:unitAndEspressoReleaseTestCoverage
```
