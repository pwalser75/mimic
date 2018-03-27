import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";

const baseURL = 'https://localhost/mimic/api';

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

    getMappings(): Promise<Mapping[]> {
        return new Promise((resolve, reject) => {
            return this.http.get(baseURL + '/mappings')
                .map((res: any) => res.json())
                .subscribe(
                    data => resolve(data),
                    error => reject(error)
                );
        });
    }

    getMapping(id: String): Promise<Mapping> {
        return new Promise((resolve, reject) => {
            return this.http.get(baseURL + '/mappings/' + id)
                .map((res: any) => res.json())
                .subscribe(
                    data => resolve(data),
                    error => reject(error)
                );
        });
    }
}