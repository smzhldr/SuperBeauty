cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_BUILD_TYPE "RELEASE")
set(CMAKE_CXX_FLAGS_RELEASE "$ENV{CXXFLAGS} -O3 -Wall")
set(CMAKE_CXX_FLAGS_DEBUG "-g -Wall")

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")

include(${SRC_DIR}/dlib/cmake)

include_directories(${SRC_DIR}/libs/include)

add_library(facedetector
        SHARED
        ${SRC_DIR}/beautysdk/facejni.cpp
        ${SRC_DIR}/beautysdk/faceengine.cpp
        ${SRC_DIR}/src/facedetector_jni.cpp)

find_library(
        log-lib
        log)

add_library(libopencv SHARED IMPORTED)
set_target_properties(libopencv PROPERTIES IMPORTED_LOCATION "${SRC_DIR}/libs/${ANDROID_ABI}/libopencv_java4.so")

target_link_libraries(
        facedetector
        dlib
        libopencv
        -ljnigraphics
        ${log-lib})
