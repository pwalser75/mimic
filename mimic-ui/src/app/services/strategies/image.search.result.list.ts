import {Component, Input} from "@angular/core";
import {ImageSearchResult} from "./images.search.strategy";

@Component({
    selector: 'image-search-result-list',
    template: `
        <div *ngIf="items == null" class="inset-base">
            <i class="fa fa-spinner fa-pulse fa-2x fa-fw"></i>
        </div>
        <div class="grid gutter inset-small" style="width: 400px; max-width: 100%">
            <div class="col-6" *ngFor="let image of items">
                <div class="card full-height card-primary material-shadow-1">
                    <header class="media aspect16to9">
                        <img class="image-contain" [src]="image.url">
                    </header>
                    <div class="content single-line text-small">
                        <b>{{image.name}}</b><br>
                        <span class="text-tiny">{{image.keywords}}</span>
                    </div>
                </div>
            </div>
        </div>`
})
export class ImageSearchResultList {

    @Input() items: ImageSearchResult[];
}
