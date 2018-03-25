import {NgModule} from "@angular/core";
import {HttpModule} from "@angular/http";
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {AppMenuComponent} from "./menu/app-menu.component";
import {LanguageMenuComponent} from "./menu/lang-menu.component";
import {UserMenuComponent} from "./menu/user-menu.component";
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {LoginComponent} from "./components/login/login.component";
import {MovieListComponent} from "./components/movie/movie-list.component";
import {MovieDetailComponent} from "./components/movie/movie-detail.component";
import {RatingComponent} from "./widgets/rating.component";
import {ApplicationService} from "./services/application.service";
import {TranslateService} from "./services/translate.service";
import {MovieService} from "./services/movie.service";
import {CommaSeparatedPipe} from "./pipes/comma-separated.pipe";
import {TranslatePipe} from "./pipes/translate.pipe";
import {ROUTING} from "./app.routes";
import {InputErrorsComponent} from "./widgets/input.errors.component";
import {InputComponent} from "./widgets/input-component.component";
import {GithubService} from "./services/github.service";
import {ChangesComponent} from "./components/changes/changes-component";
import {GithubCommitsComponent} from "./components/changes/github-commits.component";
import {MessagesService} from "./services/messages.service";
import {MessagesMenuComponent} from "./menu/messages-menu.component";
import {LimitPipe} from "./pipes/limit.pipe";
import {SaveURLPipe} from "./pipes/save-url.pipe";
import {GoogleAuthService} from "./services/google-auth.service";
import {GoogleCalendarService} from "./services/google-calendar.service";
import {CalendarComponent} from "./components/calendar/calendar.component";
import {LoginService} from "./services/login.service";
import {LoadingBarService} from "./services/loading-bar.service";
import {LoadingBarComponent} from "./widgets/loading-bar.component";
import {QuickSearchComponent} from "./widgets/quicksearch";
import {SearchService, SearchStrategyToken} from "./services/search.service";
import {MovieSearchStrategy} from "./services/strategies/movie.search.strategy";
import {MovieSearchResultList} from "./services/strategies/movie.search.result.list";
import {ImageSearchResultList} from "./services/strategies/image.search.result.list";
import {ImagesSearchStrategy} from "./services/strategies/images.search.strategy";

@NgModule({
    imports: [BrowserModule, HttpModule, ReactiveFormsModule, ROUTING],
    declarations: [
        AppComponent,
        AppMenuComponent,
        LanguageMenuComponent,
        UserMenuComponent,
        MessagesMenuComponent,
        WelcomeComponent,
        LoginComponent,
        MovieListComponent,
        MovieDetailComponent,
        ChangesComponent,
        GithubCommitsComponent,
        CalendarComponent,
        InputComponent,
        InputErrorsComponent,
        RatingComponent,
        LoadingBarComponent,
        QuickSearchComponent,
        CommaSeparatedPipe,
        LimitPipe,
        TranslatePipe,
        SaveURLPipe,
        MovieSearchResultList,
        ImageSearchResultList
    ],
    providers: [
        ApplicationService,
        MessagesService,
        TranslateService,
        SearchService,
        MovieService,
        GithubService,
        GoogleAuthService,
        GoogleCalendarService,
        LoginService,
        LoadingBarService,
        {provide: SearchStrategyToken, useClass: MovieSearchStrategy, multi: true},
        {provide: SearchStrategyToken, useClass: ImagesSearchStrategy, multi: true},
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

