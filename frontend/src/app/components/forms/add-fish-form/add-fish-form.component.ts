import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Level } from 'src/app/model/Level';
import { FishService } from 'src/app/services/fish.service';
import { LevelService } from 'src/app/services/level.service';

@Component({
  selector: 'app-add-fish-form',
  templateUrl: './add-fish-form.component.html',
  styleUrls: ['./add-fish-form.component.css']
})
export class AddFishFormComponent implements OnInit {
  fishForm: FormGroup;
  levels: Observable<Level[]>;

  constructor(
    private fb: FormBuilder,
    private fishService: FishService,
    private levelService: LevelService
  ) {
    this.fishForm = this.fb.group({
      name: ['', Validators.required],
      averageWeight: ['', [Validators.required, Validators.min(0)]],
      level_code: ['', Validators.required]
    });
    this.levels = this.levelService.levels;
  }

  ngOnInit(): void {
    // Ensure levels are loaded when component initializes
    if (this.levelService.levels.getValue().length === 0) {
      this.levelService.loadLevels();
    }
  }

  onSubmit(): void {
    if (this.fishForm.valid) {
      const selectedLevel = this.levelService.levels.getValue()
        .find(level => level.code == this.fishForm.value.level_code);

      const fishData = {
        name: this.fishForm.value.name,
        averageWeight: this.fishForm.value.averageWeight,
        level: selectedLevel || {
          code: parseInt(this.fishForm.value.level_code),
          description: 'Unknown',
          points: 0
        }
      }
      this.fishService.save(fishData);
      this.fishForm.reset();
    }
  }
}
