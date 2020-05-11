#include "SC_PlugIn.h"

// InterfaceTable contains pointers to functions in the host (server).
static InterfaceTable *ft;

struct LFPulseReset : public Unit {
    float mReset;
    double mPhase;
    float mFreqMul, mDuty;
};

void LFPulseReset_next_a(LFPulseReset* unit, int inNumSamples);
void LFPulseReset_next_k(LFPulseReset* unit, int inNumSamples);
void LFPulseReset_Ctor(LFPulseReset* unit);

void LFPulseReset_next_a(LFPulseReset* unit, int inNumSamples) {
    float* out = ZOUT(0);
    float* freq = ZIN(0);
    float nextDuty = ZIN0(2);
    float reset = ZIN0(3);
    float duty = unit->mDuty;
    float lastreset = unit->mReset;
    float tempPhase;

    float freqmul = unit->mFreqMul;
    double phase = unit->mPhase;
    LOOP1(
        inNumSamples, float z; 
        float curreset = reset;

        if (phase >= 1.f) {
            phase -= 1.f;
            duty = unit->mDuty = nextDuty;
            // output at least one sample from the opposite polarity
            z = duty <= 0.5f ? 1.f : 0.f;
        } else { z = phase < duty ? 1.f : 0.f; } phase += ZXP(freq) * freqmul;

        if(curreset > 0.f && lastreset <= 0.f) {
              phase = 1.f;
              z = 0;
         }
         lastreset = curreset;

        ZXP(out) = z;
        );

    unit->mPhase = phase;
    unit->mReset = lastreset;
}

void LFPulseReset_next_k(LFPulseReset* unit, int inNumSamples) {
    float* out = ZOUT(0);
    float freq = ZIN0(0) * unit->mFreqMul;
    float nextDuty = ZIN0(2);
    float reset = ZIN0(3);
    float duty = unit->mDuty;
    float lastreset = unit->mReset;
    float tempPhase;

    double phase = unit->mPhase;
    LOOP1(
        inNumSamples, float z; 
        float curreset = reset;

        if(curreset > 0.f && lastreset <= 0.f) {
            phase = 1.f;
            //z = 0;
        }

        if (phase >= 1.f) {
            phase = 0.f;
            duty = unit->mDuty = nextDuty;
            // output at least one sample from the opposite polarity
            z = 0.f;
        } else { z = phase < duty ? 1.f : 0.f; } 

        //tempPhase = fmod(phase+0.001, 1);
        //z = tempPhase < duty ? 1.f : 0.f;

        phase += freq;
        
         
        lastreset = curreset;

        ZXP(out) = z;
    );

    unit->mPhase = phase;
    unit->mReset = lastreset;
}

void LFPulseReset_Ctor(LFPulseReset* unit) {
    float phase;

    if (INRATE(0) == calc_FullRate) {
        SETCALC(LFPulseReset_next_a);
    } else {
        SETCALC(LFPulseReset_next_k);
    }

    unit->mFreqMul = unit->mRate->mSampleDur;
    phase = ZIN0(1);
    if (phase==0.f) {phase = 1.f; }
    unit->mPhase = phase;
    unit->mDuty = ZIN0(2);
    unit->mReset = 0;

    LFPulseReset_next_k(unit, 1);
}


// the entry point is called by the host when the plug-in is loaded
PluginLoad(LFPulseReset)
{
    // InterfaceTable *inTable implicitly given as argument to the load function
    ft = inTable; // store pointer to InterfaceTable

    DefineSimpleUnit(LFPulseReset);
}