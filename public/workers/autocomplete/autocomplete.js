import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
console.log((await fetch_data.call(null)));
var handle_message = function (event) {
console.log("Worker received:", ({ "term": event.data }));
return postMessage(({ "a": 1 }));
}
;
self.onmessage = handle_message;

export { fetch_data, handle_message }
