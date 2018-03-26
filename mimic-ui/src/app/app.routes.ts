import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./components/login/login.component";
import {MappingsComponent} from "./components/mappings/mappings-component";

const APP_NAME = "MIMIC Admin UI";

const ROUTES: Routes = [
    {
        path: '',
        redirectTo: '/mappings',
        pathMatch: 'full',
        data: {title: APP_NAME}
    },
    {
        path: 'login',
        component: LoginComponent,
        data: {title: APP_NAME + ' | Login'}
    },
    {
        path: 'mappings',
        component: MappingsComponent,
        data: {title: APP_NAME + ' | Mappings'}
    }
    /*   {
           path: 'movies',
           component: MovieListComponent,
           data: {title: APP_NAME + ' | Movies'}
       },
       {
           path: 'movie/:id',
           component: MovieDetailComponent,
           data: {title: APP_NAME + ' | Movie Detail'}
       },*/
];

export const ROUTING = RouterModule.forRoot(ROUTES, {useHash: true});

