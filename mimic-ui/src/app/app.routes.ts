import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./components/login/login.component";
import {MappingsListComponent} from "./components/mappings/mappings-list-component";
import {MappingDetailComponent} from "./components/mappings/mapping-detail-component";
import {environment} from '../environments/environment';
import {MappingEditComponent} from "./components/mappings/mapping-edit-component";

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
    },
    {
        path: 'mappings/edit/:id',
        component: MappingEditComponent,
        data: {title: TITLE + ' | Edit Mapping'}
    }
];

export const ROUTING = RouterModule.forRoot(ROUTES, {useHash: true});

