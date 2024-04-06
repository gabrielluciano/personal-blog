import { NgClass } from '@angular/common';
import { Component, Inject, inject } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBar,
  MatSnackBarAction,
  MatSnackBarActions,
  MatSnackBarConfig,
  MatSnackBarLabel,
  MatSnackBarRef,
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
  standalone: true,
  imports: [
    NgClass,
    MatSnackBarLabel,
    MatSnackBarActions,
    MatIconButton,
    MatSnackBarAction,
    MatIcon,
  ],
})
export class SnackbarComponent {
  snackBarRef = inject(MatSnackBarRef);

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: SnackbarData) {}
}
