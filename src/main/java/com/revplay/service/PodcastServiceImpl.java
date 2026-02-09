package com.revplay.service;

import java.util.List;

import com.revplay.dao.PodcastDAO;
import com.revplay.dao.PodcastDAOImpl;
import com.revplay.model.Podcast;

public class PodcastServiceImpl implements PodcastService {

    private PodcastDAO dao = new PodcastDAOImpl();

    @Override
    public List<Podcast> searchPodcasts(String keyword) {
        return dao.searchPodcasts(keyword);
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        return dao.getAllPodcasts();
    }

    @Override
    public List<com.revplay.model.PodcastEpisode> getEpisodesByPodcast(int podcastId) {
        return dao.getEpisodesByPodcast(podcastId);
    }

    @Override
    public com.revplay.model.PodcastEpisode playEpisode(int userId, int episodeId) {
        com.revplay.model.PodcastEpisode episode = dao.getEpisodeById(episodeId);
        if (episode != null) {
            dao.incrementEpisodePlayCount(episodeId);
        }
        return episode;
    }

    @Override
    public void createPodcast(Podcast podcast) {
        dao.createPodcast(podcast);
    }

    @Override
    public void addEpisode(com.revplay.model.PodcastEpisode episode) {
        dao.addEpisode(episode);
    }

    @Override
    public List<Podcast> getPodcastsByArtist(int artistId) {
        return dao.getPodcastsByArtist(artistId);
    }

    @Override
    public boolean updatePodcast(Podcast podcast) {
        return dao.updatePodcast(podcast);
    }

    @Override
    public boolean deletePodcast(int podcastId, int artistId) {
        return dao.deletePodcast(podcastId, artistId);
    }

    @Override
    public boolean updateEpisode(com.revplay.model.PodcastEpisode episode) {
        return dao.updateEpisode(episode);
    }

    @Override
    public boolean deleteEpisode(int episodeId) {
        return dao.deleteEpisode(episodeId);
    }
}
