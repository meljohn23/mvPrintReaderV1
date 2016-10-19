package com.theironyard.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.theironyard.entity.RssEntry;
import com.theironyard.entity.RssFeed;
import com.theironyard.repository.RssEntryRepository;
import com.theironyard.repository.RssFeedRepository;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RssDemoService {

    @Autowired
    private RssEntryRepository rssEntryRepository;

    @Autowired
    private RssFeedRepository rssFeedRepository;

    private List<String> feedUrls = Arrays.asList(
//            "http://rss.nytimes.com/services/xml/rss/nyt/World.xml",
//            "http://rss.nytimes.com/services/xml/rss/nyt/US.xml",
//            "http://rss.nytimes.com/services/xml/rss/nyt/Business.xml",
//            "http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml",
//            "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml"
//            "http://www.wsj.com/xml/rss/3_8068.xml",
//            "http://www.wsj.com/xml/rss/3_7085.xml",
//            "http://www.wsj.com/xml/rss/3_7014.xml"
    );

    public void loadFeeds(){
        List<SyndEntry> entries = new ArrayList<>();

        // get all my feeds / entries
        for(String feedUrl : feedUrls) {
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));

                URL url = new URL(feedUrl);

                String favicon = url.getProtocol() + "://" + url.getHost() + "/favicon.ico";

                // create our rss feed entity
                RssFeed rssFeed = new RssFeed(
                        feedUrl,
                        feed.getTitle(),
                        feed.getDescription(),
                        feed.getImage() != null ? feed.getImage().getUrl() : null,
                        favicon, null);

                // save the feed
                rssFeedRepository.save(rssFeed);
            }
            catch (FeedException | IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<RssFeed> feeds = rssFeedRepository.findAll();
        for(RssFeed rssFeed: feeds) {
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(rssFeed.getUrl())));

                for (SyndEntry entry : feed.getEntries()) {

                    // get the first image (if any)
                    List<Element> images = entry.getForeignMarkup().stream().filter(element -> element.getNamespacePrefix().equals("media") && element.getName().equals("content")).collect(Collectors.toList());
                    String imageUrl = null;
                    if (images.size() > 0) {
                        imageUrl = images.get(0).getAttributeValue("url");
                    }

                    String briefDesc = entry.getDescription().getValue();
                    while(briefDesc.endsWith(">")) {
                        briefDesc = briefDesc.substring(0, briefDesc.lastIndexOf("<")).trim();
                    }

                    // create my entry entity
                    RssEntry rssEntry = new RssEntry(
                            entry.getUri(),
                            entry.getTitle(),
                            imageUrl,
                            briefDesc,
                            entry.getAuthor(),
                            entry.getPublishedDate(),
                            entry.getLink(),
                            rssFeed.getType(),
                            rssFeed
                    );

                    // save this entry to the DB
                    rssEntryRepository.save(rssEntry);
                }

            } catch (FeedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Page<RssEntry> getPageOfEntries(List<String> types, Pageable pageable, String keyword){
        Page<RssEntry> temp = rssEntryRepository.findByTypeInAndTitleNotLikeAndDescriptionNotLike(types, keyword, keyword , pageable);
        return temp;
    }
//
//    public List<RssFeed> listFeeds(Search search) {
//        return null;
//    }
}