import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";

@Injectable()
export class GithubService {

    constructor(private http: Http) {
    }

    getCommits(githubUrl: string): Observable<Response> {

        return this.http.get(githubUrl + '/commits')
            .map((res: any) => res.json());
    }
}