import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {CodemirrorModule} from 'ng2-codemirror';
import 'codemirror';
import {ROUTING} from "./app.routes";
import {AppComponent} from "./app.component";
import {AppMenuComponent} from "./menu/app-menu.component";
import {LanguageMenuComponent} from "./menu/lang-menu.component";
import {UserMenuComponent} from "./menu/user-menu.component";
import {LoginComponent} from "./components/login/login.component";
import {ApplicationService} from "./services/application.service";
import {TranslateService} from "./services/translate.service";
import {CommaSeparatedPipe} from "./pipes/comma-separated.pipe";
import {TranslatePipe} from "./pipes/translate.pipe";
import {InputErrorsComponent} from "./widgets/input.errors.component";
import {InputComponent} from "./widgets/input-component.component";
import {MappingsService} from "./services/mappings.service";
import {MessagesService} from "./services/messages.service";
import {MessagesMenuComponent} from "./menu/messages-menu.component";
import {LimitPipe} from "./pipes/limit.pipe";
import {SaveURLPipe} from "./pipes/save-url.pipe";
import {LoginService} from "./services/login.service";
import {LoadingBarService} from "./services/loading-bar.service";
import {LoadingBarComponent} from "./widgets/loading-bar.component";
import {MappingsListComponent} from "./components/mappings/mappings-list-component";
import {MappingDetailComponent} from "./components/mappings/mapping-detail-component";
import {MappingEditComponent} from "./components/mappings/mapping-edit-component";
import {MappingCreateComponent} from "./components/mappings/mapping-create-component";
import {MappingDeleteComponent} from "./components/mappings/mapping-delete-component";


@NgModule({
    imports: [BrowserModule, HttpClientModule, ReactiveFormsModule, CodemirrorModule, ROUTING],
    declarations: [
        AppComponent,
        AppMenuComponent,
        LanguageMenuComponent,
        UserMenuComponent,
        MessagesMenuComponent,
        LoginComponent,
        MappingsListComponent,
        MappingDetailComponent,
        MappingCreateComponent,
        MappingDeleteComponent,
        MappingEditComponent,
        InputComponent,
        InputErrorsComponent,
        LoadingBarComponent,
        CommaSeparatedPipe,
        LimitPipe,
        TranslatePipe,
        SaveURLPipe,
    ],
    providers: [
        ApplicationService,
        MessagesService,
        TranslateService,
        MappingsService,
        LoginService,
        LoadingBarService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

