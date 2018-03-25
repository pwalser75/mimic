import {Component} from "@angular/core";
import {SearchService} from "../services/search.service";
import {MovieSearchResultList} from "../services/strategies/movie.search.result.list";

@Component({
    selector: 'quicksearch',
    entryComponents: [MovieSearchResultList],
    template: `
        <div class="input-widget" [class.show-dropdown]="hasSearchResults()" (focusin)="focusGained()"
             (focusout)="focusLost()">
            <div class="input">
                <input #query (keyup)="textChanged(query.value, $event)">
                <div class="input-widget-dropdown">

                    <movie-search-result-list [items]="searchResultsMap['movies']"></movie-search-result-list>
                    <hr>
                    <image-search-result-list [items]="searchResultsMap['images']"></image-search-result-list>
                </div>
            </div>
            <button class="input-widget-button">
                <i class="fa fa-search" aria-hidden="true"></i>
            </button>
        </div>`
})
export class QuickSearchComponent {

    private text: string;
    public searchResultsMap: any = {}; // map of search results: key = search strategy id, value = result list

    constructor(private searchService: SearchService) {

    }

    textChanged(value: string, event: any): void {
        if (event.keyCode == 27) {
            this.clear();
            return;
        }
        if (this.text == value.trim()) {
            // text hasn't changed
            return;
        }
        this.clear();
        this.text = value.trim();
        this.triggerSearch();
    }

    triggerSearch() {
        this.searchService.search(this.text).subscribe(result => {

            console.log("Got result :" + result.strategy.getId() + ": " + JSON.stringify(result.items));
            this.searchResultsMap[result.strategy.getId()] = result.items;
        });
    }

    clear(): void {
        // clear search results
        this.searchResultsMap = {};
    }

    focusLost(): void {
        setTimeout(() => {
            this.clear();
        }, 500);
    }

    focusGained(): void {
        this.triggerSearch();
    }

    hasSearchResults(): boolean {
        if (!this.searchResultsMap) {
            return false;
        }
        for (var strategy in this.searchResultsMap) {
            if (this.searchResultsMap[strategy] && this.searchResultsMap[strategy].length > 0) {
                return true;
            }
        }
        return false;
    }
}