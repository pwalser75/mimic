import {Inject, Injectable, InjectionToken} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {version} from "punycode";

export interface SearchStrategy {

    getId(): string;

    search(query: string): Observable<Object[]>;
}

export const SearchStrategyToken = new InjectionToken<SearchStrategy[]>('SearchStrategy');

export const SEARCH_DELAY_MS = 250;

export class SearchResult {

    constructor(public strategy: SearchStrategy, public version: number, public items: Object[]) {
    }
}

@Injectable()
export class SearchService {

    private version: number = 0;

    private strategies: SearchStrategy[] = [];

    constructor(@Inject(SearchStrategyToken) searchStrategies: SearchStrategy[]) {
        this.strategies = searchStrategies;
        console.log("Discovered search strategies:");
        this.strategies.forEach(s => console.log(`- id=${s.getId()}, class=${s.constructor.name}`));
    }

    /*  Alternative using Injector:

        constructor(private injector: Injector) {
            this.strategies = this.injector.get(SearchStrategyToken);
            ...
        }
     */

    public search(query: string): Observable<SearchResult> {
        this.version++;

        return new Observable<SearchResult>(observer => {
            let queryVersion = this.version;

            // delay search
            setTimeout(() => {
                if (queryVersion == this.version) {
                    for (let strategy of this.strategies) {

                        if (query && query.trim()) {
                            strategy.search(query).subscribe(items => {
                                    if (queryVersion == this.version) {
                                        console.log("Got search result with version " + queryVersion);
                                        observer.next(new SearchResult(strategy, version, items));
                                    } else {
                                        // discard results, query was changed in the meantime.
                                    }
                                }
                            )
                        } else {
                            // empty search result
                            observer.next(new SearchResult(strategy, version, []));
                        }
                    }
                } else {
                    // don't search, query was changed in the meantime.
                }

            }, SEARCH_DELAY_MS);
        });
    }
}