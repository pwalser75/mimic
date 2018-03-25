import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "../../services/login.service";

@Component({
    selector: 'login',
    templateUrl: 'login.html'
})
export class LoginComponent {

    form: FormGroup;

    constructor(fb: FormBuilder, private loginService: LoginService, private router: Router) {
        this.form = fb.group(
            {
                "user": ['', Validators.compose(
                    [Validators.required, Validators.minLength(3), Validators.maxLength(20),
                        Validators.pattern("[a-zA-Z][\\-_a-zA-Z0-9]+")]
                )],
                "password": ['', Validators.compose(
                    [Validators.required, Validators.minLength(6), Validators.maxLength(20)]
                )],
                "remember": ['']
            }
        );

    }

    login(): void {
        var data = this.form.value;
        this.loginService.login(data.user, data.password);
        this.router.navigate(['/welcome']);
    }
}