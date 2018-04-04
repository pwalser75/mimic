import {Component, OnDestroy, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {MappingsService} from "../../services/mappings.service";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/delay';

@Component({
    selector: 'mapping-create',
    templateUrl: 'mapping-edit.html'
})
export class MappingCreateComponent implements OnInit {

    form: FormGroup;
    loaded: boolean = true;
    mappingId: any;

    constructor(private formBuilder: FormBuilder, private mappingsService: MappingsService, private router: Router,) {

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
                    [Validators.required, Validators.maxLength(2048), Validators.pattern("(?:\/[^\/]+)*")]
                )],
                script: ['', Validators.compose(
                    [Validators.required]
                )]
            }
        );

        this.form.setValue({
            displayName: '',
            description: '',
            method: '',
            path: '',
            script: '',
        });
    }

    save(): void {
        var data = this.form.value;
        console.log("CREATE: " + JSON.stringify(data));
        this.mappingsService.save(data).then(result => {
            this.router.navigate(['/mappings']);
        });
    }

    cancel(): void {
        console.log("CANCELLED");
        this.router.navigate(['/mappings']);
    }
}