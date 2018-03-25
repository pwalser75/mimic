import {Injectable} from "@angular/core";
import {GoogleAuthService} from "./google-auth.service";

declare var gapi: any;

export interface Calendar {
    id: string;
    name: string;
    foregroundColor: string;
    backgroundColor: string;
}

export interface CalendarEvent {
    calendar: Calendar;
    subject: string;
    start: Date;
    end: Date;
}

@Injectable()
export class GoogleCalendarService {

    constructor(private authService: GoogleAuthService) {

    }

    public reload() {
        return this.authService.authenticated()
            .then(() => this.loadCalendarList())
            .then((calendars: Calendar[]) => {
                return Promise.all(calendars.map(c => this.loadEvents(c)));
            });
    }

    private loadCalendarList(): Promise<any> {
        return new Promise<any>((resolve, reject) => {

            var request = gapi.client.calendar.calendarList.list({
                'maxResults': 50
            });

            request.execute((response: any) => {
                var items = response.items;

                var result: Calendar[] = new Array();
                for (let item of items) {
                    result.push({
                        id: item.id,
                        name: item.summary,
                        foregroundColor: item.foregroundColor,
                        backgroundColor: item.backgroundColor
                    });
                }

                resolve(result);
            });
        });
    }

    private loadEvents(calendar: Calendar): Promise<any> {
        return new Promise<any>((resolve, reject) => {

            var oneWeekFromNow: Date = new Date();
            oneWeekFromNow.setDate(oneWeekFromNow.getDate() + 7);

            var request = gapi.client.calendar.events.list({
                'calendarId': calendar.id,
                'timeMin': new Date().toISOString(),
                'timeMax': oneWeekFromNow.toISOString(),
                'showDeleted': false,
                'singleEvents': true,
                'maxResults': 100,
                'orderBy': 'startTime'
            });

            request.execute((response: any) => {
                var items = response.items;

                var result: CalendarEvent[] = new Array();
                for (let item of items) {
                    result.push({
                        calendar: calendar,
                        start: new Date(item.start.dateTime ? item.start.dateTime : item.start.date + " 00:00:00"),
                        end: new Date(item.end.dateTime ? item.end.dateTime : item.end.date + " 23:59:59"),
                        subject: item.summary
                    });
                }

                resolve(result);
            });
        });
    }
}