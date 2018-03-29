import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./components/login/login.component";
import {MappingsListComponent} from "./components/mappings/mappings-list-component";
import {MappingDetailComponent} from "./components/mappings/mapping-detail-component";
import {environment} from '../environments/environment';

const TITLE = environment.appName;

const ROUTES: Routes = [
    {
        path: '',
        redirectTo: '/mappings',
        pathMatch: 'full',
        data: {title: TITLE}
    },
    {
        path: 'login',
        component: LoginComponent,
        data: {title: TITLE + ' | Login'}
    },
    {
        path: 'mappings',
        component: MappingsListComponent,
        data: {title: TITLE + ' | Mappings'}
    },
    {
        path: 'mappings/:id',
        component: MappingDetailComponent,
        data: {title: TITLE + ' | Mapping Detail'}
    }
];

export const ROUTING = RouterModule.forRoot(ROUTES, {useHash: true});

