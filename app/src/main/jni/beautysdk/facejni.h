/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_smzh_beautysdk_FaceDetector */

#ifndef _Included_com_smzh_beautysdk_FaceDetector
#define _Included_com_smzh_beautysdk_FaceDetector
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_smzh_beautysdk_FaceDetector
 * Method:    initModel
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_smzh_beautysdk_FaceDetector_init
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_smzh_beautysdk_FaceDetector
 * Method:    detect
 * Signature: ([BIIII)Ljava/lang/Object;
 */
JNIEXPORT jobjectArray JNICALL Java_com_smzh_beautysdk_FaceDetector_detect
  (JNIEnv *, jobject, jbyteArray, jint, jint, jint, jint);

/*
 * Class:     com_smzh_beautysdk_FaceDetector
 * Method:    destroy
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_smzh_beautysdk_FaceDetector_destroy
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
