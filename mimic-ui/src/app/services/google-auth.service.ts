import {Injectable} from "@angular/core";
import {LoginEvent, LoginEventType, LoginService} from "./login.service";

declare var gapi: any;

@Injectable()
export class GoogleAuthService {

    static clientId = '717388871185-4vnc6f1hl22dkbqm1nn6u78psmif8p1m.apps.googleusercontent.com';
    static apiKey = 'AIzaSyBIsyIvV_0QNfNualnDqB-k1toHt-RTYu4';
    static scopes = ['https://www.googleapis.com/auth/plus.me', 'https://www.googleapis.com/auth/calendar.readonly'];
    static logoutUrl = 'https://accounts.google.com/o/oauth2/revoke?token=';

    public userName: string;
    public userImageUrl: string;
    public userOccupation: string;
    public userLocation: string;

    constructor(private loginService: LoginService) {

        loginService.events.subscribe((event: LoginEvent) => {
            if (event.type === LoginEventType.LOGOUT) {
                this.logout();
            }
        });
    }


    public authenticated(): Promise<any> {
        if (this.isAuthenticated()) {
            return Promise.resolve();
        }
        return this.proceedAuthentication(false)
            .then(() => this.initializeGooglePlusAPI())
            .then(() => this.initializeGoogleCalendarAPI())
            .then(() => this.loadGooglePlusUserData())
            .then((response: any) => this.processUserResult(response.result))
            .catch((error: any) => {
                throw new Error("Authentication failed: " + JSON.stringify(error));
            });
    }

    logout() {
        this.setUserData(null, null);
        gapi.auth.signOut();
    }

    public isAuthenticated(): boolean {
        return this.userName != null;
    }

    private proceedAuthentication(immediate: boolean) {
        return new Promise((resolve, reject) => {
            gapi.client.setApiKey(GoogleAuthService.apiKey);
            var authorisationRequestData = {
                'client_id': GoogleAuthService.clientId,
                'scope': GoogleAuthService.scopes,
                'cookie_policy': 'single_host_origin',
                'immediate': immediate
            };
            gapi.auth.authorize(authorisationRequestData,
                (authenticationResult: any) => {
                    if (authenticationResult && !authenticationResult.error) {
                        this.setUserData('unknown', null);
                        resolve();
                    } else {
                        this.setUserData(null, null);
                        reject();
                    }
                }
            );
        });
    }

    private initializeGooglePlusAPI() {
        return new Promise((resolve, reject) => {
            resolve(gapi.client.load('plus', 'v1'));
        });
    }

    private initializeGoogleCalendarAPI() {
        return new Promise((resolve, reject) => {
            resolve(gapi.client.load('calendar', 'v3'));
        });
    }

    private loadGooglePlusUserData() {
        return new Promise((resolve, reject) => {
            resolve(gapi.client.plus.people.get({'userId': 'me'}));
        });
    }

    private processUserResult(result: any): void {
        this.userName = result.displayName;
        this.userImageUrl = result.image.url;
        if (this.userImageUrl) {
            this.userImageUrl = this.userImageUrl.split("?sz=50")[0] + "?sz=300";
        }
        this.userOccupation = result.occupation;
        var primaryLocation = result.placesLived.find((p: any) => p.primary);
        this.userLocation = primaryLocation ? primaryLocation.value : '';
    }

    private setUserData(userName: string, userImageUrl: string) {
        this.userName = userName;
        this.userImageUrl = userImageUrl;
    }
}