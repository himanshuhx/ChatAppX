# ChatAppX

A Social Networking Application built in Android Studio 3.6.3 with [Firebase Firestore](https://firebase.google.com/docs/firestore) Backend Support.


[![made-with-java](https://img.shields.io/badge/Made%20with-Java-1f425f.svg)](https://www.oracle.com/java/technologies/)
[![Code-quality-score](https://www.code-inspector.com/project/10479/score/svg)](https://frontend.code-inspector.com/public/project/10479/ChatAppX/dashboard) [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/himanshuhx/ChatAppX/graphs/commit-activity)  [![Open Source Love svg3](https://badges.frapsoft.com/os/v3/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

## Features of Application

- This Application uses Firebase Firestore Email Authentication as a SignUp method for users.
- Users can create a new account by registering through their Email Id.
- If a User has already SignedUp, he/she can login the app through SignIn page with their registered Email Id and password. 
- User can `set` and `update` their Username and new Email in Settings Activity of the Application.
- App has `Find Friends Activity` where user can look around for different people using the application.
- User can view different users profile and can send them `Friend Request` through `Add Friend` Button in user's profile activity.
- User also has the choice to cancel sent friend request later.
- User has the choice to Accept or Decline Friend Request received with `Accept Friend Request` and `Cancel Friend Request` Button.
- Request Sender is added automatically to the Contacts Activity of Current User if a user Accepts the friend request.
- User also has the choice to delete saved specific Contact from his/her Friends's List.
- User can chat with users present in his/her Contact List.

## Screenshots :camera:
<img src="https://user-images.githubusercontent.com/65825310/86353878-a00b7880-bc85-11ea-8926-d9af3b78fe20.png" width="250" height="450"> <img src="https://user-images.githubusercontent.com/65825310/86353921-b4e80c00-bc85-11ea-9593-e3a9f6c9cdfd.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86353978-d21cda80-bc85-11ea-8993-eff54a77c8be.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86354001-e4971400-bc85-11ea-84a3-51b941a2b728.png" width="250" height="450"> <img src="https://user-images.githubusercontent.com/65825310/86354026-ec56b880-bc85-11ea-8679-b3a2f2b97a68.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86354033-eeb91280-bc85-11ea-9a3d-392b5e5e1db4.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86354038-f2e53000-bc85-11ea-88a0-a66df0064601.png" width="250" height="450"> <img src="https://user-images.githubusercontent.com/65825310/86354075-055f6980-bc86-11ea-8162-a22c334f86a9.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86354095-098b8700-bc86-11ea-96f2-1cae6c656a80.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/65825310/86354092-08f2f080-bc86-11ea-805e-605fadcdbe98.png" width="250" height="450"> <img src="https://user-images.githubusercontent.com/65825310/86354101-0bede100-bc86-11ea-91fd-c134edfdbfa7.png" width="250" height="450">

## Extra Gradle Dependencies
   ```  Java
       implementation 'com.google.firebase:firebase-auth:19.3.1'
       implementation 'com.google.firebase:firebase-database:19.3.0'
       implementation 'com.google.firebase:firebase-storage:19.1.1'
       implementation 'com.google.firebase:firebase-firestore:21.4.3'
       implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'
       implementation 'com.firebaseui:firebase-ui-database:3.3.1'
       implementation 'androidx.multidex:multidex:2.0.1'
       implementation 'de.hdodenhof:circleimageview:3.1.0'
       implementation 'com.airbnb.android:lottie:3.4.0'
