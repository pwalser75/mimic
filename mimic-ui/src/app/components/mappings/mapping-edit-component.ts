import {Component, OnDestroy, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Mapping, MappingsService} from "../../services/mappings.service";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/delay';

@Component({
    selector: 'mapping-edit',
    templateUrl: 'mapping-edit.html'
})
export class MappingEditComponent implements OnInit, OnDestroy {

    private sub: any;
    mappingId: any;

    form: FormGroup;
    loaded: boolean = false;

    constructor(private formBuilder: FormBuilder, private mappingsService: MappingsService, private router: Router, private route: ActivatedRoute) {

    }

    ngOnInit(): void {

        this.form = this.formBuilder.group(
            {
                displayName: ['', Validators.compose(
                    [Validators.required, Validators.maxLength(512)]
                )],
                description: ['', Validators.compose(
                    [Validators.maxLength(2048)]
                )],
                method: ['', Validators.compose(
                    [Validators.required]
                )],
                path: ['', Validators.compose(
                    [Validators.required, Validators.maxLength(2048), Validators.pattern("(?:\/[^\\/]+)*")]
                )],
                script: ['', Validators.compose(
                    [Validators.required]
                )]
            }
        );

        this.sub = this.route.params
            .map(params => params['id'])
            .switchMap(id => this.mappingsService.get(id))
            .subscribe((mapping: Mapping) => {
                    this.mappingId = mapping.id;
                    this.loaded = true;
                    this.form.setValue({
                        displayName: mapping.displayName,
                        description: mapping.description,
                        path: mapping.path,
                        method: mapping.method,
                        script: mapping.script
                    });
                }, error => {
                    console.log("Failed to load mapping: " + error);
                }
            )
        ;
    }

    save(): void {
        var data = this.form.value;
        data.id = this.mappingId;
        console.log("UPDATE: " + JSON.stringify(data));
        this.mappingsService.save(data).then(result => {
            this.router.navigate(['/mappings']);
        });
    }

    cancel(): void {
        console.log("CANCELLED");
        this.router.navigate(['/mappings']);
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}