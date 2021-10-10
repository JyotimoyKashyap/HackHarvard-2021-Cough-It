const awsServerlessExpress = require("aws-serverless-express");

const app = require("./app");
const warmer = require("lambda-warmer");

const server = awsServerlessExpress.createServer(app);
exports.handler = (event, context) => {
  context.callbackWaitsForEmptyEventLoop = false;
  warmer(event).then((isWarmer) => {
    if (isWarmer) {
      callback(null, "warmed");
    } else {
      awsServerlessExpress.proxy(server, event, context);
    }
  });
};
