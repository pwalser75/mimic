import {Component, OnInit} from "@angular/core";
import {Mapping, MappingsService} from "../../services/mappings.service";

@Component({
    selector: 'mappings-list',
    templateUrl: 'mappings-list.html'
})
export class MappingsListComponent implements OnInit {

    mappings: Mapping[];

    constructor(private mappingsService: MappingsService) {
    }

    ngOnInit(): void {
        this.mappingsService.list().then(
            data => {
                this.mappings = data;
            },
            error => {
                console.log(error);
            }
        );
    }
}