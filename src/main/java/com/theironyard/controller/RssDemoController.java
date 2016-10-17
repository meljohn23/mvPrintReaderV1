package com.theironyard.controller;

import com.theironyard.bean.Search;
import com.theironyard.service.RssDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class RssDemoController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RssDemoService rssDemoService;

//    @Autowired
//    public RssFeed rssFeed;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Search search, Model model, @PageableDefault(size = 60, sort = "publishedDate", direction = Sort.Direction.DESC) Pageable pageable){

        rssDemoService.loadFeeds();

        ArrayList<String> types = new ArrayList<String>();
        types.add("General");
        types.add("Domestic");
        types.add("World");
        types.add("Business");
        types.add("Sports");
        types.add("Technology");

        if (search.getTypes() == null) {
            search.setTypes(types);
        }
        String sqlKeyword = search.getKeyword().trim();
        if (sqlKeyword.length() > 0) {
            sqlKeyword = "%" + sqlKeyword + "%";
        }
        model.addAttribute("entries", rssDemoService.getPageOfEntries(search.getTypes(), pageable, sqlKeyword ));
        model.addAttribute("types", types);

        return "home";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String homePost(Search search, Model model, @PageableDefault(size = 60, sort = "publishedDate", direction = Sort.Direction.DESC) Pageable pageable){
        return home(search, model, pageable);
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