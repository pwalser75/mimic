import {Component} from "@angular/core";
import {CalendarEvent, GoogleCalendarService} from "../../services/google-calendar.service";
import {LoginEvent, LoginEventType, LoginService} from "../../services/login.service";
import {GoogleAuthService} from "../../services/google-auth.service";

@Component({
    templateUrl: 'calendar.html'
})
export class CalendarComponent {

    private events: Array<CalendarEvent>;

    constructor(private loginService: LoginService,
                private calendarService: GoogleCalendarService,
                public authService: GoogleAuthService) {

        if (!loginService.checkAuthenticatedRoute()) {
            return;
        }

        this.events = [];

        loginService.events.subscribe((event: LoginEvent) => {
            if (event.type === LoginEventType.LOGIN) {
                this.refresh();
            }
            if (event.type === LoginEventType.LOGOUT) {
                this.events = [];
            }
        });
        if (authService.isAuthenticated()) {
            this.refresh();
        }
    }

    public refresh() {
        this.calendarService.reload().then((appointmentsPerCalendar: any[]) => {
            var events = new Array();
            appointmentsPerCalendar.forEach(l => l.forEach((i: any) => events.push(i)));
            events.sort((a, b) => a.start < b.start ? -1 : (a.start > b.start ? 1 : 0));
            this.events = events;
        });
    }

    public getNextDays(): Date[] {
        var result: Date[] = new Array();
        for (var i = 0; i < 7; i++) {
            var d = new Date();
            d.setDate(d.getDate() + i);
            result.push(d);
        }
        return result;
    }

    public getEventsOn(date: Date): CalendarEvent[] {

        var from: Date = new Date(date.getFullYear(), date.getMonth(), date.getDate());
        var to: Date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1);

        var result: CalendarEvent[] = new Array();
        if (this.events) {
            this.events.forEach(a => {
                if (a.start >= from && a.start < to) {
                    result.push(a);
                }
            });
        }
        return result;
    }
}