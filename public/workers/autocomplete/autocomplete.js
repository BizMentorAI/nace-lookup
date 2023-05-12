import * as cherry_core from '/js/cljs_core.js';
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1["body"]);
let reader3 = body2.getReader();
null;
var onmessage = function (event) {
return null;
}
;
self.onmessage = function (event) {
console.log("Worker received:", ({ "term": event.data }));
return postMessage(({ "a": 1 }));
};

export { onmessage }
