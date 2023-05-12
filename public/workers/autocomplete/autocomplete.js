import * as cherry_core from 'cherry-cljs/lib/cljs_core.js';
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1["body"]);
let reader3 = body2.getReader();
console.log(cherry_core.keyword("HERE"));
console.log(cherry_core.keyword("response"), body2, reader3, cherry_core.array_map(cherry_core.keyword("read"), reader3.read()));
var onmessage = function (event) {
return null;
}
;
self.onmessage = function (event) {
console.log("Worker received:", ({ "term": event.data }));
return postMessage(({ "a": 1 }));
};

export { onmessage }
