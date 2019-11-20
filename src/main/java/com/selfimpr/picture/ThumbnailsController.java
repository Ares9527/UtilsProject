package com.selfimpr.picture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Thumbnails工具类接口
 */
@RestController
@RequestMapping("/thumbnails")
public class ThumbnailsController {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailsController.class);

    @Value("${custom.path.pic.zoomPicPath}")
    private String zoomPicPath;

    @Value("${custom.path.pic.rotatePicPath}")
    private String rotatePicPath;

    @Value("${custom.path.pic.waterMarkPicPath}")
    private String waterMarkPicPath;

    @Value("${custom.path.pic.tailorPicPath}")
    private String tailorPicPath;

    @Value("${custom.path.pic.transferFormatPicPath}")
    private String transferFormatPicPath;

    @PostMapping(value = "/zoom4specifiedProportion")
    public void zoom4specifiedProportion(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.zoom4specifiedProportion(file, 0.5f, zoomPicPath);
    }

    @PostMapping(value = "/zoom4SpecifySizeKeepAspectRatio")
    public void zoom4SpecifySizeKeepAspectRatio(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.zoom4SpecifySizeKeepAspectRatio(file, 200, 300, zoomPicPath);
    }

    @PostMapping(value = "/zoom4SpecifySize")
    public void zoom4SpecifySize(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.zoom4SpecifySize(file, 200, 300, zoomPicPath);
    }

    @PostMapping(value = "/rotate")
    public void rotate(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.rotate(file, null, null,90f, rotatePicPath);
    }

    @PostMapping(value = "/waterMark")
    public void waterMark(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.waterMark(file, null, null, waterMarkPicPath);
    }

    @PostMapping(value = "/tailor")
    public void tailor(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.tailor(file, 100, 100, tailorPicPath);
    }

    @PostMapping(value = "/transferFormat")
    public void transferFormat(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.transferFormat(file, null, null, "png", transferFormatPicPath);
    }

    @PostMapping(value = "/zoom4specifiedProportionBatch")
    public void zoom4specifiedProportionBatch(@RequestParam(value = "uploadFile") MultipartFile file) {
        ThumbnailsUtils.zoom4specifiedProportionBatch(0.5f, "E://test/batch");
    }

    @GetMapping(value = "/output2Stream")
    public void output2Stream(HttpServletRequest request, HttpServletResponse response) {
        ThumbnailsUtils.output2Stream("E://test/img2.jpg", response);
    }
}
