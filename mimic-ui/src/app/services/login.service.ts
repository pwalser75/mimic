import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";
import {Channel, MessagesService} from "./messages.service";
import {Router} from "@angular/router";


export interface User {
    name: string;
}

export enum LoginEventType {
    LOGIN,
    LOGOUT
}

export class LoginEvent {
    constructor(public type: LoginEventType, public user: User) {
    }
}

@Injectable()
export class LoginService {

    private user: User;
    private channel: Channel;

    private eventSource: Subject<LoginEvent> = new Subject<LoginEvent>();
    public events: Observable<LoginEvent> = this.eventSource.asObservable();

    constructor(messagesService: MessagesService, private router: Router) {
        this.channel = messagesService.createChannel("Login");
    }

    login(name: string, password: string): void {
        this.user = {
            name: name
        };
        this.emitEvent(new LoginEvent(LoginEventType.LOGIN, this.user));
        this.channel.success("User " + name + " logged in");
    }

    logout(): void {
        if (this.user) {
            this.user = null;
            this.emitEvent(new LoginEvent(LoginEventType.LOGOUT, null));
            this.channel.warning("User logged out");
        }
    }

    getUser(): User {
        return this.user;
    }

    // called by components of routes which require authentication: if not authenticated, reroute to welcome page.
    public checkAuthenticatedRoute(): boolean {
        if (!this.user) {
            this.channel.error("unauthorized access to " + this.router.url + ", redirecting...");
            this.router.navigate(['/welcome']);
            return false;
        }
        return true;
    }

    private emitEvent(event: LoginEvent) {
        if (this.eventSource && event) {
            this.eventSource.next(event);
        }
    }
}