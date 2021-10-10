const express = require("express");
const controller = require("../controllers/controller");
const router = express.Router();

router.route("/prediction").post(controller.getPrediction);

module.exports = router;
