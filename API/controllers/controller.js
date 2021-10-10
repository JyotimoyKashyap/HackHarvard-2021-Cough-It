const fs = require("fs");
const { PythonShell } = require("python-shell");
const { exec } = require("child_process");
const axios = require("axios");

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

    let dataToSend;

    let options = {};

    exec(
      "ffmpeg -i assets/data/sample.m4a assets/output/sample.wav",
      (err, stdout, stderr) => {
        if (err) {
          //some err occurred
          console.error(err);
        } else {
          console.log("Converted");
        }
      }
    );

    PythonShell.run(
      "scripts/preprocessing.py",
      options,
      async function (err, result) {
        if (err) throw err;
        dataToSend = result.toString();

        const data = JSON.stringify(dataToSend);

        const config = {
          method: "post",
          url: "https://runtime.sagemaker.us-east-2.amazonaws.com/endpoints/tensorflow-inference-2021-10-10-02-56-05-523/invocations",
          headers: {
            "X-Amz-Content-Sha256":
              "beaead3198f7da1e70d03ab969765e0821b24fc913697e929e726aeaebf0eba3",
            "X-Amz-Date": "20211010T095700Z",
            Authorization:
              "AWS4-HMAC-SHA256 Credential=AKIAXV27PUM6AN7L5KGU/20211010/us-east-2/sagemaker/aws4_request, SignedHeaders=host;x-amz-content-sha256;x-amz-date, Signature=9e0dc83ba031fd9d19112c396329799d83b1fa5f83836bb18463a1366e989bd0",
            "Content-Type": "application/json",
          },
          data: data,
        };

        await axios(config)
          .then(function (response) {
            console.log(JSON.stringify(response.data));
            return res.status(200).json({
              status: "success",
              data: response.data,
            });
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    );
  } catch (err) {
    console.log(err);
    res.status(400).json({
      status: "fail",
      message: err,
    });
  }
};
