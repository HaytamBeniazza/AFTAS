import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend';

  // 🫧 SIMPLE 250 BUBBLE SYSTEM 🌊
  bubbles: any[] = [];

  ngOnInit() {
    this.generateBubbles();
    console.log('🫧 Generated bubbles:', this.bubbles.length);
    console.log('🌊 First few bubbles:', this.bubbles.slice(0, 3));
  }

  generateBubbles() {
    this.bubbles = [];

    for (let i = 0; i < 250; i++) {
      this.bubbles.push({
        id: i,
        size: this.randomBetween(8, 30),
        left: Math.random() * 100,
        opacity: this.randomBetween(0.1, 0.6),
        duration: this.randomBetween(15, 35),
        delay: Math.random() * 15
      });
    }

    console.log('✅ Bubbles generated successfully!', this.bubbles.length);
  }

  private randomBetween(min: number, max: number): number {
    return Math.random() * (max - min) + min;
  }

  trackByBubbleId(index: number, bubble: any): number {
    return bubble.id;
  }

  onImageLoad(imagePath: string): void {
    console.log('✅ Image loaded successfully:', imagePath);
  }

  onImageError(imagePath: string): void {
    console.log('❌ Image failed to load:', imagePath);
  }
}
