import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

const baseURL = environment.serverUrl + '/mimic/api';

export interface Mapping {
    id: number;
    displayName: string;
    description: string;
    method: string;
    path: string;
    script: string;
    createdAt: string;
    lastModifiedAt: string;
}

@Injectable()
export class MappingsService {

    constructor(private http: HttpClient) {

    }

    list(): Observable<Mapping[]> {
        return this.http.get<Mapping[]>(baseURL + '/mappings');
    }

    get(id: String): Observable<Mapping> {
        return this.http.get<Mapping>(baseURL + '/mappings/' + id);
    }

    save(mapping: Mapping): Observable<string> {
        if (!mapping.id) {
            //create
            return this.http.post<string>(baseURL + '/mappings', mapping);
        } else {
            //update
            return this.http.put<string>(baseURL + '/mappings/' + mapping.id, mapping);
        }

    }

    delete(id: String): Observable<string> {
        return this.http.delete<string>(baseURL + '/mappings/' + id);
    }
}