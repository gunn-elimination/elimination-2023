package net.gunn.elimination.routes.announcements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Announcement {
    @Id
    @GeneratedValue
    @JsonInclude
    public Long id;

    @Column
    public String title;
    @Column
    public String body;

    @Column
    public boolean active;

    @Column
    @JsonSerialize(using = DateSerializer.class)
    public Date startDate;
    @Column
    @JsonSerialize(using = DateSerializer.class)
    public Date endDate;

    @Override
    public String toString() {
        return """
            ## %s
            %s
            """.formatted(title, body);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    public Announcement() {}
    public Announcement (String title, String body, Date startDate, Date endDate, boolean active) {
        this.title = title;
        this.body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }
    
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getID() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean shouldDisplayToNonAdmins() {
        return active && startDate.before(new Date(System.currentTimeMillis())) && endDate.after(new Date(System.currentTimeMillis()));
    }
}
