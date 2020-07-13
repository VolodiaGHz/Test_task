package com.hrynyk.Repository;

import com.hrynyk.Entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface AnnouncementRepo extends JpaRepository<AnnouncementEntity, Long> {
    /*
        Get all data from db
     */
    @Query("select an from AnnouncementEntity an")
    List<AnnouncementEntity> getAllAnnouncement();

    /*
        Get all data by id
     */
    @Query("select an from AnnouncementEntity  an where an.id=?1")
    AnnouncementEntity getById(int id);

    /*
        delete data from db
     */
    @Transactional
    @Modifying
    @Query("delete from AnnouncementEntity where id = ?1")
    void deleteById(int id);

    /*
        Change date. Used when we edited our announcement
     */
    @Transactional
    @Modifying
    @Query("update AnnouncementEntity  set  dateAdded = ?2 where id =?1")
    void changeData(int id, Timestamp dataAdded);

    /*
        Change nickname. Used when we edited our announcement
     */
    @Transactional
    @Modifying
    @Query("update AnnouncementEntity  set  nickName = ?2 where id =?1")
    void changeNick(int id, String nick);

    /*
        Change description. Used when we edited our announcement
     */
    @Transactional
    @Modifying
    @Query("update AnnouncementEntity  set  description = ?2 where id =?1")
    void changeDescription(int id, String description);

    /*
        Change title. Used when we edited our announcement
     */
    @Transactional
    @Modifying
    @Query("update AnnouncementEntity  set  title = ?2 where id =?1")
    void changeTitle(int id, String title);

}
