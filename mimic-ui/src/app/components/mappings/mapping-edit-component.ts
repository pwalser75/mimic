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

    mapping: Mapping;
    form: FormGroup;

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
                    [Validators.required, Validators.maxLength(2048)]
                )],
                script: ['', Validators.compose(
                    [Validators.required]
                )]
            }
        );

        this.sub = this.route.params
            .delay(500)
            .map(params => params['id'])
            .switchMap(id => this.mappingsService.getMapping(id))
            .subscribe((mapping: Mapping) => {
                this.mapping = mapping;
                this.form.setValue({
                    displayName: mapping.displayName,
                    description: mapping.description,
                    path: mapping.path,
                    method: mapping.method,
                    script: mapping.script
                });
            });
    }

    save(): void {
        var data = this.form.value;
        console.log("SAVE: " + JSON.stringify(data));

        // this.loginService.login(data.user, data.password);
        // this.router.navigate(['/welcome']);
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}