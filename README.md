# evernote-client

<p align="center">
  <img src="https://media.giphy.com/media/me6D7yzBD8wlq/giphy.gif">
  <img src="https://media.giphy.com/media/l5qnZd43AZgdy/giphy.gif">
  <img src="https://media.giphy.com/media/BuDd9KnL62c6I/giphy.gif">
 </p>
 
<p align="center">
  <img src="https://media.giphy.com/media/qwqpx9UzUCLEA/giphy.gif">
  <img src="https://media.giphy.com/media/Nv7OQN3E4kisg/giphy.gif">
</p>


## Description

This is a small client for Evernote SDK.

The application must satisfy the following functional requirements:

- [X] Login screen
- [X] Main screen with a list showing user's notes.
- [X] A toolbar button for sorting the list by the title of the note and created/updated time.
- [X] A floating button to create new notes. For creating notes will be 2 options:
	* [X] Handwritten notes: a new screen for drawing will be presented to the user and then the image of the note will be processed by an OCR. The OCR result will be saved as the content of the note and the image will be attached to the created note.
	* [X] Normal text input notes
- [X] An activity to show the detail of the note (Not edit)
- [X] Logout of the application
- [X] Android permissions
- [ ] Test WritePadSDK
- [ ] Write tests

The initial propose for the application flow is displayed in the following diagram:

<p align="center">
  <img src="https://github.com/elloza/evernote-client/blob/master/diagrams/basic_flowchart.png" alt="Basic flowchart" title="Basic flowchart" />
</p>

## References and Notes

* The initial structure of the application will be generated with AndroidStarters application. The template followed is [MVP Android Starterts](https://github.com/androidstarters/android-starter). If the application grows, it could be interesting to add interactors.

* The Evernote SDK is very coupled to the Android Framework and in the Demo Sample the Authors uses a proper library called Android Tasks. I will try to isolate their client and use RXJava for execute async operations.

* The login in the Evernote platform will be implemented with the Sandbox account. For test this application please register first: [Evernote SandBox](https://sandbox.evernote.com/Registration.action)

* The Handwritten note is performed drawing in a custom view integrated with this library: [FreeDrawView](https://github.com/RiccardoMoro/FreeDrawView)

* The OCR library used in this application is [tess-two](https://github.com/rmtheis/tess-two). Good tutorial: [Tutorial](http://imperialsoup.com/2016/04/29/simple-ocr-android-app-using-tesseract-tutorial/)

* The results obtained with the OCR will be very poor. For developing a great OCR it will be necessary configure language and even train the OCR with the handwrite of the user.
