package com.company.planner.service;

import com.company.planner.entity.Session;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service(SessionService.NAME)
public class SessionServiceBean implements SessionService {

    @Inject
    private DataManager dataManager;

    @Override
    public Session rescheduleSession(Session session, LocalDateTime newSessionStartDate) {
        LocalDateTime dayStart = newSessionStartDate.truncatedTo(ChronoUnit.DAYS).withHour(8);
        LocalDateTime dayEnd = newSessionStartDate.truncatedTo(ChronoUnit.DAYS).withHour(19);

        Long sessionsSameTime = dataManager.loadValue("select count(s) from planner_Session s where "+
                "(s.startDate between :dayStart and :dayEnd) " + // Текущий доклад состоится в рабочее время
                "and s.speaker = :speaker " +                   // Один и тот же спикер
                "and s.id <> :sessionId", Long.class)           // текущий id доклада <> новому ('<>' == '!=')
                .parameter("dayStart", dayStart)
                .parameter("dayEnd", dayEnd)
                .parameter("speaker", session.getSpeaker())
                .parameter("sessionId", session.getId())
                .one();
        if(sessionsSameTime >= 2) return session;

        session.setStartDate(newSessionStartDate);
        return dataManager.commit(session);
    }
}