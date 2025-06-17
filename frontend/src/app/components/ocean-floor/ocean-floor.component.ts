import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-ocean-floor',
  template: `
    <div class="ocean-floor-container">
      <div class="sand-floor"></div>
    </div>
  `,
  styles: [`
    .ocean-floor-container {
      width: 100%;
      height: 80px;
      position: relative;
      overflow: hidden;
    }

    .sand-floor {
      width: 100%;
      height: 100%;
      background: #d2b48c;
      position: relative;
    }

    .sand-floor::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image:
        /* Large sand particles */
        radial-gradient(circle at 5% 15%, rgba(160, 130, 98, 0.6) 1.5px, transparent 1.5px),
        radial-gradient(circle at 15% 25%, rgba(139, 115, 85, 0.5) 1.2px, transparent 1.2px),
        radial-gradient(circle at 25% 35%, rgba(205, 175, 149, 0.4) 1.8px, transparent 1.8px),
        radial-gradient(circle at 35% 45%, rgba(160, 140, 115, 0.5) 1.4px, transparent 1.4px),
        radial-gradient(circle at 45% 55%, rgba(139, 115, 85, 0.6) 1.0px, transparent 1.0px),
        radial-gradient(circle at 55% 65%, rgba(194, 163, 115, 0.4) 1.6px, transparent 1.6px),
        radial-gradient(circle at 65% 75%, rgba(160, 130, 98, 0.5) 1.2px, transparent 1.2px),
        radial-gradient(circle at 75% 85%, rgba(139, 115, 85, 0.4) 1.8px, transparent 1.8px),
        radial-gradient(circle at 85% 95%, rgba(205, 175, 149, 0.5) 1.4px, transparent 1.4px),

        /* Medium sand particles */
        radial-gradient(circle at 10% 10%, rgba(160, 140, 115, 0.3) 0.8px, transparent 0.8px),
        radial-gradient(circle at 20% 20%, rgba(139, 115, 85, 0.4) 0.6px, transparent 0.6px),
        radial-gradient(circle at 30% 30%, rgba(194, 163, 115, 0.3) 1.0px, transparent 1.0px),
        radial-gradient(circle at 40% 40%, rgba(160, 130, 98, 0.4) 0.8px, transparent 0.8px),
        radial-gradient(circle at 50% 50%, rgba(205, 175, 149, 0.3) 0.6px, transparent 0.6px),
        radial-gradient(circle at 60% 60%, rgba(139, 115, 85, 0.4) 1.0px, transparent 1.0px),
        radial-gradient(circle at 70% 70%, rgba(160, 140, 115, 0.3) 0.8px, transparent 0.8px),
        radial-gradient(circle at 80% 80%, rgba(194, 163, 115, 0.4) 0.6px, transparent 0.6px),
        radial-gradient(circle at 90% 90%, rgba(160, 130, 98, 0.3) 1.0px, transparent 1.0px),

        /* Small sand particles */
        radial-gradient(circle at 12% 18%, rgba(139, 115, 85, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 22% 28%, rgba(160, 140, 115, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 32% 38%, rgba(194, 163, 115, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 42% 48%, rgba(160, 130, 98, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 52% 58%, rgba(205, 175, 149, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 62% 68%, rgba(139, 115, 85, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 72% 78%, rgba(160, 140, 115, 0.2) 0.4px, transparent 0.4px),
        radial-gradient(circle at 82% 88%, rgba(194, 163, 115, 0.2) 0.4px, transparent 0.4px),

        /* Tiny sand dust particles */
        radial-gradient(circle at 8% 12%, rgba(160, 140, 115, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 18% 22%, rgba(139, 115, 85, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 28% 32%, rgba(194, 163, 115, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 38% 42%, rgba(160, 130, 98, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 48% 52%, rgba(205, 175, 149, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 58% 62%, rgba(139, 115, 85, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 68% 72%, rgba(160, 140, 115, 0.15) 0.2px, transparent 0.2px),
        radial-gradient(circle at 78% 82%, rgba(194, 163, 115, 0.15) 0.2px, transparent 0.2px);
      background-size: 100% 100%;
    }

    .sand-floor::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image:
        /* Additional scattered sand particles for more realism */
        radial-gradient(circle at 7% 23%, rgba(139, 115, 85, 0.3) 1.2px, transparent 1.2px),
        radial-gradient(circle at 17% 33%, rgba(160, 140, 115, 0.3) 0.8px, transparent 0.8px),
        radial-gradient(circle at 27% 43%, rgba(194, 163, 115, 0.3) 1.0px, transparent 1.0px),
        radial-gradient(circle at 37% 53%, rgba(160, 130, 98, 0.3) 0.6px, transparent 0.6px),
        radial-gradient(circle at 47% 63%, rgba(205, 175, 149, 0.3) 1.4px, transparent 1.4px),
        radial-gradient(circle at 57% 73%, rgba(139, 115, 85, 0.3) 0.8px, transparent 0.8px),
        radial-gradient(circle at 67% 83%, rgba(160, 140, 115, 0.3) 1.0px, transparent 1.0px),
        radial-gradient(circle at 77% 93%, rgba(194, 163, 115, 0.3) 1.2px, transparent 1.2px),
        radial-gradient(circle at 87% 13%, rgba(160, 130, 98, 0.3) 0.6px, transparent 0.6px),
        radial-gradient(circle at 3% 73%, rgba(205, 175, 149, 0.3) 0.8px, transparent 0.8px);
      background-size: 100% 100%;
    }
  `]
})
export class OceanFloorComponent implements OnInit, OnDestroy {

  constructor() {}

  ngOnInit(): void {
    // Simple component, no initialization needed
  }

  ngOnDestroy(): void {
    // Simple component, no cleanup needed
  }
}
