import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {filter, map, mergeMap} from "rxjs/operators";
import {MessagesService} from "./services/messages.service";

@Component({
    selector: 'app-component',
    templateUrl: 'application.html'
})
export class AppComponent implements OnInit {
    public isTestable: boolean = true;

    constructor(private router: Router,
                private activatedRoute: ActivatedRoute,
                private titleService: Title,
                private messagesService: MessagesService) {
    }

    ngOnInit(): void {
        this.router.events.pipe(
            map(() => this.activatedRoute),
            map(route => {
                while (route.firstChild) {
                    route = route.firstChild;
                }
                return route;
            }),
            filter(route => route.outlet === 'primary'),
            mergeMap(route => route.data),
            filter(event => event instanceof NavigationEnd)
        ).subscribe((event) => this.titleService.setTitle(event['title']));


        this.messagesService.events.subscribe(
            event => {
                if (event.message) {
                    console.log(event.message.toString());
                }
            }
        );
    }
}