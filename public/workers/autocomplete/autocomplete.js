import * as cherry_core from 'cherry-cljs/lib/cljs_core.js';
console.log("IN WEB WORKER");
self.onmessage = function (event) {
console.log("Worker received:", ({ "term": event.data }));
return postMessage(({ "a": 1 }));
};
