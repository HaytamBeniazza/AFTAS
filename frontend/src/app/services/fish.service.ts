import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, forkJoin, of, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import {Fish} from '../model/Fish';
import { MyResponse } from '../model/MyResponse';
import { AlertService } from '../components/alerts/alert-service.service';
import { LevelService } from './level.service';

@Injectable({
  providedIn: 'root'
})
export class FishService {
  constructor(private http: HttpClient, private alertService: AlertService, private levelService: LevelService) {
    this.findAll();
  }
  public url = 'http://localhost:8080/fish';
  public fishs = new BehaviorSubject<Fish[]>([]);
  public pagination = new BehaviorSubject<MyResponse<Fish>>({} as MyResponse<Fish>);
  public isImporting = new BehaviorSubject<boolean>(false);
  public importProgress = new BehaviorSubject<string>('');

  public findAll(): void {
    this.http.get<Fish[]>(this.url).subscribe(
      (response) => {
        this.fishs.next(response);
      }
    );
  }

  public getFishPaginated(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.url}/paginated?page=${page}&size=${size}`);
  }

  public save(fish: Fish): void {
    // Convert Fish object to FishDtoReq format expected by backend
    const fishDto = {
      name: fish.name,
      averageWeight: fish.averageWeight,
      level_id: fish.level?.code || 1 // Use level code as level_id, default to 1 if missing
    };

    this.http.post<MyResponse<Fish>>(this.url, fishDto).subscribe(
      (response) => {
        this.fishs.next(this.fishs.getValue().concat(response.data));
        this.alertService.showMsg('Fish saved successfully');
      },
      (error) => {
        this.alertService.showMsg(error.error.message);
      }
    );
  }

  public delete(fishName: string): void {
    this.http.delete(`${this.url}/${fishName}`).subscribe(
      () => {
        const currentFish = this.fishs.getValue();
        const updatedFish = currentFish.filter(fish => fish.name !== fishName);
        this.fishs.next(updatedFish);
        this.alertService.showMsg('Fish deleted successfully');
      },
      (error) => {
        this.alertService.showMsg(error.error.message);
      }
    );
  }

  // 🌍 COMPREHENSIVE FISH IMPORT - Using Backend Service 🌍
  public async importAllFishSpecies(): Promise<void> {
    this.isImporting.next(true);
    this.importProgress.next('Starting comprehensive fish import...');

    try {
      this.importProgress.next('Calling backend import service...');

      const response = await this.http.post<any>(`${this.url}/import-all`, {}).toPromise();

      this.alertService.showMsg(`🎉 ${response.message}`);
      this.importProgress.next('Import completed successfully!');

      // Refresh the fish list
      this.findAll();

    } catch (error: any) {
      console.error('Import error:', error);
      this.alertService.showMsg('Error during fish import: ' + (error.error?.message || error.message));
      this.importProgress.next('Import failed. Please try again.');
    } finally {
      this.isImporting.next(false);
    }
  }

  // FishBase API - Most comprehensive fish database
  private async importFromFishBase(): Promise<any[]> {
    try {
      const batchSize = 100;
      const maxBatches = 50; // Get up to 5000 fish
      const allFish: any[] = [];

      for (let i = 0; i < maxBatches; i++) {
        const offset = i * batchSize;
        const response = await this.http.get<any[]>(
          `https://fishbase.ropensci.org/species?limit=${batchSize}&offset=${offset}`
        ).pipe(
          catchError(() => of([]))
        ).toPromise();

        if (!response || response.length === 0) break;
        allFish.push(...response);

        // Small delay to be respectful to the API
        await this.delay(100);
      }

      return allFish.map(fish => ({
        name: this.cleanFishName(fish.Species || fish.SpecCode || `Fish_${Math.random().toString(36).substr(2, 9)}`),
        averageWeight: this.parseWeight(fish.Weight) || this.estimateWeightFromLength(fish.Length),
        habitat: fish.DemersPelag || 'Unknown',
        maxLength: fish.Length || 0
      }));
    } catch (error) {
      console.error('FishBase import error:', error);
      return [];
    }
  }

  // iNaturalist API - Great for photos and observations
  private async importFromiNaturalist(): Promise<any[]> {
    try {
      const fishClasses = ['Actinopterygii', 'Chondrichthyes', 'Agnatha']; // Different fish classes
      const allFish: any[] = [];

      for (const fishClass of fishClasses) {
        const response = await this.http.get<any>(
          `https://api.inaturalist.org/v1/taxa?q=${fishClass}&rank=species&per_page=200&iconic_taxa=Actinopterygii`
        ).pipe(
          catchError(() => of({ results: [] }))
        ).toPromise();

        if (response?.results) {
          allFish.push(...response.results);
        }
        await this.delay(200);
      }

      return allFish.map(fish => ({
        name: this.cleanFishName(fish.name || fish.preferred_common_name || `iNat_${fish.id}`),
        averageWeight: this.estimateWeightFromTaxon(fish),
        habitat: 'Various',
        source: 'iNaturalist'
      }));
    } catch (error) {
      console.error('iNaturalist import error:', error);
      return [];
    }
  }

  // GBIF API - Global Biodiversity Information
  private async importFromGBIF(): Promise<any[]> {
    try {
      const fishOrders = ['Perciformes', 'Cypriniformes', 'Siluriformes', 'Salmoniformes', 'Gadiformes'];
      const allFish: any[] = [];

      for (const order of fishOrders) {
        const response = await this.http.get<any>(
          `https://api.gbif.org/v1/species/search?q=${order}&rank=SPECIES&limit=200&kingdom=Animalia&phylum=Chordata&class=Actinopterygii`
        ).pipe(
          catchError(() => of({ results: [] }))
        ).toPromise();

        if (response?.results) {
          allFish.push(...response.results);
        }
        await this.delay(150);
      }

      return allFish.map(fish => ({
        name: this.cleanFishName(fish.scientificName || fish.canonicalName || `GBIF_${fish.key}`),
        averageWeight: this.estimateWeightFromScientificName(fish.scientificName),
        habitat: 'Various',
        source: 'GBIF'
      }));
    } catch (error) {
      console.error('GBIF import error:', error);
      return [];
    }
  }

  // Encyclopedia of Life API
  private async importFromEOL(): Promise<any[]> {
    try {
      // EOL API is more complex, using search endpoint
      const fishKeywords = ['fish', 'salmon', 'tuna', 'cod', 'bass', 'trout', 'shark', 'ray'];
      const allFish: any[] = [];

      for (const keyword of fishKeywords) {
        try {
          const response = await this.http.get<any>(
            `https://eol.org/api/search/1.0.json?q=${keyword}&page=1&exact=false&filter_by_taxon_concept_id=&filter_by_hierarchy_entry_id=&filter_by_string=&cache_ttl=`
          ).pipe(
            catchError(() => of({ results: [] }))
          ).toPromise();

          if (response?.results) {
            allFish.push(...response.results.slice(0, 50)); // Limit per keyword
          }
          await this.delay(300);
        } catch (error) {
          console.log(`EOL search failed for ${keyword}:`, error);
        }
      }

      return allFish.map(fish => ({
        name: this.cleanFishName(fish.title || fish.content || `EOL_${Math.random().toString(36).substr(2, 9)}`),
        averageWeight: this.estimateWeightFromName(fish.title),
        habitat: 'Various',
        source: 'EOL'
      }));
    } catch (error) {
      console.error('EOL import error:', error);
      return [];
    }
  }

  // OBIS API - Ocean Biodiversity
  private async importFromOBIS(): Promise<any[]> {
    try {
      const response = await this.http.get<any>(
        `https://api.obis.org/v3/taxon?scientificname=Actinopterygii&size=500`
      ).pipe(
        catchError(() => of({ results: [] }))
      ).toPromise();

      if (!response?.results) return [];

      return response.results.map((fish: any) => ({
        name: this.cleanFishName(fish.scientificName || fish.acceptedNameUsage || `OBIS_${fish.AphiaID}`),
        averageWeight: this.estimateWeightFromScientificName(fish.scientificName),
        habitat: 'Marine',
        source: 'OBIS'
      }));
    } catch (error) {
      console.error('OBIS import error:', error);
      return [];
    }
  }

  // Utility methods
  private addUniquefish(allFish: Fish[], newFish: any[], fishNames: Set<string>, levels: any[]): void {
    newFish.forEach(fishData => {
      const cleanName = this.cleanFishName(fishData.name);
      if (!fishNames.has(cleanName) && cleanName.length > 2) {
        fishNames.add(cleanName);
        allFish.push({
          name: cleanName,
          averageWeight: fishData.averageWeight || this.generateRandomWeight(),
          level: this.assignLevelByWeight(fishData.averageWeight || this.generateRandomWeight(), levels)
        });
      }
    });
  }

  private cleanFishName(name: string): string {
    if (!name) return '';
    return name
      .replace(/[^a-zA-Z0-9\s-]/g, '') // Remove special characters
      .replace(/\s+/g, ' ') // Replace multiple spaces with single space
      .trim()
      .substring(0, 50); // Limit length
  }

  private parseWeight(weight: any): number {
    if (!weight) return 0;
    const parsed = parseFloat(weight.toString());
    return isNaN(parsed) ? 0 : Math.max(0.1, parsed);
  }

  private estimateWeightFromLength(length: any): number {
    if (!length) return this.generateRandomWeight();
    const lengthNum = parseFloat(length.toString());
    if (isNaN(lengthNum)) return this.generateRandomWeight();
    // Rough estimation: weight = length^3 * 0.1 (very approximate)
    return Math.max(0.1, Math.pow(lengthNum / 10, 3) * 0.1);
  }

  private estimateWeightFromTaxon(taxon: any): number {
    // Basic estimation based on taxon info
    return this.generateRandomWeight();
  }

  private estimateWeightFromScientificName(name: string): number {
    if (!name) return this.generateRandomWeight();
    // Basic heuristics based on common fish names
    const lowerName = name.toLowerCase();
    if (lowerName.includes('shark') || lowerName.includes('tuna')) return Math.random() * 100 + 20;
    if (lowerName.includes('salmon') || lowerName.includes('bass')) return Math.random() * 10 + 2;
    if (lowerName.includes('sardine') || lowerName.includes('anchovy')) return Math.random() * 0.5 + 0.1;
    return this.generateRandomWeight();
  }

  private estimateWeightFromName(name: string): number {
    return this.estimateWeightFromScientificName(name);
  }

  private generateRandomWeight(): number {
    // Generate realistic fish weights (0.1kg to 50kg, with most being smaller)
    const random = Math.random();
    if (random < 0.6) return Math.random() * 2 + 0.1; // 60% small fish (0.1-2kg)
    if (random < 0.9) return Math.random() * 10 + 2; // 30% medium fish (2-12kg)
    return Math.random() * 40 + 10; // 10% large fish (10-50kg)
  }

  private assignLevelByWeight(weight: number, levels: any[]): any {
    if (levels.length === 0) return { code: 1, description: 'Default', points: 10 };

    // Sort levels by points to assign appropriately
    const sortedLevels = levels.sort((a, b) => a.points - b.points);

    if (weight < 1) return sortedLevels[0] || levels[0];
    if (weight < 5) return sortedLevels[Math.floor(sortedLevels.length / 3)] || levels[0];
    if (weight < 15) return sortedLevels[Math.floor(sortedLevels.length * 2 / 3)] || levels[0];
    return sortedLevels[sortedLevels.length - 1] || levels[0];
  }

  private async batchSaveFish(fishArray: Fish[]): Promise<void> {
    const batchSize = 10; // Save in small batches to avoid overwhelming the server

    for (let i = 0; i < fishArray.length; i += batchSize) {
      const batch = fishArray.slice(i, i + batchSize);
      const savePromises = batch.map(fish => {
        // Convert Fish object to FishDtoReq format expected by backend
        const fishDto = {
          name: fish.name,
          averageWeight: fish.averageWeight,
          level_id: fish.level?.code || 1 // Use level code as level_id, default to 1 if missing
        };

        return this.http.post<MyResponse<Fish>>(this.url, fishDto).pipe(
          catchError(error => {
            console.log(`Failed to save fish ${fish.name}:`, error);
            return of(null);
          })
        ).toPromise();
      });

      await Promise.all(savePromises);
      this.importProgress.next(`Saved ${Math.min(i + batchSize, fishArray.length)} of ${fishArray.length} fish...`);

      // Small delay between batches
      await this.delay(100);
    }

    // Refresh the fish list
    this.findAll();
  }

  private delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}
