import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {SearchStrategy} from "../search.service";
import {Http} from "@angular/http";

export interface ImageSearchResult {
    name: string;
    keywords: string;
    url: string;
}

@Injectable()
export class ImagesSearchStrategy implements SearchStrategy {

    private limit: number = 4;

    constructor(private http: Http) {
    }

    getId(): string {
        return "images";
    }

    search(query: string): Observable<Object[]> {
        console.log("ImagesSearchStrategy: search for " + query);
        query = query.toLowerCase();

        return new Observable<Object[]>(observer => {

            setTimeout(() => {

                let resource: string = "data/image-search.json";

                new Promise<ImageSearchResult[]>((resolve, reject) => {
                    this.http.get(resource)
                        .map((res: any) => res.json()).subscribe(
                        data => resolve(data),
                        error => reject(error)
                    );
                }).then(
                    data => {
                        let matches: ImageSearchResult[] = data
                            .filter(q => this.matches(q, query));

                        // limit number of items
                        matches = matches.slice(0, Math.min(this.limit, matches.length));
                        observer.next(matches);
                    },
                    error => observer.error(error)
                );
            }, 700);
        });
    }

    private matches(image: any, query: string): boolean {
        if (!query || !image) {
            return false;
        }
        return query.split(" ")
            .every(term =>
                image.name.split(/[\s\-]+/).some(s => s.startsWith(term.toLowerCase())) || // by name
                image.keywords.split(/\s*,\s*/).some(s => s.startsWith(term.toLowerCase())) // by keyword
            );
    }
}