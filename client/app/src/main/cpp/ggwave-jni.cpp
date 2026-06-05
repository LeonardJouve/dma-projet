#include <jni.h>
#include "ggwave.h"
#include <vector>
#include <string.h>

static int ggwaveId = -1;

extern "C" JNIEXPORT jshortArray JNICALL
Java_com_example_myapplication_GgwaveManager_generateAudio(JNIEnv *env, jobject thiz, jstring payload) {
    if (ggwaveId < 0) {
        ggwave_Parameters parameters = ggwave_getDefaultParameters();
        parameters.sampleFormatInp = GGWAVE_SAMPLE_FORMAT_I16;
        parameters.sampleFormatOut = GGWAVE_SAMPLE_FORMAT_I16;
        parameters.sampleRateInp = 48000;
        ggwaveId = ggwave_init(parameters);
    }

    const char *c_payload = env->GetStringUTFChars(payload, nullptr);
    int payload_len = strlen(c_payload);

    // n = nombre d'OCTETS (bytes)
    const int n = ggwave_encode(ggwaveId, c_payload, payload_len, GGWAVE_PROTOCOL_ULTRASOUND_NORMAL, 50, nullptr, 1);

    std::vector<char> waveform(n);

    const int ret = ggwave_encode(ggwaveId, c_payload, payload_len, GGWAVE_PROTOCOL_ULTRASOUND_NORMAL, 50, waveform.data(), 0);

    env->ReleaseStringUTFChars(payload, c_payload);

    int numSamples = ret / 2;

    jshortArray result = env->NewShortArray(numSamples);
    env->SetShortArrayRegion(result, 0, numSamples, (jshort*)waveform.data());

    return result;
}