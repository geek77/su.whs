package su.whs.utils;

import su.whs.math.Complex;
import su.whs.math.FFT;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class EarBase {
	private AudioRecord mInput;
	private int mBufferSize = 4096;
	private short[] mData;
	private Thread mThread;
	
	public AudioRecord initInput() {
		return new AudioRecord(
				MediaRecorder.AudioSource.MIC,
				AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM),
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				mBufferSize
				);
	}
	
	public void Start() {
		mInput = initInput();
		mData = new short[mBufferSize];
		mThread = new Thread(new Runnable() {
			private Complex[] mOverlaps;
			
			private int acquire() {
				int samples = 0;
				try {
					mInput.startRecording();
					samples = mInput.read(mData, 0, mBufferSize);
				} catch (Throwable t) {
					
				}	
				return samples;
			}
			
			private Complex[] computeFFT(int size) {
				double[] data = new double[mBufferSize];
				int BPS = 2; 
				double AMP = 100.0;
				int i;
				int fi;
				for (i = 0, fi = 0; i<mBufferSize-BPS+1;i+=BPS,fi++ ) {
					double sample = 0;
					for (int b=0;b<BPS;b++) {
						int v = mData[i+b];
						if (b<BPS-1||BPS==1) 
							v &= 0xFF;
						sample += v << (b<<3);
					}					
					data[fi] = AMP * (sample / 32768.0);
				}
				
				// init complex array
				Complex[] fftTemp = new Complex[mBufferSize];
				for(i=0; i<mBufferSize; i++) {
					fftTemp[i] = new Complex(data[i],0);
				}
				
				return FFT.fft(fftTemp);
				// Complex[] inv = FFT.ifft(fftTemp);
			}
			
			private void overlap(Complex[] fft) {
				
			}
			
			@Override
			public void run() {
				mOverlaps = new Complex[mBufferSize];
				for(int i=0; i<mBufferSize; i++) mOverlaps[i] = new Complex(0,0);
				do {
					int samples = acquire();
					if (samples>0) {
						Complex[] fft = computeFFT(samples);
						overlap(fft);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						break;
					}
				} while (true);
				
			}});
		mThread.start();
	}
	
}
