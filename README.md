# Project Title

My solution for the alc simple android journal. The app can be able to add view and edit journal entry by users and also allow them to access the app authenticated via their google credentials.

## Getting Started

You can copy the project and run a build on android studio to be able to use an emulator or connected phone via ADB or just download the apk to use.

### Prerequisites

Aside internet and android studio and android studio emulator or an android device there is barely any needed setup to run this project.


### Installing

Git cloning this app or downloading the app and following the standard android app installation guied is sufficient.

## Running the tests

The automated test can be run on android studio using a connected device.

### Break down into end to end tests

The test tests the ui functionality and the database functionality

```
        //Load the first entry in the database by id
        JournalEntry jEntry = mDb.journalDao().loadJournalById(id);
        String name = jEntry.getTitle();

        //Test that it is actually the saved Journal that is returned
        assertTrue("Test Journal" == "Test Journal");
```

### And coding style tests

The test used the Esspresso ui test and junit for the database functionality test

```     //click on the update button
        onView(withId(R.id.update_button)).perform(click());
        
        //test that the update journal screen loads
        onView(withId(R.id.activity_update)).check(matches((isDisplayed())));
```

## Deployment

Download the apk from here and install

## Built With

* [Gradle]
* [Maven](https://maven.apache.org/) - Dependency Management


## Versioning

I use [Github](http://github.com/) for versioning. 

## Authors

* **Abd-afeez Abd-hamid** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)


## Acknowledgments

* Andela ALC Team
* The open source contributors to the packages i used
* and all tech enthusiast