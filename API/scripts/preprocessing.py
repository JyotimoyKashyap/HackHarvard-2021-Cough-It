import os
import json
import librosa

x,sr=librosa.load("assets/output/sample.wav",22050)
x=librosa.util.fix_length(x,154350)
mfcc=librosa.feature.mfcc(x, n_mfcc=15, n_fft=2048, hop_length=512)
mfcc=mfcc.T.tolist()
final_arr = []
for x in mfcc:
    new_list = [[i] for i in x]
    final_arr.append(new_list)
finalArray = []
finalArray.append(final_arr)
print(finalArray)

