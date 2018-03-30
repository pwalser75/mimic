import {Component, OnDestroy, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {MappingsService} from "../../services/mappings.service";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/delay';

@Component({
    selector: 'mapping-delete',
    template: ''
})
export class MappingDeleteComponent implements OnInit, OnDestroy {

    private sub: any;

    constructor(private mappingsService: MappingsService, private router: Router, private route: ActivatedRoute) {

    }

    ngOnInit(): void {

        this.sub = this.route.params
            .map(params => params['id'])
            .subscribe((id: string) => {
                    console.log("DELETE: " + id);
                    this.mappingsService.delete(id).then(result => {
                        this.router.navigate(['/mappings']);
                    });
                }, error => {
                    console.log("Failed to delete mapping: " + error);
                }
            )
        ;
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}