package com.hrynyk.Service;

import com.hrynyk.Entity.AnnouncementEntity;
import com.hrynyk.Repository.AnnouncementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnnouncementService {
    @Autowired
    AnnouncementRepo announcementRepo;

    /*
        Method which get all data from db.
     */
    public List<AnnouncementEntity> getAll() {
        List<AnnouncementEntity> announcementEntities = announcementRepo.getAllAnnouncement();
        return announcementEntities;
    }

    /*
        Method which get data by id.
     */
    public AnnouncementEntity getAnnouncementById(int id) {
        AnnouncementEntity announcement = announcementRepo.getById(id);
        return announcement;
    }

    /*
        Method which create new announcement.
     */
    public AnnouncementEntity createAnnouncement(String title, String description, String nick, Timestamp dataAdded) {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setTitle(title);
        announcementEntity.setDescription(description);
        announcementEntity.setNickName(nick);
        announcementEntity.setDateAdded(dataAdded);
        announcementRepo.save(announcementEntity);
        return announcementEntity;
    }

    /*
        Method which edit announcement.
     */
    public boolean editAnnouncement(int id, String title, String description, String nick) {
        if (checkData(id, title, description, nick)) {
            return true;
        }
        return false;
    }


    /*
       Method which designed to get top 3 announcement which similar to our chosen one.
    */
    public List<AnnouncementEntity> findSimilarAnnouncement(int id) {
        AnnouncementEntity main = getAnnouncementById(id);
        String[] mainTitle = main.getTitle().toLowerCase().split("\\s");
        String[] mainDescription = main.getDescription().toLowerCase().split("\\s");
        List<AnnouncementEntity> announcementEntities = getAll();
        List<AnnouncementEntity> similarAnnouncements = new ArrayList<>();
        List<Integer> amountOfSimilarWords = new ArrayList<>();
        checkForDuplicates(announcementEntities, id);
        for (int i = 0; i < announcementEntities.size(); i++) {
            AnnouncementEntity similar = announcementEntities.get(i);
            String[] similarTitle = similar.getTitle().toLowerCase().split("\\s");
            String[] similarDescription = similar.getDescription().toLowerCase().split("\\s");
            int resultCheckinTitle = checkTitle(mainTitle, similarTitle);
            int resultCheckingDescription = checkDescription(mainDescription, similarDescription);
            int sumResults = resultCheckingDescription + resultCheckinTitle;
            if (sumResults > 0) {
                similarAnnouncements.add(similar);
                amountOfSimilarWords.add(sumResults);
            }
        }
        announcementEntities.clear();
        if (similarAnnouncements.size() < 3) {
            return similarAnnouncements;
        } else {
            while (announcementEntities.size() < 3) {
                int indexOfElement = amountOfSimilarWords.indexOf(Collections.max(amountOfSimilarWords));
                announcementEntities.add(similarAnnouncements.get(indexOfElement));
                amountOfSimilarWords.set(indexOfElement, 0);
            }
        }
        return announcementEntities;
    }

    /*
        This method designed to delete an element from list which we used to compare with other objects in the same list.
     */
    private List<AnnouncementEntity> checkForDuplicates(List<AnnouncementEntity> announcementEntities, int id) {
        for (AnnouncementEntity a : announcementEntities) {
            if (a.getId() == id) {
                announcementEntities.remove(a);
                return announcementEntities;
            }
        }
        return announcementEntities;
    }

    /*
        Designed to find similar objects by counting the same words in description
     */
    public int checkDescription(String[] mainDescription, String[] similarDescription) {
        int limit = Math.min(mainDescription.length, similarDescription.length);
        int countWords = 0;
        for (int i = 0; i < limit; i++) {
            if (mainDescription[i].equals(similarDescription[i])) {
                countWords = countWords + 1;
            }
        }
        return countWords;
    }

    /*
        Designed to find similar objects by counting the same words in title
     */
    public int checkTitle(String[] mainTitle, String[] similarTitle) {
        int limit = Math.min(mainTitle.length, similarTitle.length);
        int countWords = 0;
        for (int i = 0; i < limit; i++) {
            if (mainTitle[i].equals(similarTitle[i])) {
                countWords = countWords + 1;
            }
        }
        return countWords;
    }

    /*
        Method which check data entered by user when trying to edit announcement. Also designed to save changed data.
        Thanks to this method we can edit announcement by changing title, description, nickname. Moreover we can change
        announcement by edited only one parameter.
     */
    public boolean checkData(int id, String title, String description, String nick) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms").format(new Date());
        Timestamp dataAdded = Timestamp.valueOf(timeStamp);
        if (!title.isEmpty() || !description.isEmpty() || !nick.isEmpty()) {
            if (title.isEmpty() && description.isEmpty()) {
                announcementRepo.changeNick(id, nick);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else if (title.isEmpty() && nick.isEmpty()) {
                announcementRepo.changeDescription(id, description);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else if (description.isEmpty() && nick.isEmpty()) {
                announcementRepo.changeTitle(id, title);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else if (title.isEmpty()) {
                announcementRepo.changeDescription(id, description);
                announcementRepo.changeNick(id, nick);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else if (description.isEmpty()) {
                announcementRepo.changeTitle(id, title);
                announcementRepo.changeNick(id, nick);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else if (nick.isEmpty()) {
                announcementRepo.changeTitle(id, title);
                announcementRepo.changeDescription(id, description);
                announcementRepo.changeData(id, dataAdded);
                return true;
            } else {
                announcementRepo.changeTitle(id, title);
                announcementRepo.changeDescription(id, description);
                announcementRepo.changeNick(id, nick);
                announcementRepo.changeData(id, dataAdded);
                return true;
            }
        }
        return false;
    }

    /*
        Method for deleting announcement.
     */
    public void deleteAnnouncement(int id) {
        announcementRepo.deleteById(id);
    }
}
