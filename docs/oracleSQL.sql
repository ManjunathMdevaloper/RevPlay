/* =========================================================
   RevPlay Database Setup Script
   Database  : Oracle (SQL Developer)
   Schema    : SYSTEM
   Purpose   : Console-based Music Streaming Application
   ========================================================= */


/* =========================================================
   1. USERS TABLE
   - Stores login details for Users and Artists
   - Base table for entire application
   ========================================================= */

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    user_id NUMBER PRIMARY KEY,
    name VARCHAR2(100),
    email VARCHAR2(100) UNIQUE,
    password VARCHAR2(100),
    role VARCHAR2(20), -- USER or ARTIST
    security_question VARCHAR2(200),
    security_answer VARCHAR2(100),
    password_hint VARCHAR2(100),
    created_at DATE DEFAULT SYSDATE
);

-- Insert fake users
INSERT INTO users VALUES (users_seq.NEXTVAL,'Rahul','rahul@gmail.com','r123','USER','Fav color?','Blue','color',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Ananya','ananya@gmail.com','a123','USER','Pet name?','Tom','pet',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Vikram','vikram@gmail.com','v123','USER','City?','Delhi','city',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Sneha','sneha@gmail.com','s123','USER','School?','DAV','school',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Kiran','kiran@gmail.com','k123','USER','Hero?','SRK','hero',SYSDATE);

INSERT INTO users VALUES (users_seq.NEXTVAL,'Arjun','arjun@gmail.com','ar123','ARTIST','Food?','Pizza','food',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Neha','neha@gmail.com','ne123','ARTIST','Movie?','Titanic','movie',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Ravi','ravi@gmail.com','ra123','ARTIST','Bike?','R15','bike',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Meera','meera@gmail.com','me123','ARTIST','Song?','Hello','song',SYSDATE);
INSERT INTO users VALUES (users_seq.NEXTVAL,'Amit','amit@gmail.com','am123','ARTIST','Place?','Goa','place',SYSDATE);

COMMIT;


/* =========================================================
   2. ARTISTS TABLE
   - Stores artist profile details
   - Linked to USERS table
   ========================================================= */

CREATE SEQUENCE artists_seq START WITH 101 INCREMENT BY 1;

CREATE TABLE artists (
    artist_id NUMBER PRIMARY KEY,
    user_id NUMBER REFERENCES users(user_id),
    bio VARCHAR2(200),
    genre VARCHAR2(50),
    social_links VARCHAR2(200)
);

-- Insert artists
INSERT INTO artists VALUES (artists_seq.NEXTVAL,6,'Indie singer','Pop','insta/arjun');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,7,'Melody queen','Melody','insta/neha');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,8,'Rock star','Rock','insta/ravi');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,9,'Soul voice','Classical','insta/meera');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,10,'EDM artist','EDM','insta/amit');

INSERT INTO artists VALUES (artists_seq.NEXTVAL,6,'Alt pop','Pop','yt/arjun');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,7,'Romantic','Romance','yt/neha');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,8,'Metal','Metal','yt/ravi');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,9,'Devotional','Bhajan','yt/meera');
INSERT INTO artists VALUES (artists_seq.NEXTVAL,10,'Tech beats','EDM','yt/amit');

COMMIT;


/* =========================================================
   3. ALBUMS TABLE
   - Albums created by artists
   ========================================================= */

CREATE SEQUENCE albums_seq START WITH 201 INCREMENT BY 1;

CREATE TABLE albums (
    album_id NUMBER PRIMARY KEY,
    title VARCHAR2(100),
    release_date DATE,
    artist_id NUMBER REFERENCES artists(artist_id)
);

-- Insert albums
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Midnight Vibes',SYSDATE,101);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Heart Beats',SYSDATE,102);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Rock Nation',SYSDATE,103);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Soul Touch',SYSDATE,104);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'EDM Blast',SYSDATE,105);

INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Love Notes',SYSDATE,101);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Peace',SYSDATE,102);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Metal Storm',SYSDATE,103);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Bhakti',SYSDATE,104);
INSERT INTO albums VALUES (albums_seq.NEXTVAL,'Night Drop',SYSDATE,105);

COMMIT;


/* =========================================================
   4. SONGS TABLE
   - Songs uploaded by artists
   ========================================================= */

CREATE SEQUENCE songs_seq START WITH 301 INCREMENT BY 1;

CREATE TABLE songs (
    song_id NUMBER PRIMARY KEY,
    title VARCHAR2(100),
    genre VARCHAR2(50),
    duration NUMBER,
    release_date DATE,
    play_count NUMBER DEFAULT 0,
    artist_id NUMBER REFERENCES artists(artist_id),
    album_id NUMBER REFERENCES albums(album_id)
);

-- Insert songs
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Dreamer','Pop',210,SYSDATE,120,101,201);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Night Walk','Pop',190,SYSDATE,95,101,206);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Heart Song','Melody',230,SYSDATE,150,102,202);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Love Tune','Romance',200,SYSDATE,180,102,207);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Rock On','Rock',250,SYSDATE,300,103,203);

INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Metal Fire','Metal',260,SYSDATE,210,103,208);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Peaceful','Classical',240,SYSDATE,80,104,204);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Divine','Bhajan',270,SYSDATE,60,104,209);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'EDM Drop','EDM',220,SYSDATE,400,105,205);
INSERT INTO songs VALUES (songs_seq.NEXTVAL,'Bass Night','EDM',230,SYSDATE,500,105,210);

COMMIT;


/* =========================================================
   5. PLAYLISTS TABLE
   ========================================================= */

CREATE SEQUENCE playlists_seq START WITH 401 INCREMENT BY 1;

