package com.company.planner.web.screens.session;

import com.haulmont.cuba.gui.screen.*;
import com.company.planner.entity.Session;

@UiController("planner_Session.browse")
@UiDescriptor("session-browse.xml")
@LookupComponent("sessionsTable")
@LoadDataBeforeShow
public class SessionBrowse extends StandardLookup<Session> {
}