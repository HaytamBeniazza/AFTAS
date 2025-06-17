import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Fish } from 'src/app/model/Fish';
import { FishService } from 'src/app/services/fish.service';

@Component({
  selector: 'app-fish-page',
  templateUrl: './fish-page.component.html',
  styleUrls: ['./fish-page.component.css']
})
export class FishPageComponent {
  fishList: Observable<Fish[]>;
  isImporting: Observable<boolean>;
  importProgress: Observable<string>;
  activeTab: string = 'list';

  constructor(private fishService: FishService) {
    this.fishList = this.fishService.fishs;
    this.isImporting = this.fishService.isImporting;
    this.importProgress = this.fishService.importProgress;
  }

  deleteFish(fishName: string): void {
    if (confirm('Are you sure you want to delete this fish?')) {
      this.fishService.delete(fishName);
    }
  }

  async importAllFishSpecies(): Promise<void> {
    const confirmed = confirm(
      '🌍 This will import ALL fish species from multiple global databases!\n\n' +
      '⚠️ This process may take several minutes and will add thousands of fish species.\n\n' +
      'Sources include:\n' +
      '• FishBase (Most comprehensive fish database)\n' +
      '• iNaturalist (Community observations)\n' +
      '• GBIF (Global biodiversity data)\n' +
      '• Encyclopedia of Life\n' +
      '• OBIS (Ocean biodiversity)\n\n' +
      'Are you sure you want to proceed?'
    );

    if (confirmed) {
      await this.fishService.importAllFishSpecies();
    }
  }

  getCurrentFishCount(): number {
    return this.fishService.fishs.getValue().length;
  }
}
