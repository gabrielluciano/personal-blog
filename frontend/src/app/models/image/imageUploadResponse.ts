export interface ImageUploadResponse {
  message: string;
  name: string;
  variants: {
    '1024': string;
    '768': string;
    '480': string;
    '300': string;
  };
}
