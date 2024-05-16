# Playlist API - BUILDING

### Technologies

- Java 8
- JUnit
- Mockito
- Docker
- Postgres
- Spring Boot

### Functionalities

#### Authentication context

- [x] Register as an user or musician;
- [x] RBAC (Role-based access control)
- [x] JWT Token Authentication strat;
- [x] Login as an user or musician;
- [x] Get profile as user;
- [x] Get profile as musician;
- [ ] Recover password receiving a new one on e-mail;
- [x] Update profile as user or musician.

#### User context

- [x] Create an playlist;
- [x] List all own playlists;
- [x] Get specific playlist;
- [x] List all musics on an album;
- [x] Fetch for an specific musician/album/music;
- [x] Fetch all albums from a musician;
- [x] Fetch one album by its id and name;
- [x] Fetch all albums by name;
- [x] Fetch all musics by name;
- [x] Fetch one music by id;
- [x] Insert musics on playlist and reorder;
- [x] Edit music order from playlist and handle re-order;
- [x] Remove musics from playlist and reorder;
- [x] Insert musics on favorite;
- [x] Remove musics from favorite;
- [ ] List favourite musics;
- [x] Fetch for others playlist;
- [x] Copy others playlist;
- [x] Export Albums to Excel
-

#### Musician context

- [x] Create an album;
- [x] Disable an album;
- [x] Update an album;
- [x] Get an album;
- [x] Get all own albums;
- [x] Register a new music;
- [x] Insert a new music on album;
- [x] Insert existing music on album and reorder;
- [x] Remove music from an album (may be a single).
- [x] Get an music;
- [x] Get all own musics;
- [x] Edit an music (name, category, duration, order)
- [x] Disable an music;
