import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompetitionPageComponent } from './pages/competition-page/competition-page.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { RegisterFormComponent } from './components/register-form/register-form.component';
import { LoggedInGuard } from './Guards/LoggedInGuard';
import { ManageMemberPageComponent } from './pages/manage-member-page/manage-member-page.component';
import { ManagerAuthGuard } from './Guards/ManagerAuthGuard';
import { AuthGuard } from './Guards/AuthGuard';
import { ErrorPageComponent } from './component/error-page/error-page.component';
import { IsAccountNoneGuard } from './Guards/IsAccountNoneGuard';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'error', component: ErrorPageComponent },
  { path: 'competition', component: CompetitionPageComponent, canActivate: [AuthGuard, IsAccountNoneGuard] },
  { path: 'member', component: CompetitionPageComponent },
  { path: 'login', component: LoginFormComponent, canActivate: [LoggedInGuard] },
  { path: 'signup', component: RegisterFormComponent, canActivate: [LoggedInGuard] },
  { path: 'managemember', component: ManageMemberPageComponent, canActivate: [ManagerAuthGuard, IsAccountNoneGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
