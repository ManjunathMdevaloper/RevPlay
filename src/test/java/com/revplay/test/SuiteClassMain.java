package com.revplay.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AlbumServiceTest.class,
        PlaylistServiceTest.class,
        PodcastServiceTest.class,
        SongServiceTest.class,
        UserServiceTest.class,
        BasicTest.class,
        GeneralModelTest.class,
        RevPlayAppMethodsTest.class,
        ArtistServiceTest.class,
        ArtistSongServiceTest.class,
        UserDAOTest.class,
        SongDAOTest.class,
        PodcastDAOTest.class,
        ArtistDAOTest.class,
        PlaylistDAOTest.class,
        AlbumDAOTest.class,
        ArtistSongDAOTest.class,
        FavoriteDAOTest.class,
        ListeningHistoryDAOTest.class,
//        DAOExceptionTest.class
})
public class SuiteClassMain {

}
