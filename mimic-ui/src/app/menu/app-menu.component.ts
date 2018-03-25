import {Component} from "@angular/core";
import {ApplicationService} from "../services/application.service";

@Component({
    selector: 'app-menu',
    templateUrl: 'app-menu.html'
})
export class AppMenuComponent {

    constructor(public applicationService: ApplicationService) {
    }
}

