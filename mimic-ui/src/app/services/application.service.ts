import {Injectable} from "@angular/core";
import {LoginService, User} from "./login.service";
import {Channel} from "./messages.service";


@Injectable()
export class ApplicationService {

    private user: User;
    private log: Channel;

    constructor(private loginService: LoginService) {
        this.user = loginService.getUser();

        loginService.events.subscribe((event) => {
            this.user = event.user;
        });
    }

    public getUser(): User {
        return this.user;
    }

    public isLoggedIn(): boolean {
        return this.user != null;
    }
}