//
// Created by derong.liu on 2019-08-16.
//

#include <jni.h>
#include "facejni.h"

#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing.h>
#include <dlib/opencv/cv_image.h>

using namespace dlib;
using namespace std;
using namespace cv;

#ifndef SUPERBEAUTY_DETECTOR_H
#define SUPERBEAUTY_DETECTOR_H


class FaceEngine {

private:
    frontal_face_detector detector;
    shape_predictor pose_model;

    jobject produceFace(JNIEnv *,jclass, cv::Rect,full_object_detection);

public:
    int init(const char *);

    int detect(jlong, jlong);

    jobjectArray detect(JNIEnv *, jobject, jbyteArray, jint, jint, jint height, jint);

    int destroy();

};


#endif //SUPERBEAUTY_DETECTOR_H
