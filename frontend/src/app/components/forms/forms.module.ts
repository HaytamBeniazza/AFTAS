import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddFishFormComponent } from './add-fish-form/add-fish-form.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AddFishFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    AddFishFormComponent
  ]
})
export class ComponentsFormsModule { }
