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
- [x] Login as an user or musician;
- [x] Get profile as user;
- [x] Get profile as musician;
- [ ] Recover password;
- [x] Update profile as user or musician.

#### User context

- [x] Create an playlist;
- [x] List all own playlists;
- [x] Get specific playlist;
- [x] List all musics on an album;
- [x] Fetch for an specific musician/album/music;
- [x] Fetch all albums from a musician;
- [x] Fetch all albums by name;
- [x] Fetch all musics by name;
- [ ] Insert musics on playlist;
- [ ] Remove musics from playlist;
- [ ] Insert musics on favorite;
- [ ] Remove musics from favorite;
- [ ] Fetch for others playlist;
- [ ] Copy others playlist;

#### Musician context

- [x] Create an album;
- [x] Disable an album;
- [x] Update an album;
- [x] Get an album;
- [x] Get all own albums;
- [x] Register a new music;
- [ ] Insert existing music on album;
- [x] Get an music;
- [x] Get all own musics;
- [ ] Edit an music (name, category, duration, album)
- [x] Disable an music;
- [ ] Remove music from an album (may be a single).