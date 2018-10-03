/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware.Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maxra
 */
@Entity
@Table(name = "reminder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reminder.findAll", query = "SELECT r FROM Reminder r")
    , @NamedQuery(name = "Reminder.findByReminderId", query = "SELECT r FROM Reminder r WHERE r.reminderId = :reminderId")
    , @NamedQuery(name = "Reminder.findByReminderDate", query = "SELECT r FROM Reminder r WHERE r.reminderDate = :reminderDate")
    , @NamedQuery(name = "Reminder.findBySnoozeIncrement", query = "SELECT r FROM Reminder r WHERE r.snoozeIncrement = :snoozeIncrement")
    , @NamedQuery(name = "Reminder.findBySnoozeIncrementTypeId", query = "SELECT r FROM Reminder r WHERE r.snoozeIncrementTypeId = :snoozeIncrementTypeId")
    , @NamedQuery(name = "Reminder.findByAppointmentId", query = "SELECT r FROM Reminder r WHERE r.appointmentId = :appointmentId")
    , @NamedQuery(name = "Reminder.findByCreatedBy", query = "SELECT r FROM Reminder r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "Reminder.findByCreatedDate", query = "SELECT r FROM Reminder r WHERE r.createdDate = :createdDate")
    , @NamedQuery(name = "Reminder.findByRemindercol", query = "SELECT r FROM Reminder r WHERE r.remindercol = :remindercol")})
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "reminderId")
    private Integer reminderId;
    @Basic(optional = false)
    @Column(name = "reminderDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminderDate;
    @Basic(optional = false)
    @Column(name = "snoozeIncrement")
    private int snoozeIncrement;
    @Basic(optional = false)
    @Column(name = "snoozeIncrementTypeId")
    private int snoozeIncrementTypeId;
    @Basic(optional = false)
    @Column(name = "appointmentId")
    private int appointmentId;
    @Basic(optional = false)
    @Column(name = "createdBy")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "remindercol")
    private String remindercol;

    public Reminder() {
    }

    public Reminder(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Reminder(Integer reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId, String createdBy, Date createdDate, String remindercol) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.remindercol = remindercol;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getSnoozeIncrement() {
        return snoozeIncrement;
    }

    public void setSnoozeIncrement(int snoozeIncrement) {
        this.snoozeIncrement = snoozeIncrement;
    }

    public int getSnoozeIncrementTypeId() {
        return snoozeIncrementTypeId;
    }

    public void setSnoozeIncrementTypeId(int snoozeIncrementTypeId) {
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemindercol() {
        return remindercol;
    }

    public void setRemindercol(String remindercol) {
        this.remindercol = remindercol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reminderId != null ? reminderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reminder)) {
            return false;
        }
        Reminder other = (Reminder) object;
        if ((this.reminderId == null && other.reminderId != null) || (this.reminderId != null && !this.reminderId.equals(other.reminderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedulingsoftware.Reminder[ reminderId=" + reminderId + " ]";
    }
    
}
