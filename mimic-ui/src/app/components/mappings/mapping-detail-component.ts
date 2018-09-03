import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Mapping, MappingsService} from "../../services/mappings.service";
import 'codemirror/mode/javascript/javascript'
import {environment} from "../../../environments/environment";

@Component({
    selector: 'mapping-detail',
    templateUrl: 'mapping-detail.html'
})
export class MappingDetailComponent implements OnInit, OnDestroy {

    private sub: any;

    mapping: Mapping;
    config: any;
    content: String;
    error: String;

    constructor(private mappingsService: MappingsService, private route: ActivatedRoute, private titleService: Title) {
        /*
        this.config = {lineNumbers: true, mode: 'text/javascript'};
        this.content = `// ... some code !
package main

import "fmt"

// Send the sequence 2, 3, 4, ... to channel 'ch'.
func generate(ch chan<- int) {
	for i := 2; ; i++ {
		ch <- i  // Send 'i' to channel 'ch'
	}
}`
*/
    }

    ngOnInit(): void {
        this.sub = this.route.params.subscribe(params => {
            var id = params['id'];
            this.mappingsService.get(id).subscribe(
                data => {
                    this.mapping = data;
                    this.error = null;
                    this.titleService.setTitle(environment.appName + ' | ' + (this.mapping ? this.mapping.displayName : '-'));
                },
                error => {
                    this.mapping = null;
                    this.error = "Mapping not found: " + error;
                    console.log("could not load mapping #" + id);

                }
            );
        });
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}