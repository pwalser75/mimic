import {RouterModule, Routes} from "@angular/router";
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {MovieListComponent} from "./components/movie/movie-list.component";
import {MovieDetailComponent} from "./components/movie/movie-detail.component";
import {LoginComponent} from "./components/login/login.component";
import {ChangesComponent} from "./components/changes/changes-component";
import {CalendarComponent} from "./components/calendar/calendar.component";

const APP_NAME = "Angular2 Demo";

const ROUTES: Routes = [
    {
        path: '',
        redirectTo: '/welcome',
        pathMatch: 'full',
        data: {title: APP_NAME}
    },
    {
        path: 'welcome',
        component: WelcomeComponent,
        data: {title: APP_NAME + ' | Welcome'}
    },
    {
        path: 'login',
        component: LoginComponent,
        data: {title: APP_NAME + ' | Login'}
    },
    {
        path: 'movies',
        component: MovieListComponent,
        data: {title: APP_NAME + ' | Movies'}
    },
    {
        path: 'movie/:id',
        component: MovieDetailComponent,
        data: {title: APP_NAME + ' | Movie Detail'}
    },
    {
        path: 'changes',
        component: ChangesComponent,
        data: {title: APP_NAME + ' | Changes'}
    },
    {
        path: 'calendar',
        component: CalendarComponent,
        data: {title: APP_NAME + ' || Calendar'}
    }
];

export const ROUTING = RouterModule.forRoot(ROUTES, {useHash: true});

