//
// Created by derong.liu on 2019-08-16.
//

#include "faceengine.h"

int FaceEngine::init(const char *sdkPath) {
    detector = dlib::get_frontal_face_detector();
    dlib::deserialize(sdkPath)
            >> pose_model; // 68点人脸标记模型 shape_predictor_68_face_landmarks.dat
    return 1;
}

jobjectArray FaceEngine::detect(JNIEnv *env, jobject obj, jbyteArray data, jint type, jint width, jint height, jint orientation) {

    cv::Rect box(0, 0, 0, 0);
    std::vector<cv::Point2d> pts2d;

    jbyte *ptr = env->GetByteArrayElements(data, 0);
    Mat image = Mat(height, width, CV_8UC4, (unsigned char *) ptr);

    jclass faceClass = env->FindClass("com/smzh/beautysdk/Face");

    if (image.empty()) {
        return env->NewObjectArray(0, faceClass, nullptr);
    }
    Mat result;
    matrix<bgr_pixel> img;
    cvtColor(image, result, CV_RGBA2BGR);
    assign_image(img, cv_image<bgr_pixel>(result));

    std::vector<dlib::rectangle> dets = detector(img);

    if (dets.empty()) {
        return env->NewObjectArray(0, faceClass, nullptr);
    }

    jobjectArray faceArray = env->NewObjectArray(dets.size(), faceClass, nullptr);

    for (int i = 0; i < dets.size(); i++) {

        full_object_detection shape = pose_model(img, dets[i]); // 一个人的人脸特征

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

        jobject face = produceFace(env, faceClass, box, shape);
        env->SetObjectArrayElement(faceArray, i, face);
        env->DeleteLocalRef(face);
    }
    return faceArray;
}

int FaceEngine::destroy() {
    return 0;
}

jobject FaceEngine::produceFace(JNIEnv *env, jclass faceClass, cv::Rect box, full_object_detection shape) {

    //获得得该类型的构造函数  函数名为 <init> 返回类型必须为 void 即 V
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

    return face;
}