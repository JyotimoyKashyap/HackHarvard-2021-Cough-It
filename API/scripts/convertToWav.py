import os
import argparse

from pydub import AudioSegment

formats_to_convert = ['.m4a']

filepath = "assets/data/sample.m4a"

(path, file_extension) = os.path.splitext(filepath)
file_extension_final = file_extension.replace('.', '')
filename = "converted"
try:
    track = AudioSegment.from_file(filepath,
    file_extension_final)
    wav_filename = filename.replace(file_extension_final, 'wav')
    wav_path = "assets" + '/' + wav_filename
    print('CONVERTING: ' + str(filepath))
    file_handle = track.export(wav_path, format='wav')
    os.remove(filepath)
except:
    print("ERROR CONVERTING " + str(filepath))