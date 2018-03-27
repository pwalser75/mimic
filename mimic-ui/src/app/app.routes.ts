import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./components/login/login.component";
import {MappingsListComponent} from "./components/mappings/mappings-list-component";
import {MappingDetailComponent} from "./components/mappings/mapping-detail-component";
import {APP_NAME} from './constants';

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
        component: MappingsListComponent,
        data: {title: APP_NAME + ' | Mappings'}
    },
    {
        path: 'mappings/:id',
        component: MappingDetailComponent,
        data: {title: APP_NAME + ' | Mapping Detail'}
    }
];

export const ROUTING = RouterModule.forRoot(ROUTES, {useHash: true});

