import {Component, OnDestroy, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {MappingsService} from "../../services/mappings.service";
import {delay, map, switchMap} from "rxjs/operators";

@Component({
    selector: 'mapping-delete',
    template: ''
})
export class MappingDeleteComponent implements OnInit, OnDestroy {

    private sub: any;

    constructor(private mappingsService: MappingsService, private router: Router, private route: ActivatedRoute) {

    }

    ngOnInit(): void {

        this.sub = this.route.params.pipe(
            map(params => params['id'])
        ).subscribe((id: string) => {
                console.log("DELETE: " + id);
                this.mappingsService.delete(id).subscribe(result => {
                    this.router.navigate(['/mappings']);
                });
            }, error => {
                console.log("Failed to delete mapping: " + error);
            }
        );
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}