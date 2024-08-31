## Diary security
This has been achieved using firebase authentication. You can authenticate using google account. This step is ommited in the video because of privacy reasons. Once logged in, the app will remember the user.

https://github.com/user-attachments/assets/5f619347-f31e-4177-ac7d-731775d733b2

## Viewing entries
All entries are stored in firestore database. Once user logs in, if there is not data, app will populate the diary with 5 entries. Entries consist of title, content, date, photo and location details. Photos are stored as uri in firestore and the actucal files that uri's point to are stored in firebase storage. There is no voice recording. Note the app asks for location and notification permissions on startup.

https://github.com/user-attachments/assets/f035deae-1190-4e5d-b5a0-00005ab03d09

## Creating Entries

https://github.com/user-attachments/assets/9b4381b8-a97e-4f32-85c4-3907bf0f8488

## Editing Entries

https://github.com/user-attachments/assets/88b6c5ce-393d-458d-9531-67ebb0270f0f

## Map

https://github.com/user-attachments/assets/c0c9ecb6-c822-484d-a4c9-ddf6fd587060

## Geofence and Nofitication

https://github.com/user-attachments/assets/1055f81d-15ba-47ff-9ce3-8420b2f74209

