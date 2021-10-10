const app = require("./app");
const http = require("http");

//set port, listen for requests
const PORT = process.env.PORT || 8080;

http.createServer(app);
app.listen(8080, () => {
  console.log(`Local server running on port ${PORT}`);
});
