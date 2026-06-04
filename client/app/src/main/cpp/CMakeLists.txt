cmake_minimum_required(VERSION 3.22.1)

project("ggwave_native")

add_library(
        ggwave_native
        SHARED
        ggwave.cpp
        ggwave-jni.cpp)

find_library(log-lib log)
target_link_libraries(ggwave_native ${log-lib})