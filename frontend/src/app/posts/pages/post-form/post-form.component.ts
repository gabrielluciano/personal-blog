import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VALID_SLUG_PATTERN } from 'src/app/shared/util/regexPatterns';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss'],
})
export class PostFormComponent implements OnInit {
  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      title: ['', [Validators.required]],
      subtitle: ['', [Validators.required]],
      slug: ['', [Validators.required, Validators.pattern(VALID_SLUG_PATTERN)]],
      metaTitle: ['', [Validators.required]],
      metaDescription: ['', [Validators.required]],
      imageUrl: ['', [Validators.required]],
      tags: [[], [Validators.required]],
      content: [''],
    });
  }
}
