//
// Created by derong.liu on 2019-08-15.
//


#include <jni.h>
#include "facejni.h"
#include "faceengine.h"

using namespace dlib;
using namespace std;
using namespace cv;

static FaceEngine *pFaceEngine;

JNIEXPORT jint JNICALL Java_com_smzh_beautysdk_FaceDetector_init
        (JNIEnv *env, jobject obj, jstring path) {
    //获取绝对路径

    const char *modelPath = env->GetStringUTFChars(path, 0);
    if (modelPath == nullptr) {
        return 0;
    }
    pFaceEngine = new FaceEngine;
    return pFaceEngine->init(modelPath);
}


JNIEXPORT jint JNICALL Java_com_smzh_beautysdk_FaceDetector_landMarks2
        (JNIEnv *env, jobject obj, jlong intPtr, jlong outPtr) {
    return pFaceEngine->detect(intPtr, outPtr);
}

JNIEXPORT jobjectArray JNICALL Java_com_smzh_beautysdk_FaceDetector_detect
        (JNIEnv *env, jobject obj, jbyteArray data, jint type, jint width, jint height,
         jint orientation) {
    return pFaceEngine->detect(env, obj, data, type, width, height, orientation);
}

JNIEXPORT jint JNICALL Java_com_smzh_beautysdk_FaceDetector_destroy
        (JNIEnv *env, jobject obj) {
    pFaceEngine->destroy();
    pFaceEngine = nullptr;
    return 0;
}


void nBitmapToMat2(JNIEnv *env, jclass, jobject bitmap, jlong m_addr, jboolean needUnPremultiplyAlpha) {
    /*AndroidBitmapInfo  info;
    void*              pixels = 0;
    Mat&               dst = *((Mat*)m_addr);

    try {
        CV_Assert( AndroidBitmap_getInfo(env, bitmap, &info) >= 0 );
        CV_Assert( info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                   info.format == ANDROID_BITMAP_FORMAT_RGB_565 );
        CV_Assert( AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0 );
        CV_Assert( pixels );
        dst.create(info.height, info.width, CV_8UC4);
        if( info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 )
        {
            Mat tmp(info.height, info.width, CV_8UC4, pixels);
            if(needUnPremultiplyAlpha) cvtColor(tmp, dst, COLOR_mRGBA2RGBA);
            else tmp.copyTo(dst);
        } else {
            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
            Mat tmp(info.height, info.width, CV_8UC2, pixels);
            cvtColor(tmp, dst, COLOR_BGR5652RGBA);
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    } catch(const cv::Exception& e) {
        AndroidBitmap_unlockPixels(env, bitmap);
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je) je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
        return;
    } catch (...) {
        AndroidBitmap_unlockPixels(env, bitmap);
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code {nBitmapToMat}");
        return;
    }*/
}


