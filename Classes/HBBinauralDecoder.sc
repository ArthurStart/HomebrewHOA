
HBBinauralDecoder {
    classvar <>binIRs;

    *loadIRs { | server |
        // Load the binaural IRs.
        var path = '/Users/arthurstart/Music/ImpulseResponses/GoogleAmbiHRTFs/';
        binIRs = [
            Buffer.read(server, path++'binaural_decoder_0.wav'),
            Buffer.read(server, path++'binaural_decoder_1.wav'),
            Buffer.read(server, path++'binaural_decoder_2.wav'),
            Buffer.read(server, path++'binaural_decoder_3.wav')
        ];
    }

    *ar { | in |
        var conv, notconv;
        // Check input is 4ch FOA.
        if (in.size != 4, {
            "Input to HBGoogleBinauralDecoder.ar must be first order Ambisonic channels.".postln;
            ^nil;
        });

        // conv = 4.collect({ |item, i| Convolution2.ar(in[i], binIRs[i])});
        conv = [
            Convolution2.ar(in[0], binIRs[0], framesize: 512),
            Convolution2.ar(in[1], binIRs[1], framesize: 512),
            Convolution2.ar(in[2], binIRs[2], framesize: 512),
            Convolution2.ar(in[3], binIRs[3], framesize: 512)
        ];



        notconv = [
            in[0],
            in[1],
            in[2],
            in[3]
        ];

        ^[in, conv, [Mix.ar(conv*[1, -1, 1, 1]), Mix.ar(conv)]];

        // Convolve, sum, output.
    }
}