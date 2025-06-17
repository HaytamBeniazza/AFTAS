import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Level } from '../model/Level';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LevelService {
  public url = 'http://localhost:8080/level';
  public levels = new BehaviorSubject<Level[]>([]);

  constructor(private http: HttpClient) {
    this.loadLevels();
   }

  public loadLevels(): void {
    this.http.get<Level[]>(this.url).pipe(
      catchError(error => {
        console.warn('Could not load levels from backend, creating default levels:', error);
        // Create default levels if backend is not available
        const defaultLevels: Level[] = [
          { code: 1, description: 'Small Fish', points: 10 },
          { code: 2, description: 'Medium Fish', points: 25 },
          { code: 3, description: 'Large Fish', points: 50 },
          { code: 4, description: 'Very Large Fish', points: 100 },
          { code: 5, description: 'Giant Fish', points: 200 }
        ];
        return of(defaultLevels);
      })
    ).subscribe(
      (response) => {
        console.log('Levels loaded:', response);
        this.levels.next(response);
      }
    );
  }

  public getLevels(): Observable<Level[]> {
    if (this.levels.getValue().length === 0) {
      this.loadLevels();
    }
    return this.levels.asObservable();
  }

  public hasLevels(): boolean {
    return this.levels.getValue().length > 0;
  }
}
