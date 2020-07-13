package com.hrynyk.Controller;

import com.hrynyk.Entity.AnnouncementEntity;
import com.hrynyk.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AnnouncementController {

    @Autowired
    AnnouncementService service;

    /*
        Method which call main screen of Application, this screen contains list of announcements
     */
    @GetMapping
    public String mainScreen(Map<String, Object> model) {
        List<AnnouncementEntity> announcementEntities = service.getAll();
        List data = createListWithInfo(announcementEntities);
        model.put("announcements", data);
        return "main";
    }

    /*
        Method which call screen with details of chosen announcement from list.
     */
    @GetMapping("/announcement")
    public String announcementScreen(@RequestParam(value = "id", required = false) int id,
                                     Map<String, Object> model) {
        List<AnnouncementEntity> announcementEntities = new ArrayList<>();
        announcementEntities.add(service.getAnnouncementById(id));
        List mainData = createListWithInfo(announcementEntities);
        model.put("announcement", mainData);
        List similarData = createListWithInfo(service.findSimilarAnnouncement(id));
        model.put("similarAnnouncements", similarData);
        return "announcement";
    }

    /*
       Method which create new announcement.
    */
    @PostMapping("/createAnnouncement")
    public String createAnnouncement(@RequestParam String title, @RequestParam String description,
                                     @RequestParam String nick, Map<String, Object> model) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms").format(new Date());
        Timestamp dataAdded = Timestamp.valueOf(timeStamp);
        AnnouncementEntity announcement = service.createAnnouncement(title, description, nick, dataAdded);
        List<AnnouncementEntity> announcementEntities = new ArrayList<>();
        announcementEntities.add(announcement);
        List data = createListWithInfo(announcementEntities);
        model.put("announcement", data);

        return "announcement";
    }

    /*
           Method which delete announcement
        */
    @PostMapping("/deleteAnnouncement")
    public String deleteAnnouncement(@RequestParam(value = "id") int id, Map<String, Object> model) {
        service.deleteAnnouncement(id);
        List<AnnouncementEntity> announcementEntities = service.getAll();
        List data = createListWithInfo(announcementEntities);
        model.put("announcements", data);
        return "main";
    }

    /*
       Method which edit announcement
    */
    @PostMapping("/editAnnouncement")
    public String editAnnouncement(@RequestParam(value = "id") int id, @RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String nick, Map<String, Object> model) {

        if (service.editAnnouncement(id, title, description, nick)) {
            List<AnnouncementEntity> announcementEntities = new ArrayList<>();
            announcementEntities.add(service.getAnnouncementById(id));
            List data = createListWithInfo(announcementEntities);
            model.put("announcement", data);
            return "announcement";
        }
        List<AnnouncementEntity> announcementEntities = new ArrayList<>();
        announcementEntities.add(service.getAnnouncementById(id));
        List data = createListWithInfo(announcementEntities);
        model.put("announcement", data);
        return "announcement";
    }


    /*
       Method which help us to create map with all necessary data. Also designed to avoid duplication in the code.
       We need it to separate data by tags. Then we must input this into a list and finally create new map from the list
       which send data on the screen.
    */
    public Map<String, Object> createMapWithInfo(AnnouncementEntity announcement) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", announcement.getId());
        map.put("title", announcement.getTitle());
        map.put("description", announcement.getDescription());
        map.put("nickName", announcement.getNickName());
        map.put("dateAdded", announcement.getDateAdded());
        return map;
    }

    /*
      Method which help us to create list with all necessary data. Also designed to avoid duplication in the code
      Input map with data to the list.
   */
    public List<Object> createListWithInfo(List<AnnouncementEntity> announcementEntities) {
        List data = new ArrayList();
        for (int i = 0; i < announcementEntities.size(); i++) {
            AnnouncementEntity announcementEntity = announcementEntities.get(i);
            data.add(createMapWithInfo(announcementEntity));
        }
        return data;
    }
}
