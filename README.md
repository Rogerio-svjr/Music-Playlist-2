# Music Playlist 2

This project started as a way to apply the concepts I learned in my studies and courses, but as I had more and more ideas for it, it turned into my main goal.

The application will be a simplified version of Spotify, allowing users to search for songs, artists and albums and create their own playlists.

The project's backend structure is divided into three layers: Repository, Service and Controller.

The Repository layer is responsible to manage the persistence of entities in the database, ensuring that their states are saved, updated and retrieved as needed. It uses the JPA Repository for this.

The Service layer handles HTTP requests to the Spotify API, as well as CRUD operations between the database and the application. With the spotify API, the application can access Spotify's entire portfolio of songs and albums, which can be saved to the database if the user wants to add them to a playlist.
Of course, the audio file is restricted to premium users only, so the application won't be able to actually play the songs, just show their information.

The Controller layer acts as an intermediary, handling incoming HTTP requests, processing them, and returning appropriate JSON responses. It acts as a dispatcher, directing traffic and delegating work to other layers like the service layer.

This project is a long-term goal, as I haven't fully mastered all the concepts needed for it's development yet and I'm still studying them as I go. The frontend is the next task I'll focus on after the backend is complete.