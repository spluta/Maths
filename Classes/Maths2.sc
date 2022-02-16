Maths2 {
	*ar {|rise=0.1, fall=0.1, linExp = 0.5, loop = 1, plugged = 0, trig = 0|

		var sampleRate = SampleRate.ir, isExp, isPlugged;
		var riseIn = rise.clip(0.0001,10*60), fallIn = fall.clip(0.0001,10*60);
		var freq = 1/(riseIn+fallIn), width=riseIn/(riseIn+fallIn);

		var plugTrig = Trig1.ar(1-plugged, 1/sampleRate);
		var loopTrig = Trig1.ar(loop, 1/sampleRate);

		var phasor2;

		var phasor = Phasor.ar(Silent.ar+loopTrig+plugTrig, 2*freq/sampleRate, -1, 1, -1);
		var phasorTrig = Trig1.ar(0.5-phasor, 1/sampleRate)+EnvGen.ar(Env([0,0,1,0], [1/sampleRate,1/sampleRate,1/sampleRate]));
		var latchTrig = (phasorTrig+(DelayN.ar(loopTrig, 0.01, 0.01))).clip(0,1);
		var postEnv =(Latch.ar(K2A.ar(loop), latchTrig)>0);

		var eof, eor;

		var inTrig = Trig1.ar(trig, 1/sampleRate);

		var postEnv2;
		var maths, maths2, interp;

		var pluggedIsSignal = plugged.rate != \scalar;
		var needsMaths = pluggedIsSignal or: { plugged < 1 };
		var needsMaths2 = pluggedIsSignal or: { plugged >= 1 };

		if(needsMaths){
			phasor = phasor.linlin(-1,1,width.neg, 1-width);
			maths = phasor.bilin(0, width.neg, 1-width, 0, -1, 1);
			maths = 1-(maths.abs);
			maths = maths*postEnv;
		};

		if(needsMaths2){
			phasor2 = Phasor.ar(inTrig, 2*freq/sampleRate, -1, 1, -1);
			postEnv2 = SetResetFF.ar(Delay1.ar(inTrig), Trig1.ar(0.5-phasor2, 1/sampleRate));
			phasor2 = phasor2.linlin(-1,1,width.neg, 1-width);
			maths2 = phasor2.bilin(0, width.neg, 1-width, 0, -1, 1);
			maths2 = 1-(maths2.abs);
			maths2 = maths2*postEnv2;
		};

		if(pluggedIsSignal) {
			maths = Select.ar(plugged, [maths, maths2]);
		} {
			if(plugged > 0) { maths = maths2 };
		};

		isExp = (linExp>0.5).asInteger;
		interp = Select.kr(isExp, [linExp.linlin(0, 0.5, 1, 0) , linExp.linlin(0.5, 1, 0, 1)]);
		maths = Select.ar(isExp, [ maths-1, maths]);
		maths = (maths**8*interp)+(maths*(1-interp));
		maths = Select.ar(isExp, [ maths+1, maths]);

		isPlugged = plugged>0;
		if(pluggedIsSignal){
			postEnv = phasor*postEnv;
			postEnv2 = phasor2*postEnv2;
			eof = Select.ar(isPlugged, [postEnv.neg>0, postEnv2.neg>0]);
			eor = Select.ar(isPlugged, [postEnv>0, postEnv2>0]);
		}{
			if(needsMaths){
				postEnv = phasor*postEnv;
			}{
				postEnv = phasor2*postEnv2;
			};
			eof = postEnv.neg>0;
			eor = postEnv>0;
		}

		^[maths, eof, eor]
	}
}
