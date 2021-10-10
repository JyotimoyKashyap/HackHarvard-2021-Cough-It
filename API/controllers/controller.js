const fs = require("fs");
const { PythonShell } = require("python-shell");
const { spawn } = require("child_process");
const Mp32Wav = require("mp3-to-wav");
const ffmpeg = require("ffmpeg");
const shell = require("shelljs");
const { exec } = require("child_process");

exports.getPrediction = async (req, res) => {
  try {
    const audioSample = req.body.audio;

    fs.writeFileSync(
      "./assets/data/sample.m4a",
      Buffer.from(
        audioSample.replace("data:audio/mpeg_4; codecs=opus;base64,", ""),
        "base64"
      )
    );

    // try {
    //   const mp32Wav = new Mp32Wav(
    //     "./assets/data/sample.mp3",
    //     "./assets/output"
    //   );
    //   mp32Wav.exec();
    // } catch (err) {
    //   console.log(err);
    // }

    let dataToSend;

    let options = {};

    // await PythonShell.run(
    //   "scripts/convertToWav.py",
    //   options,
    //   function (err, result) {
    //     if (err) throw err;
    //     console.log(result.toString());
    //   }
    // );

    // shell.exec("scripts/conversion.sh");

    exec(
      "ffmpeg -i assets/data/sample.m4a assets/output/sample.wav",
      (err, stdout, stderr) => {
        if (err) {
          //some err occurred
          console.error(err);
        } else {
          // the *entire* stdout and stderr (buffered)
          console.log(`stdout: ${stdout}`);
          console.log(`stderr: ${stderr}`);
        }
      }
    );

    await PythonShell.run(
      "scripts/preprocessing.py",
      options,
      function (err, result) {
        if (err) throw err;
        dataToSend = result.toString();
        return res.status(200).json({
          status: "success",
          data: dataToSend,
        });
      }
    );

    // return res.status(200).json({
    //   status: "success",
    //   // data: dataToSend,
    // });
  } catch (err) {
    console.log(err);
    res.status(400).json({
      status: "fail",
      message: err,
    });
  }
};
