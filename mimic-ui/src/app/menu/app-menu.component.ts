import {Component} from "@angular/core";
import {ApplicationService} from "../services/application.service";
import {environment} from "../../environments/environment";

@Component({
    selector: 'app-menu',
    templateUrl: 'app-menu.html'
})
export class AppMenuComponent {

    environmentName: String = environment.name;

    constructor(public applicationService: ApplicationService) {
    }
}

