# MAB
MAB (Movable floating Action Button) library for Jetpack Compose. 
MAB — FAB that can actually float on the screen.

[Click here to learn how to use it](https://github.com/KovshefulCoder/MAB/tree/master#how-to-use)


## Example




## Introduction (and justification xD)

For me, almost all current FAB implementations are fooling you - their floating buttons are actually static; you can simply click them or expand to selection options like many other regular buttons.

To fight this injustice against FAB, I created this library to make it great again, to make it able to float on the screen and interact with other elements.

How exactly interact? MabController connected with MAB contains an index of the list’s elements below this MAB. If it still isn't clear, I hope [an example](https://github.com/KovshefulCoder/MAB/blob/master/app/src/main/java/com/kovsheful/mabexample/MainActivity.kt) will help.

## How to use?

### Setup

[Jitpack link](https://jitpack.io/#KovshefulCoder/MAB/1.0.0)

1. Set up your project settings.
Open "settings.gradle" and paste "maven { url 'https://jitpack.io' }" in the dependencyResolutionManagement section, as specified below:

settings.gradle
```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // DO NOT ADD IT HERE!!!
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } //Yes, right here
    }
}
rootProject.name = ""
include ''
```

2. Add the dependency inside "build.gradle"

```kotlin
dependencies {
            ...
	        implementation 'com.github.KovshefulCoder:MAB:1.0.0'
            ...
	}
```

3. Sync project and you ready to go!


### Code

1. Create MabController instance using rememberMabController()
    
    ```kotlin
    val mabController = rememberMabController()
    ```
    

2. Call MAB() composable and provide to it controller from step 1
    
    ```kotlin
    val mabController = rememberMabController()
    ...
    MAB(
      mabController = mabController
    ) 
    ```
    

Currently, I recommend placing it like general FABs are placed, at the right bottom angle. You can use Box with Modifier.align(Alignment = BottomEnd)

 (Yes, I didn't test it with Scaffold :( )

```kotlin
Box() {
    val mabController = rememberMabController()
    ...
    MAB(
        modifier = Modifier.align(Alignment.BottomEnd),
        mabController = mabController
    )
}
```

3. Nearby create LazyColumn (Why exactly it? [Explained here](https://github.com/KovshefulCoder/MAB/tree/master#what-next))
    
    ```kotlin
    Box() {
        val mabController = rememberMabController()
        LazyColumn()
        ...
        MAB(
            modifier = Modifier.align(Alignment.BottomEnd),
            mabController = mabController
        )
    }
    ```
    

4. Define the.onGloballyPositioned() modifier for the list itself and its elements (in this example Row) and call inside onListCoordinatesChanged and onListElementCoordinatesChanged, respectively, and provide them incoming coordinates with "it".
    
    ```kotlin
    Box() {
        val mabController = rememberMabController()
        LazyColumn(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                   mabController.onListElementCoordinatesChanged(
                       it, coordinates
                   )
            },
        ) {
            items(n) {
                Row (
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                   mabController.onListCoordinatesChanged(coordinates)
            },
                )
            }
        }
        ...
        MAB(
            modifier = Modifier.align(Alignment.BottomEnd),
            mabController = mabController
        )
    }
    ```
    

5. Feel free to collect indexOfElementBelow, which contains the index of the element above which MAB are placed right now, and finalIndex, which contains the index of the last element below MAB by the moment the user stopped dragging and removed their finger from the screen (the initial value for both is -1).
    
    ```kotlin
    mabController.value.indexOfElementBelow
    mabController.value.finalIndex
    ```
    

6. To reset finalIndex to its initial value, use the reserFinalIndex() method of the mabController instance.
    
    ```kotlin
    mabController.resetFinalIndex()
    ```
	
    

## What next?
Initially, I created MAB to interact with LazyColumn to drag it to the needed place to create new element of the list.

Because of this, I can guarantee that it will work only with LazyColumn and also when MAB and LazyColumn are in the same Box with BottomEnd alignment on the last.
(Such limitations surely are not because I am lazy azz and have little experience with testing in Compose)

But there is no solid barrier to try using it with, for instance, a regular Column or an inside Scaffold.
🔊Русское поле экспериментов🔊

:computer:
Happy coding and God Fix Graddle :metal:
:computer:



### Contacts

Contact me to send spam bots and battle GIFs cause this s👋t works incorrectly:

Discord: Kovshichek#5502

Or just use Issues lol
