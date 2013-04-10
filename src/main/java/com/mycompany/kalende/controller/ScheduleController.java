/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Jacob
 */
@ManagedBean(name="scheduleController")
@SessionScoped
public class ScheduleController implements Serializable {
    
    private ScheduleModel budgetModel;    
    private ScheduleModel trackingModel;

    public ScheduleEvent budgetEvent = new DefaultScheduleEvent();
    public ScheduleEvent trackingEvent = new DefaultScheduleEvent();

    private String theme;

    public ScheduleController() {
        
    }
    
    @PostConstruct
    public void init() {
        budgetModel = new DefaultScheduleModel();
             budgetModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
             budgetModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
        
        trackingModel = new DefaultScheduleModel();
            trackingModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
            trackingModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));
        
    }

    public Date getRandomDate(Date base) {
            Calendar date = Calendar.getInstance();
            date.setTime(base);
            date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);        //set random day of month
            return date.getTime();
    }

    public Date getInitialDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

            return calendar.getTime();
    }
    
    public ScheduleModel getBudgetModel() {
        return budgetModel;
    }
    
    public void setBudgetModel(ScheduleModel model) {
        this.budgetModel = model;
    }

    public ScheduleModel getTrackingModel() {
        return trackingModel;
    }

    public void setTrackingModel(ScheduleModel model) {
        this.trackingModel = model;
    }
    
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);

        return t.getTime();
    }

    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);

        return t.getTime();
    }

    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);

        return t.getTime();
    }

    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);

        return t.getTime();
    }

    private Date today6Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);

        return t.getTime();
    }

    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);

        return t.getTime();
    }

    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);

        return t.getTime();
    }

    private Date fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);

        return t.getTime();
    }

    public ScheduleEvent getTrackingEvent() {
        return trackingEvent;
    }

    public void setTrackingEvent(ScheduleEvent event) {
        this.trackingEvent = event;
    }
    
    public void addBudgetEvent(ActionEvent actionEvent) {
        if(budgetEvent.getId() == null)
            budgetModel.addEvent(budgetEvent);
        else
            budgetModel.updateEvent(budgetEvent);

        budgetEvent = new DefaultScheduleEvent();
    }

    public void addTrackingEvent(ActionEvent actionEvent) {
        if(trackingEvent.getId() == null)
            trackingModel.addEvent(trackingEvent);
        else
            trackingModel.updateEvent(trackingEvent);

        trackingEvent = new DefaultScheduleEvent();
    }

    public void onBudgetEventSelect(ScheduleEntrySelectEvent selectEvent) {
        budgetEvent = selectEvent.getScheduleEvent();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event Select", budgetEvent.toString());
        System.out.print(budgetEvent.toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }

    public void onBudgetDateSelect(MouseAdapter  event) {
        System.out.print(event.toString());
        
//        budgetEvent = new DefaultScheduleEvent("", event.getDate(), event.getDate());
//
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "+", budgetEvent.getStartDate().toString());
//        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onBudgetMouseDown(MouseEvent event) {
        System.out.print("Mouse Down!!!!");
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mouse Down", event.getPoint().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onBudgetEventMove(ScheduleEntryMoveEvent event) {
        System.out.print("Event Move");
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onBudgetEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}