import {Component} from "@angular/core";
import {ApplicationService} from "../../services/application.service";
import {User} from "../../services/login.service";
import {Channel, MessagesService} from "../../services/messages.service";

@Component({
    selector: 'welcome',
    templateUrl: 'welcome.html'
})
export class WelcomeComponent {

    public title: string;
    public message: string;
    private log: Channel;

    constructor(private applicationService: ApplicationService, messageService: MessagesService) {
        this.log = messageService.createChannel("Application");
        this.title = "Angular Demo";
        this.message = "Hello from Angular";
    }

    public getUser(): User {
        return this.applicationService.getUser();
    }
}