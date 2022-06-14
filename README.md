# GymStatsProject

## Introduction
To open file, set gradle settings to Java 11

This is a copy of the strong android app.
Multiple plugins are used, material design for making a bottom sheet, room for databases, dependency injection with Hilt, charts from MPAndroidChart,
live data and sectioned recyclerview.

The application is fragment based, each fragment is loaded into the Main Actitivity container. 
There are also seperate activities for detail popup views. 
MVVM architecture is used, each fragment and activity has its own view model. Everything is almost fully functional
with the exception of the graphs which are partially built but have no database.

## Screenshots

Some screenshots showing how the app works.


<img src="https://user-images.githubusercontent.com/58133509/173678673-ff73a20c-82bd-4714-af0b-913cce293a54.png" width="270" height="585">

#### History fragment, previous finished workouts are loaded from the DB.

<img src="https://user-images.githubusercontent.com/58133509/173679024-72f8d522-4220-4573-b94d-ad53020e3c06.png" width="270" height="585">

#### Detailed view on finished workout, entered by clicking on the workout in the history fragment.

<img src="https://user-images.githubusercontent.com/58133509/173679204-831d5b35-e1f2-4345-bd45-c8df2a7d6efe.png" width="270" height="585">

#### Workouts fragment containing custom workout templates

<img src="https://user-images.githubusercontent.com/58133509/173679322-f6d7207a-a050-41e2-b948-8bbee317d318.png" width="270" height="585">

#### Weight graph. Graphing how weight changes over time


<img src="https://user-images.githubusercontent.com/58133509/173679543-8e1d3654-992d-4e3c-b591-6ffbc101250f.png" width="270" height="585">

#### Currently active workout, containing a timer and exercises with sets. Can choose different exercises, add new sets and finish and save the workout to history.