CREATE TABLE playlists (
    playlist_id NUMBER PRIMARY KEY,
    name VARCHAR2(100),
    description VARCHAR2(200),
    is_public CHAR(1), -- Y or N
    user_id NUMBER REFERENCES users(user_id)
);

-- Insert playlists
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'My Hits','Top songs','Y',1);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Chill','Relax music','N',2);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Workout','Energy','Y',3);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Romantic','Love','Y',4);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Focus','Study','N',5);

INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Party','Dance','Y',1);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Sleep','Soft','N',2);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Drive','Road','Y',3);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Classic','Old','Y',4);
INSERT INTO playlists VALUES (playlists_seq.NEXTVAL,'Mood','Mixed','N',5);

COMMIT;


/* =========================================================
   6. PLAYLIST_SONGS (Junction Table)
   ========================================================= */

CREATE TABLE playlist_songs (
    playlist_id NUMBER REFERENCES playlists(playlist_id),
    song_id NUMBER REFERENCES songs(song_id),
    PRIMARY KEY (playlist_id, song_id)
);

INSERT INTO playlist_songs VALUES (401,301);
INSERT INTO playlist_songs VALUES (401,302);
INSERT INTO playlist_songs VALUES (402,303);
INSERT INTO playlist_songs VALUES (403,304);
INSERT INTO playlist_songs VALUES (404,305);
INSERT INTO playlist_songs VALUES (405,306);
INSERT INTO playlist_songs VALUES (406,307);
INSERT INTO playlist_songs VALUES (407,308);
INSERT INTO playlist_songs VALUES (408,309);
INSERT INTO playlist_songs VALUES (409,310);

COMMIT;


/* =========================================================
   7. FAVORITES TABLE
   ========================================================= */

CREATE TABLE favorites (
    user_id NUMBER REFERENCES users(user_id),
    song_id NUMBER REFERENCES songs(song_id),
    added_at DATE DEFAULT SYSDATE,
    PRIMARY KEY (user_id, song_id)
);

INSERT INTO favorites VALUES (1,301,SYSDATE);
INSERT INTO favorites VALUES (2,302,SYSDATE);
INSERT INTO favorites VALUES (3,303,SYSDATE);
INSERT INTO favorites VALUES (4,304,SYSDATE);
INSERT INTO favorites VALUES (5,305,SYSDATE);
INSERT INTO favorites VALUES (1,306,SYSDATE);
INSERT INTO favorites VALUES (2,307,SYSDATE);
INSERT INTO favorites VALUES (3,308,SYSDATE);
INSERT INTO favorites VALUES (4,309,SYSDATE);
INSERT INTO favorites VALUES (5,310,SYSDATE);

COMMIT;


/* =========================================================
   8. LISTENING_HISTORY (Songs)
   ========================================================= */

CREATE SEQUENCE history_seq START WITH 501 INCREMENT BY 1;

CREATE TABLE listening_history (
    history_id NUMBER PRIMARY KEY,
    user_id NUMBER REFERENCES users(user_id),
    song_id NUMBER REFERENCES songs(song_id),
    played_at DATE DEFAULT SYSDATE
);

INSERT INTO listening_history VALUES (history_seq.NEXTVAL,1,301,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,1,302,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,2,303,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,2,304,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,3,305,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,3,306,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,4,307,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,4,308,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,5,309,SYSDATE);
INSERT INTO listening_history VALUES (history_seq.NEXTVAL,5,310,SYSDATE);

COMMIT;


/* =========================================================
   9. PODCASTS
   ========================================================= */

CREATE SEQUENCE podcasts_seq START WITH 601 INCREMENT BY 1;

CREATE TABLE podcasts (
    podcast_id NUMBER PRIMARY KEY,
    title VARCHAR2(100),
    host_name VARCHAR2(100),
    category VARCHAR2(50),
    description VARCHAR2(200),
    created_at DATE DEFAULT SYSDATE
);

INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Tech Talks','Rahul Verma','Technology','Latest tech discussions',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Daily News','Anita Sharma','News','Daily news updates',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Health Hub','Dr Kumar','Health','Health tips',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Finance Guru','Amit Jain','Finance','Investment advice',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Career Guide','Neha Singh','Education','Career guidance',SYSDATE);

INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Startup Stories','Rohit Mehta','Business','Startup journeys',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Movie Mania','Suresh','Entertainment','Movie reviews',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Spiritual Talks','Swami Dev','Spiritual','Peaceful talks',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Travel Diaries','Kiran Rao','Travel','Travel stories',SYSDATE);
INSERT INTO podcasts VALUES (podcasts_seq.NEXTVAL,'Science World','Dr Arya','Science','Science explained',SYSDATE);

COMMIT;


/* =========================================================
   10. PODCAST_EPISODES
   ========================================================= */

CREATE SEQUENCE episodes_seq START WITH 701 INCREMENT BY 1;

CREATE TABLE podcast_episodes (
    episode_id NUMBER PRIMARY KEY,
    podcast_id NUMBER REFERENCES podcasts(podcast_id),
    title VARCHAR2(100),
    duration NUMBER,
    release_date DATE,
    play_count NUMBER DEFAULT 0
);

INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,601,'AI Future',1800,SYSDATE,120);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,601,'Cyber Security',1700,SYSDATE,90);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,602,'Morning Headlines',1200,SYSDATE,300);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,603,'Healthy Life',1500,SYSDATE,200);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,604,'Stock Basics',1600,SYSDATE,180);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,605,'Resume Tips',1400,SYSDATE,160);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,606,'Startup Failures',1900,SYSDATE,140);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,607,'Top Movies',1300,SYSDATE,220);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,608,'Meditation',2000,SYSDATE,100);
INSERT INTO podcast_episodes VALUES (episodes_seq.NEXTVAL,609,'Europe Trip',1800,SYSDATE,110);

COMMIT;