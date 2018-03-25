import {Component} from "@angular/core";
import {MessagesService} from "../services/messages.service";

@Component({
    selector: 'messages-menu',
    templateUrl: 'messages-menu.html'
})
export class MessagesMenuComponent {

    constructor(public messagesService: MessagesService) {
    }

    public getNumberOfMessages(): number {
        let messages = this.messagesService.getMessages();
        return messages ? messages.length : 0;
    }
}