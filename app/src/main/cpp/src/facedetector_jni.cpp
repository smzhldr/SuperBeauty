//
// Created by Lvlingling on 11/17/21.
//

#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing.h>
#include <dlib/opencv/cv_image.h>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif

struct Engine {
    dlib::shape_predictor pose_mode;
    dlib::frontal_face_detector detector;
};

JNIEXPORT jlong JNICALL Java_com_smzh_beauty_facedetector_FaceDetector_create(JNIEnv *env, jobject instance, jstring path) {
    const char *modelPath = env->GetStringUTFChars(path, nullptr);

    dlib::frontal_face_detector detector = dlib::get_frontal_face_detector();
    auto *engine = new Engine;
    engine->detector = dlib::get_frontal_face_detector();
    auto& mode = engine->pose_mode;
    dlib::deserialize(modelPath) >> mode;

    env->ReleaseStringUTFChars(path, modelPath);
    return (long) engine;
}

JNIEXPORT jobjectArray JNICALL Java_com_smzh_beauty_facedetector_FaceDetector_detect(JNIEnv *env, jobject obj,
                                                                                     jlong instance,
                                                                                     jbyteArray data,
                                                                                     jint type,
                                                                                     jint width,
                                                                                     jint height) {
    auto *engine = (Engine *) instance;

    jbyte *ptr = env->GetByteArrayElements(data, nullptr);
    cv::Mat image = cv::Mat(height, width, CV_8UC4, (unsigned char *) ptr);

    jclass faceClass = env->FindClass("com/smzh/beauty/facedetector/Face");
    if (image.empty()) {
        return env->NewObjectArray(0, faceClass, nullptr);
    }
    cv::Mat result;
    dlib::matrix<dlib::bgr_pixel> img;
    cvtColor(image, result, cv::COLOR_RGBA2BGR);
    assign_image(img, dlib::cv_image<dlib::bgr_pixel>(result));

    std::vector<dlib::rectangle> dets = engine->detector(img);

    if (dets.empty()) {
        return env->NewObjectArray(0, faceClass, nullptr);
    }

    jobjectArray faceArray = env->NewObjectArray(dets.size(), faceClass, nullptr);

    cv::Rect box(0, 0, 0, 0);
    std::vector<cv::Point2d> pts2d;
    for (int i = 0; i < dets.size(); i++) {
        dlib::full_object_detection shape = engine->pose_mode(img, dets[i]); // 一个人的人脸特征
        box.x = dets[i].left();
        box.y = dets[i].top();
        box.width = (int) dets[i].width();
        box.height = (int) dets[i].height();

        int top = box.y - box.height / 3;
        top = top < 0 ? 0 : top;
        int boxHeight = box.height * 4 / 3;
        boxHeight = boxHeight + top > image.size().height ? image.size().height - top : height;
        box.y = top;
        box.height = boxHeight;

        jmethodID faceConstructMID = env->GetMethodID(faceClass, "<init>", "()V");

        jobject face = env->NewObject(faceClass, faceConstructMID);

        jclass pointClass = env->FindClass("android/graphics/PointF");
        jmethodID pointConstructMID = (env)->GetMethodID(pointClass, "<init>", "(FF)V");

        jobjectArray facePoint = env->NewObjectArray((size_t) shape.num_parts(), pointClass, nullptr);
        for (size_t j = 0; j < shape.num_parts(); j++) {
            jobject point = env->NewObject(pointClass, pointConstructMID, (jfloat) (shape.part(j).x()), (jfloat) (shape.part(j).y()));
            env->SetObjectArrayElement(facePoint, j, point);
            env->DeleteLocalRef(point);
        }
        jmethodID setPoints = env->GetMethodID(faceClass, "setFacePoints", "([Landroid/graphics/PointF;)V");
        env->CallVoidMethod(face, setPoints, facePoint);
        env->DeleteLocalRef(facePoint);

        jclass rectClass = env->FindClass("android/graphics/RectF");
        jmethodID rectConstructMID = env->GetMethodID(rectClass, "<init>", "(FFFF)V");
        jobject rect = env->NewObject(rectClass, rectConstructMID, (jfloat) (box.x), (jfloat) (box.y), (jfloat) (box.width), (jfloat) (box.height));

        jmethodID setRect = env->GetMethodID(faceClass, "setRect", "(Landroid/graphics/RectF;)V");
        env->CallVoidMethod(face, setRect, rect);
        env->DeleteLocalRef(rect);

        env->SetObjectArrayElement(faceArray, i, face);
        env->DeleteLocalRef(face);
    }

    return faceArray;
}

JNIEXPORT jint JNICALL Java_com_smzh_beauty_facedetector_FaceDetector_destroy(JNIEnv *env, jobject obj, jlong instance) {

    if (instance != 0) {
        delete (Engine *) instance;
    }
    return 0;
}
}
