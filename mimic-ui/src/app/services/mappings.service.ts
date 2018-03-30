import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
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

    constructor(private http: Http) {

    }

    list(): Promise<Mapping[]> {
        return new Promise((resolve, reject) => {
            return this.http.get(baseURL + '/mappings')
                .map((res: any) => res.json())
                .subscribe(
                    data => resolve(data),
                    error => reject(error)
                );
        });
    }

    get(id: String): Promise<Mapping> {
        return new Promise((resolve, reject) => {
            return this.http.get(baseURL + '/mappings/' + id)
                .map((res: any) => res.json())
                .subscribe(
                    data => resolve(data),
                    error => reject(error)
                );
        });
    }

    save(mapping: Mapping): Promise<string> {
        if (!mapping.id) {
            //create
            return new Promise((resolve, reject) => {
                return this.http.post(baseURL + '/mappings', mapping)
                    .map((res: any) => res.json())
                    .subscribe(
                        data => resolve(data),
                        error => reject(error)
                    );
            });
        } else {
            //update
            return new Promise((resolve, reject) => {
                return this.http.put(baseURL + '/mappings/' + mapping.id, mapping)
                    .map((res: any) => res.json())
                    .subscribe(
                        data => resolve(data),
                        error => reject(error)
                    );
            });
        }

    }

    delete(id: String): Promise<string> {
        return new Promise((resolve, reject) => {
            return this.http.delete(baseURL + '/mappings/' + id)
                .map((res: any) => res.json())
                .subscribe(
                    data => resolve(data),
                    error => reject(error)
                );
        });
    }
}