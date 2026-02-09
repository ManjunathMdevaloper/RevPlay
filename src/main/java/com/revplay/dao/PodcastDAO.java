package com.revplay.dao;

import java.util.List;
import com.revplay.model.Podcast;

public interface PodcastDAO {

    List<Podcast> searchPodcasts(String keyword);

    List<Podcast> getAllPodcasts();

    List<com.revplay.model.PodcastEpisode> getEpisodesByPodcast(int podcastId);

    com.revplay.model.PodcastEpisode getEpisodeById(int episodeId);

    void incrementEpisodePlayCount(int episodeId);

    void createPodcast(Podcast podcast);

    void addEpisode(com.revplay.model.PodcastEpisode episode);

    List<Podcast> getPodcastsByArtist(int artistId);

    boolean updatePodcast(Podcast podcast);

    boolean deletePodcast(int podcastId, int artistId);

    boolean updateEpisode(com.revplay.model.PodcastEpisode episode);

    boolean deleteEpisode(int episodeId);
}
