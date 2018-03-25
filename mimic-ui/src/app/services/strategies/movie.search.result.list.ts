import {Component, Input} from "@angular/core";
import {MovieSearchResult} from "./movie.search.strategy";

@Component({
    selector: 'movie-search-result-list',
    template: `
        <div *ngIf="items == null" class="inset-base">
            <i class="fa fa-spinner fa-pulse fa-2x fa-fw"></i>
        </div>
        <ul class="list list-icons">
            <a href="javascript:void(0)" *ngFor="let movie of items" [routerLink]="['/movie', movie.id]">
                <li>
                    <div class="list-icon">
                        <img [src]="movie.image">
                    </div>
                    <span>
                        <div class="single-line"><b>{{movie.title}}</b> ({{movie.year}})</div>
                        <div class="single-line text-tiny">{{movie.genres | commaSeparated}}</div>
                    </span>
                </li>
            </a>
        </ul>`
})
export class MovieSearchResultList {

    @Input() items: MovieSearchResult[];
}