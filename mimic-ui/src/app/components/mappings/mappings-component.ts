import {Component, OnInit} from "@angular/core";
import {Mapping, MappingsService} from "../../services/mappings.service";
import {LoadingBarService} from "../../services/loading-bar.service";

@Component({
    selector: 'mimic-mappings',
    templateUrl: 'mappings.html'
})
export class MappingsComponent implements OnInit {

    mappings: Mapping[];

    constructor(private mappingsService: MappingsService, private loadingService: LoadingBarService) {
    }

    ngOnInit(): void {
        this.loadingService.start();
        this.mappingsService.getMappings().then(
            data => {
                this.mappings = data;
                this.loadingService.stop();
            },
            error => {
                console.log(error),
                    this.loadingService.stop();
            }
        );
    }
}