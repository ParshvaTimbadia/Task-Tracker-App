# Task-Tracker-App (2022)
Split tasks amongst friends, roommates and more. Divide and conquer!

> Tools - Android Studio, Android SDKs, Firebase

## About

People living with roommates generally find that it is sometimes difficult to delegate chores/tasks to all those who reside in the house. Here is an app that could be a solution to this problem by having a fixed set of tasks for the week/month assigned to all members of a group by a coordinator. It’s up to you to take turns and be the coordinator or change the same over certain intervals to keep everyone involved. A common shared view of the task board comes in handy for quick reference of the remaining tasks. An apk of the app has also been included.

*Target audience:* Everyone. Although, people living with roommates or housemates would be able to better relate to the functionalities on offer. However, parents can also use this app to delegate certain tasks amongst their children or to have a common task board for the household for quick reference.

## Execution

Install Android Studio and clone the repository. Load the repository as an android project. The app would need to be connected to firebase. Authentication would have to be set up alongside an instance of realtime database. More information here https://firebase.google.com/docs/android/setup and replace the `google-services.json`, with your version of the same. Also, one would have to do a *clean and rebuild project* in Android Studio, to build the project again locally.

## App Screens

<div align="center">

| Splash Screen | Login Screen |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/aa90b62b-4dc9-4abd-a463-d8af4cac05c6" alt="Splash Screen" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/02cbc2ed-992b-4ae7-ab79-92aed390683d" alt="Login Screen" width="250" height="500"> |

| Register Screen | Sidebar Screen |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/e652f9bd-718f-49a5-9484-ac6f693d18d4" alt="Register Screen" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/c0fccd1c-2cc7-487a-88df-e31d3f41923a" alt="Sidebar Screen" width="250" height="500"> |

| Update Profile Screen | Progress Screen |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/17710702-daec-40a2-83fa-7ec44ccf6885" alt="Update Profile Screen" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/c185bd85-d4b7-4080-bda0-7be4021fc7c4" alt="Progress Screen" width="250" height="500"> |

| Create Board List | Create Task Screen |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/9697ba2f-06b7-46e8-b972-668c5008ccee" alt="Create Board List" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/400c1d68-f36b-4965-8863-3c8ff9600300" alt="Create Task Screen" width="250" height="500"> |

| Add Member to Task Screen | Task Created List |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/147a5014-b98d-407d-b657-e56369c314da" alt="Add Member to Task Screen" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/df1f79ef-854d-4d75-8752-be144e52cac9" alt="Task Created List" width="250" height="500"> |

| Update Task Screen | Notification Received |
|---------------|--------------|
| <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/d243af9d-962b-4e66-878e-b9d7fc3c3e36" alt="Update Task Screen" width="250" height="500"> | <img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/059c522b-86c4-447b-8279-198beeaa6874" alt="Image" width="250" height="500"> |

| Task Created List | Task Created List |
|---------------|--------------|
|<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/a6c96dcb-02e0-4981-967d-cd530fe8795b" alt="Image" width="250" height="500">|<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/2e071e8b-aceb-4d37-b6ef-4a1758329983" alt="Image" width="250" height="500">|

</div>

## Database Schema
Fields shown match the state of the app shown above in the screens, for more clarity

**Overview**
<p align="center">
<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/5db181b0-9d82-499d-9dca-4a9a8d53050d" alt="Image" width="400" height="300">
</p>

**Boards**
<p align="center">
<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/325028ae-b210-45ee-893c-3ca845d0c3ae" alt="Image" width="350" height="225">
</p>

**Tasks**
<p align="center">
<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/f944daf0-16d5-4075-a8ab-c72a4d3bd36f" alt="Image" width="400" height="250">
</p>

**Users**
<p align="center">
<img src="https://github.com/divitvasu/Task-Tracker-App/assets/30820920/93ef34f5-bba5-469a-8dcd-efa0b7ddf220" alt="Image" width="400" height="250">
</p>

## Future Scope
There could potentially be a few bugs, here and there, as this is not a production version of the app. But, we have made sure to do some basic testing of the functionalities on offer. Also, we did not focus a lot on the UI/UX for the time being and rather focussed on getting the functionalities to work. But, this could be improved in a future update. The user passwords could also be hashed and stored.
  
## Contributors
@ParshvaTimbadia @ShivaniBhalerao

## References and Resources
- [Connecting to Firebase](https://firebase.google.com/docs/android/setup)
- [Build Android App](https://developer.android.com/studio/run)
