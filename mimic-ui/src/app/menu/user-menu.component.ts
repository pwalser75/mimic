import {Component} from "@angular/core";
import {ApplicationService} from "../services/application.service";
import {LoginService} from "../services/login.service";

@Component({
    selector: 'user-menu',
    templateUrl: 'user-menu.html'
})
export class UserMenuComponent {

    constructor(public applicationService: ApplicationService, public loginService: LoginService) {
    }

}

