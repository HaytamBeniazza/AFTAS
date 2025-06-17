import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Fish } from 'src/app/model/Fish';
import { FishService } from 'src/app/services/fish.service';

interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

@Component({
  selector: 'app-fish-list',
  templateUrl: './fish-list.component.html',
  styleUrls: ['./fish-list.component.css']
})
export class FishListComponent implements OnInit {
  fishData: PaginatedResponse<Fish> = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 20,
    number: 0,
    first: true,
    last: true
  };

  currentPage = 0;
  pageSize = 20;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  searchTerm = '';
  Math = Math;

  constructor(private fishService: FishService) {}

  ngOnInit(): void {
    this.loadFish();
  }

  onImageError(event: any): void {
    // Replace broken image with default fish emoji
    const img = event.target;
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder) {
      placeholder.style.display = 'flex';
    }
  }

  loadFish(): void {
    this.loading = true;
    this.fishService.getFishPaginated(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.fishData = response;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading fish:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadFish();
  }

  onPageSizeChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.pageSize = +target.value;
    this.currentPage = 0;
    this.loadFish();
  }

  deleteFish(fishName: string): void {
    if (confirm(`Are you sure you want to delete ${fishName}?`)) {
      this.fishService.delete(fishName);
      // Reload current page after deletion
      setTimeout(() => {
        this.loadFish();
      }, 500);
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, this.currentPage + 2);

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  searchFish(): void {
    // For now, just reload - you can implement search later
    this.currentPage = 0;
    this.loadFish();
  }
}
