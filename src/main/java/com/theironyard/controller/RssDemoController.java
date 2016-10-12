package com.theironyard.controller;

import com.theironyard.service.RssDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class RssDemoController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RssDemoService rssDemoService;

//    @Autowired
//    public RssFeed rssFeed;


    @RequestMapping(path = "/")
    public String home(Model model, @PageableDefault(size = 30, sort = "publishedDate", direction = Sort.Direction.DESC) Pageable pageable){

        rssDemoService.loadFeeds();

        ArrayList<String> types = new ArrayList<String>();
            types.add("General");
            types.add("Domestic");
            types.add("World");
            types.add("Business");
        model.addAttribute("entries", rssDemoService.getPageOfEntries(types, pageable));

        return "home";
    }

//    @RequestMapping(path = "/", method = RequestMethod.GET)
//    public String listFeeds(Search search, Model model){
//
//        // list the rssFeed types we have
//        model.addAttribute("type", );
//
//        // get the widgets matching this search
//        List<RssFeed> rssFeeds = rssDemoService.listFeeds(search);
//        model.addAttribute("rssFeeds", rssFeeds);
//
//        return "home";
//    }
}