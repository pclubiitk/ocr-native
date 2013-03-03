1. Loading any image format (bmp, jpg, png) from given source.  Then convert the image to grayscale and binarize it using the threshold value (Otsu algorithm). ***This part is done**

2.  Detecting image features like resolution and inversion. So that we can finally convert it to a straightened image for further processing.

3. Image can be skewed or it can have a lot of noise, so deskew and denoising algorithms are applied to improve the image quality.

4. Lines detection and removing. This step is required to improve page layout analysis, to achieve better recognition quality for underlined text, to detect tables, etc. 

5. Page layout analysis. In this step we try to identify the Òtext zonesÓ present in the image. So that only that portion is used for recognition and rest of the region is left out.

6. Detection of text lines and words. Here we also need to take care of different font sizes and small spaces between words.

7. Recognition of characters. This is the main algorithm of OCR; an image of every character must be converted to appropriate character code. Sometimes this algorithm produces several character codes for uncertain images. For instance, recognition of the image of "I" character can produce "I", "|" "1", "l" codes and the final character code will be selected later.

8. Saving results to selected output format, for instance, searchable PDF, DOC, RTF, TXT. It is important to save original page layout: columns, fonts, colors, pictures, background and so on.

