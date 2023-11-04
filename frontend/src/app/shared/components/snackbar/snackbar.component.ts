import { Component, Inject, inject } from '@angular/core';
import {
  MatSnackBarRef,
  MAT_SNACK_BAR_DATA,
  MatSnackBarConfig,
  MatSnackBar,
} from '@angular/material/snack-bar';

const DURATION_IN_SECONDS = 10;

export interface SnackbarData {
  message: string;
  style?: 'error' | 'success' | 'warn';
}

export function getSnackBarDefaultConfig(message: string, style: SnackbarData['style']) {
  const config: MatSnackBarConfig<SnackbarData> = {
    duration: DURATION_IN_SECONDS * 1000,
    horizontalPosition: 'center',
    verticalPosition: 'top',
    data: {
      message: message,
      style: style,
    },
  };

  return config;
}

export function showSnackBar(snackBar: MatSnackBar, message: string, style: SnackbarData['style']) {
  snackBar.openFromComponent(SnackbarComponent, getSnackBarDefaultConfig(message, style));
}

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
})
export class SnackbarComponent {
  snackBarRef = inject(MatSnackBarRef);

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: SnackbarData) {}
}
