import {Component} from "@angular/core";
import {LoginService} from "../../services/login.service";

@Component({
    templateUrl: 'changes.html'
})
export class ChangesComponent {

    constructor(private loginService: LoginService) {
        if (!loginService.checkAuthenticatedRoute()) {
            return;
        }
    }
}