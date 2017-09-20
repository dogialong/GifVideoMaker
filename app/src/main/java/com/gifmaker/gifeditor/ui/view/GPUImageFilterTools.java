/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gifmaker.gifeditor.ui.view;

import android.graphics.PointF;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class GPUImageFilterTools {
    public static FilterList getListFilter() {
        final FilterList filters = new FilterList();
        filters.addFilter("Normal", FilterType.NORMAL);
        filters.addFilter("Contrast", FilterType.CONTRAST);

        filters.addFilter("Pixelation", FilterType.PIXELATION);
        filters.addFilter("Hue", FilterType.HUE);
        filters.addFilter("Gamma", FilterType.GAMMA);
        filters.addFilter("Sepia", FilterType.SEPIA);
        filters.addFilter("Grayscale", FilterType.GRAYSCALE);
        filters.addFilter("Sharpness", FilterType.SHARPEN);
        filters.addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION);
        filters.addFilter("Emboss", FilterType.EMBOSS);
        filters.addFilter("Posterize", FilterType.POSTERIZE);
        filters.addFilter("Grouped filters", FilterType.FILTER_GROUP);
        filters.addFilter("Saturation", FilterType.SATURATION);
        filters.addFilter("Exposure", FilterType.EXPOSURE);
        filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW);
        filters.addFilter("Monochrome", FilterType.MONOCHROME);
        filters.addFilter("Opacity", FilterType.OPACITY);
        filters.addFilter("RGB", FilterType.RGB);
        filters.addFilter("White Balance", FilterType.WHITE_BALANCE);
        filters.addFilter("Vignette", FilterType.VIGNETTE);

        filters.addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR);

        filters.addFilter("Box Blur", FilterType.BOX_BLUR);
        filters.addFilter("CGA Color Space", FilterType.CGA_COLORSPACE);
        filters.addFilter("Dilation", FilterType.DILATION);
        filters.addFilter("Kuwahara", FilterType.KUWAHARA);
        filters.addFilter("RGB Dilation", FilterType.RGB_DILATION);

        filters.addFilter("Toon", FilterType.TOON);
        filters.addFilter("Smooth Toon", FilterType.SMOOTH_TOON);

        filters.addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION);

        filters.addFilter("Haze", FilterType.HAZE);

        filters.addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION);

        filters.addFilter("Swirl", FilterType.SWIRL);
        filters.addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION);


        filters.addFilter("Color Balance", FilterType.COLOR_BALANCE);

        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);

        filters.addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR);

        filters.addFilter("Transform (2-D)", FilterType.TRANSFORM2D);
        return filters;
    }

    public static GPUImageFilter createFilterForType(final FilterType type) {
        switch (type) {
            case NORMAL:
                return new GPUImageFilter();
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case GAMMA:
                return new GPUImageGammaFilter(2.0f);

            case PIXELATION:
                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SEPIA:
                return new GPUImageSepiaFilter();
            case SHARPEN:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case SOBEL_EDGE_DETECTION:
                return new GPUImageSobelEdgeDetection();
            case THREE_X_THREE_CONVOLUTION:
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{
                        -1.0f, 0.0f, 1.0f,
                        -2.0f, 0.0f, 2.0f,
                        -1.0f, 0.0f, 1.0f
                });
                return convolution;
            case EMBOSS:
                return new GPUImageEmbossFilter();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case FILTER_GROUP:
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case EXPOSURE:
                return new GPUImageExposureFilter(0.0f);
            case HIGHLIGHT_SHADOW:
                return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case MONOCHROME:
                return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case OPACITY:
                return new GPUImageOpacityFilter(1.0f);
            case RGB:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case WHITE_BALANCE:
                return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case GAUSSIAN_BLUR:
                return new GPUImageGaussianBlurFilter();

            case BOX_BLUR:
                return new GPUImageBoxBlurFilter();
            case CGA_COLORSPACE:
                return new GPUImageCGAColorspaceFilter();
            case DILATION:
                return new GPUImageDilationFilter();
            case KUWAHARA:
                return new GPUImageKuwaharaFilter();
            case RGB_DILATION:
                return new GPUImageRGBDilationFilter();

            case TOON:
                return new GPUImageToonFilter();
            case SMOOTH_TOON:
                return new GPUImageSmoothToonFilter();

            case BULGE_DISTORTION:
                return new GPUImageBulgeDistortionFilter();

            case HAZE:
                return new GPUImageHazeFilter();

            case NON_MAXIMUM_SUPPRESSION:
                return new GPUImageNonMaximumSuppressionFilter();

            case SWIRL:
                return new GPUImageSwirlFilter();
            case WEAK_PIXEL_INCLUSION:
                return new GPUImageWeakPixelInclusionFilter();
            case COLOR_BALANCE:
                return new GPUImageColorBalanceFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;

            case BILATERAL_BLUR:
                return new GPUImageBilateralFilter();

            case TRANSFORM2D:
                return new GPUImageTransformFilter();

            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }


    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    public enum FilterType {
        NORMAL,CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE,
        GAUSSIAN_BLUR, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, TOON, SMOOTH_TOON, BULGE_DISTORTION, HAZE, NON_MAXIMUM_SUPPRESSION,
         SWIRL, WEAK_PIXEL_INCLUSION, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, TRANSFORM2D
    }

    public static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

}
