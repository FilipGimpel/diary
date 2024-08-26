2
Rules:

    In evaluating the project, apart from practical and substantive correctness, the quality and readability of the code you write will also be taken into account.
    The project should be implemented using native Android SDK tools in the Kotlin language variant.
    The graphical interface should be implemented using native Android Views or declaratively in Jetpack Compose.
    The project description includes sample assessment criteria. It should be noted that the final assessment is made by the evaluator.
    The project should include a video recording in *.mp4 format, showcasing the application's functionality.

Digital Diary

The task's goal is to create a mobile application that allows users to create and store personalized entries in written form, with the ability to add photos and voice recordings.
Functional Requirements:

    Creating Entries: The user can create entries containing text notes along with a location marker (based on the current location obtained, e.g., from GPS, the note will include the name of the place where the note was made [3p]). Additionally, each note can include a photo [1p] and a voice recording [2p], made when creating the note.
    Editing Entries: Each entry can be edited after its creation. [non-compliance with the criterion: -5p]
    Data Storage: All entries should be saved in an external database (e.g., Firebase or a custom API). [3p]
    Adding Captions to Photos: The user can add a text description directly (drawing on the image) to the photo before saving it in the entry. [2p]
    Viewing Entries: The user can view all entries in the application. [non-compliance with the criterion: -5p]
    Diary Security: The user launching the application must enter a password or PIN to access the diary's content in the application. [1p]
    Map: The application has a separate map screen with marked entries where they were made. [2p]
    The application should notify (notification in the status bar [2p]) when entering the range (approx. 1 km) of any place where one of the entries was made (Geofence mechanism or similar [3p]).

Technical Requirements:

    All literals should be placed in resources (res/values) so they can be translated into other languages [1p].
    Before submitting the project for evaluation, prepare a set of sample data loaded during the application's startup to demonstrate all functionalities. (ideally, place this data directly in the application's code) [non-compliance with the criterion: -2p].

