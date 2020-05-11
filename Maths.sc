Maths {
	*ar { |riseDur=0.5, fallDur=0.5, logExp = 0.5, loop = 1, trig = 0|
		var slewUp, slewDown, riseToFall, oscOut, pulse, trigOut, trigOut2, trigOutSig, sig, frontEdge, freq, dur, backEdge;

		riseDur = riseDur.clip(0.001, 10);//K2A.ar(max(0.001, riseDur));
		fallDur = fallDur.clip(0.001, 10);//K2A.ar(max(0.001, fallDur));

		dur = riseDur+fallDur;
		riseToFall = (riseDur/(riseDur+fallDur)).clip(0.01, 0.99);
		logExp = logExp.clip(0, 1);

		freq = 1/dur;

		pulse = LFPulseReset.ar(freq, 0.999, riseToFall, trig);

		riseToFall = Select.kr(EnvGen.kr(Env([0,0,1], [0.001*dur, 0])), [riseToFall, Latch.kr(riseToFall, A2K.kr(pulse)-0.5)]);

		slewUp = freq/riseToFall;
		slewDown = freq/(1-riseToFall);

		trigOut = Trig1.ar(trig, dur*riseToFall);
		trigOutSig = Slew.ar(trigOut, slewUp, slewDown);

		oscOut = Slew.ar(SelectX.ar(pulse, [Slew.ar(pulse, 44100, slewDown), Slew.ar(pulse, slewUp, 44100)]), slewUp, slewDown);
		oscOut = SelectX.ar((freq>10).asInteger, [oscOut, Slew.ar(pulse, slewUp, slewDown)]);

		sig = Select.ar(loop, [trigOutSig, oscOut]);

		frontEdge = Select.kr(loop, [trigOut, trigOut+pulse])-0.1;

		sig = LinSelectX.ar(logExp*2, [sig.explin(0.001, 1, 0, 1), sig, sig.linexp(0, 1, 0.001, 1)]);
		^[sig, pulse]
	}
}

