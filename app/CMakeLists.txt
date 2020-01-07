# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)



set(PATH_TO_MEDIACORE ${CMAKE_SOURCE_DIR}/src/main/cpp)



# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
file(GLOB FILES_JNI_LAYER "${PATH_TO_MEDIACORE}/native/*.cpp")
file(GLOB SRC_LIST ${PROJECT_SOURCE_DIR}/src/main/cpp/*.cpp)
file(GLOB FILES_TEST "${PATH_TO_MEDIACORE}/*.cpp")
file(GLOB FILES_LIB_COMMON "${PATH_TO_MEDIACORE}/common/*.cpp")
file(GLOB FILES_LIB_COMMON_EGL_CORE "${PATH_TO_MEDIACORE}/common/egl_core/*.cpp")
file(GLOB FILES_LIB_COMMON_EGL_CORE "${PATH_TO_MEDIACORE}/common/egl_core/*.cpp")
file(GLOB FILES_LIB_COMMON_RESAMPLER "${PATH_TO_MEDIACORE}/common/libresampler/*.cpp")
file(GLOB FILES_LIB_COMMON_MSG_Q "${PATH_TO_MEDIACORE}/common/message_queue/*.cpp")
file(GLOB FILES_LIB_COMMON_OPENGL_MEDIA "${PATH_TO_MEDIACORE}/common/opengl_media/*.cpp")
file(GLOB FILES_LIB_COMMON_OPENGL_MEDIA_RENDER "${PATH_TO_MEDIACORE}/common/opengl_media/render/*.cpp")
file(GLOB FILES_LIB_COMMON_OPENGL_MEDIA_TEXTURE "${PATH_TO_MEDIACORE}/common/opengl_media/texture/*.cpp")
file(GLOB FILES_LIB_COMMON_OPENGL_MEDIA_TEX_COPIER "${PATH_TO_MEDIACORE}/common/opengl_media/texture_copier/*.cpp")
file(GLOB FILES_LIB_CAMERA_PREVIEW "${PATH_TO_MEDIACORE}/camera_preview/*.cpp")
file(GLOB FILES_LIB_LIVE_CORE_COMMON "${PATH_TO_MEDIACORE}/livecore/common/*.cpp")
file(GLOB FILES_LIB_LIVE_CORE_AUDIOENCODER "${PATH_TO_MEDIACORE}/livecore/audio_encoder/*.cpp")
file(GLOB FILES_LIB_LIVE_CORE_CONSUMER "${PATH_TO_MEDIACORE}/livecore/consumer/*.cpp")
file(GLOB FILES_LIB_LIVE_CORE_PUBLISHER "${PATH_TO_MEDIACORE}/livecore/publisher/*.cpp")
file(GLOB FILES_LIB_MUSIC_DECODER "${PATH_TO_MEDIACORE}/music_decoder/*.cpp")
file(GLOB FILES_LIB_RECORDER_PROCESSOR "${PATH_TO_MEDIACORE}/recorder_processor/*.cpp")
file(GLOB FILES_LIB_VIDEO_CONSUMER_A_ENCODER "${PATH_TO_MEDIACORE}/video_consumer/libaudio_encoder/*.cpp")
file(GLOB FILES_LIB_VIDEO_CONSUMER_MUSIC_MERGER "${PATH_TO_MEDIACORE}/video_consumer/libmusic_merger/*.cpp")
file(GLOB FILES_LIB_VIDEO_CONSUMER "${PATH_TO_MEDIACORE}/video_consumer/libvideo_consumer/*.cpp")
file(GLOB FILES_LIB_VIDEO_ENCODER "${PATH_TO_MEDIACORE}/video_encoder/*.cpp")
file(GLOB FILES_LIB_VIDEO_ENCODER_HW "${PATH_TO_MEDIACORE}/video_encoder/hw_encoder/*.cpp")
file(GLOB FILES_LIB_VIDEO_ENCODER_X264 "${PATH_TO_MEDIACORE}/video_encoder/soft_encoder/*.cpp")
file(GLOB FILES_LIB_VIDEO_ENCODER_SOFT_COLOR "${PATH_TO_MEDIACORE}/video_encoder/soft_encoder/color_conversion/color_conversion_armv7a.cpp")


add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SRC_LIST}

        ${FILES_TEST}
        ${FILES_JNI_LAYER}
        ${FILES_LIB_COMMON}
        ${FILES_LIB_COMMON_EGL_CORE}
        ${FILES_LIB_COMMON_MSG_Q}
        ${FILES_LIB_COMMON_RESAMPLER}
        ${FILES_LIB_COMMON_OPENGL_MEDIA}
        ${FILES_LIB_COMMON_OPENGL_MEDIA_RENDER}
        ${FILES_LIB_COMMON_OPENGL_MEDIA_TEXTURE}
        ${FILES_LIB_COMMON_OPENGL_MEDIA_TEX_COPIER}
        ${FILES_LIB_CAMERA_PREVIEW}
        ${FILES_LIB_LIVE_CORE_COMMON}
        ${FILES_LIB_LIVE_CORE_AUDIOENCODER}
        ${FILES_LIB_LIVE_CORE_CONSUMER}
        ${FILES_LIB_LIVE_CORE_PUBLISHER}
        ${FILES_LIB_MUSIC_DECODER}
        ${FILES_LIB_RECORDER_PROCESSOR}
        ${FILES_LIB_VIDEO_CONSUMER_A_ENCODER}
        ${FILES_LIB_VIDEO_CONSUMER_MUSIC_MERGER}
        ${FILES_LIB_VIDEO_CONSUMER}
        ${FILES_LIB_VIDEO_ENCODER}
        ${FILES_LIB_VIDEO_ENCODER_HW}
        ${FILES_LIB_VIDEO_ENCODER_X264}
        ${FILES_LIB_VIDEO_ENCODER_SOFT_COLOR})
#avcodec
add_library(
        avcodec

        SHARED

        IMPORTED

)
SET_TARGET_PROPERTIES(
        avcodec
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavcodec.so
)

#avformat
add_library(
        avformat

        SHARED

        IMPORTED
)
SET_TARGET_PROPERTIES(
        avformat
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavformat.so
)

#avutil
add_library(
        avutil

        SHARED

        IMPORTED
)
SET_TARGET_PROPERTIES(
        avutil
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavutil.so
)


add_library(
        avfilter

        SHARED

        IMPORTED
)
SET_TARGET_PROPERTIES(
        avfilter
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libavfilter.so
)
add_library(
        swresample

        SHARED

        IMPORTED
)
SET_TARGET_PROPERTIES(
        swresample
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libswresample.so
)
add_library(
        swscale

        SHARED

        IMPORTED
)

SET_TARGET_PROPERTIES(
        swscale
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libswscale.so
)

add_library(
        postproc

        SHARED

        IMPORTED
)

SET_TARGET_PROPERTIES(
        postproc
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libpostproc.so
)
add_library(
        x264

        STATIC

        IMPORTED
)

SET_TARGET_PROPERTIES(
        x264
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libx264.a
)
add_library(
        fdk-aac

        STATIC

        IMPORTED
)

SET_TARGET_PROPERTIES(
        fdk-aac
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libfdk-aac.a)
add_library(
        sox

        STATIC

        IMPORTED
)

SET_TARGET_PROPERTIES(
        sox
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libsox.a)

add_library(
        vo-aacenc

        STATIC

        IMPORTED
)

SET_TARGET_PROPERTIES(
        vo-aacenc
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/${ANDROID_ABI}/libvo-aacenc.a)


# 可以一次导入多个路径
include_directories(${PROJECT_SOURCE_DIR}/src/main/cpp/thirdlib/ffmpeg/include
        ${PROJECT_SOURCE_DIR}/src/main/cpp/thirdlib/x264/include
        ${PROJECT_SOURCE_DIR}/src/main/cpp/thirdlib/fdkaac/include
        ${PROJECT_SOURCE_DIR}/src/main/cpp/thirdlib/sox/include

        ${PATH_TO_MEDIACORE}
        ${PATH_TO_MEDIACORE}/native
        ${PATH_TO_MEDIACORE}/common
        ${PATH_TO_MEDIACORE}/camera_preview
        ${PATH_TO_MEDIACORE}/livecore
        ${PATH_TO_MEDIACORE}/livecore/common
        ${PATH_TO_MEDIACORE}/livecore/audio_encoder
        ${PATH_TO_MEDIACORE}/livecore/consumer
        ${PATH_TO_MEDIACORE}/livecore/platform_dependent
        ${PATH_TO_MEDIACORE}/livecore/publisher
        ${PATH_TO_MEDIACORE}/music_decoder
        ${PATH_TO_MEDIACORE}/recorder_processor
        ${PATH_TO_MEDIACORE}/video_consumer
        ${PATH_TO_MEDIACORE}/video_consumer/libaudio_encoder
        ${PATH_TO_MEDIACORE}/video_consumer/libmusic_merger
        ${PATH_TO_MEDIACORE}/video_consumer/libvideo_consumer
        ${PATH_TO_MEDIACORE}/video_encoder
        ${PATH_TO_MEDIACORE}/video_encoder/soft_encoder
        ${PATH_TO_MEDIACORE}/video_encoder/hw_encoder)




# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib
        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        postproc
        fdk-aac
        x264
        sox
        vo-aacenc
        android
        GLESv3
        EGL
        OpenSLES
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})