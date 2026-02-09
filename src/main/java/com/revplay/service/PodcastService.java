package com.revplay.service;

import java.util.List;
import com.revplay.model.Podcast;

public interface PodcastService {

    List<Podcast> searchPodcasts(String keyword);

    List<Podcast> getAllPodcasts();

    List<com.revplay.model.PodcastEpisode> getEpisodesByPodcast(int podcastId);

    com.revplay.model.PodcastEpisode playEpisode(int userId, int episodeId);

    void createPodcast(Podcast podcast);

    void addEpisode(com.revplay.model.PodcastEpisode episode);

    List<Podcast> getPodcastsByArtist(int artistId);

    boolean updatePodcast(Podcast podcast);

    boolean deletePodcast(int podcastId, int artistId);

    boolean updateEpisode(com.revplay.model.PodcastEpisode episode);

    boolean deleteEpisode(int episodeId);
}
